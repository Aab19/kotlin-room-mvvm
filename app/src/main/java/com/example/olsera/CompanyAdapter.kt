package com.example.olsera

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.olsera.databinding.ItemListCompanyBinding

open class CompanyAdapter(
    val context: Context,
    val companyClickInterface: CompanyClickInterface
) : RecyclerView.Adapter<CompanyAdapter.ListCompanyViewHolder>() {

    private var companyList: ArrayList<Company> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCompanyViewHolder =
        ListCompanyViewHolder(
            ItemListCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ListCompanyViewHolder, position: Int) {
        val item = companyList[position]
        holder.setData(item, position)
        holder.itemView.setOnClickListener {
            companyClickInterface.onCompanyClick(item)
        }
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Company>) {
        companyList.clear()
        companyList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ListCompanyViewHolder(private val v: ItemListCompanyBinding) :
        RecyclerView.ViewHolder(v.root) {
        fun setData(data: Company, position: Int) {

            if (position % 2 == 1) {
                v.listItemContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            } else {
                v.listItemContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )

            }

            Glide
                .with(context)
                .load(R.drawable.ols)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(35)))
                .into(v.ivItemImage)
            v.tvCompanyName.text = data.companyName
            v.tvCompanyName.setTextColor(
                if (data.status == "Active") ContextCompat.getColor(
                    context,
                    R.color.black
                ) else ContextCompat.getColor(context, R.color.grey_text)
            )
            v.tvCompanyStatus.setTextColor(
                if (data.status == "Active") ContextCompat.getColor(
                    context,
                    R.color.black
                ) else ContextCompat.getColor(context, R.color.grey_text)
            )
            v.tvCompanyBooking.visibility = if (data.status == "Active") View.VISIBLE else View.GONE
            v.tvCompanyBooking.text = if (data.status == "Active") "Online Booking Enabled" else ""
            v.tvCompanyStatus.text = data.status
        }
    }

}

interface CompanyClickInterface {
    fun onCompanyClick(company: Company)
}