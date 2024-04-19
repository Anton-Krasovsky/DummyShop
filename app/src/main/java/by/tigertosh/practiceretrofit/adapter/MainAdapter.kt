package by.tigertosh.practiceretrofit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.tigertosh.practiceretrofit.R
import by.tigertosh.practiceretrofit.databinding.ItemListBinding
import by.tigertosh.practiceretrofit.network.data.Product
import com.squareup.picasso.Picasso

class MainAdapter : ListAdapter<Product, MainAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemListBinding.bind(view)
        fun bind(product: Product) = with(binding){
            Picasso.get().load(product.images[0]).into(productImage)
            title.text = product.title
            description.text = product.description
            price.text = buildString {
                append(product.price.toString())
                append("$")
            }
        }

    }

    class Comparator() : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}