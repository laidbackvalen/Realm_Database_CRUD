package com.valenpatel.realmdatabasepractice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.valenpatel.realmdatabasepractice.models.ArticleModel
import com.valenpatel.realmdatabasepractice.R
import io.realm.Realm

class ArticleAdapter(val context: Context, private val articles: MutableList<ArticleModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

        //sending data from adapter to activity using interface
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentItems: ArticleModel) {
            itemView.findViewById<TextView>(R.id.title).text = currentItems.title
            itemView.findViewById<TextView>(R.id.description).text = currentItems.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.article_recyclerview_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItems = articles[position]
        holder.bind(currentItems)

        // Set click listener for the card view
        holder.itemView.findViewById<CardView>(R.id.cardView).setOnClickListener {
            listener.onItemClick(position)
        }

        holder.itemView.findViewById<TextView>(R.id.delete).setOnClickListener {
            removeItem(position)
        }
    }
    fun removeItem(position: Int) {
        // Remove item from the list
        val articleToRemove = articles[position]
        articles.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()

        // Optionally: Remove item from Realm or other database
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val article = realm.where(ArticleModel::class.java).equalTo("id", articleToRemove.id).findFirst()
            article?.deleteFromRealm()
        }
    }
}