package com.giftox.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User?)

    @Query("SELECT * FROM User LIMIT 1")
    fun getLoggedInUser(): Flow<User?>

    @Query("DELETE FROM User")
    suspend fun nuke()

}