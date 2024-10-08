package com.valenpatel.realmdatabasepractice.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valenpatel.realmdatabasepractice.models.ArticleModel
import io.realm.Realm
import io.realm.kotlin.where
import java.util.UUID

class ArticleViewModel : ViewModel() {

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    // LiveData to observe changes in articles
    private val _allArticles = MutableLiveData<List<ArticleModel>>()
    val allArticles: LiveData<List<ArticleModel>> get() = _allArticles

    init {
        loadArticles()
    }

    fun addArticle(title: String, description: String) {
        realm.executeTransaction { realm ->
            val article = realm.createObject(ArticleModel::class.java, UUID.randomUUID().toString())
            article.title = title
            article.description = description
            realm.insertOrUpdate(article)
        }
        loadArticles() // Refresh the list after adding a new article
    }

    private fun loadArticles() {
        val articles = realm.where<ArticleModel>().findAll()
        _allArticles.value = realm.copyFromRealm(articles)
        // Log the articles
        Log.d("ArticleViewModel", "Articles: ${_allArticles.value}")
    }

    private fun deleteArticle(article: ArticleModel) {
        realm.executeTransaction {
            article.deleteFromRealm()
        }
        loadArticles() // Refresh the list after deleting an article
    }

    fun updateArticle(id: String, newTitle: String, newDescription: String) {
        val article = realm.where<ArticleModel>().equalTo("id", id).findFirst()
        realm.executeTransaction {
            article?.title = newTitle
            article?.description = newDescription
            if (article != null) {
                realm.insertOrUpdate(article)
            }
        }
        loadArticles() // Refresh the list after updating an article
    }

    override fun onCleared() {
        super.onCleared()
        realm.close() // Close Realm instance when ViewModel is cleared
    }
}
