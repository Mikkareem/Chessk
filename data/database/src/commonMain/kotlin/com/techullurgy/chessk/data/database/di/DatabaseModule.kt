package com.techullurgy.chessk.data.database.di

import androidx.room.RoomDatabase
import com.techullurgy.chessk.data.database.ChessKDatabase
import com.techullurgy.chessk.data.database.daos.GameDao
import com.techullurgy.chessk.data.database.getDatabaseFromBuilder
import com.techullurgy.chessk.data.database.roomBuilder
import org.koin.dsl.module

val databaseModule = module {
    single<RoomDatabase.Builder<ChessKDatabase>> { roomBuilder() }
    single<ChessKDatabase> { getDatabaseFromBuilder(get()) }
    single<GameDao> { get<ChessKDatabase>().gameDao() }
}