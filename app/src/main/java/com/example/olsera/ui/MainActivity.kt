package com.example.olsera.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olsera.CompanyAdapter
import com.example.olsera.CompanyAdapterPaging
import com.example.olsera.CompanyClickInterface
import com.example.olsera.CompanyClickPageInterface
import com.example.olsera.databinding.ActivityMainBinding
import com.example.olsera.db.Company
import com.example.olsera.viewmodel.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CompanyClickInterface, CompanyClickPageInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CompanyViewModel
    private lateinit var adapterPaging: CompanyAdapterPaging
    private var dataPaging: PagingData<Company>? = null
    private lateinit var dataCompany: Company
    private var allStatus = 0
    private var active = 0
    private var inactive = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initViewModel()


        binding.fab.setOnClickListener {
            val intent = Intent(this, InputCompany::class.java)
            startActivity(intent)
        }

        with(binding) {

//            repeat(40) {
//                val company = Company("Active", "Company ${it}", "Address", "Solo", -7.779219, 110.378007, "57115")
//                viewModel.addCompany(company)
//            }

//            lifecycleScope.launch(Dispatchers.IO) {
//                viewModel.getAllCompanies().collectLatest {
//                    dataPaging = it
//                    companyAdapterPaging.submitData(it)
//                }
//            }

//            viewModel.allCompanies.observe(this@MainActivity, Observer { list ->
//                list?.let { company ->
//                    companyAdapter.updateList(company)
//                    data = company
//
//                    company.map {
//                        if (it.status == "Active") {
//                            active++
//                        }
//                        if (it.status == "Inactive") {
//                            inactive++
//                        }
//
//                        allStatus++
//                    }
//
//                    binding.rbAllStatus.text = "All Status ($allStatus)"
//                    binding.rbActive.text = "Active ($active)"
//                    binding.rbInactive.text = "Inactive ($inactive)"
//
//                }
//            })

//            rbAllStatus.setOnClickListener {
//                companyAdapter.updateList(data!!)
//            }
//
//            rbActive.setOnClickListener {
//                val filter = data!!.filter { s -> s.status == "Active" }
//                companyAdapter.updateList(filter)
//            }
//
//            rbInactive.setOnClickListener {
//                val filter = data!!.filter { s -> s.status == "Inactive" }
//                companyAdapter.updateList(filter)
//            }

        }

    }

    private fun initRecyclerView() {
        binding.rvListCompany.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapterPaging = CompanyAdapterPaging(this@MainActivity)
            adapter = adapterPaging
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[CompanyViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            viewModel.getAllCompanies().collectLatest {
                adapterPaging.submitData(it)
            }
        }
        Log.e("DATAXX", adapterPaging.snapshot().toString())
//        viewModel.insertDummyCompany()
    }

    override fun onCompanyClick(company: Company) {
        dataCompany = company
        val page = InputCompany::class.java
        val intent = Intent(this, page)
        val bundle = Bundle()
        bundle.putParcelable("company", company)
        intent.putExtra("companyType", "Edit")
        intent.putExtra("companyID", company.companyID)
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2) {
            viewModel.deleteCompany(dataCompany)
        }
    }
}