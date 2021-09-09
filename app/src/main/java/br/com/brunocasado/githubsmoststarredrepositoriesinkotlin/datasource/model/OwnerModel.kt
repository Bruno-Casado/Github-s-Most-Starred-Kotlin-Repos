package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model

import com.google.gson.annotations.SerializedName

data class OwnerModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
