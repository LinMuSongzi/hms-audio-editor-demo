package com.example.musicplay.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContextViewModel :ViewModel() {


    companion object
    {
        const val TAG = "ContextViewModel"
    }


    val changeItem = MutableStateFlow("1")

    val changePosition = MutableStateFlow(1)

    init {
        observer()
    }


    private fun observer(){

        viewModelScope.launch{

            changeItem.collect {
                Log.i(TAG, "changeItem observer: $it")
            }

            changePosition.collect {
                Log.i(TAG, "changePosition observer: $it")
            }

        }

    }

}