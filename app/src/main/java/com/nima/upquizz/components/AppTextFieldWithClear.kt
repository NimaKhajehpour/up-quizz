package com.nima.upquizz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AppTextFieldWithClear(
    modifier: Modifier = Modifier,
    value: String,
    singleLine: Boolean,
    label: String,
    isError: Boolean = false,
    supportingText: (@Composable() () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        singleLine = singleLine,
        trailingIcon = {
            IconButton(
                onClick = {
                    onClear()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(imageVector = Icons.Default.Clear, null)
            }
        },
        label = {
            Text(label)
        },
        visualTransformation = visualTransformation,
        isError = isError,
        supportingText = supportingText
    )
}