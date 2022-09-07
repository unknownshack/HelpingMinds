package com.example.helpingminds.Utility.CustomClass

import com.example.helpingminds.Model.Reminder
import java.text.SimpleDateFormat
import java.util.*

class CustomDate {
    companion object {
        fun GetDateFromString(dateString: String, format: String): Calendar {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(format, Locale.ENGLISH) // eg "yyyy-MM-dd"
            cal.time = sdf.parse(dateString) // all done
            return cal
        }

        fun GetStringFromDate(date: Calendar, format: String): String {
            //e.g.: "yyyy-MM-dd'T'HH:mm:ss"
            val sdf = SimpleDateFormat(format)
            return sdf.format(date.time)
        }

        fun getTimeFromDate(dateString: String): Triple<Int, Int, String> {
            var time = dateString.split("T")[1]
            var hourOfDay = time.split(":")[0].toInt()
            var minute = time.split(":")[1].toInt()

            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} am"
                    } else {
                        "${hourOfDay + 12}:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            }
            return Triple(hourOfDay, minute, formattedTime)
        }

        fun GetStringDayFormatFromDay(day: Int): String {
            return when (day) {
                1 -> {
                    "1st"
                }
                2 -> {
                    "2nd"
                }
                3 -> {
                    "3rd"
                }
                else -> {
                    "${day}th"
                }
            }
        }

        fun getMonthString(month: Int): String {
            val result = when (month) {
                0 -> "Jan"
                1 -> "Feb"
                2 -> "Mar"
                3 -> "Apr"
                4 -> "May"
                5 -> "Jun"
                6 -> "Jul"
                7 -> "Aug"
                8 -> "Sept"
                9 -> "Oct"
                10 -> "Nov"
                11 -> "Dec"
                else -> {
                    "Apr"
                }
            }
            return result
        }

        fun getMonthFullName(month: Int): String {
            val result = when (month) {
                0 -> "January"
                1 -> "February"
                2 -> "March"
                3 -> "April"
                4 -> "May"
                5 -> "June"
                6 -> "July"
                7 -> "August"
                8 -> "September"
                9 -> "October"
                10 -> "November"
                11 -> "December"
                else -> {
                    "April"
                }
            }
            return result
        }
    }
}