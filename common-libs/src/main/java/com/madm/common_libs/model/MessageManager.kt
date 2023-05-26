package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*


data class MessagesManager(var receiverId : String, var context: Context){
    private var messageList: MessageList? = null
    private val s: Server = Server(context)


    fun getAllMessages(callbackFunction: (List<Message>) -> Unit) {
        s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList!!.messages)
        }
    }

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
    @IgnoredOnParcel var date: Date? = null,
    @IgnoredOnParcel private var type: String? = null,
) : Parcelable {
    enum class MessageType { REQUEST, NOTIFICATION, ACCEPTANCE, ERROR }


    @IgnoredOnParcel
    val messageType: MessageType?
        get() = when(this.type){
            "REQUEST" -> MessageType.REQUEST
            "NOTIFICATION" -> MessageType.NOTIFICATION
            "ACCEPTANCE" -> MessageType.ACCEPTANCE
            else -> MessageType.ERROR
        }

    fun send(context : Context){
        val s : Server = Server(context)
        s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES)
    }
}








