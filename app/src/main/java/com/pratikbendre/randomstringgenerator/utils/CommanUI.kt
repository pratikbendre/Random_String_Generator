package com.pratikbendre.randomstringgenerator.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TitleText(text: String) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )
    }
}

