package com.example.olsera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.olsera.db.Company
import com.example.olsera.db.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(private val repository: CompanyRepository) : ViewModel() {


    fun getAllCompanies(): Flow<PagingData<Company>> {

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { repository.getAllCompanies() }
        ).flow.cachedIn(viewModelScope)

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

    fun insertDummyCompany() = viewModelScope.launch(Dispatchers.IO) {
        for (i in 1..100) {
            val company =
                Company("Active", "Company $i", "Address", "Solo", -7.779219, 110.378007, "57115")
            repository.insert(company)
        }
    }


}