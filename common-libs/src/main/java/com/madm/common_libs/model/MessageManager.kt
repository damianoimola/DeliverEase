package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


data class MessagesManager(var receiverId : String, var context: Context){
    private var messageList: MessageList? = null
    private val s : Server = Server.getInstance(context)

    /**
     * retrieves every message saved in the server
     * @param callbackFunction: what the application should do after retrieving the messages
     * @return if the communication went well
     */
    fun getAllMessages(callbackFunction: (List<Message>?) -> Unit) : Boolean {
        return s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
                this.messageList = ret
                callbackFunction(this.messageList?.messages)
            }
    }

    /**
     * retrieves the messages of the receiver who was set in the MessageManager receiverId
     * @param callbackFunction: what the application should do after retrieving the messages
     * @return if the communication went well
     */
    fun getReceivedMessages(callbackFunction: (List<Message>) -> Unit) : Boolean {
        return s.makeGetRequest<MessageList>(Server.RequestKind.MESSAGES) { ret ->
                this.messageList = ret
                callbackFunction(this.messageList!!.messages.filter { it.receiverID == this.receiverId || it.receiverID == "0" })
            }
    }
}

/**
 * List of messages from the server
 */
@Parcelize
data class MessageList(
    @IgnoredOnParcel var messages: List<Message> = listOf()
) : Parcelable


/**
 * Represent every possible message in the application: the request for a shift change,
 * the acceptance of a change and the notification about an admin's communication or the
 * publication of the calendar.
 */
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


    /**
     * Format the message date
     */
    @IgnoredOnParcel
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ITALIAN)

    /**
     * Date of the message as a String
     */
    @IgnoredOnParcel
    var date: String = "Date not available"
        set(value){
            field = value
            this.messageDate = dateFormat.parse(field)!!
        }

    /**
     * the identification of the message
     */
    @IgnoredOnParcel
    var id: String? = UUID.randomUUID().toString()

    /**
     * the type of the message between request, acceptance and notification
     */
    @IgnoredOnParcel
    val messageType: MessageType
        get() = MessageType.valueOf(this.type!!)

    init {
        this.date = dateFormat.format(this.messageDate)
    }


    /**
     * @param context: the context of the application
     * @param callbackFunction: what the application should do after sending the message
     * @return if the communication went well
     */
    fun send(context : Context, callbackFunction: (Boolean) -> Unit = { }) : Boolean{
        val s : Server = Server.getInstance(context)

        return s.makePostRequest<Message>(this, Server.RequestKind.MESSAGES, callbackFunction)
    }
}








