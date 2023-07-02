package com.madm.deliverease.ui.widgets

import java.time.LocalDate

fun localDateFormat(weekDay: WeekDay, selectedWeek: Int, selectedYear: Int): LocalDate {
    var year = selectedYear
    //caso cambio anno
    if(weekDay.number < 7 && weekDay.month == 12 && selectedWeek != 0 && selectedWeek != 1) year++

    var month = weekDay.month
    //caso settimana spezzata
    if (weekDay.number < 7 && selectedWeek != 0 && selectedWeek != 1) {
        //caso non dicembre
        if(weekDay.month != 12) month++
        //caso dicembre
        else month = 1
    }

    return LocalDate.of(year, month, weekDay.number)
}