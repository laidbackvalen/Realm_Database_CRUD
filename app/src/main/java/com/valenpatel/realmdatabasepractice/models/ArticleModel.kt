package com.valenpatel.realmdatabasepractice.models
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ArticleModel : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var description: String = ""

    override fun toString(): String {
        return "ArticleModel(id='$id', title='$title', description='$description')"
    }
}