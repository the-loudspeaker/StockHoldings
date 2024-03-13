package com.example.stockholdings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun CustomTopBar(){
    Surface(
        color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Upstox Holding",
            modifier = Modifier.padding(Dp(12f)),
            color = Color.White
        )
    }
}