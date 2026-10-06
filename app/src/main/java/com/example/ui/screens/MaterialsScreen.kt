package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.MaterialsRepository
import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.model.MaterialCategory
import com.example.ui.components.MaterialCard
import com.example.ui.components.MaterialTableView
import com.example.viewmodel.SortOption
import com.example.viewmodel.ViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialsScreen(
  materials: List<BuildingMaterial>,
  currentLanguage: AppLanguage,
  selectedCategory: MaterialCategory,
  searchQuery: String,
  viewMode: ViewMode,
  sortOption: SortOption,
  cartQuantities: Map<String, Int>,
  onCategorySelected: (MaterialCategory) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onViewModeChanged: (ViewMode) -> Unit,
  onSortOptionChanged: (SortOption) -> Unit,
  onMaterialClick: (BuildingMaterial) -> Unit,
  onAddToCart: (BuildingMaterial) -> Unit,
  modifier: Modifier = Modifier
) {
  var showSortMenu by remember { mutableStateOf(false) }

  Column(modifier = modifier.fillMaxSize()) {
    // Search Bar & Filter Controls Row
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = {
          Text(
            text = "Search 100+ materials, brands, uses...",
            fontSize = 14.sp
          )
        },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChanged("") }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("material_search_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // View Mode Toggle (Cards vs Table) & Sort Button Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Material Count Indicator
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = "${materials.size} Materials (2026)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Sort Dropdown
          Box {
            TextButton(
              onClick = { showSortMenu = true },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("sort_menu_btn")
            ) {
              Icon(Icons.Default.Sort, contentDescription = "Sort", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = sortOption.label, fontSize = 12.sp)
            }

            DropdownMenu(
              expanded = showSortMenu,
              onDismissRequest = { showSortMenu = false }
            ) {
              SortOption.values().forEach { option ->
                DropdownMenuItem(
                  text = { Text(option.label) },
                  onClick = {
                    onSortOptionChanged(option)
                    showSortMenu = false
                  },
                  trailingIcon = if (option == sortOption) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                  } else null
                )
              }
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // View mode toggle
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Row(modifier = Modifier.padding(2.dp)) {
              IconButton(
                onClick = { onViewModeChanged(ViewMode.CARDS) },
                modifier = Modifier
                  .size(34.dp)
                  .testTag("view_mode_cards"),
                colors = IconButtonDefaults.iconButtonColors(
                  containerColor = if (viewMode == ViewMode.CARDS) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent
                )
              ) {
                Icon(
                  imageVector = Icons.Default.ViewAgenda,
                  contentDescription = "Cards View",
                  modifier = Modifier.size(18.dp)
                )
              }

              IconButton(
                onClick = { onViewModeChanged(ViewMode.TABLE) },
                modifier = Modifier
                  .size(34.dp)
                  .testTag("view_mode_table"),
                colors = IconButtonDefaults.iconButtonColors(
                  containerColor = if (viewMode == ViewMode.TABLE) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent
                )
              ) {
                Icon(
                  imageVector = Icons.Default.TableChart,
                  contentDescription = "Table View",
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Category Horizontal Scroll Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MaterialsRepository.getCategories().forEach { category ->
          val isSelected = category == selectedCategory
          FilterChip(
            selected = isSelected,
            onClick = { onCategorySelected(category) },
            label = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = category.iconEmoji)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = category.getLocalizedName(currentLanguage),
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("cat_chip_${category.name}")
          )
        }
      }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

    // Content: Cards or Table
    if (materials.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "🔍", fontSize = 48.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No building materials found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Try clearing the search query or selecting a different category.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(onClick = {
            onSearchQueryChanged("")
            onCategorySelected(MaterialCategory.ALL)
          }) {
            Text("Show All Materials")
          }
        }
      }
    } else {
      when (viewMode) {
        ViewMode.CARDS -> {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .testTag("materials_cards_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            items(materials, key = { it.id }) { material ->
              MaterialCard(
                material = material,
                currentLanguage = currentLanguage,
                cartQuantity = cartQuantities[material.id] ?: 0,
                onClick = { onMaterialClick(material) },
                onAddToCart = { onAddToCart(material) }
              )
            }
          }
        }
        ViewMode.TABLE -> {
          MaterialTableView(
            materials = materials,
            currentLanguage = currentLanguage,
            onMaterialClick = onMaterialClick,
            modifier = Modifier.padding(8.dp)
          )
        }
      }
    }
  }
}
