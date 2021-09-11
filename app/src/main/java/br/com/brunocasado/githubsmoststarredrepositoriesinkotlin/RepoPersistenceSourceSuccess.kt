package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Success

sealed class RepoPersistenceSourceSuccess : Success.FeatureSuccess() {
    object SaveKotlinRepoListSuccess : RepoPersistenceSourceSuccess()
}