package com.example.olsera

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.locale.SearchZoneRect
import com.example.olsera.databinding.ActivityInputCompanyBinding
import com.example.olsera.utils.showCustomToast
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class InputCompany : AppCompatActivity() {

    private lateinit var binding: ActivityInputCompanyBinding
    private var selectedRadioButton: String? = ""
    private lateinit var viewModel: CompanyViewModel
    private var data: Company? = null
    private var companyID = -1
    private var listener: ((Boolean) -> Unit?)? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CompanyViewModel::class.java]

        val companyType = intent.getStringExtra("companyType")
        if (companyType.equals("Edit")) {
            val bundle = intent.extras!!
            if (bundle.getParcelable<Company>("company") !== null) {
                data = bundle.getParcelable<Company>("company")!!
            }
            companyID = intent.getIntExtra("companyID", -1)
            binding.tvHead.text = "Edit Location"
            binding.btnSave.text = "Update"
            binding.btnDelete.visibility = View.VISIBLE
            (binding.etCompanyName as TextView).text = data!!.companyName
            (binding.etCompanyAddress as TextView).text = data!!.address
            (binding.etCompanyCity as TextView).text = data!!.city
            (binding.etCompanyZipCode as TextView).text = data!!.zipCode
            if (data!!.status == "Active") {
                binding.rbActive.isChecked = true
            } else {
                binding.rbInactive.isChecked = true
            }
            selectedRadioButton = data!!.status
        } else {
            binding.btnSave.text = "Save"
        }


        with(binding) {
            closeBtn.setOnClickListener { onBackPressed() }
            btnCancel.setOnClickListener {
                (binding.etCompanyName as TextView).text = ""
                (binding.etCompanyAddress as TextView).text = ""
                (binding.etCompanyCity as TextView).text = ""
                (binding.etCompanyZipCode as TextView).text = ""
            }

            btnDelete.setOnClickListener {
//                listener!!(true)
                Toast(this@InputCompany).showCustomToast(
                    "Delete Company Successfull",
                    this@InputCompany
                )
                val intent = Intent()
                setResult(1, intent)
                finish()
            }

            btnSave.setOnClickListener {
                if (validateProductDetails()) {
                    val companyName = binding.etCompanyName.text.toString().trim { it <= ' ' }
                    val companyAddress = binding.etCompanyAddress.text.toString().trim { it <= ' ' }
                    val companyCity = binding.etCompanyCity.text.toString().trim { it <= ' ' }
                    val companyZipcode = binding.etCompanyZipCode.text.toString().trim { it <= ' ' }
                    val dCompany = Company(
                        selectedRadioButton!!,
                        companyName,
                        companyAddress,
                        companyCity,
                        companyZipcode,
                    )

                    if (companyType.equals("Edit")) {

                        dCompany.companyID = companyID
                        viewModel.updateCompany(dCompany)
                        Toast(this@InputCompany).showCustomToast(
                            "Update Company Successfull",
                            this@InputCompany
                        )

                    } else {
                        viewModel.addCompany(dCompany)
                        Toast(this@InputCompany).showCustomToast(
                            "Add Company Successfull",
                            this@InputCompany
                        )
                    }

                    startActivity(Intent(this@InputCompany, MainActivity::class.java))
                    finish()

                }
            }

            rgStatus.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                selectedRadioButton = radio.text.toString()
            }

        }

    }

    private fun validateProductDetails(): Boolean {
        return when {

            selectedRadioButton!!.isEmpty() || selectedRadioButton!! == "" -> {
                Toast(this).showCustomToast("Status tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyName.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("Name tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyAddress.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("Address tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyCity.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("City tidak boleh kosong", this)
                false
            }

            binding.etCompanyZipCode.length() < 1 -> {
                Toast(this).showCustomToast("Zipcode tidak boleh kosong", this)
                false
            }

            else -> {
                true
            }
        }
    }

    companion object{
        fun newInstance(listener: ((Boolean) -> Unit)): InputCompany {
            val inputCompany = InputCompany()
            inputCompany.listener = listener
            return inputCompany
        }
    }

}