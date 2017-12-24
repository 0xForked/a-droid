package id.asmith.someapp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import id.asmith.someapp.R.string.email
import id.asmith.someapp.R.string.phone
import id.asmith.someapp.data.model.LocalUser
import id.asmith.someapp.data.model.LocalUser as local
import id.asmith.someapp.util.AppConstants as utils

/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */

class SQLHandler(context: Context?) : SQLiteOpenHelper(context, utils.DB_NAME,
        null, utils.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable: String = "Create table ${utils.DB_TABLE} " +
                "(" +
                    "${utils.KEY_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${utils.KEY_UID} TEXT, " +
                    "${utils.KEY_USERNAME} TEXT," +
                    "${utils.KEY_NAME} TEXT," +
                    "${utils.KEY_EMAIL} TEXT," +
                    "${utils.KEY_PHONE} TEXT, " +
                    "${utils.KEY_TOKEN} TEXT," +
                    "${utils.KEY_LOGGED} TEXT," +
                    "${utils.KEY_CREATED} TEXT," +
                    "${utils.KEY_UPDATED} TEXT " +
                ")"

        // Here you create tables
        db.execSQL(createTable)

        Log.d("Debug", "Databases Created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop old
        db.execSQL("DROP TABLE IF EXISTS " + utils.DB_TABLE)
        // Create new
        onCreate(db)

        Log.e("Debug", "Table is Exist and will deleted")

    }

    fun getUser(): HashMap<String, String> {
        val userData = HashMap<String, String>()
        val db: SQLiteDatabase = this.readableDatabase

        val  cursor: Cursor = db.rawQuery("SELECT * FROM ${utils.DB_TABLE}",
                null )
        cursor.moveToFirst()
        if (cursor.moveToFirst()) {
            userData.put("uid", cursor.getString(1))
            userData.put("username", cursor.getString(2))
            userData.put("name", cursor.getString(3))
            userData.put("email", cursor.getString(4))
            userData.put("phone", cursor.getString(5))
            userData.put("token", cursor.getString(6))
            userData.put("logged", cursor.getString(7))
        }
        cursor.close()
        db.close()

        Log.d("Debug", "User data" + userData.toString())

        return userData

    }

    fun addUserData(user: LocalUser) {

        deleteUserData()
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(utils.KEY_UID, user.uid)
        values.put(utils.KEY_USERNAME, user.username)
        values.put(utils.KEY_NAME, user.name)
        values.put(utils.KEY_EMAIL, user.email)
        values.put(utils.KEY_PHONE, user.phone)
        values.put(utils.KEY_TOKEN, user.token)
        values.put(utils.KEY_LOGGED, user.logged)
        values.put(utils.KEY_CREATED, user.created)
        values.put(utils.KEY_UPDATED, user.updated)
        db.insert(utils.DB_TABLE, null, values)
        db.close()

        Log.d("Debug", "Success add user data ${user.uid}, ${user.username}, ${user.name}," +
                " $email, $phone, ${user.email}, ${user.logged}, ${user.created}, ${user.updated}")

    }

    fun loggedStatusOut(uid: String) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(utils.KEY_LOGGED, "false")
        db.update(utils.DB_TABLE, values, "uid=$uid", null)
        db.close()
    }

    fun deleteUserData() {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(utils.DB_TABLE, null, null)
        db.close()

    }


}
