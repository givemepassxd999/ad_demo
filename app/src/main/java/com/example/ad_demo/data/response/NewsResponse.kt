package com.example.ad_demo.data.response

import com.google.gson.annotations.SerializedName

class NewsResponse : Response {

    @SerializedName("status")
    val status: String? = null

    @SerializedName("totalResults")
    val totalResults: Int? = null

    @SerializedName("articles")
    val articles: List<Article>? = null
}

class Article{
    @SerializedName("title")
    val title: String? = null

    @SerializedName("urlToImage")
    val urlToImage: String? = null

    @SerializedName("description")
    val description: String? = null

    @SerializedName("content")
    val content: String? = null

    @SerializedName("publishedAt")
    val publishedAt: String? = null
}