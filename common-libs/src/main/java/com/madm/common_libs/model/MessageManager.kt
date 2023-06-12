package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.network.NetworkConnection
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


data class MessagesManager(var receiverId : String, var context: Context){
    private var messageList: MessageList? = null
    private val s : Server = Server.getInstance(context)

    fun getAllMessages(callbackFunction: (List<Message>?) -> Unit) : Boolean {
        return s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
                this.messageList = ret
                callbackFunction(this.messageList?.messages)
            }
    }

    fun getReceivedMessages(callbackFunction: (List<Message>) -> Unit) : Boolean {
        return s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
                this.messageList = ret
                callbackFunction(this.messageList!!.messages.filter { it.receiverID == this.receiverId || it.receiverID == "0" })
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
    @IgnoredOnParcel var messageDate: Date = Date(),
    @IgnoredOnParcel private var type: String? = null,
) : Parcelable {
    enum class MessageType (val displayName: String) {
        REQUEST("REQUEST"),
        NOTIFICATION("NOTIFICATION"),
        ACCEPTANCE("ACCEPTANCE")
    }

    @IgnoredOnParcel
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ITALIAN)

    @IgnoredOnParcel
    var date: String = "Date not available"
        set(value){
            field = value
            this.messageDate = dateFormat.parse(field)!!
        }

    @IgnoredOnParcel
    var id: String? = UUID.randomUUID().toString()

    @IgnoredOnParcel
    val messageType: MessageType
        get() = MessageType.valueOf(this.type!!)

    init {
        this.date = dateFormat.format(this.messageDate)
    }


    fun send(context : Context, callbackFunction: (Boolean) -> Unit = { }) : Boolean{
        val s : Server = Server.getInstance(context)

        return s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES, callbackFunction)
    }
}








