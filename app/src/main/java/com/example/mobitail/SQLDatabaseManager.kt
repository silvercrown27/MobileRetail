package com.example.mobitail

import android.content.Context
import android.database.sqlite.SQLiteDatabase

object SQLDatabaseManager {
    private var database: SQLiteDatabase? = null

    fun getDatabase(context: Context): SQLiteDatabase {
        if (database == null || !database!!.isOpen) {
            val databasePath = context.getDatabasePath("mobitail").path
            database = SQLiteDatabase.openOrCreateDatabase(databasePath, null)
            createTablesIfNotExists(database!!)
        }
        return database!!
    }

    private fun createTablesIfNotExists(database: SQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS users(" +
                "firstname VARCHAR," +
                "lastname VARCHAR," +
                "email VARCHAR," +
                "devicename VARCHAR," +
                "deviceid VARCHAR," +
                "contact VARCHAR," +
                "gender VARCHAR," +
                "location VARCHAR)")
    }
}
