package com.techullurgy.chessk.feature.game.api

import com.techullurgy.chessk.feature.game.data.di.gameDataModule
import com.techullurgy.chessk.feature.game.presentation.di.gamePresentationModule
import org.koin.dsl.module

val gameModule = module {
    includes(gameDataModule, gamePresentationModule)
}