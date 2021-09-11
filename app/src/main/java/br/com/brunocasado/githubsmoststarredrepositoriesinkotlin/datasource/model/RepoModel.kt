package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    primaryKeys = ["id"]
)
data class RepoModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks")
    val forks: Int,
    @Embedded
    @SerializedName("owner")
    val owner: OwnerModel
)
