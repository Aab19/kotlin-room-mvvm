package com.example.olsera.db

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface CompanyDao {
    @Query("SELECT * FROM Companies order by companyID ASC")
//    fun getAllCompany(): LiveData<List<Company>>
    fun getAllCompany(): PagingSource<Int, Company>

    @Insert(onConflict = IGNORE)
    suspend fun insertCompany(company: Company)

    @Update
    suspend fun updateCompany(company: Company)

    @Delete
    suspend fun deleteCompany(company: Company)
}