package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatsImageService
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.domain.AppResult

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<AppResult<CatsViewData>>(AppResult.Empty)
    private val catsLiveData: LiveData<AppResult<CatsViewData>> = _catsLiveData
    private val catsContext = CoroutineName("CatsCoroutine") + Dispatchers.IO

    fun getData() = viewModelScope.launch(catsContext) {
        try {
            _catsLiveData.postValue(AppResult.Loading)
            coroutineScope {
                val fact = async {
                    catsService.getCatFact().takeIf { response ->
                        response.isSuccessful && response.body() != null
                    }?.body()
                }
                val catsImageUrl = async {
                    catsImageService.getCatImage().url
                }

                _catsLiveData.postValue(
                    AppResult.Success(
                        CatsViewData(
                            fact.await()?.text,
                            catsImageUrl.await()
                        )
                    )
                )
            }
        } catch (e: Throwable) {
            _catsLiveData.postValue(AppResult.Failure(e))
        }
    }
}