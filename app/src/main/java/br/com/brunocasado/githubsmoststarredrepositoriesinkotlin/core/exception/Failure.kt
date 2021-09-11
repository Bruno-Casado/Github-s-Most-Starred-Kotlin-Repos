package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    abstract class FeatureFailure: Failure()
}