package com.yklib.mqttsample.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yklib.mqttsample.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return MainViewModel(application) as T
    }
}