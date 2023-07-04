package com.madm.common_libs.internal_storage_manager

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.madm.common_libs.R
import com.madm.common_libs.model.Calendar
import com.madm.common_libs.model.WorkDay
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList


/**
 * Stores the calendar draft in local to have it saved but not published
 * @param context the context of the application
 * @param newDraftCalendar a draft of the calendar still not saved in the server
 */
fun saveDraftCalendar(context: Context, newDraftCalendar: Calendar) {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    try {
        val file = File(context.filesDir, context.getString(R.string.draft_calendar_file_name))

        // Create the directory if it doesn't exist
        if (!file.exists())
            file.createNewFile()

        // Retrieve file content if exists
        val existingCalendar: Calendar =
            if (file.exists()) {
                // If the file exists, read its content
                val jsonContent = file.readText()
                gson.fromJson(jsonContent, Calendar::class.java) ?: Calendar()
            } else {
                // If the file doesn't exist, create an empty list
                Calendar()
            }

        // create a copy of actual drafted days (because is a List, so unmodifiable)
        val copyExistingCalendarDays = ArrayList(existingCalendar.days)

        // if new days are still drafted, update them
        newDraftCalendar.days.forEach { workDay ->
            val stillDraftedDate = workDay.date in existingCalendar.days.map { it.date }.distinct()

            if(stillDraftedDate) {
                val prevDraftedDay = copyExistingCalendarDays.indexOfFirst { it.date == workDay.date }
                copyExistingCalendarDays[prevDraftedDay] = workDay
            } else {
                copyExistingCalendarDays.add(workDay)
            }
        }

        // update old drafted days with the new ones
        existingCalendar.days = copyExistingCalendarDays

        // convert the updated list back to JSON
        val updatedJson = gson.toJson(existingCalendar)

        // write the JSON back to the file
        file.writeText(updatedJson)
    } catch (e: IOException) {
        // Handle file I/O errors
        Log.e("ERROR", "Error occurred while accessing the file: ${e.message}")
    } catch (e: Exception) {
        // Handle other exceptions
        Log.e("ERROR", "An error occurred: ${e.message}")
    }
}

/**
 * Parses the gson saved and retrieves the list of working day still in draft
 * @param context: the context of the application
 * @return the list of working day still in draft
 */
fun retrieveDraftCalendar(context: Context): List<WorkDay>? {
    val gson: Gson = GsonBuilder().create()
    try {
        val file = File(context.filesDir, context.getString(R.string.draft_calendar_file_name))
        if (!file.exists()) {
            return null
        }
        val jsonContent = file.readText()
        return gson.fromJson(jsonContent, Calendar::class.java)?.days?.toList()
    } catch (e: IOException) {
        Log.e("ERROR", "Error occurred while reading the file: ${e.message}")
    } catch (e: Exception) {
        Log.e("ERROR", "An error occurred: ${e.message}")
    }
    return null
}


/**
 * deletes the list of drafted days in the local file passed as argument
 * @param context the context of the application
 * @param datesToDelete
 * @return if every working day passed has been deleted
 */
fun deleteDraftDays(context: Context, datesToDelete: List<WorkDay>): Boolean {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    try {
        val file = File(context.filesDir, context.getString(R.string.draft_calendar_file_name))
        if (!file.exists()) {
            return false
        }

        val jsonContent = file.readText()
        val cal = gson.fromJson(jsonContent, Calendar::class.java) ?: Calendar()

        // Remove objects based on objectIdsToDelete
        val list = ArrayList(cal.days)
        val iterator = list.iterator()

        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (datesToDelete.any{it.date == obj.date}) {
                iterator.remove()
            }
        }

        // update object
        cal.days = list

        val updatedJson = gson.toJson(cal)
        file.writeText(updatedJson)
        return true
    } catch (e: IOException) {
        Log.e("ERROR", "Error occurred while reading/writing the file: ${e.message}")
    } catch (e: Exception) {
        Log.e("ERROR", "An error occurred: ${e.message}")
    }
    return false
}

