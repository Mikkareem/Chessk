package com.techullurgy.chessk.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

private fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ChessKDatabase> {
    return Room.databaseBuilder(context, ChessKDatabase::class.java, "chessk_db")
}

actual val roomBuilder: Scope.() -> RoomDatabase.Builder<ChessKDatabase>
    get() = { getDatabaseBuilder(get()) }