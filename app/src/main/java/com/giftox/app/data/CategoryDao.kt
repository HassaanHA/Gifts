package com.giftox.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<Category>?)

    @Query("SELECT * FROM Category")
    fun getAll(): Flow<List<Category>>

    @Query("DELETE FROM Category")
    suspend fun nuke()

}