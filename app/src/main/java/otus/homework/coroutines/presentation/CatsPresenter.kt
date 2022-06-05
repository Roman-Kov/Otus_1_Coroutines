package otus.homework.coroutines.presentation

import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatsImageService
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.domain.CrashMonitor

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)

    fun onInitComplete() = presenterScope.launch {
        try {
            coroutineScope {
                val fact = async {
                    catsService.getCatFact()
                }
                val catsImageUrl = async {
                    catsImageService.getCatImage().url
                }

                _catsView?.populate(
                    CatsViewData(
                        fact.await().text,
                        catsImageUrl.await()
                    )
                )
            }
        } catch (e: Throwable) {
            handleError(e)
        }
    }

    private fun handleError(e: Throwable) = when (e) {
        is CancellationException -> throw e
        is java.net.SocketTimeoutException -> _catsView?.showServerError(true, e.message.orEmpty())
        else -> {
            _catsView?.showServerError(false, e.message.orEmpty())
            CrashMonitor.trackWarning()
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