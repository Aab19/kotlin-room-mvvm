package com.example.olsera.db

import androidx.paging.PagingSource
import javax.inject.Inject

class CompanyRepository @Inject constructor (private val companyDao: CompanyDao) {

//    val allCompanies: LiveData<List<Company>> = companyDao.getAllCompany()
//    val allCompanies: PagingSource<Int, Company> = companyDao.getAllCompany()

    fun getAllCompanies() : PagingSource<Int, Company> {
        return companyDao.getAllCompany()
    }

    suspend fun insert(company: Company) {
        companyDao.insertCompany(company)
    }

    suspend fun delete(company: Company) {
        companyDao.deleteCompany(company)
    }

    suspend fun update(company: Company) {
        companyDao.updateCompany(company)
    }

}