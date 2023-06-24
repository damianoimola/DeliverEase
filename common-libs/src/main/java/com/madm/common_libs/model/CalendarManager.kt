package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


data class CalendarManager (
    var context: Context
) {
    private var calendar : Calendar? = null
    private val s: Server = Server.getInstance(context)



    fun insertDays(workDays : List<WorkDay>) : Boolean {
        val s : Server = Server.getInstance(context)

        return s.makePostRequest<List<WorkDay>>(workDays, Server.RequestKind.CALENDAR)
    }


    fun getDays(callbackFunction: (List<WorkDay>) -> Unit) : Boolean {

        return s.makeGetRequest<Calendar>(Server.RequestKind.CALENDAR) { ret ->
                this.calendar = ret
                callbackFunction(this.calendar!!.days)
            }
    }
}

data class Calendar(
    @IgnoredOnParcel var days: List<WorkDay> = listOf()
)


@Parcelize
class WorkDay(
    @IgnoredOnParcel var riders: List<String>? = null,
) : Parcelable {

    @Transient
    @IgnoredOnParcel
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN)

    @IgnoredOnParcel
    var date: String? = null

    @Transient
    @IgnoredOnParcel
    var workDayDate: Date? = null
    set(value){
        field = value
        if(value != null)
            this.date = dateFormat.format(value)
    }
    get() {
        return if(field == null && this.date != null)
            dateFormat.parse(this.date!!)
        else if (field == null && this.date == null)
            null
        else
            field
    }

    fun insertOrUpdate(context : Context) : Boolean{
        val s : Server = Server.getInstance(context)

        return s.makePostRequest<WorkDay>(this, Server.RequestKind.CALENDAR)
    }
}