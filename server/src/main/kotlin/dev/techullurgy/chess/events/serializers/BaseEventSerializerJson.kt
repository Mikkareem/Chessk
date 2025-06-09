package dev.techullurgy.chess.events.serializers

import dev.techullurgy.chess.events.PieceMove
import dev.techullurgy.chess.events.Disconnected
import dev.techullurgy.chess.events.TimerUpdate
import dev.techullurgy.chess.events.GameStarted
import dev.techullurgy.chess.events.CellSelection
import dev.techullurgy.chess.events.EnterRoomHandshake
import dev.techullurgy.chess.events.GameUpdate
import dev.techullurgy.chess.events.ReceiverBaseEvent
import dev.techullurgy.chess.events.ResetSelection
import dev.techullurgy.chess.events.ResetSelectionDone
import dev.techullurgy.chess.events.SelectionResult
import dev.techullurgy.chess.events.SenderBaseEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val receiverBaseEventJson = Json {
    serializersModule = SerializersModule {
        polymorphic(ReceiverBaseEvent::class, EnterRoomHandshake::class, EnterRoomHandshake.serializer())
        polymorphic(ReceiverBaseEvent::class, CellSelection::class, CellSelection.serializer())
        polymorphic(ReceiverBaseEvent::class, PieceMove::class, PieceMove.serializer())
        polymorphic(ReceiverBaseEvent::class, ResetSelection::class, ResetSelection.serializer())
        polymorphic(ReceiverBaseEvent::class, Disconnected::class, Disconnected.serializer())
    }
}

val senderBaseEventJson = Json {
    serializersModule = SerializersModule {
        polymorphic(SenderBaseEvent::class, GameUpdate::class, GameUpdate.serializer())
        polymorphic(SenderBaseEvent::class, TimerUpdate::class, TimerUpdate.serializer())
        polymorphic(SenderBaseEvent::class, GameStarted::class, GameStarted.serializer())
        polymorphic(SenderBaseEvent::class, SelectionResult::class, SelectionResult.serializer())
        polymorphic(SenderBaseEvent::class, ResetSelectionDone::class, ResetSelectionDone.serializer())
    }
}