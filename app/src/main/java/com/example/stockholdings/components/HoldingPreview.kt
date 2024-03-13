package com.example.stockholdings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.stockholdings.HoldingPage
import com.example.stockholdings.model.StockResponse
import com.example.stockholdings.ui.theme.StockHoldingsTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Preview(showBackground = true)
@Composable
fun HoldingPreview() {
    val response =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(StockResponse::class.java)
            .fromJson(
                """
            {
              "userHolding": [
                {
                  "symbol": "TCS",
                  "quantity": 10,
                  "ltp": 3250.50,
                  "avgPrice": 2480.30,
                  "close": 3312
                },
                {
                  "symbol": "Wipro",
                  "quantity": 80,
                  "ltp": 550.20,
                  "avgPrice": 380.30,
                  "close": 580
                },
                {
                  "symbol": "SBI",
                  "quantity": 12,
                  "ltp": 650.50,
                  "avgPrice": 680.30,
                  "close": 613
                },
                {
                  "symbol": "TataMotors",
                  "quantity": 100,
                  "ltp": 650.50,
                  "avgPrice": 280.30,
                  "close": 780
                },
                {
                  "symbol": "Reliance",
                  "quantity": 10,
                  "ltp": 2887.10,
                  "avgPrice": 2780.30,
                  "close": 2610
                }
              ]
            }
        """.trimIndent()
            )
    StockHoldingsTheme {
        if (response != null) {
            HoldingPage(response)
        }
    }
}