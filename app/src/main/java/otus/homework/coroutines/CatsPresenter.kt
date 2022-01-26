package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)

    fun onInitComplete() = presenterScope.launch {
        try {
            coroutineScope {
                catsService.getCatFact().let { response ->
                    if (response.isSuccessful && response.body() != null) {
                        _catsView?.populate(response.body()!!)
                    }
                }
            }
        } catch (e: Throwable) {
            if (e is java.net.SocketTimeoutException) {
                _catsView?.showServerError(true, e.message.orEmpty())
            } else {
                _catsView?.showServerError(false, e.message.orEmpty())
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancelChildren()
        _catsView = null
    }
}