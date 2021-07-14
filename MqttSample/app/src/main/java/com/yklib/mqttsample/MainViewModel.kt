package com.yklib.mqttsample

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yklib.mqttsample.data.ChatDTO
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class MainViewModel(application: Application) : AndroidViewModel(application)
{
    private val TAG = this::class.java.simpleName

    val arrChatData : MutableLiveData<MutableList<ChatDTO>> = MutableLiveData(mutableListOf())
    private val packageName = application.packageName
    private val gson : Gson = Gson()
    private val mqttClient : MqttClient = MqttClient("tcp://141.164.57.0:1883", MqttClient.generateClientId(), null).apply {
        connect()
        subscribe(packageName)
        setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "connectionLost: ${cause?.cause}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if (topic == packageName) {
                    val data = message?.let {
                        gson.fromJson(String(it.payload, Charset.defaultCharset()), ChatDTO::class.java)
                    } ?: return

                    arrChatData.postValue(arrChatData.value?.apply {
                        add(data)
                    })
                    Log.d(TAG, "messageArrived: a")
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "deliveryComplete: $token")
            }
        })
    }

    fun sendChatData(data: ChatDTO)
    {
        data.createTime = System.currentTimeMillis().toString()
        mqttClient.publish(packageName, MqttMessage(gson.toJson(data).toByteArray(Charsets.UTF_8)))
    }
}