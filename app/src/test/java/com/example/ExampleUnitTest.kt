package com.example

import com.example.data.ConstructionPackage
import com.example.data.EstimatorCalculator
import com.example.data.MaterialsRepository
import com.example.model.AppLanguage
import com.example.viewmodel.MaterialsViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testMaterialsCountOver100() {
    val total = MaterialsRepository.allMaterials.size
    assertTrue("Materials count must be 100+, found: $total", total >= 100)
  }

  @Test
  fun testAllLanguagesPresent() {
    MaterialsRepository.allMaterials.forEach { mat ->
      assertFalse("En name missing for ${mat.id}", mat.nameEn.isBlank())
      assertFalse("Hi name missing for ${mat.id}", mat.nameHi.isBlank())
      assertFalse("Te name missing for ${mat.id}", mat.nameTe.isBlank())
      assertFalse("Ta name missing for ${mat.id}", mat.nameTa.isBlank())
      assertFalse("Kn name missing for ${mat.id}", mat.nameKn.isBlank())
      assertFalse("Ml name missing for ${mat.id}", mat.nameMl.isBlank())
      assertEquals(2026, mat.lastUpdatedYear)
      assertTrue("Price must be > 0 for ${mat.id}", mat.approxPrice2026 > 0)
      assertTrue("GST must be valid for ${mat.id}", mat.gstPercentage in listOf(5, 12, 18, 28))
    }
  }

  @Test
  fun test1000SqFtEstimator() {
    val result = EstimatorCalculator.calculate(1000.0, ConstructionPackage.STANDARD)
    assertEquals(1000.0, result.builtUpAreaSqFt, 0.1)
    assertEquals(1950000.0, result.totalEstimatedCost, 1.0)
    assertTrue(result.items.isNotEmpty())
    val cementItem = result.items.find { it.name.contains("Cement") }
    assertNotNull(cementItem)
  }

  @Test
  fun testCartCalculation() {
    val vm = MaterialsViewModel()
    val firstMat = MaterialsRepository.allMaterials.first()
    vm.addToCart(firstMat.id, 2)
    val summary = vm.getCartSummary()
    assertEquals(1, summary.entries.size)
    assertEquals(2, summary.totalItemsCount)
    val expectedSubtotal = firstMat.approxPrice2026 * 2
    assertEquals(expectedSubtotal, summary.subtotal, 0.01)
    val expectedGst = expectedSubtotal * (firstMat.gstPercentage / 100.0)
    assertEquals(expectedGst, summary.totalGst, 0.01)
    assertEquals(expectedSubtotal + expectedGst, summary.grandTotal, 0.01)
  }
}
