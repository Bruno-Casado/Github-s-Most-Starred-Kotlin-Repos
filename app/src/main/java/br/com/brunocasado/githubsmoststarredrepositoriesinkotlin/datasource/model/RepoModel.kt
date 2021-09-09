package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model

import com.google.gson.annotations.SerializedName

data class RepoModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("owner")
    val owner: OwnerModel
)
