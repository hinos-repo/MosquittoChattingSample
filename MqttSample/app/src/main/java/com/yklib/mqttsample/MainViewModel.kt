package com.yklib.mqttsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yklib.mqttsample.data.ChatDTO
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.nio.charset.Charset

class MainViewModel(application: Application) : AndroidViewModel(application)
{
    val newChatData : MutableLiveData<ChatDTO> = MutableLiveData()
    private val packageName = application.packageName
    private val gson : Gson = Gson()
    private val mqttClient : MqttClient = MqttClient("tcp://***.***.***", MqttClient.generateClientId(), null).apply {
        connect()
        subscribe(packageName)
        setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?)
            {
                println("connectionLost")
            }
            override fun messageArrived(topic: String?, message: MqttMessage?)
            {
                if (topic == packageName)
                {
                    val data = message?.let {
                        gson.fromJson(String(it.payload, Charset.defaultCharset()), ChatDTO::class.java)
                    } ?: return

                    newChatData.postValue(data)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?)
            {
                println("deliveryComplete")
            }
        })
    }
    fun sendChatData(data : ChatDTO)
    {
        data.createTime = System.currentTimeMillis().toString()
        mqttClient.publish(packageName, MqttMessage(gson.toJson(data).toByteArray()))
    }
}