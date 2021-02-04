package com.example.carshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.car_item.view.*
import java.text.DecimalFormat

class CarAdapter(private val carList: List<CarItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {
    val dec = DecimalFormat("#,###.##")

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val model: TextView = itemView.text_model
        val seats: TextView = itemView.text_seats
        val date: TextView = itemView.text_date
        val category: TextView = itemView.text_category
        val condition: TextView = itemView.text_condition
        val price: TextView = itemView.text_price

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return CarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {

        val currentItem = carList[position]
        holder.model.text = currentItem.model
        holder.seats.text = currentItem.seats.toString()
        holder.date.text = currentItem.date.toString()
        holder.category.text = currentItem.category
        holder.condition.text = currentItem.condition
        holder.price.text = dec.format(currentItem.price)
    }

    override fun getItemCount() = carList.size
}