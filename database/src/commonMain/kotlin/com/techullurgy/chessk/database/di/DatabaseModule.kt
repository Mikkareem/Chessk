package com.techullurgy.chessk.database.di

import androidx.room.RoomDatabase
import com.techullurgy.chessk.database.ChessKDatabase
import com.techullurgy.chessk.database.daos.GameDao
import com.techullurgy.chessk.database.getDatabaseFromBuilder
import com.techullurgy.chessk.database.roomBuilder
import org.koin.dsl.module

val databaseModule = module {
    single<RoomDatabase.Builder<ChessKDatabase>> { roomBuilder() }
    single<ChessKDatabase> { getDatabaseFromBuilder(get()) }
    single<GameDao> { get<ChessKDatabase>().gameDao() }
}