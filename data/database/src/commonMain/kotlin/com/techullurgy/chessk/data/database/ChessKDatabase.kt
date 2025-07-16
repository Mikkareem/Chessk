package com.techullurgy.chessk.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.chessk.data.database.converters.BoardPiecesTypeConverter
import com.techullurgy.chessk.data.database.converters.CutPiecesTypeConverter
import com.techullurgy.chessk.data.database.converters.MoveTypeConverter
import com.techullurgy.chessk.data.database.converters.MovesTypeConverter
import com.techullurgy.chessk.data.database.daos.GameDao
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.scope.Scope

@Database(
    entities = [GameEntity::class, MemberEntity::class, TimerEntity::class],
    version = 1
)
@TypeConverters(
    MoveTypeConverter::class,
    MovesTypeConverter::class,
    BoardPiecesTypeConverter::class,
    CutPiecesTypeConverter::class,
)
@ConstructedBy(ChessKDatabaseConstructor::class)
abstract class ChessKDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}

@Suppress("KotlinNoActualForExpect")
internal expect object ChessKDatabaseConstructor : RoomDatabaseConstructor<ChessKDatabase> {
    override fun initialize(): ChessKDatabase
}

internal fun getDatabaseFromBuilder(
    builder: RoomDatabase.Builder<ChessKDatabase>,
): ChessKDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()

internal expect val roomBuilder: Scope.() -> RoomDatabase.Builder<ChessKDatabase>

