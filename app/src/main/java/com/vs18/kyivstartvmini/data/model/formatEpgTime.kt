package com.vs18.kyivstartvmini.data.model

import android.annotation.SuppressLint
import java.time.*
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
private val epgFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss Z")

@SuppressLint("NewApi")
fun parseEpgTime(value: String): ZonedDateTime{
    return ZonedDateTime
        .parse(value, epgFormatter)
        .withZoneSameInstant(ZoneId.of("Europe/Kiev"))
}

@SuppressLint("NewApi")
fun isNow(start: String, end: String): Boolean {
    val now = ZonedDateTime.now(ZoneId.of("Europe/Kiev"))
    return now.isAfter(parseEpgTime(start)) && now.isBefore(parseEpgTime(end))
}

@SuppressLint("NewApi")
fun formatTime(value: String): String =
    parseEpgTime(value)
        .format(DateTimeFormatter.ofPattern("HH:mm"))

@SuppressLint("NewApi")
fun dayLabel(value: String): String {
    val date = parseEpgTime(value).toLocalDate()
    val today = LocalDate.now(ZoneId.of("Europe/Kiev"))
    return when (date) {
        today -> "Сьогодні"
        today.plusDays(1) -> "Завтра"
        else -> date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}