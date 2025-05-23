package com.nima.upquizz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizListItem(
    modifier: Modifier = Modifier,
    isAdmin: Boolean,
    title: String,
    displayName: String,
    rate: String,
    category: String,
    description: String,
    onUserClick: () -> Unit,
    onRateClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onClick: () -> Unit
    ) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 32.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 5.dp)
                )
            FlowRow (
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 5.dp),
                verticalArrangement = Arrangement.Top,
                horizontalArrangement = Arrangement.Start
            ){
                TagComponent(
                    title = displayName,
                ) {
                    onUserClick()
                }
                TagComponent(
                    title = rate
                ) {
                    onRateClick()
                }
                TagComponent(
                    title = category
                ) {
                    onCategoryClick()
                }
            }
            Text(description, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
                )
            Button(
                onClick = {
                    onClick()
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Take Quiz")
            } //todo add quiz is admin controls
        }
    }
}