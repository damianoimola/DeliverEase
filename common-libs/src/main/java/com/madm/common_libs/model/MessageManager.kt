package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


data class MessagesHandler(var receiverId : String, var context: Context){
    private var messageList: MessageList? = null
    private val s: Server = Server(context)

    fun getReceivedMessages(callbackFunction: (List<Message>) -> Unit) {
        s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList!!.messages.filter { it.receiverID == this.receiverId })
        }
    }

    fun getSentMessages(senderId: String, callbackFunction: (List<Message>) -> Unit){
        s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList!!.messages.filter { it.senderID == senderId })
        }
    }

    fun getConversationMessages(senderId: String, callbackFunction: (List<Message>) -> Unit) {
        s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(
                this.messageList!!.messages.filter {
                    (it.senderID == senderId && it.receiverID == this.receiverId)
                    ||
                    (it.senderID == this.receiverId && it.receiverID == senderId)
                }
            )
        }
    }
}




@Parcelize
data class MessageList(
    @IgnoredOnParcel var messages: List<Message> = listOf()
) : Parcelable



@Parcelize
data class Message(
    @IgnoredOnParcel var senderID: String? = null,
    @IgnoredOnParcel var receiverID: String? = null,
    @IgnoredOnParcel var body: String? = null,
    @IgnoredOnParcel var messageType: MessageType? = null
) : Parcelable {
    enum class MessageType { REQUEST, NOTIFICATION, ACCEPTANCE }

    @IgnoredOnParcel
    private val type: String
    get() = when(this.messageType){
        MessageType.REQUEST -> "REQUEST"
        MessageType.NOTIFICATION -> "NOTIFICATION"
        MessageType.ACCEPTANCE -> "ACCEPTANCE"
        else -> "ERROR"
    }

    fun send(context : Context){
        val s : Server = Server(context)
        s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES)
    }
}








