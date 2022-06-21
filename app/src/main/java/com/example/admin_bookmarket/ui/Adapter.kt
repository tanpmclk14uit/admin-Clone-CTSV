package com.example.admin_bookmarket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.data.common.Constants.DEFAULT_IMG_PLACEHOLDER
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.CardBookBinding


class Adapter(
    private var values: MutableList<Book>, private val itemListener:RecyclerViewClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CardBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun onChange(newList:MutableList<Book>)
    {
        values = newList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){

            setOnClickListener {
                itemListener.recyclerViewListClicked(this, values[position].id!!)
            }
        }
        val item = values[position]
        holder.bookName.text = item.Name
        holder.bookAuthor.text = item.Description
        Glide
            .with(holder.itemView)
            .load(item.Image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerInside()
            .placeholder(DEFAULT_IMG_PLACEHOLDER)
            .into(holder.bookImage)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding:CardBookBinding) : RecyclerView.ViewHolder(binding.root) {
        val bookName = binding.bookTitle
        val bookAuthor = binding.bookAuthor
        val bookImage = binding.BookImage
    }

}