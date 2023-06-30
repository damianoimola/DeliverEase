package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


/**
 * Manages the communication with the server about the calendar, to read and (over)write it
 */
data class CalendarManager(
    var context: Context
) {
    private var calendar: Calendar? = null
    private val s: Server = Server.getInstance(context)


    fun insertDays(workDays: List<WorkDay>): Boolean {
        val s: Server = Server.getInstance(context)

        return s.makePostRequest<List<WorkDay>>(workDays, Server.RequestKind.CALENDAR)
    }


    fun getDays(callbackFunction: (List<WorkDay>) -> Unit): Boolean {

        return s.makeGetRequest<Calendar>(Server.RequestKind.CALENDAR) { ret ->
            this.calendar = ret
            callbackFunction(this.calendar!!.days)
        }
    }
}

/**
 * Contains every working day of the pizzeria saved inside the server
 */
data class Calendar(
    @IgnoredOnParcel var days: List<WorkDay> = listOf()
)

/**
 * Contains every information about a rider working day
 */
@Parcelize
class WorkDay(
    @IgnoredOnParcel var riders: List<String>? = null,
) : Parcelable {

    /**
     * Format the working day is saved to
     */
    @Transient
    @IgnoredOnParcel
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)

    /**
     * Date of the working day as a String
     */
    @IgnoredOnParcel
    var date: String? = null

    /**
     * Date of the working day as a Date
     */
    @Transient
    @IgnoredOnParcel
    var workDayDate: Date? = null
        set(value) {
            field = value
            if (value != null)
                this.date = dateFormat.format(value)
        }
        get() {
            return if (field == null && this.date != null)
                dateFormat.parse(this.date!!)
            else if (field == null && this.date == null)
                null
            else
                field
        }
}