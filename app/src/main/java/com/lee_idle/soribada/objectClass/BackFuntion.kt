package com.lee_idle.soribada.objectClass

object BackFuntion {
    /* 
    TODO:
     더 이상 뒤로 갈 곳이 없는지 확인할 Boolean 값을 하나 만들어서
     MainActivity에서 해당 Boolean 값을 보고 뒤로가기 버튼 숨기기 처리하여
     뒤로가지 못하게 할 필요 있음
     */
    private var _backTraceFuntion: (() -> Unit)? = null
    public val backTraceFuntion: (() -> Unit)?
        get() = _backTraceFuntion
    fun setBackTraceFuntion(funtion: () -> Unit) {
        _backTraceFuntion = funtion
    }


}