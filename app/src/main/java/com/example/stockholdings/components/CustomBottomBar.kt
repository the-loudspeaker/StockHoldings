package com.example.stockholdings.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.example.stockholdings.model.StockResponse

@Composable
fun CustomBottomBar(stockResponse: StockResponse){
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (isExpanded) +90f else -90f, label = "icon rotator")
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