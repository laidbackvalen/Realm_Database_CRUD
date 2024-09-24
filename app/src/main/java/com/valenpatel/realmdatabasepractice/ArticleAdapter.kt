package com.valenpatel.realmdatabasepractice

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm

class ArticleAdapter(val context: Context, private val articles: MutableList<ArticleModel>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
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

        holder.itemView.findViewById<TextView>(R.id.delete).setOnClickListener {
            removeItem(position)
        }

    }
    fun removeItem(position: Int) {
        // Remove item from the list
        val articleToRemove = articles[position]
        articles.removeAt(position)
        notifyItemRemoved(position)

        // Optionally: Remove item from Realm or other database
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val article = realm.where(ArticleModel::class.java).equalTo("id", articleToRemove.id).findFirst()
            article?.deleteFromRealm()
        }
    }
}