package com.ayush.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<EVENT : Any, STATE : Any, SIDE_EFFECT : Any>(
    private val initialState: STATE
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SIDE_EFFECT>(Channel.Factory.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    abstract fun onEvent(event: EVENT)

    protected fun setState(reducer: STATE.() -> STATE) {
        _uiState.update { currentState ->
            currentState.reducer()
        }
    }

    protected fun sendSideEffect(effect: SIDE_EFFECT) {
        viewModelScope.launch(Dispatchers.IO) {
            _sideEffect.send(effect)
        }
    }

    protected fun currentState(): STATE {
        return _uiState.value
    }

    protected fun resetState() {
        _uiState.update { initialState }
    }
}