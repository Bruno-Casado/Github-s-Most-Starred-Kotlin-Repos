package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core

import androidx.lifecycle.MutableLiveData
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.ViewStatus.*
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure

class ViewState<T> private constructor(
    val status: ViewStatus = SUCCESS,
    val data: T? = null,
    val failure: Failure? = null
) {
    companion object {
        fun <T> success(data: T): ViewState<T> = ViewState(SUCCESS, data)

        fun <T> loading(data: T): ViewState<T> = ViewState(LOADING, data)

        fun <T> failure(failure: Failure): ViewState<T> = ViewState(FAILURE, failure = failure)
    }

    fun handleIt(
        onSuccess: (T) -> Unit = {},
        onFailure: (Failure) -> Unit = {},
        isLoading: (Boolean) -> Unit = {}
    ): ViewState<T> {
        when (status) {
            SUCCESS -> {
                this.data?.let { onSuccess(it) }
                isLoading(false)
            }
            LOADING -> isLoading(true)
            FAILURE -> {
                this.failure?.let { onFailure(it) }
                isLoading(false)
            }
        }
        return this
    }
}

enum class ViewStatus {
    LOADING, SUCCESS, FAILURE
}

fun <T> MutableLiveData<ViewState<T>>.postSuccess(data: T) = postValue(ViewState.success(data))

fun <T> MutableLiveData<ViewState<T>>.postLoading(data: T) = postValue(ViewState.loading(data))

fun <T> MutableLiveData<ViewState<T>>.postFailure(failure: Failure) =
    postValue(ViewState.failure(failure))