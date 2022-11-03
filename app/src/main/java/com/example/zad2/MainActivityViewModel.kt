package com.example.zad2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {

    private var _uiState = MutableSharedFlow<String>()
    val uiState = _uiState.asSharedFlow()


    fun refreshThreads(){
        viewModelScope.launch {
            emitThreadIdWithRandomDelay(Dispatchers.Unconfined)
            emitThreadIdWithRandomDelay(Dispatchers.IO)
        }
    }

    private fun emitThreadIdWithRandomDelay(
        defaultDispatcher: CoroutineDispatcher,
        times: Int = 20
    ) {
        viewModelScope.launch {
            async(defaultDispatcher) {
                for (i in 0 until times) {
                    val delayTime = Random.nextInt(1, 1001).toLong()
                    delay(delayTime)
                    _uiState.emit("${Thread.currentThread().id}:$delayTime")
                }
                _uiState.emit("done")
            }
        }
    }
}