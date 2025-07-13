package com.techullurgy.chessk.feature.game.data

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.base.AppResultFailureException
import com.techullurgy.chessk.base.takeSuccessResult
import com.techullurgy.chessk.data.database.DatabaseDataSource
import com.techullurgy.chessk.data.database.models.TimerEntity
import com.techullurgy.chessk.data.remote.RemoteDataSource
import com.techullurgy.chessk.data.websockets.WebsocketDataSource
import com.techullurgy.chessk.feature.game.data.mappers.toEntity
import com.techullurgy.chessk.feature.game.data.mappers.toGameEntity
import com.techullurgy.chessk.feature.game.data.mappers.toMemberEntities
import com.techullurgy.chessk.feature.game.models.BrokerEvent
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.GameStarted
import com.techullurgy.chessk.shared.events.GameUpdate
import com.techullurgy.chessk.shared.events.ResetSelectionDone
import com.techullurgy.chessk.shared.events.SelectionResult
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.events.TimerUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

internal class GameRoomMessageBroker(
    private val wsDataSource: WebsocketDataSource<ServerToClientBaseEvent, ClientToServerBaseEvent>,
    private val dbDataSource: DatabaseDataSource,
    private val remoteDataSource: RemoteDataSource,
    scope: CoroutineScope
) : MessageBroker<BrokerEvent> {
    private val wsEvents = wsDataSource.eventsFlow

    private val isGameRoomsAvailable = dbDataSource.isGameRoomsAvailable()

    private val connectable = MutableStateFlow<Boolean?>(null)

    private val retryChannel: Channel<Unit> = Channel(capacity = Channel.CONFLATED)
    private val refreshChannel: Channel<Unit> = Channel(capacity = Channel.CONFLATED)

    private val _brokerEventsFlow = channelFlow {
        launch {
            refreshChannel.receiveAsFlow()
                .onStart {
                    send(BrokerEvent.BrokerRefreshingEvent)
                    fetchAnyJoinedRoomsAndStoreLocally()
                    send(BrokerEvent.BrokerRefreshedEvent)
                }
                .collectLatest {
                    send(BrokerEvent.BrokerRefreshingEvent)
                    fetchAnyJoinedRoomsAndStoreLocally()
                    send(BrokerEvent.BrokerRefreshedEvent)
                }
        }

        launch {
            retryChannel.receiveAsFlow().collectLatest {
                if (connectable.value == true) {
                    send(BrokerEvent.BrokerRetryingEvent)
                    wsDataSource.startSession()
                    send(BrokerEvent.BrokerRetriedEvent)
                }
            }
        }

        launch {
            isGameRoomsAvailable.collect { available ->
                if (available) {
                    connectable.value = true
                    wsDataSource.startSession()
                } else {
                    connectable.value = false
                    wsDataSource.stopSession()
                }
            }
        }

        launch {
            wsEvents.collect { result ->
                when (result) {
                    AppResult.Failure -> send(BrokerEvent.BrokerFailureEvent)
                    AppResult.Loading -> send(BrokerEvent.BrokerLoadingEvent)
                    is AppResult.Success<ServerToClientBaseEvent?> -> {
                        with(dbDataSource) {
                            when (val data = result.data) {
                                is GameStarted -> gameStartedUpdate(
                                    data.roomId,
                                    data.members.map { it.toEntity() },
                                    data.assignedColor
                                )

                                is GameUpdate -> updateGame(
                                    data.roomId,
                                    data.board,
                                    data.cutPieces,
                                    data.lastMove,
                                    data.currentTurn,
                                    data.kingInCheckIndex
                                )

                                is ResetSelectionDone -> resetSelection(data.roomId)
                                is SelectionResult -> updateAvailableMoves(
                                    data.roomId,
                                    data.selectedIndex,
                                    data.availableMoves
                                )

                                is TimerUpdate -> updateTimer(
                                    TimerEntity(data.roomId, data.whiteTime, data.blackTime)
                                )
                                null -> send(BrokerEvent.BrokerConnectedEvent)
                            }
                        }
                    }

                    is AppResult.FailureWithException -> send(BrokerEvent.BrokerFailureEvent)
                }
            }
        }

        awaitClose {
            connectable.value = null
        }
    }

    override val brokerEventsFlow = _brokerEventsFlow
        .distinctUntilChanged()
        .onStart {
            dbDataSource.invalidateGameRooms()
        }
        .onCompletion {
            dbDataSource.invalidateGameRooms()
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    override val isConnected = wsDataSource.isConnected

    override fun retry() {
        retryChannel.trySend(Unit)
    }

    override fun refresh() {
        refreshChannel.trySend(Unit)
    }

    private suspend fun fetchAnyJoinedRoomsAndStoreLocally() {
        try {
            val roomsResponse = remoteDataSource.fetchAnyJoinedRoomsAvailable().takeSuccessResult()
            val gameEntities = roomsResponse.map { it.toGameEntity() }
            val memberEntities = roomsResponse.map { it.toMemberEntities() }

            dbDataSource.saveJoinedRoomsDetails(gameEntities, memberEntities)
        } catch (_: AppResultFailureException) {
            println("Not able to fetch Joined Rooms")
        }
    }
}