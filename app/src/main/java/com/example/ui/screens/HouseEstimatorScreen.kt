package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ConstructionPackage
import com.example.data.HouseEstimateResult
import com.example.ui.theme.PriceBadgeColor
import com.example.ui.theme.QualityBadgeColor

@Composable
fun HouseEstimatorScreen(
  estimateResult: HouseEstimateResult,
  onAreaChanged: (Double) -> Unit,
  onPackageChanged: (ConstructionPackage) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val quickAreas = listOf(600.0, 800.0, 1000.0, 1200.0, 1500.0, 2000.0)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("house_estimator_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Hero Card: Total Budget
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("estimate_hero_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "${estimateResult.builtUpAreaSqFt.toInt()} sq.ft House Budget (2026)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Estimated Complete Construction Cost",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surface
            ) {
              Text(
                text = "₹${estimateResult.costPerSqFt.toInt()}/sq.ft",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Big Price Display
          Text(
            text = estimateResult.costInLakhs,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )

          Text(
            text = "Total: ₹${String.format("%,.0f", estimateResult.totalEstimatedCost)} INR",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
          )

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(12.dp))

          // Split: Materials 65% vs Labor 35%
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Materials (~65%)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
              Text(
                text = "₹${String.format("%,.0f", estimateResult.materialCost)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Labor & Supervision (~35%)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
              Text(
                text = "₹${String.format("%,.0f", estimateResult.laborCost)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        }
      }
    }

    // Built-up Area Selector Slider & Quick Chips
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Built-up Area (Sq.Ft)",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = "${estimateResult.builtUpAreaSqFt.toInt()} sq.ft",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Slider(
            value = estimateResult.builtUpAreaSqFt.toFloat(),
            onValueChange = { onAreaChanged(it.toDouble()) },
            valueRange = 400f..3000f,
            steps = 25,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("area_slider")
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Quick selection chips
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            quickAreas.forEach { area ->
              val isSelected = estimateResult.builtUpAreaSqFt.toInt() == area.toInt()
              FilterChip(
                selected = isSelected,
                onClick = { onAreaChanged(area) },
                label = { Text("${area.toInt()}", fontSize = 11.sp) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("area_chip_${area.toInt()}")
              )
            }
          }
        }
      }
    }

    // Construction Quality Package Selector
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Select Construction Quality Package",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))

          ConstructionPackage.values().forEach { pack ->
            val isSelected = estimateResult.selectedPackage == pack
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              border = if (isSelected) null else CardDefaults.outlinedCardBorder(),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              onClick = { onPackageChanged(pack) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                RadioButton(
                  selected = isSelected,
                  onClick = { onPackageChanged(pack) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = pack.label,
                      fontWeight = FontWeight.Bold,
                      style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                      text = "₹${pack.baseRatePerSqFt.toInt()}/sq.ft",
                      fontWeight = FontWeight.ExtraBold,
                      color = PriceBadgeColor,
                      style = MaterialTheme.typography.bodyMedium
                    )
                  }
                  Text(
                    text = pack.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }
    }

    // Bill of Quantities Breakdown List
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "📦 Estimated Bill of Quantities",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        IconButton(onClick = {
          val summary = buildString {
            appendLine("=== BUILT-UP ESTIMATE (2026) ===")
            appendLine("Area: ${estimateResult.builtUpAreaSqFt.toInt()} sq.ft")
            appendLine("Package: ${estimateResult.selectedPackage.label} (₹${estimateResult.costPerSqFt.toInt()}/sq.ft)")
            appendLine("Total Estimated Budget: ${estimateResult.costInLakhs} (₹${String.format("%,.0f", estimateResult.totalEstimatedCost)})")
            appendLine("--- Material & Quantity Breakdown ---")
            estimateResult.items.forEach {
              appendLine("${it.name}: ${it.quantityStr} - ₹${String.format("%,.0f", it.approxCost)}")
            }
          }
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("BuildMat House Estimate", summary)
          clipboard.setPrimaryClip(clip)
          Toast.makeText(context, "Copied estimate summary to clipboard!", Toast.LENGTH_SHORT).show()
        }) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Copy Summary")
        }
      }
    }

    items(estimateResult.items) { item ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = item.iconEmoji, fontSize = 24.sp)
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = item.name,
              fontWeight = FontWeight.SemiBold,
              style = MaterialTheme.typography.bodyMedium
            )
            Text(
              text = "Required: ${item.quantityStr}",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "₹${String.format("%,.0f", item.approxCost)}",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${String.format("%.1f", item.percentOfTotal)}% of total",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
