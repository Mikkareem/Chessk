package com.techullurgy.chessk.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private fun getDatabaseBuilder(): RoomDatabase.Builder<ChessKDatabase> {
    val dbFilePath = documentDirectory() + "/chessk_db.db"
    return Room.databaseBuilder(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}


actual val roomBuilder: Scope.() -> RoomDatabase.Builder<ChessKDatabase>
    get() = { getDatabaseBuilder() }