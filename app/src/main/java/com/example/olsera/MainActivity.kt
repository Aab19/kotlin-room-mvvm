package com.example.olsera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.olsera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CompanyClickInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CompanyViewModel
    private lateinit var adapter: CompanyAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var data: List<Company>? = null
    private lateinit var dataCompany: Company
    private var allStatus = 0
    private var active = 0
    private var inactive = 0
    private var page = 1
    private var pageSize = 20
    private var isLoading = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CompanyAdapter(this, this)
        layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener {
            val intent = Intent(this, InputCompany::class.java)
            startActivity(intent)
        }

        with(binding) {
            rvListCompany.layoutManager = layoutManager
            val companyAdapter = adapter
            rvListCompany.adapter = companyAdapter
            viewModel = ViewModelProvider(
                this@MainActivity,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )[CompanyViewModel::class.java]
            viewModel.allCompanies.observe(this@MainActivity, Observer { list ->
                list?.let { company ->
                    companyAdapter.updateList(company)
                    data = company

                    company.map {
                        if (it.status == "Active") {
                            active++
                        }
                        if (it.status == "Inactive") {
                            inactive++
                        }

                        allStatus++
                    }

                    binding.rbAllStatus.text = "All Status ($allStatus)"
                    binding.rbActive.text = "Active ($active)"
                    binding.rbInactive.text = "Inactive ($inactive)"

                }
            })

            rbAllStatus.setOnClickListener {
                companyAdapter.updateList(data!!)
            }

            rbActive.setOnClickListener {
                val filter = data!!.filter { s -> s.status == "Active" }
                companyAdapter.updateList(filter)
            }

            rbInactive.setOnClickListener {
                val filter = data!!.filter { s -> s.status == "Inactive" }
                companyAdapter.updateList(filter)
            }

            rvListCompany.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val total = adapter.itemCount
                    if (!isLoading) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            page++



                        }
                    }

                    super.onScrolled(recyclerView, dx, dy)

                }
            })

        }

    }

//    private fun deleteListCompany(boolean: Boolean) {
//        if(boolean) {
//            viewModel.deleteCompany(dataCompany)
//        }
//    }

    override fun onCompanyClick(company: Company) {
        dataCompany = company
//        val page = InputCompany.newInstance(this::deleteListCompany)::class.java
        val page = InputCompany::class.java
        val intent = Intent(this, page)
        val bundle = Bundle()
        bundle.putParcelable("company", company)
        intent.putExtra("companyType", "Edit")
        intent.putExtra("companyID", company.companyID)
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)

        /* Delete Company */
//        viewModel.deleteCompany(company)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            viewModel.deleteCompany(dataCompany)
        }
    }
}