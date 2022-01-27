package com.giftox.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(occasions: List<Occasion>?)

    @Query("SELECT * FROM Occasion")
    fun getAll(): Flow<List<Occasion>>

    @Query("DELETE FROM Occasion")
    suspend fun nuke()

}