package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @SerializedName("items")
    val items: List<RepoModel>
)