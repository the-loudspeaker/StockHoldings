package com.example.stockholdings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import com.example.stockholdings.model.UserHolding

@Composable
fun HoldingWidget(it: UserHolding, showDivider: Boolean = true){
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
        if (showDivider) {
            Divider(modifier = Modifier.padding(vertical = Dp(4f)))
        }
    }
}