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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.PriceBadgeColor
import com.example.viewmodel.CartSummary

@Composable
fun ProcurementCartScreen(
  cartSummary: CartSummary,
  currentLanguage: AppLanguage,
  onUpdateQuantity: (String, Int) -> Unit,
  onRemoveItem: (String) -> Unit,
  onClearCart: () -> Unit,
  onBrowseMaterials: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Column(modifier = modifier.fillMaxSize().testTag("procurement_cart_screen")) {
    if (cartSummary.entries.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "🛒", fontSize = 56.sp)
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Your Procurement List is Empty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Add materials from the catalog to calculate total procurement expenses, GST breakdown, and create supplier quotations.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onBrowseMaterials,
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Browse 100+ Materials")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Summary Header Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Procurement Quotation (2026)",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                TextButton(onClick = onClearCart) {
                  Text("Clear All", color = MaterialTheme.colorScheme.error)
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Subtotal (Excl. GST):", style = MaterialTheme.typography.bodySmall)
                Text(text = "₹${String.format("%,.0f", cartSummary.subtotal)}", fontWeight = FontWeight.SemiBold)
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Total GST Tax:", style = MaterialTheme.typography.bodySmall)
                Text(text = "₹${String.format("%,.0f", cartSummary.totalGst)}", fontWeight = FontWeight.SemiBold)
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
              ) {
                Text(text = "Estimated Grand Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(
                  text = "₹${String.format("%,.0f", cartSummary.grandTotal)}",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }

        items(cartSummary.entries, key = { it.material.id }) { entry ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "${entry.material.category.iconEmoji} ${entry.material.getName(currentLanguage)}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                  )
                  Text(
                    text = "₹${entry.material.approxPrice2026.toInt()} / ${entry.material.unit} (+${entry.material.gstPercentage}% GST)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                IconButton(onClick = { onRemoveItem(entry.material.id) }, modifier = Modifier.size(28.dp)) {
                  Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Stepper
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                ) {
                  IconButton(
                    onClick = { onUpdateQuantity(entry.material.id, entry.quantity - 1) },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                  }
                  Text(
                    text = "${entry.quantity}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                  )
                  IconButton(
                    onClick = { onUpdateQuantity(entry.material.id, entry.quantity + 1) },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                  }
                }

                // Item Total with GST
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "₹${String.format("%,.0f", entry.totalWithGst)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PriceBadgeColor
                  )
                  Text(
                    text = "Incl. GST: ₹${String.format("%,.0f", entry.gstAmount)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // Bottom Action Bar: Copy / Share Quotation
      Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onBrowseMaterials,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Items")
          }

          Button(
            onClick = {
              val quoteText = buildString {
                appendLine("=== BUILDING MATERIALS PROCUREMENT QUOTATION (2026) ===")
                appendLine("Generated via BuildMat India")
                appendLine("--------------------------------------------------")
                cartSummary.entries.forEach { e ->
                  appendLine("• ${e.material.nameEn} (${e.material.getName(currentLanguage)}): ${e.quantity} ${e.material.unit} @ ₹${e.material.approxPrice2026.toInt()} = ₹${String.format("%,.0f", e.itemCost)} + GST(${e.material.gstPercentage}%) = ₹${String.format("%,.0f", e.totalWithGst)}")
                }
                appendLine("--------------------------------------------------")
                appendLine("Subtotal: ₹${String.format("%,.0f", cartSummary.subtotal)}")
                appendLine("GST: ₹${String.format("%,.0f", cartSummary.totalGst)}")
                appendLine("GRAND TOTAL: ₹${String.format("%,.0f", cartSummary.grandTotal)}")
              }
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Material Quotation", quoteText)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Copied procurement quotation to clipboard!", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .weight(1.4f)
              .testTag("copy_quote_btn")
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Copy Quotation")
          }
        }
      }
    }
  }
}
