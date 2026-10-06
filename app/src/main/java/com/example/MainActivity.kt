package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.ui.components.MaterialDetailDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.MaterialsViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        BuildMatApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildMatApp(viewModel: MaterialsViewModel = viewModel()) {
  val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
  val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
  val selectedMaterialForDetail by viewModel.selectedMaterialForDetail.collectAsStateWithLifecycle()
  val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
  val cartQuantities by viewModel.cartQuantities.collectAsStateWithLifecycle()
  val estimateResult = viewModel.getEstimateResult()
  val cartSummary = viewModel.getCartSummary()

  var showLanguageMenu by remember { mutableStateOf(false) }
  var showWebLinkDialog by remember { mutableStateOf(false) }

  // Handle hardware Back button: return to home materials tab or dismiss modal
  BackHandler(enabled = activeTab != AppTab.MATERIALS || selectedMaterialForDetail != null) {
    if (selectedMaterialForDetail != null) {
      viewModel.selectMaterial(null)
    } else {
      viewModel.setActiveTab(AppTab.MATERIALS)
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "BuildMat India",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primary
              ) {
                Text(
                  text = "2026",
                  color = MaterialTheme.colorScheme.onPrimary,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "Price List & Multi-Language Guide (100+ Materials)",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
          }
        },
        actions = {
          // Language Switcher Dropdown Button
          Box {
            FilledTonalButton(
              onClick = { showLanguageMenu = true },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
              modifier = Modifier.testTag("language_switch_btn")
            ) {
              Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Language",
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = selectedLanguage.nativeName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            DropdownMenu(
              expanded = showLanguageMenu,
              onDismissRequest = { showLanguageMenu = false }
            ) {
              AppLanguage.values().forEach { lang ->
                DropdownMenuItem(
                  text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = lang.nativeName,
                        fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        text = "(${lang.displayName})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  },
                  onClick = {
                    viewModel.setLanguage(lang)
                    showLanguageMenu = false
                  },
                  trailingIcon = if (lang == selectedLanguage) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                  } else null,
                  modifier = Modifier.testTag("lang_opt_${lang.code}")
                )
              }
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Web Link Button
          IconButton(
            onClick = { showWebLinkDialog = true },
            modifier = Modifier.testTag("website_link_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = "Website Link",
              tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }

          // Cart Quick Icon with Badge
          BadgedBox(
            badge = {
              if (cartSummary.totalItemsCount > 0) {
                Badge {
                  Text(text = "${cartSummary.totalItemsCount}")
                }
              }
            },
            modifier = Modifier.padding(end = 8.dp)
          ) {
            IconButton(
              onClick = { viewModel.setActiveTab(AppTab.CART) },
              modifier = Modifier.testTag("cart_top_btn")
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
      ) {
        AppTab.values().forEach { tab ->
          val isSelected = activeTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { viewModel.setActiveTab(tab) },
            icon = {
              Text(
                text = tab.iconEmoji,
                fontSize = if (isSelected) 20.sp else 16.sp
              )
            },
            label = {
              Text(
                text = tab.title,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            },
            modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (activeTab) {
        AppTab.MATERIALS -> {
          MaterialsScreen(
            materials = viewModel.getFilteredMaterials(),
            currentLanguage = selectedLanguage,
            selectedCategory = selectedCategory,
            searchQuery = searchQuery,
            viewMode = viewMode,
            sortOption = sortOption,
            cartQuantities = cartQuantities,
            onCategorySelected = { viewModel.setCategory(it) },
            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
            onViewModeChanged = { viewModel.setViewMode(it) },
            onSortOptionChanged = { viewModel.setSortOption(it) },
            onMaterialClick = { viewModel.selectMaterial(it) },
            onAddToCart = { viewModel.addToCart(it.id, 1) }
          )
        }

        AppTab.ESTIMATOR -> {
          HouseEstimatorScreen(
            estimateResult = estimateResult,
            onAreaChanged = { viewModel.setArea(it) },
            onPackageChanged = { viewModel.setPackage(it) }
          )
        }

        AppTab.COMPARISON -> {
          PriceComparisonScreen(
            materials = viewModel.getFilteredMaterials(),
            currentLanguage = selectedLanguage,
            onMaterialClick = { viewModel.selectMaterial(it) }
          )
        }

        AppTab.GUIDELINES -> {
          SiteGuidelinesScreen()
        }

        AppTab.CART -> {
          ProcurementCartScreen(
            cartSummary = cartSummary,
            currentLanguage = selectedLanguage,
            onUpdateQuantity = { id, qty -> viewModel.updateCartQty(id, qty) },
            onRemoveItem = { id -> viewModel.removeFromCart(id) },
            onClearCart = { viewModel.clearCart() },
            onBrowseMaterials = { viewModel.setActiveTab(AppTab.MATERIALS) }
          )
        }
      }

      // Material Detail Dialog if selected
      selectedMaterialForDetail?.let { material ->
        MaterialDetailDialog(
          material = material,
          currentLanguage = selectedLanguage,
          cartQuantity = cartQuantities[material.id] ?: 0,
          onDismiss = { viewModel.selectMaterial(null) },
          onUpdateCart = { qty -> viewModel.updateCartQty(material.id, qty) }
        )
      }

      // Web Link Dialog
      if (showWebLinkDialog) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val webUrl = "https://ais-pre-u3ekfm3syzpbl4p4beqqng-155169715749.asia-southeast1.run.app"
        AlertDialog(
          onDismissRequest = { showWebLinkDialog = false },
          icon = { Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
          title = { Text("BuildMat India Web Portal", fontWeight = FontWeight.Bold) },
          text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text(
                "Access the live web portal and multi-language building materials guide on any browser or device:",
                style = MaterialTheme.typography.bodySmall
              )
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text("Website Link:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(webUrl, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
              }
            }
          },
          confirmButton = {
            Button(onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                context.startActivity(intent)
              } catch (e: Exception) {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Website Link", webUrl))
                Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
              }
              showWebLinkDialog = false
            }) {
              Text("Open Browser")
            }
          },
          dismissButton = {
            OutlinedButton(onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              clipboard.setPrimaryClip(ClipData.newPlainText("Website Link", webUrl))
              Toast.makeText(context, "Copied website link to clipboard!", Toast.LENGTH_SHORT).show()
              showWebLinkDialog = false
            }) {
              Text("Copy Link")
            }
          }
        )
      }
    }
  }
}
