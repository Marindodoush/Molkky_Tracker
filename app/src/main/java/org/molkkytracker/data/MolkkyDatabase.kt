package org.molkkytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class MolkkyDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile private var instance: MolkkyDatabase? = null

        fun get(context: Context): MolkkyDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MolkkyDatabase::class.java,
                    "molkky.db"
                ).build().also { instance = it }
            }
    }
}
