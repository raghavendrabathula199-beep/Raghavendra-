package com.example.data

import com.example.model.AppLanguage
import com.example.model.BuildingMaterial
import com.example.model.MaterialCategory

object MaterialsRepository {
  val allMaterials: List<BuildingMaterial> by lazy {
    MaterialsStructure.list +
        MaterialsFinishes.list +
        MaterialsServicesAndHardware.list +
        MaterialsExtendedPart4.list +
        MaterialsExtendedPart5.list +
        MaterialsExtendedPart6.list +
        MaterialsExtendedPart7.list
  }

  fun getMaterials(
    category: MaterialCategory = MaterialCategory.ALL,
    query: String = "",
    selectedLanguage: AppLanguage = AppLanguage.EN
  ): List<BuildingMaterial> {
    return allMaterials.filter { material ->
      val matchesCategory = category == MaterialCategory.ALL || material.category == category
      val matchesSearch = if (query.isBlank()) true else material.matchesQuery(query)
      matchesCategory && matchesSearch
    }
  }

  fun getMaterialById(id: String): BuildingMaterial? {
    return allMaterials.find { it.id == id }
  }

  fun getCategories(): List<MaterialCategory> = MaterialCategory.values().toList()
}
