package com.techullurgy.chessk.navigation

import com.techullurgy.chessk.feature.user_details.api.userDetailModule
import org.koin.dsl.module

val appModule = module {
    includes(userDetailModule)
}