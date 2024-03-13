package com.example.stockholdings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.stockholdings.model.StockResponse
import com.example.stockholdings.repository.DataRepository
import com.example.stockholdings.ui.theme.StockHoldingsTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class MainActivity : ComponentActivity() {
    private val dataRepository = DataRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.Main) {
            var response = StockResponse(userHolding = emptyList())
            try {
                response = dataRepository.getData()
            } catch (e: Exception) {
                println(e)
            }
            setContent {
                StockHoldingsTheme {
                    HoldingPage(response)
                }
            }
        }
    }
}

@Composable
fun HoldingPage(stockResponse: StockResponse) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (isExpanded) +90f else -90f, label = "icon rotator")

    Scaffold(
        bottomBar = {
            if (stockResponse.userHolding.isNotEmpty()) {
                BottomAppBar(containerColor = Color.White,
                    contentPadding = PaddingValues(horizontal = Dp(8f)),
                    modifier = Modifier
                        .height(if (isExpanded) Dp(240f) else Dp(96f))
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .animateContentSize(
                            animationSpec = snap()
                        )) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Back Arrow",
                            modifier = Modifier
                                .rotate(rotation)
                                .then(Modifier.align(Alignment.CenterHorizontally)),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        if (isExpanded) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(Dp(16f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    val totalPNLString =
                                        "₹${"%.2f".format(stockResponse.calculateTotalCurrentValue())}"
                                    Text(text = "Current Value: ", fontWeight = FontWeight.Bold)
                                    Text(text = totalPNLString)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    val totalPNLString =
                                        "₹${"%.2f".format(stockResponse.calculateTotalInvestment())}"
                                    Text(text = "Total Investment: ", fontWeight = FontWeight.Bold)
                                    Text(text = totalPNLString)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    val totalPNLString =
                                        "₹${"%.2f".format(stockResponse.calculateDailyPNL())}"
                                    Text(
                                        text = "Today's Profit & Loss: ",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = totalPNLString)
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(Modifier.padding(top = if (isExpanded) Dp(64f) else Dp(16f))),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val totalPNLString =
                                "₹${"%.2f".format(stockResponse.calculateTotalPNL())}"
                            Text(text = "Profit & Loss: ", fontWeight = FontWeight.Bold)
                            Text(text = totalPNLString)
                        }
                    }
                }
            }
        },
    ) { innerDefaultPadding ->
        run {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(Modifier.padding(innerDefaultPadding)),
                verticalArrangement = Arrangement.spacedBy(Dp(8f))
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Upstox Holding",
                        modifier = Modifier.padding(Dp(12f)),
                        color = Color.White
                    )
                }
                if (stockResponse.userHolding.isEmpty()) {
                    ErrorPage()
                }
                stockResponse.userHolding.forEach {
                    //Calculations for P/L, required later.
                    val currentVal = it.ltp * it.quantity
                    val investedValue = it.avgPrice * it.quantity
                    val pl = currentVal - investedValue
                    val plString = "P/L: ₹ ${"%.2f".format(pl)}"
                    val annotatedString = buildAnnotatedString {
                        append(plString.substring(0, plString.indexOf(" ")))
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold),
                        ) {
                            append(plString.substring(plString.indexOf(" ₹")))
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = Dp(8f)),
                        verticalArrangement = Arrangement.spacedBy(
                            Dp(4f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.symbol, fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "LTP: ${"%.2f".format(it.ltp)}"
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.quantity.toString(),
                            )
                            Text(
                                text = annotatedString
                            )
                        }
                        if (stockResponse.userHolding.indexOf(it) != stockResponse.userHolding.lastIndex) {
                            Divider(modifier = Modifier.padding(vertical = Dp(4f)))
                        }
                    }
                }
                Surface(color = Color(0xFFc7c7cc), modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }
    }
}

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

@Composable
fun ErrorPage() {
    Scaffold {
        run {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(Modifier.padding(it)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Error occurred while fetching data.")
            }
        }
    }
}