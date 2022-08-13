package com.example.olsera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    val allCompanies: LiveData<List<Company>>
    val repository: CompanyRepository

    init {
        val dao = CompanyDatabase.getDatabase(application).companyDao()
        repository = CompanyRepository(dao)
        allCompanies = repository.allCompanies
    }

    fun deleteCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(company)
    }
    fun updateCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(company)
    }
    fun addCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(company)
    }

}