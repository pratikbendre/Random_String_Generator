package com.pratikbendre.randomstringgenerator.ui.randomString

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratikbendre.randomstringgenerator.data.repository.RandomStringRepository
import com.pratikbendre.randomstringgenerator.model.RandomText
import com.pratikbendre.randomstringgenerator.utils.DispatcherProvider
import com.pratikbendre.randomstringgenerator.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomStringViewModel @Inject constructor(
    private val randomStringRepository: RandomStringRepository,
    private val dispatcherProvider: DispatcherProvider
) :
    ViewModel() {
    private val _randomString =
        MutableStateFlow<UiState<RandomText>>(UiState.Success(RandomText("", 0, "")))

    val randomString: StateFlow<UiState<RandomText>> = _randomString

    fun fetchData(text_length: Int) {

        viewModelScope.launch(dispatcherProvider.io) {
            randomStringRepository.getString(text_length).catch { e ->
                _randomString.value = UiState.Error(e.message.toString())
            }.collect {
                _randomString.value = UiState.Success(it)
            }
        }

    }
}