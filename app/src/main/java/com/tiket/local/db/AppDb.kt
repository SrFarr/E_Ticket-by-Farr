package com.tiket.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tiket.local.dao.AuthDao
import com.tiket.local.dao.TransaksiDao
import com.tiket.local.dao.TransportasiDao
import com.tiket.local.dao.UserDao
import com.tiket.local.entity.Transaksi
import com.tiket.local.entity.Transportasi
import com.tiket.local.entity.User

@Database(
    entities = [User::class, Transaksi::class, Transportasi::class],
    version = 5,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun authDao(): AuthDao
    abstract fun userDao(): UserDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun transportasiDao(): TransportasiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "tiket_db"
                )
                    .fallbackToDestructiveMigration() 
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
