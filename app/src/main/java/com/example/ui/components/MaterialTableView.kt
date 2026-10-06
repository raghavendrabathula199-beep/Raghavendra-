package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.ui.theme.GstBadgeColor
import com.example.ui.theme.PriceBadgeColor

@Composable
fun MaterialTableView(
  materials: List<BuildingMaterial>,
  currentLanguage: AppLanguage,
  onMaterialClick: (BuildingMaterial) -> Unit,
  modifier: Modifier = Modifier
) {
  val horizontalScrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("material_table_view")
  ) {
    // Scrollable Table Container
    Box(
      modifier = Modifier
        .fillMaxSize()
        .horizontalScroll(horizontalScrollState)
    ) {
      Column(modifier = Modifier.width(1050.dp)) {
        // Table Header Row
        Surface(
          color = MaterialTheme.colorScheme.primaryContainer,
          shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Material Name",
              modifier = Modifier.width(220.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Category",
              modifier = Modifier.width(130.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Unit",
              modifier = Modifier.width(100.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "2026 Price (₹)",
              modifier = Modifier.width(120.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "GST %",
              modifier = Modifier.width(70.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "1000 sq.ft Req",
              modifier = Modifier.width(170.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Brands & Grade",
              modifier = Modifier.width(200.dp),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }

        // Table Rows
        LazyColumn(
          modifier = Modifier.fillMaxSize()
        ) {
          itemsIndexed(materials) { index, material ->
            val rowBg = if (index % 2 == 0) {
              MaterialTheme.colorScheme.surface
            } else {
              MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }

            Surface(
              color = rowBg,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onMaterialClick(material) }
                .testTag("table_row_${material.id}")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Name (Localized + EN)
                Column(modifier = Modifier.width(220.dp)) {
                  Text(
                    text = material.getName(currentLanguage),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                  )
                  if (currentLanguage != AppLanguage.EN && material.getName(currentLanguage) != material.nameEn) {
                    Text(
                      text = material.nameEn,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }

                // Category
                Row(
                  modifier = Modifier.width(130.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = material.category.iconEmoji, fontSize = 14.sp)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = material.category.getLocalizedName(currentLanguage),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }

                // Unit
                Text(
                  text = material.unit,
                  modifier = Modifier.width(100.dp),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurface
                )

                // 2026 Price
                Column(modifier = Modifier.width(120.dp)) {
                  Text(
                    text = "₹${material.approxPrice2026.toInt()}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PriceBadgeColor
                  )
                  Text(
                    text = "₹${material.priceRangeMin.toInt()} - ${material.priceRangeMax.toInt()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                // GST %
                Surface(
                  color = GstBadgeColor.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(4.dp),
                  modifier = Modifier.width(60.dp)
                ) {
                  Text(
                    text = "${material.gstPercentage}%",
                    color = GstBadgeColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))

                // 1000 sq ft requirement
                Text(
                  text = material.estimatedQtyFor1000SqFt,
                  modifier = Modifier.width(170.dp),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
                )

                // Brands & Grade
                Column(modifier = Modifier.width(200.dp)) {
                  Text(
                    text = material.brandExamples.take(2).joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = material.qualityGrade,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
          }
        }
      }
    }
  }
}
