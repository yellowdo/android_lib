package ko.go.anavl.repository.remote.controller

class ResponseCallback<T> {
    var start: Start? = null
        private set
    var responseItem: ResponseItem<T>? = null
        private set
    var responseList: ResponseList<T>? = null
        private set
    var completed: Completed? = null
        private set
    var failure: Failure? = null
        private set

    fun setStart(start: Start?): ResponseCallback<T> {
        this.start = start
        return this
    }

    fun setResponseItem(res: ResponseItem<T>?): ResponseCallback<T> {
        responseItem = res
        return this
    }

    fun setResponseList(responseList: ResponseList<T>?): ResponseCallback<T> {
        this.responseList = responseList
        return this
    }

    fun setCompleted(completed: Completed?): ResponseCallback<T> {
        this.completed = completed
        return this
    }

    fun setFailure(fail: Failure?): ResponseCallback<T> {
        failure = fail
        return this
    }

    interface Start {
        fun onStart()
    }

    interface ResponseItem<T> {
        fun onResponse(item: T)
    }

    interface ResponseList<T> {
        fun onResponse(item: List<T>?)
    }

    interface Completed {
        fun onCompleted()
    }

    interface Failure {
        fun onFailure(msg: String?, t: Throwable?)
    }

    companion object {
        @JvmStatic
        fun <T2> build(cls: Class<T2>?): ResponseCallback<T2> {
            return ResponseCallback()
        }
    }
}