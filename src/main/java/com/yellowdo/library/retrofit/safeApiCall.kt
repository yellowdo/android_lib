package ko.go.anavl.repository.remote.util

import retrofit2.Response

/*
* service의 suspend 함수를 받아 RepoResult로 반환
* */
suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): RepoResult<T> {
    return try {
        call.invoke().let {
            if (it.isSuccessful) {
                RepoResult.Success(it.body()!!)
            } else {
                val error = when (it.code()) {
                    403 -> "Authentication failed"
                    else -> "error"
                }
                RepoResult.Error(it.errorBody()?.string() ?: error)
            }
        }
    } catch (e: Exception) {
        RepoResult.Error(e.message ?: "Internet error runs")
    }
}

suspend fun <T : Any, U : Any> safeApiCallMap(call: suspend () -> Response<T>, response: suspend (Response<T>) -> U): RepoResult<U> {
    return try {
        call.invoke().let {
            if (it.isSuccessful) {
                RepoResult.Success(response(it))
            } else {
                val error = when (it.code()) {
                    403 -> "Authentication failed"
                    else -> "error"
                }
                RepoResult.Error(it.errorBody()?.string() ?: error)
            }
        }
    } catch (e: Exception) {
        RepoResult.Error(e.message ?: "Internet error runs")
    }
}