package com.techullurgy.chessk.shared.events

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val baseEventJson = Json {
    serializersModule = SerializersModule {
        polymorphic(ServerToClientBaseEvent::class, GameUpdate::class, GameUpdate.serializer())
        polymorphic(ServerToClientBaseEvent::class, TimerUpdate::class, TimerUpdate.serializer())
        polymorphic(ServerToClientBaseEvent::class, GameStarted::class, GameStarted.serializer())
        polymorphic(
            ServerToClientBaseEvent::class,
            SelectionResult::class,
            SelectionResult.serializer()
        )
        polymorphic(
            ServerToClientBaseEvent::class,
            ResetSelectionDone::class,
            ResetSelectionDone.serializer()
        )

        polymorphic(ClientToServerBaseEvent::class, EnterRoomHandshake::class, EnterRoomHandshake.serializer())
        polymorphic(ClientToServerBaseEvent::class, CellSelection::class, CellSelection.serializer())
        polymorphic(ClientToServerBaseEvent::class, PieceMove::class, PieceMove.serializer())
        polymorphic(ClientToServerBaseEvent::class, ResetSelection::class, ResetSelection.serializer())
        polymorphic(ClientToServerBaseEvent::class, Disconnected::class, Disconnected.serializer())
    }
}