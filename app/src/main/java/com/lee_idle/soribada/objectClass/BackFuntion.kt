package com.lee_idle.soribada.objectClass

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object BackFuntion {
    private var _isPossible: MutableLiveData<Boolean> = MutableLiveData(false)
    public val isPossible: LiveData<Boolean>
        get() = _isPossible
    fun setPossible(isPossible: Boolean) {
        _isPossible.value = isPossible
    }

    private var _backTraceFuntion: (() -> Unit)? = null
    public val backTraceFuntion: (() -> Unit)?
        get() = _backTraceFuntion
    fun setBackTraceFuntion(funtion: () -> Unit) {
        _backTraceFuntion = funtion
    }
}