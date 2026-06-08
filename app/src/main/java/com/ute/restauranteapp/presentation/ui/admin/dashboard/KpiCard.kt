package com.ute.restauranteapp.presentation.ui.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ute.restauranteapp.theme.*

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector,
    color: Color = Accent,
    hasAlert: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {

    val cardModifier = modifier.fillMaxWidth()

    val container: @Composable (@Composable () -> Unit) -> Unit = { content ->
        if (onClick != null) {
            Surface(
                onClick = onClick,
                shape = MaterialTheme.shapes.large,
                color = Surface,
                modifier = cardModifier
            ) { content() }
        } else {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = Surface,
                modifier = cardModifier
            ) { content() }
        }
    }

    container {
        Column(
            modifier = Modifier
                .background(color.copy(alpha = 0.06f), MaterialTheme.shapes.large)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color.copy(alpha = 0.15f), MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (hasAlert) {
                    Text("⚠️", fontSize = 18.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (hasAlert) Error else TextFaint
                )
            }
        }
    }
}