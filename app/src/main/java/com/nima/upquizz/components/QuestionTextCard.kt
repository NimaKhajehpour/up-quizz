package com.nima.upquizz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionTextCard(
    modifier: Modifier = Modifier,
    text: String
    ) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Text(text, modifier = Modifier.padding(16.dp).fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium
            )
    }
}