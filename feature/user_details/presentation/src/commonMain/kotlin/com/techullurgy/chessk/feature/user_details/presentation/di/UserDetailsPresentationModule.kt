package com.techullurgy.chessk.feature.user_details.presentation.di

import com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload.ProfilePicUploadViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userDetailsPresentationModule = module {
    viewModelOf(::ProfilePicUploadViewModel)
}