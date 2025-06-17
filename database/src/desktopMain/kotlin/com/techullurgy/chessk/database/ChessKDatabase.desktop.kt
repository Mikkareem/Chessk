package com.techullurgy.chessk.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope
import java.io.File

private fun getDatabaseBuilder(): RoomDatabase.Builder<ChessKDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "chessk_db.db")
    return Room.databaseBuilder(
        name = dbFile.absolutePath
    )
}

actual val roomBuilder: Scope.() -> RoomDatabase.Builder<ChessKDatabase>
    get() = { getDatabaseBuilder() }