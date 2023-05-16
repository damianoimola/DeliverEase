package com.madm.common_libs.server


data class Message(
    var senderId: String,
    var receiverId: String,
    var messageBody: String
)

data class MessageList(var receiverId : String){
    private lateinit var messageList: List<Message>

    fun getReceivedMessages() : List<Message>{
        return messageList.filter { it.receiverId == this.receiverId }
    }

    fun getSentMessages(senderId: String) : List<Message>{
        return messageList.filter { it.senderId == senderId }
    }

    fun getConversationMessages(senderId: String) : List<Message>{
        return messageList.filter { it.senderId == senderId && it.receiverId == this.receiverId }
    }
}




