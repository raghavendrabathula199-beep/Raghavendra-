package com.example.model

data class BuildingMaterial(
  val id: String,
  val nameEn: String,
  val nameHi: String,
  val nameTe: String,
  val nameTa: String,
  val nameKn: String,
  val nameMl: String,
  val category: MaterialCategory,
  val unit: String,
  val approxPrice2026: Double,
  val priceRangeMin: Double,
  val priceRangeMax: Double,
  val premiumPrice2026: Double,
  val qualityGrade: String,
  val brandExamples: List<String>,
  val typicalUses: String,
  val buyingTips: String,
  val gstPercentage: Int,
  val estimatedQtyFor1000SqFt: String,
  val genuineCheckTips: String,
  val storageRecommendations: String,
  val safetyPrecautions: String,
  val lastUpdatedYear: Int = 2026
) {
  fun getName(lang: AppLanguage): String {
    return when (lang) {
      AppLanguage.EN -> nameEn
      AppLanguage.HI -> nameHi
      AppLanguage.TE -> nameTe
      AppLanguage.TA -> nameTa
      AppLanguage.KN -> nameKn
      AppLanguage.ML -> nameMl
    }
  }

  fun matchesQuery(query: String): Boolean {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return true
    return nameEn.lowercase().contains(q) ||
        nameHi.lowercase().contains(q) ||
        nameTe.lowercase().contains(q) ||
        nameTa.lowercase().contains(q) ||
        nameKn.lowercase().contains(q) ||
        nameMl.lowercase().contains(q) ||
        typicalUses.lowercase().contains(q) ||
        brandExamples.any { it.lowercase().contains(q) } ||
        buyingTips.lowercase().contains(q) ||
        category.displayNameEn.lowercase().contains(q)
  }
}
