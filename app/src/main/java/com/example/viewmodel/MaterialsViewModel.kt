package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.ConstructionPackage
import com.example.data.EstimatorCalculator
import com.example.data.HouseEstimateResult
import com.example.data.MaterialsRepository
import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.model.MaterialCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

enum class ViewMode {
  CARDS,
  TABLE
}

enum class SortOption(val label: String) {
  DEFAULT("Default"),
  PRICE_LOW_HIGH("Price: Low to High"),
  PRICE_HIGH_LOW("Price: High to Low"),
  NAME_A_Z("Name: A to Z"),
  GST_RATE("GST Rate")
}

enum class AppTab(val title: String, val iconEmoji: String) {
  MATERIALS("Materials Guide", "🏗️"),
  ESTIMATOR("1,000 Sq.Ft Cost", "📐"),
  COMPARISON("Price Compare", "⚖️"),
  GUIDELINES("Site Guidelines", "📋"),
  CART("Procurement List", "🛒")
}

data class CartEntry(
  val material: BuildingMaterial,
  val quantity: Int,
  val itemCost: Double,
  val gstAmount: Double,
  val totalWithGst: Double
)

data class CartSummary(
  val entries: List<CartEntry>,
  val subtotal: Double,
  val totalGst: Double,
  val grandTotal: Double,
  val totalItemsCount: Int
)

class MaterialsViewModel : ViewModel() {
  private val _selectedLanguage = MutableStateFlow(AppLanguage.EN)
  val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow(MaterialCategory.ALL)
  val selectedCategory: StateFlow<MaterialCategory> = _selectedCategory.asStateFlow()

  private val _viewMode = MutableStateFlow(ViewMode.CARDS)
  val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

  private val _sortOption = MutableStateFlow(SortOption.DEFAULT)
  val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

  private val _selectedMaterialForDetail = MutableStateFlow<BuildingMaterial?>(null)
  val selectedMaterialForDetail: StateFlow<BuildingMaterial?> = _selectedMaterialForDetail.asStateFlow()

  private val _activeTab = MutableStateFlow(AppTab.MATERIALS)
  val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

  // 1,000 sq ft estimator state
  private val _builtUpArea = MutableStateFlow(1000.0)
  val builtUpArea: StateFlow<Double> = _builtUpArea.asStateFlow()

  private val _selectedPackage = MutableStateFlow(ConstructionPackage.STANDARD)
  val selectedPackage: StateFlow<ConstructionPackage> = _selectedPackage.asStateFlow()

  // Procurement Cart: material id -> quantity
  private val _cartQuantities = MutableStateFlow<Map<String, Int>>(emptyMap())
  val cartQuantities: StateFlow<Map<String, Int>> = _cartQuantities.asStateFlow()

  fun setLanguage(lang: AppLanguage) {
    _selectedLanguage.value = lang
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategory(category: MaterialCategory) {
    _selectedCategory.value = category
  }

  fun setViewMode(mode: ViewMode) {
    _viewMode.value = mode
  }

  fun setSortOption(option: SortOption) {
    _sortOption.value = option
  }

  fun selectMaterial(material: BuildingMaterial?) {
    _selectedMaterialForDetail.value = material
  }

  fun setActiveTab(tab: AppTab) {
    _activeTab.value = tab
  }

  fun setArea(area: Double) {
    _builtUpArea.value = area.coerceIn(300.0, 10000.0)
  }

  fun setPackage(pack: ConstructionPackage) {
    _selectedPackage.value = pack
  }

  fun addToCart(materialId: String, quantity: Int = 1) {
    val current = _cartQuantities.value.toMutableMap()
    val existing = current[materialId] ?: 0
    current[materialId] = existing + quantity
    _cartQuantities.value = current
  }

  fun updateCartQty(materialId: String, quantity: Int) {
    val current = _cartQuantities.value.toMutableMap()
    if (quantity <= 0) {
      current.remove(materialId)
    } else {
      current[materialId] = quantity
    }
    _cartQuantities.value = current
  }

  fun removeFromCart(materialId: String) {
    val current = _cartQuantities.value.toMutableMap()
    current.remove(materialId)
    _cartQuantities.value = current
  }

  fun clearCart() {
    _cartQuantities.value = emptyMap()
  }

  fun getFilteredMaterials(): List<BuildingMaterial> {
    val list = MaterialsRepository.getMaterials(
      category = _selectedCategory.value,
      query = _searchQuery.value,
      selectedLanguage = _selectedLanguage.value
    )
    return when (_sortOption.value) {
      SortOption.DEFAULT -> list
      SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.approxPrice2026 }
      SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.approxPrice2026 }
      SortOption.NAME_A_Z -> list.sortedBy { it.getName(_selectedLanguage.value) }
      SortOption.GST_RATE -> list.sortedBy { it.gstPercentage }
    }
  }

  fun getEstimateResult(): HouseEstimateResult {
    return EstimatorCalculator.calculate(_builtUpArea.value, _selectedPackage.value)
  }

  fun getCartSummary(): CartSummary {
    val entries = _cartQuantities.value.mapNotNull { (id, qty) ->
      val mat = MaterialsRepository.getMaterialById(id) ?: return@mapNotNull null
      val itemCost = mat.approxPrice2026 * qty
      val gst = itemCost * (mat.gstPercentage / 100.0)
      CartEntry(
        material = mat,
        quantity = qty,
        itemCost = itemCost,
        gstAmount = gst,
        totalWithGst = itemCost + gst
      )
    }
    val subtotal = entries.sumOf { it.itemCost }
    val totalGst = entries.sumOf { it.gstAmount }
    val grandTotal = subtotal + totalGst
    val totalItems = entries.sumOf { it.quantity }
    return CartSummary(entries, subtotal, totalGst, grandTotal, totalItems)
  }
}
