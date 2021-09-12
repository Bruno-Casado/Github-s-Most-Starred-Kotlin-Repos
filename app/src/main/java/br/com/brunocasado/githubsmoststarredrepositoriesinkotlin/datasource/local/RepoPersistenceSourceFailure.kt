package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure

sealed class RepoPersistenceSourceFailure : Failure.FeatureFailure() {
    object GetKotlinRepoListPersistenceError : RepoPersistenceSourceFailure()
    object SaveKotlinRepoListPersistenceError : RepoPersistenceSourceFailure()
}