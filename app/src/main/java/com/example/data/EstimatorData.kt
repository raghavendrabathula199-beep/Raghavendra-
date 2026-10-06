package com.example.data

enum class ConstructionPackage(val label: String, val baseRatePerSqFt: Double, val description: String) {
  BUDGET("Budget Package", 1650.0, "PPC Cement, Fly Ash Bricks, 2x2 Vitrified Tiles, Standard CP Fittings"),
  STANDARD("Standard Quality", 1950.0, "OPC 53/PPC, Fe 550D TMT Steel, Red Wire-Cut Bricks, Granite Counters, Jaquar Fittings, Premium Paint"),
  PREMIUM("Luxury / Premium", 2450.0, "High Grade OPC/RMC, Tata/JSW 550D Steel, AAC/Porotherm Blocks, GVT/Italian Marble, Concealed Cisterns, UPVC Windows, Royal Emulsion")
}

data class MaterialEstimateItem(
  val name: String,
  val quantityStr: String,
  val approxCost: Double,
  val percentOfTotal: Double,
  val iconEmoji: String
)

data class HouseEstimateResult(
  val builtUpAreaSqFt: Double,
  val selectedPackage: ConstructionPackage,
  val totalEstimatedCost: Double,
  val costInLakhs: String,
  val costPerSqFt: Double,
  val items: List<MaterialEstimateItem>,
  val laborCost: Double,
  val materialCost: Double
)

object EstimatorCalculator {
  fun calculate(areaSqFt: Double, pack: ConstructionPackage): HouseEstimateResult {
    val totalCost = areaSqFt * pack.baseRatePerSqFt
    val factor = areaSqFt / 1000.0

    // Typical Indian residential construction breakdown:
    // Material is ~65%, Labor is ~35%
    val materialTotal = totalCost * 0.65
    val laborTotal = totalCost * 0.35

    val cementBags = (420 * factor).toInt()
    val cementCost = cementBags * (if (pack == ConstructionPackage.PREMIUM) 420.0 else 380.0)

    val steelTons = 4.0 * factor
    val steelCost = steelTons * 1000 * (if (pack == ConstructionPackage.PREMIUM) 74.0 else 68.0)

    val sandTons = 65.0 * factor
    val sandCost = sandTons * 1400.0

    val aggregateTons = 60.0 * factor
    val aggregateCost = aggregateTons * 1250.0

    val bricksCount = (20000 * factor).toInt()
    val bricksCost = bricksCount * (if (pack == ConstructionPackage.BUDGET) 7.0 else 11.0)

    val flooringSqFt = (950 * factor).toInt()
    val flooringCost = flooringSqFt * (if (pack == ConstructionPackage.PREMIUM) 120.0 else if (pack == ConstructionPackage.STANDARD) 65.0 else 45.0)

    val paintCost = totalCost * 0.05
    val plumbingCost = totalCost * 0.08
    val electricalCost = totalCost * 0.08
    val woodworkCost = totalCost * 0.11

    val items = listOf(
      MaterialEstimateItem("Cement (OPC & PPC)", "$cementBags Bags", cementCost, (cementCost / totalCost) * 100, "🧱"),
      MaterialEstimateItem("TMT Steel Rebar (Fe 550D)", String.format("%.2f Tons", steelTons), steelCost, (steelCost / totalCost) * 100, "🏛️"),
      MaterialEstimateItem("Sand & M-Sand", String.format("%.1f Tons", sandTons), sandCost, (sandCost / totalCost) * 100, "⏳"),
      MaterialEstimateItem("Coarse Aggregates (20mm & 10mm)", String.format("%.1f Tons", aggregateTons), aggregateCost, (aggregateCost / totalCost) * 100, "🪨"),
      MaterialEstimateItem("Bricks / AAC Blocks", "$bricksCount Pcs", bricksCost, (bricksCost / totalCost) * 100, "🧱"),
      MaterialEstimateItem("Flooring Tiles & Granite", "$flooringSqFt sq.ft.", flooringCost, (flooringCost / totalCost) * 100, "✨"),
      MaterialEstimateItem("Doors, Windows & Woodwork", "Complete Set", woodworkCost, (woodworkCost / totalCost) * 100, "🚪"),
      MaterialEstimateItem("Plumbing, Pipes & Tanks", "Concealed + Fixtures", plumbingCost, (plumbingCost / totalCost) * 100, "🚿"),
      MaterialEstimateItem("Electrical Wires & Switches", "Complete Wiring", electricalCost, (electricalCost / totalCost) * 100, "⚡"),
      MaterialEstimateItem("Painting & Wall Putty", "Interior + Exterior", paintCost, (paintCost / totalCost) * 100, "🎨"),
      MaterialEstimateItem("Masonry & Carpentry Labor", "Contract & Skilled", laborTotal, (laborTotal / totalCost) * 100, "👷")
    )

    val lakhs = totalCost / 100000.0
    val costInLakhsStr = String.format("₹ %.2f Lakhs", lakhs)

    return HouseEstimateResult(
      builtUpAreaSqFt = areaSqFt,
      selectedPackage = pack,
      totalEstimatedCost = totalCost,
      costInLakhs = costInLakhsStr,
      costPerSqFt = pack.baseRatePerSqFt,
      items = items,
      laborCost = laborTotal,
      materialCost = materialTotal
    )
  }
}
