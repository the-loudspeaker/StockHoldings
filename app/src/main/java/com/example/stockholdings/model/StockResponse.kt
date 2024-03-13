package com.example.stockholdings.model

import com.squareup.moshi.Json

data class StockResponse(
    @Json(name = "userHolding") var userHolding: List<UserHolding>
) {
    fun calculateTotalCurrentValue(): Double {
        return userHolding.sumOf { it.ltp*it.quantity }
    }
    fun calculateTotalInvestment(): Double {
        return userHolding.sumOf { it.avgPrice*it.quantity }
    }
    fun calculateTotalPNL(): Double = calculateTotalCurrentValue()-calculateTotalInvestment()
    fun calculateDailyPNL(): Double {
        return userHolding.sumOf { (it.close-it.ltp)*it.quantity }
    }
}

data class UserHolding(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "ltp") val ltp: Double,
    @Json(name = "avgPrice") val avgPrice: Double,
    @Json(name = "close") val close: Int
)
