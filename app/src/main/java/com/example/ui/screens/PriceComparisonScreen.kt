package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.model.MaterialCategory
import com.example.ui.theme.PriceBadgeColor
import com.example.ui.theme.QualityBadgeColor

@Composable
fun PriceComparisonScreen(
  materials: List<BuildingMaterial>,
  currentLanguage: AppLanguage,
  onMaterialClick: (BuildingMaterial) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategoryFilter by remember { mutableStateOf<MaterialCategory?>(null) }

  val filteredList = remember(materials, selectedCategoryFilter) {
    if (selectedCategoryFilter == null) materials else materials.filter { it.category == selectedCategoryFilter }
  }

  Column(modifier = modifier.fillMaxSize().testTag("price_comparison_screen")) {
    // Header explanation card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "⚖️ Standard vs Premium Grade Price Index (2026)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Compare baseline construction quality versus premium branded grades to decide where to invest your budget for safety, aesthetics, and longevity.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
        )
      }
    }

    // Comparison List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filteredList, key = { it.id }) { mat ->
        val diff = mat.premiumPrice2026 - mat.approxPrice2026
        val diffPercent = if (mat.approxPrice2026 > 0) (diff / mat.approxPrice2026) * 100 else 0.0

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("compare_card_${mat.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Material title
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${mat.category.iconEmoji} ${mat.getName(currentLanguage)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primaryContainer
              ) {
                Text(
                  text = "+${diffPercent.toInt()}% Premium",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Comparison Two Columns: Standard vs Premium
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Standard Grade Column
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                    text = "Standard Grade",
                    style = MaterialTheme.typography.labelSmall,
                    color = PriceBadgeColor,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "₹${mat.approxPrice2026.toInt()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = PriceBadgeColor
                  )
                  Text(
                    text = "per ${mat.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = mat.qualityGrade,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                  )
                }
              }

              // Premium Grade Column
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = QualityBadgeColor.copy(alpha = 0.12f),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                    text = "Premium Grade",
                    style = MaterialTheme.typography.labelSmall,
                    color = QualityBadgeColor,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "₹${mat.premiumPrice2026.toInt()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = QualityBadgeColor
                  )
                  Text(
                    text = "per ${mat.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Top Tier / Architect Spec",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buying Advice Summary
            Text(
              text = "💡 ${mat.buyingTips}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
              onClick = { onMaterialClick(mat) },
              modifier = Modifier.align(Alignment.End),
              contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
              Text("View Full Specification", fontSize = 12.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
            }
          }
        }
      }
    }
  }
}
