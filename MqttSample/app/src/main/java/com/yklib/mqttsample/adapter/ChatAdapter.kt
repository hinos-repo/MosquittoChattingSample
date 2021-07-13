package com.yklib.mqttsample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yklib.mqttsample.data.ChatDTO
import com.yklib.mqttsample.databinding.ItemChatBinding

class ChatAdapter(private val arrChat : MutableList<ChatDTO>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>()
{
    class ChatViewHolder(private val itemChatBinding: ItemChatBinding) : RecyclerView.ViewHolder(itemChatBinding.root)
    {
        fun bindViewHolder(data : ChatDTO)
        {
            itemChatBinding.run {
                this.item = data
                executePendingBindings()
            }
        }
    }

    fun addData(newData : ChatDTO)
    {
        val arrNewData = mutableListOf<ChatDTO>().apply {
            addAll(arrChat)
            add(newData)
            sortBy {
                it.createTime
            }
        }
        calDiff(arrNewData)
    }

    fun changeAllData(arrNewData : MutableList<ChatDTO>)
    {
        calDiff(arrNewData)
    }

    private fun calDiff(arrNewData : MutableList<ChatDTO>)
    {
        val diffCallback = DiffUtilChatCallback(arrChat, arrNewData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.arrChat.clear()
        this.arrChat.addAll(arrNewData)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder
    {
        return ChatViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) = holder.bindViewHolder(arrChat[position])

    override fun getItemCount(): Int = arrChat.size

    class DiffUtilChatCallback(
        private val oldData : MutableList<ChatDTO>,
        private val newData : MutableList<ChatDTO>
    ) : DiffUtil.Callback()
    {
        override fun getOldListSize(): Int
        {
            return oldData.size
        }

        override fun getNewListSize(): Int
        {
            return newData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldData[oldItemPosition].createTime == newData[newItemPosition].createTime
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }
    }
}