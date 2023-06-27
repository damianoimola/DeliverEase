package com.madm.common_libs.internal_storage_manager

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.madm.common_libs.model.Calendar
import com.madm.common_libs.model.WorkDay
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList


fun saveDraftCalendar(context: Context, calendar: Calendar) {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    try {
        val file: File = File(context.filesDir, "draft_calendar.json")

        // Create the directory if it doesn't exist
        if (!file.exists())
            file.createNewFile()


        val cal: Calendar = if (file.exists()) {
            // If the file exists, read its content
            val jsonContent = file.readText()
            gson.fromJson(jsonContent, Calendar::class.java) ?: Calendar()
        } else {
            // If the file doesn't exist, create an empty list
            Calendar()
        }

//        val objectList: MutableList<WorkDay> = if (file.exists()) {
//            // If the file exists, read its content
//            val jsonContent = file.readText()
//            gson.fromJson(jsonContent, Calendar::class.java)?.days?.toMutableList() ?: mutableListOf()
//        } else {
//            // If the file doesn't exist, create an empty list
//            mutableListOf()
//        }

        cal.days = cal.days + calendar.days

        // Convert the updated list back to JSON
        val updatedJson = gson.toJson(cal)

        // Write the JSON back to the file
        file.writeText(updatedJson)

        println("Object added successfully.")
    } catch (e: IOException) {
        // Handle file I/O errors
        println("Error occurred while accessing the file: ${e.message}")
    } catch (e: Exception) {
        // Handle other exceptions
        println("An error occurred: ${e.message}")
    }
}


fun retrieveDraftCalendar(context: Context): List<WorkDay>? {
    val gson: Gson = GsonBuilder().create()
    try {
        val file = File(context.filesDir, "draft_calendar.json")
        if (!file.exists()) {
            return null
        }
        val jsonContent = file.readText()
        return gson.fromJson(jsonContent, Calendar::class.java)?.days?.toList()
    } catch (e: IOException) {
        println("Error occurred while reading the file: ${e.message}")
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
    return null
}


fun deleteDraftDays(context: Context, datesToDelete: List<WorkDay>): Boolean {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    try {
        val file = File(context.filesDir, "draft_calendar.json")
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

        println("Objects deleted successfully.")
        return true
    } catch (e: IOException) {
        println("Error occurred while reading/writing the file: ${e.message}")
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
    return false
}
