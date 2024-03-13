package com.example.stockholdings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.stockholdings.components.CustomBottomBar
import com.example.stockholdings.components.CustomTopBar
import com.example.stockholdings.components.EmptyHoldingsWidget
import com.example.stockholdings.components.ErrorPage
import com.example.stockholdings.components.HoldingWidget
import com.example.stockholdings.model.StockResponse
import com.example.stockholdings.repository.DataRepository
import com.example.stockholdings.ui.theme.StockHoldingsTheme
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
            try {
                val response = dataRepository.getData()
                setContent { StockHoldingsTheme { HoldingPage(response) } }
            } catch (e: Exception) {
                setContent { StockHoldingsTheme { ErrorPage() } }
            }
        }
    }
}

@Composable
fun HoldingPage(stockResponse: StockResponse) {
    Scaffold(
        topBar = { CustomTopBar() },
        bottomBar = {
            if (stockResponse.userHolding.isNotEmpty()) {
                CustomBottomBar(stockResponse = stockResponse)
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
                if (stockResponse.userHolding.isEmpty()) {
                    EmptyHoldingsWidget()
                }
                stockResponse.userHolding.forEach {
                    HoldingWidget(
                        it,
                        showDivider = stockResponse.userHolding.indexOf(it) != stockResponse.userHolding.lastIndex
                    )
                }
                Surface(color = Color(0xFFc7c7cc), modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }
    }
}
