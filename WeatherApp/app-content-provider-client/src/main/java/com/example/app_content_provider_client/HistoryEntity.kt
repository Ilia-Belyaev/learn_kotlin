package com.example.app_content_provider_client

import java.util.concurrent.locks.Condition


const val ID = "id"
const val CITY = "city"
const val TEMPERATURE = "temperature"

data class HistoryEntity(
    val id: Long = 0,
    val city: String = "",
    val temperature: Int = 0,
    val condition: String = ""
)


