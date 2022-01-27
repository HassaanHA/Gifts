package com.giftox.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(home: Home?)

    @Query("SELECT * FROM Home LIMIT 1")
    fun getData(): Flow<Home>

    @Query("DELETE FROM Home")
    suspend fun nuke()

}