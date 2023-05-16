package com.madm.common_libs.server

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    @IgnoredOnParcel var senderID: String? = null,
    @IgnoredOnParcel var receiverID: String? = null,
    @IgnoredOnParcel var message: String? = null
) : Parcelable {
    fun send(context : Context){
        val s : Server = Server(context)
        s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES)
    }
}

data class MessagesHandler(var receiverId : String, var context: Context){
    private var messageList: List<Message> = listOf()


    fun getReceivedMessages(callbackFunction: (List<Message>) -> Unit) {
        val s: Server = Server(context)

        s.makeListsGetRequest<Message>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList.filter { it.receiverID == this.receiverId })
        }
    }

    fun getSentMessages(senderId: String, callbackFunction: (List<Message>) -> Unit){
        val s: Server = Server(context)

        s.makeListsGetRequest<Message>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList.filter { it.senderID == senderId })
        }
    }

    fun getConversationMessages(senderId: String, callbackFunction: (List<Message>) -> Unit) {
        val s: Server = Server(context)

        s.makeListsGetRequest<Message>(Server.RequestKind.MESSAGES) { ret ->
            this.messageList = ret
            callbackFunction(this.messageList.filter {
                    (it.senderID == senderId && it.receiverID == this.receiverId)
                    ||
                    (it.senderID == this.receiverId && it.receiverID == senderId)
                }
            )
        }
    }

}




