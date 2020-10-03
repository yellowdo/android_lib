package ko.go.anavl.repository.remote.controller

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallbackImpl<T>(private val listener: ResponseCallback<T>) : Callback<List<T>?> {
    init {
        listener.start?.onStart()
    }

    override fun onResponse(c: Call<List<T>?>, r: Response<List<T>?>) {
        when {
            r.code() == 200 && r.body() != null -> {
                if (r.body()?.isNotEmpty()!!) listener.responseItem?.onResponse(r.body()!![0])
                listener.responseList?.onResponse(r.body())
            }
            else -> listener.failure?.onFailure("결과가 없습니다.", NullPointerException())
        }
        listener.completed?.onCompleted()
    }

    override fun onFailure(c: Call<List<T>?>, t: Throwable) {
        listener.start?.onStart()
        listener.failure?.onFailure("네트워크에 문제가 발생하였습니다.", t)
        listener.completed?.onCompleted()
    }
}