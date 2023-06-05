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

    fun addNewMessage(msg: Message) {
        messageList?.messages?.add(msg)
    }

    fun getAllMessages(callbackFunction: (MutableList<Message>?) -> Unit) {
        s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList?.messages)
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
    @IgnoredOnParcel var messages: MutableList<Message> = mutableListOf()
) : Parcelable



@Parcelize
data class Message(
    @IgnoredOnParcel var senderID: String? = null,
    @IgnoredOnParcel var receiverID: String? = null,
    @IgnoredOnParcel var body: String? = null,
    @IgnoredOnParcel var date: Date? = null,
    @IgnoredOnParcel private var type: String? = null,
) : Parcelable {
    enum class MessageType (val displayName: String) {
        REQUEST("REQUEST"),
        NOTIFICATION("NOTIFICATION"),
        ACCEPTANCE("ACCEPTANCE")
    }

    @IgnoredOnParcel
    var id: String? = null

    @IgnoredOnParcel
    val messageType: MessageType
        get() = MessageType.valueOf(this.type!!)

    fun send(context : Context, callbackFunction: (Boolean) -> Unit = { }){
        val s : Server = Server(context)
        s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES, callbackFunction)
    }
}








