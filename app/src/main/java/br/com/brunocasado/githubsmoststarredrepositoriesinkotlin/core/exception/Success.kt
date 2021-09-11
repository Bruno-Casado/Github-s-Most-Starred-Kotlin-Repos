package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception

sealed class Success {
    abstract class FeatureSuccess : Success()
}