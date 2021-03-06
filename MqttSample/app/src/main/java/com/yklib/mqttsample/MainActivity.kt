package com.yklib.mqttsample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yklib.mqttsample.adapter.ChatAdapter
import com.yklib.mqttsample.data.ChatDTO
import com.yklib.mqttsample.databinding.ActivityMainBinding
import com.yklib.mqttsample.factory.ViewModelFactory


/**
 * Hinos
 * create 2021.07.18
 * */
class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var adtChatList : ChatAdapter = ChatAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(this.application)).get(MainViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.run {
            vRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            vRecyclerView.adapter = adtChatList
            mainViewModel = this@MainActivity.viewModel
            vBtnSend.setOnClickListener {
                viewModel.sendChatData(
                    ChatDTO(
                        vEtName.text.toString(),
                        vEtInput.text.toString(),
                        System.currentTimeMillis().toString()
                    )
                )
                vEtInput.text.clear()
            }
            vEtName.setText(Build.MODEL)
        }
        initObserve()
    }

    private fun initObserve()
    {
        viewModel.arrChatData.observe(this, {
            adtChatList.changeAllData(it)
            binding.vRecyclerView.scrollToPosition(adtChatList.itemCount-1)
        })
    }
}