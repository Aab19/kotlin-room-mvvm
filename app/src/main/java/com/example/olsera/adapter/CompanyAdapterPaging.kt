package com.example.olsera

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.olsera.db.Company

class CompanyAdapterPaging(val companyClickInterface: CompanyClickPageInterface) :
    PagingDataAdapter<Company, CompanyAdapterPaging.CompanyViewHolder>(DiffUtilCallback()) {

    class CompanyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(company: Company, position: Int) {
            Log.e("DEBUG DATA", "item: $company")

            if (position % 2 == 1) {
                itemView.findViewById<ConstraintLayout>(R.id.listItemContainer)
                    .setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.grey
                        )
                    )
            } else {
                itemView.findViewById<ConstraintLayout>(R.id.listItemContainer)
                    .setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.white
                        )
                    )

            }

            Glide
                .with(itemView.context)
                .load(R.drawable.ols)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(35)))
                .into(itemView.findViewById<ImageView>(R.id.ivItemImage))
            itemView.findViewById<TextView>(R.id.tvCompanyName).text = company.companyName
            itemView.findViewById<TextView>(R.id.tvCompanyName).setTextColor(
                if (company.status == "Active") ContextCompat.getColor(
                    itemView.context,
                    R.color.black
                ) else ContextCompat.getColor(itemView.context, R.color.grey_text)
            )
            itemView.findViewById<TextView>(R.id.tvCompanyStatus).setTextColor(
                if (company.status == "Active") ContextCompat.getColor(
                    itemView.context,
                    R.color.black
                ) else ContextCompat.getColor(itemView.context, R.color.grey_text)
            )
            itemView.findViewById<TextView>(R.id.tvCompanyBooking).visibility =
                if (company.status == "Active") View.VISIBLE else View.GONE
            itemView.findViewById<TextView>(R.id.tvCompanyBooking).text =
                if (company.status == "Active") "Online Booking Enabled" else ""
            itemView.findViewById<TextView>(R.id.tvCompanyStatus).text = company.status
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Company>() {
        override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem.companyID == newItem.companyID
        }

        override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem == newItem
        }

    }

    @SuppressLint("CutPasteId")
    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position)!!, position)
        holder.itemView.setOnClickListener {
            companyClickInterface.onCompanyClick(getItem(position)!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_company, parent, false)
        )
    }

}

interface CompanyClickPageInterface {
    fun onCompanyClick(company: Company)
}