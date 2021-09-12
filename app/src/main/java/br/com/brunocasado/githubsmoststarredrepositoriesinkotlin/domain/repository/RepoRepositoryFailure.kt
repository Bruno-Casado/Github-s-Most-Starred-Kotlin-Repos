package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.repository

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure

sealed class RepoRepositoryFailure : Failure.FeatureFailure() {
    object LastPageReached : RepoRepositoryFailure()
}