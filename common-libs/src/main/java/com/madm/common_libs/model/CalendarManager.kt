package com.madm.common_libs.model

import android.content.Context
import android.os.Parcelable
import com.madm.common_libs.server.Server
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Date


data class CalendarManager (
    var context: Context
) {
    private var calendar : Calendar? = null
    private val s: Server = Server(context)


    fun getDays(callbackFunction: (List<Day>) -> Unit) {
        s.makeGetRequest<Calendar>(Server.RequestKind.CALENDAR) { ret ->
            this.calendar = ret
            callbackFunction(this.calendar!!.days)
        }
    }
}

data class Calendar(
    @IgnoredOnParcel var days: List<Day> = listOf()
)


// TODO: refactor in WorkDay
@Parcelize
data class Day(
    @IgnoredOnParcel var date: Date? = null,
    @IgnoredOnParcel var riders: List<String>? = null
) : Parcelable