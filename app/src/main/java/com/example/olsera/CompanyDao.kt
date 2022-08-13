package com.example.olsera

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface CompanyDao {
    @Query("SELECT * FROM Companies order by companyID ASC")
    fun getAllCompany(): LiveData<List<Company>>

    @Insert(onConflict = IGNORE)
    suspend fun insertCompany(company: Company)

    @Update
    suspend fun updateCompany(company: Company)

    @Delete
    suspend fun deleteCompany(company: Company)
}