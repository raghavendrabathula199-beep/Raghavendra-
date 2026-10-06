package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.ui.theme.GstBadgeColor
import com.example.ui.theme.PriceBadgeColor
import com.example.ui.theme.QualityBadgeColor

@Composable
fun MaterialDetailDialog(
  material: BuildingMaterial,
  currentLanguage: AppLanguage,
  cartQuantity: Int,
  onDismiss: () -> Unit,
  onUpdateCart: (Int) -> Unit
) {
  var quantity by remember { mutableStateOf(if (cartQuantity > 0) cartQuantity else 1) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.92f)
        .testTag("material_detail_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Dialog Top Bar with Close button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = material.category.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = material.category.getLocalizedName(currentLanguage),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Updated for India (2026)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_detail_dialog")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }

        // Scrollable Body Content
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
          // Main Title
          Text(
            text = material.getName(currentLanguage),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Multi-Language Names Grid Card
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "🌐 Multi-Language Names",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(modifier = Modifier.fillMaxWidth()) {
                LanguageBadge("English", material.nameEn, Modifier.weight(1f))
                LanguageBadge("हिन्दी (Hindi)", material.nameHi, Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(modifier = Modifier.fillMaxWidth()) {
                LanguageBadge("తెలుగు (Telugu)", material.nameTe, Modifier.weight(1f))
                LanguageBadge("தமிழ் (Tamil)", material.nameTa, Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(modifier = Modifier.fillMaxWidth()) {
                LanguageBadge("ಕನ್ನಡ (Kannada)", material.nameKn, Modifier.weight(1f))
                LanguageBadge("മലയാളം (Malayalam)", material.nameMl, Modifier.weight(1f))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 2026 Price & Quality Comparison
          Text(
            text = "💰 2026 Market Pricing & Quality Comparison",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            PriceCard(
              title = "Standard Grade",
              price = "₹${material.approxPrice2026.toInt()}",
              unit = material.unit,
              range = "₹${material.priceRangeMin.toInt()} - ₹${material.priceRangeMax.toInt()}",
              color = PriceBadgeColor,
              modifier = Modifier.weight(1f)
            )

            PriceCard(
              title = "Premium Grade",
              price = "₹${material.premiumPrice2026.toInt()}",
              unit = material.unit,
              range = "Top Spec",
              color = QualityBadgeColor,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // GST & House Requirement Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = GstBadgeColor.copy(alpha = 0.12f),
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "GST Rate", style = MaterialTheme.typography.labelSmall, color = GstBadgeColor)
                Text(
                  text = "${material.gstPercentage}% GST",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = GstBadgeColor
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = MaterialTheme.colorScheme.secondaryContainer,
              modifier = Modifier.weight(2f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = "Estimated for 1,000 sq.ft House",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                  text = material.estimatedQtyFor1000SqFt,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Brand Examples
          DetailSection(
            icon = Icons.Default.Stars,
            title = "Common Brands in India",
            content = material.brandExamples.joinToString(" • ")
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Quality Grade & Standard
          DetailSection(
            icon = Icons.Default.Verified,
            title = "Quality Grade & Certification",
            content = material.qualityGrade
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Typical Uses
          DetailSection(
            icon = Icons.Default.Build,
            title = "Typical Construction Uses",
            content = material.typicalUses
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Buying Tips
          DetailSection(
            icon = Icons.Default.ShoppingCart,
            title = "Smart Buying Tips",
            content = material.buyingTips
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Tips to Identify Genuine Products
          DetailSection(
            icon = Icons.Default.ThumbUp,
            title = "How to Identify Genuine Products (ISI Tests)",
            content = material.genuineCheckTips,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Storage Recommendations
          DetailSection(
            icon = Icons.Default.Inventory2,
            title = "Storage Recommendations on Site",
            content = material.storageRecommendations
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Safety Precautions
          DetailSection(
            icon = Icons.Default.HealthAndSafety,
            title = "Site Safety Precautions",
            content = material.safetyPrecautions,
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
          )
        }

        // Bottom Action Bar: Quantity & Add to Cart
        Surface(
          tonalElevation = 8.dp,
          shadowElevation = 8.dp,
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Quantity Stepper
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            ) {
              IconButton(
                onClick = { if (quantity > 1) quantity-- },
                enabled = quantity > 1,
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
              }
              Text(
                text = "$quantity",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
              )
              IconButton(
                onClick = { quantity++ },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
              }
            }

            // Total approximate cost
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Total (w/o GST)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "₹${(material.approxPrice2026 * quantity).toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = PriceBadgeColor
              )
            }

            Button(
              onClick = {
                onUpdateCart(quantity)
                onDismiss()
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("save_to_cart_btn")
            ) {
              Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = if (cartQuantity > 0) "Update List" else "Add to List")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LanguageBadge(langTitle: String, name: String, modifier: Modifier = Modifier) {
  Column(modifier = modifier.padding(horizontal = 4.dp)) {
    Text(
      text = langTitle,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontSize = 10.sp
    )
    Text(
      text = name,
      style = MaterialTheme.typography.bodySmall,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
private fun PriceCard(
  title: String,
  price: String,
  unit: String,
  range: String,
  color: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(text = title, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
      Text(text = price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = color)
      Text(text = "/ $unit", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = range, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
    }
  }
}

@Composable
private fun DetailSection(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  content: String,
  containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = containerColor,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, lineHeight = 18.sp)
    }
  }
}
