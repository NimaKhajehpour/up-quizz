package com.nima.upquizz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TagComponent(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: (@Composable() () -> Unit)? = null,
    onClick: () -> Unit
    ) {
    AssistChip(
        modifier = modifier.padding(3.dp),
        onClick = {
            onClick()
        },
        label = {
            Text(title)
        },
        leadingIcon = leadingIcon,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.tertiary,
            leadingIconContentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiaryContainer)
    )
}