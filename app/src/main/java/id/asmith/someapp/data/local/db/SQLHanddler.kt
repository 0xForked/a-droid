package id.asmith.someapp.data.local.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import id.asmith.someapp.util.AppConstants as utils



/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */

class SQLHandler(context: Context) : ManagedSQLiteOpenHelper(context,
        utils.DB_NAME, null, utils.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Customer", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "uid" to TEXT,
                "username" to TEXT,
                "name" to TEXT,
                "email" to TEXT + UNIQUE,
                "phone" to TEXT,
                "token" to TEXT,
                "isLoggedIn" to TEXT,
                "created_at" to TEXT,
                "updated_at" to TEXT)
        Log.d("Debug", "Databases Created")

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop old
        db.dropTable(utils.DB_TABLE, true)
        // Create new
        onCreate(db)

    }

    fun addUserData(uid: String, username: String, name: String, email: String, phone: String,
                 token: String, logged: String, created_at: String, updated_at: String) {

        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put("uid", uid)
        values.put("username", username)
        values.put("name", name)
        values.put("email", email)
        values.put("phone", phone)
        values.put("token", token)
        values.put("isLoggedIn", logged)
        values.put("created_at", created_at)
        values.put("updated_at", updated_at)
        db.insert(utils.DB_TABLE, null, values)
        db.close()

        Log.d("Debug", "Success get user data")

    }

    fun deleteUserData() {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(utils.DB_TABLE, null, null)
        db.close()
        Log.d("Debug", "Delete all user info from database")

    }

    fun getUser(): HashMap<String, String> {
        val userData = HashMap<String, String>()
        val db: SQLiteDatabase = this.readableDatabase

        //anko belum bisa rawQuery
        //db.select(utils.DB_TABLE).whereSimple("(uid = ? )", uid)

        val  cursor: Cursor = db.rawQuery("SELECT * FROM ${utils.DB_TABLE}",
                null )
        cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            userData.put("uid", cursor.getString(1))
            userData.put("username", cursor.getString(1))
            userData.put("name", cursor.getString(1))
            userData.put("email", cursor.getString(1))
            userData.put("phone", cursor.getString(1))
            userData.put("token", cursor.getString(1))
        }
        cursor.close()
        db.close()

        Log.d("Debug", "User data" + userData.toString())

        return userData

    }



}