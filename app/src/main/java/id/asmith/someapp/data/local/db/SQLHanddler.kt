package id.asmith.someapp.data.local.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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

    fun addUserData(uid: String, username: String, name: String, email: String, phone: String,
                    token: String, logged: String, created_at: String, updated_at: String) {

        deleteUserData()
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(utils.KEY_UID, uid)
        values.put(utils.KEY_USERNAME, username)
        values.put(utils.KEY_NAME, name)
        values.put(utils.KEY_EMAIL, email)
        values.put(utils.KEY_PHONE, phone)
        values.put(utils.KEY_TOKEN, token)
        values.put(utils.KEY_LOGGED, logged)
        values.put(utils.KEY_CREATED, created_at)
        values.put(utils.KEY_UPDATED, updated_at)
        db.insert(utils.DB_TABLE, null, values)
        db.close()

        Log.d("Debug", "Success add user data $uid, $username, $name, $email, $phone," +
                "$token, $logged, $created_at, $updated_at")

    }

    fun loggedStatusOut(logged: String) {
        
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



}
