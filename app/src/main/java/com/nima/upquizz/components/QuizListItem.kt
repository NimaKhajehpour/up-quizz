package com.nima.upquizz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nima.upquizz.R

@Composable
fun QuizListItem(
    modifier: Modifier = Modifier,
    isAdmin: Boolean,
    title: String,
    displayName: String,
    rate: String,
    category: String,
    description: String,
    approved: Boolean,
    expanded: Boolean = false,
    onUserClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onExpand: (() -> Unit)? = null,
    onAction: (() -> Unit)? = null,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 5.dp)
                )
            FlowRow (
                modifier = Modifier
                    .fillMaxWidth()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = approved
            ) {
                Text("Take Quiz")
            }
            if (isAdmin){
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(100))
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            if (onExpand != null) {
                                onExpand()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    IconButton(
                        onClick = onExpand ?: {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        if (expanded) {
                            Icon(painterResource(R.drawable.arrow_up), null, Modifier.size(24.dp))
                        } else {
                            Icon(painterResource(R.drawable.arrow_down), null, Modifier.size(24.dp))
                        }
                    }
                }
                AnimatedVisibility (expanded){
                    Button(
                        onClick = onAction ?: {},
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(if (approved) "Unapprove" else "Approve")
                    }
                }
            }
        }
    }
}