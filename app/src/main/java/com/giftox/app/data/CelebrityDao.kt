package com.giftox.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CelebrityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(celebrities: Celebrity?)

    @Query("SELECT * FROM Celebrity WHERE celebrity_id = :id LIMIT 1")
    fun getById(id: Long): Flow<Celebrity>

    @Query("DELETE FROM Celebrity")
    suspend fun nuke()

}