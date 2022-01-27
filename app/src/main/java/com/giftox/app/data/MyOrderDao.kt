package com.giftox.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MyOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orders: List<MyOrder>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: MyOrder?)

    @Query("SELECT * FROM MyOrder")
    fun getAll(): Flow<List<MyOrder>>

    @Query("DELETE FROM MyOrder")
    suspend fun nuke()

}