package org.molkkytracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM remembered_players ORDER BY name ASC")
    fun observeAll(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Query("DELETE FROM remembered_players WHERE id = :id")
    suspend fun delete(id: Long)
}
