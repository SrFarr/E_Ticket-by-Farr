package com.tiket.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tiket.local.dao.AuthDao
import com.tiket.local.dao.TransaksiDao
import com.tiket.local.dao.TransportasiDao
import com.tiket.local.dao.UserDao
import com.tiket.local.entity.Transaksi
import com.tiket.local.entity.Transportasi
import com.tiket.local.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val transportasiDao = INSTANCE!!.transportasiDao()
                                val dummyList = listOf(
                                    Transportasi(nama = "Garuda 101", maskapai = "Garuda Indonesia", jenis = "Pesawat", asal = "Jakarta", tujuan = "Bali", waktuBerangkat = System.currentTimeMillis() + 3600_000, harga = 1200000.0),
                                    Transportasi(nama = "Lion 202", maskapai = "Lion Air", jenis = "Pesawat", asal = "Jakarta", tujuan = "Surabaya", waktuBerangkat = System.currentTimeMillis() + 7200_000, harga = 800000.0),
                                    Transportasi(nama = "AirAsia 303", maskapai = "AirAsia", jenis = "Pesawat", asal = "Jakarta", tujuan = "Medan", waktuBerangkat = System.currentTimeMillis() + 10800_000, harga = 900000.0),
                                    Transportasi(nama = "Sriwijaya 404", maskapai = "Sriwijaya Air", jenis = "Pesawat", asal = "Jakarta", tujuan = "Makassar", waktuBerangkat = System.currentTimeMillis() + 14400_000, harga = 1100000.0),
                                    Transportasi(nama = "KAI Argo Parahyangan", maskapai = "KAI", jenis = "Kereta", asal = "Bandung", tujuan = "Jakarta", waktuBerangkat = System.currentTimeMillis() + 3600_000, harga = 150000.0),
                                    Transportasi(nama = "KAI Taksaka", maskapai = "KAI", jenis = "Kereta", asal = "Yogyakarta", tujuan = "Jakarta", waktuBerangkat = System.currentTimeMillis() + 5400_000, harga = 200000.0),
                                    Transportasi(nama = "KAI Argo Wilis", maskapai = "KAI", jenis = "Kereta", asal = "Surabaya", tujuan = "Bandung", waktuBerangkat = System.currentTimeMillis() + 7200_000, harga = 180000.0),
                                    Transportasi(nama = "Citilink QZ101", maskapai = "Citilink", jenis = "Pesawat", asal = "Jakarta", tujuan = "Balikpapan", waktuBerangkat = System.currentTimeMillis() + 9000_000, harga = 950000.0),
                                    Transportasi(nama = "Garuda 102", maskapai = "Garuda Indonesia", jenis = "Pesawat", asal = "Jakarta", tujuan = "Lombok", waktuBerangkat = System.currentTimeMillis() + 10800_000, harga = 1250000.0),
                                    Transportasi(nama = "KAI Senja Utama", maskapai = "KAI", jenis = "Kereta", asal = "Jakarta", tujuan = "Semarang", waktuBerangkat = System.currentTimeMillis() + 12600_000, harga = 160000.0)
                                )
                                dummyList.forEach { transportasiDao.insert(it) }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

    }
}
