package com.genius.contactutils.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.genius.contactutils.models.DbData


/**
 * Created by geniuS on 19/02/2021.
 */
class DbHelper(context: Context?) :

    SQLiteOpenHelper(context, "database", null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + DbData._TABLE + "(" +
                    DbData.NUMBER + " TEXT NOT NULL," +
                    DbData.NAME + " TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, from: Int, to: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DbData._TABLE)
        onCreate(db)
    }

    fun getData(sql: String?): Cursor {
        val database = readableDatabase
        return database.rawQuery(sql, null)
    }

    /*
    * This methods return the name of user if contact is saved in sqlite db
    * */
    fun ifExistsGetName(TABLE_NAME: String,
                        colName: String,
                        number: String?) : String {
        var name = ""
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM $TABLE_NAME WHERE $colName='$number'",
                null
            )
            if (cursor.moveToFirst()) {
                name = cursor.getString(1)
                db.close()
                return name
            }
            Log.d(
                "debug",
                "Table is:$TABLE_NAME ColumnName:$colName Column Value:$number"
            )
            db.close()
        } catch (errorException: java.lang.Exception) {
            Log.d("debug", "Exception occured $errorException")
        }
        return name
    }

    /*
    * This method returns a boolean when a contact is found in sqlite db
    * */
    fun checkIfRecordExist(
        TABLE_NAME: String,
        colName: String,
        number: String?,
        closeDb: Boolean
    ): Boolean {
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery(
                "SELECT $colName FROM $TABLE_NAME WHERE $colName='$number'",
                null
            )
            if (cursor.moveToFirst()) {
                if (closeDb) db.close()
                Log.e(
                    "debug",
                    "Table is:$TABLE_NAME ColumnName:$colName"
                )
                return true //record Exists
            }
            Log.d(
                "debug",
                "Table is:$TABLE_NAME ColumnName:$colName Column Value:$number"
            )
            if (closeDb) db.close()
        } catch (errorException: java.lang.Exception) {
            Log.d("debug", "Exception occured $errorException")
        }
        return false
    }

    /*
    * This method lists all the number saved in db
    * */
    fun getAllNumbers(): ArrayList<DbData> {
        val db = this.readableDatabase
        val list = ArrayList<DbData>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM " + DbData._TABLE, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name = cursor.getString(cursor.getColumnIndex(DbData.NAME)).toString()
                val number = cursor.getString(cursor.getColumnIndex(DbData.NUMBER)).toString()
                list.add(DbData(name, number))
                cursor.moveToNext()
            }
        }
        db.close()
        return list
    }

    /*
    * This method deletes existing record
    * */
    fun deleteRecord(number: String) {
        val readableDatabase = this.readableDatabase
        readableDatabase.delete(
            DbData._TABLE,
            DbData.NUMBER + "=?",
            arrayOf(number)
        )
        readableDatabase.close()
    }

    /*
    * This method inserts new record
    * */
    fun insertRecord(values: ContentValues) {
        val db = this.writableDatabase
        db.insert(DbData._TABLE, null, values)
        db.close()
    }

    companion object {
        private const val DB_VERSION = 4
    }
}