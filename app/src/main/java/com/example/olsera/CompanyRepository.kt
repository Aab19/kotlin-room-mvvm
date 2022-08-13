package com.example.olsera

import androidx.lifecycle.LiveData

class CompanyRepository(private val companyDao: CompanyDao) {

    val allCompanies: LiveData<List<Company>> = companyDao.getAllCompany()

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