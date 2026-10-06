package com.example.data

data class SiteGuideline(
  val id: String,
  val title: String,
  val category: String,
  val iconEmoji: String,
  val summary: String,
  val detailedSteps: List<String>,
  val standardNorm: String
)

object SiteGuidelinesData {
  val guidelines = listOf(
    SiteGuideline(
      id = "sg_cement",
      title = "Field Quality Tests for Cement",
      category = "Quality Control",
      iconEmoji = "🧱",
      summary = "5 quick on-site field tests to ensure cement is fresh and adulteration-free before casting.",
      detailedSteps = listOf(
        "Date of Manufacture: Check weekly code printed on bag. Strength diminishes by 20% after 3 months, 30% after 6 months, and 50% after 12 months.",
        "Color Inspection: Open the bag and observe; color should be uniform greenish-grey. Earthy or light brown tint indicates excessive clay/adulteration.",
        "Temperature Test: Thrust bare arm deep into the cement bag. It should feel distinctly cool. Warmth indicates hydration has already begun.",
        "Smoothness / Pinch Test: Take a small pinch between thumb and index finger and rub. It should feel silky smooth like talcum powder, never gritty.",
        "Water Float Test: Throw a small handful of cement onto a bucket of clean water. Good cement particles should float for a brief moment before sinking slowly."
      ),
      standardNorm = "IS 269 / IS 12269 / IS 1489"
    ),
    SiteGuideline(
      id = "sg_steel",
      title = "TMT Steel Unit Weight & Bend Checks",
      category = "Structural Integrity",
      iconEmoji = "🏛️",
      summary = "Verify standard nominal rebar weight per meter to ensure you are not being overbilled or supplied underweight rolling.",
      detailedSteps = listOf(
        "Standard Rebar Weight Formula: Weight (W in kg/m) = D² / 162, where D is rebar diameter in mm.",
        "8mm rebar = 0.395 kg/m (Tolerance ±7%)",
        "10mm rebar = 0.617 kg/m (Tolerance ±7%)",
        "12mm rebar = 0.888 kg/m (Tolerance ±5%)",
        "16mm rebar = 1.578 kg/m (Tolerance ±5%)",
        "20mm rebar = 2.466 kg/m (Tolerance ±3%)",
        "Cold Bend Test: Cut a 50cm sample and bend 180 degrees around a mandrel. No cracks or micro-fissures should appear on the outer stretched surface."
      ),
      standardNorm = "IS 1786:2008 (High Strength Deformed Bars)"
    ),
    SiteGuideline(
      id = "sg_sand",
      title = "Sand Silt Content Jar Field Test",
      category = "Mortar & Concrete",
      iconEmoji = "⏳",
      summary = "Excessive silt ruins cement hydration and causes plaster delamination and hollow sounds.",
      detailedSteps = listOf(
        "Take a transparent glass measuring cylinder or straight-sided glass jar.",
        "Fill with clean water up to 50ml, and add 1/2 teaspoon of common table salt (accelerates silt settlement).",
        "Pour sand into the jar until water level reaches roughly 100ml.",
        "Shake the mixture vigorously for 1 minute to suspend all fine clay and silt particles.",
        "Place the jar on a level table and allow it to stand undisturbed for 3 hours.",
        "A clear darker layer of silt will settle directly on top of the heavier sand grains.",
        "Calculate silt % = (Height of silt layer / Total height of sand + silt) * 100.",
        "If silt content exceeds 5% to 8%, the sand must be thoroughly washed before use."
      ),
      standardNorm = "IS 2386 (Part 2) Silt Content Limit < 6%"
    ),
    SiteGuideline(
      id = "sg_bricks",
      title = "Brick Water Absorption & Sound Tests",
      category = "Masonry Quality",
      iconEmoji = "🧱",
      summary = "Tests to determine compressive hardness, firing quality, and salt efflorescence in red clay bricks.",
      detailedSteps = listOf(
        "Ringing Sound Test: Take two bricks and strike them firmly against each other. A clear metallic bell ringing sound indicates thorough kiln firing.",
        "Drop Hardness Test: Drop a brick flat from a height of 1 meter (chest height) onto firm ground. It should not fracture or shatter.",
        "Nail Scratch Test: Try to make a scratch mark on the brick surface with a sharp steel nail. First-class bricks will resist fingernail and light nail scratching.",
        "Water Absorption Test: Weigh 5 dry bricks (W1). Submerge in clean water for 24 hours. Wipe surface dry with cloth and weigh again (W2). Water absorption % = ((W2 - W1) / W1) * 100. Must not exceed 20% for first class bricks.",
        "Efflorescence Test: Immerse brick ends in 25mm water until evaporated. White salt deposits covering >50% surface area indicate harmful alkali salts."
      ),
      standardNorm = "IS 3495 (Parts 1 to 4)"
    ),
    SiteGuideline(
      id = "sg_curing",
      title = "Curing Rules for Indian Weather Conditions",
      category = "Site Practice",
      iconEmoji = "💧",
      summary = "Proper hydration curing is 50% responsible for final concrete compressive strength and crack prevention.",
      detailedSteps = listOf(
        "Roof Slabs: Construct mortar bunds (ponding) 50mm high dividing slab into quadrants. Keep flooded continuously with minimum 25mm water for 14 to 21 days.",
        "Vertical Columns & Beams: Wrap tightly with wet hessian (jute gunny bags). Spray water minimum 3 times daily (morning, noon, evening) so burlap never dries.",
        "Plastered Walls: Light mist curing starting 24 hours after application. Continue for 7 days minimum. Avoid high pressure hose jets that dislodge green plaster.",
        "Summer Season Precautions: Concrete temperature should not exceed 35°C during pour. Cast slabs early morning or evening to prevent rapid plastic shrinkage cracking.",
        "Use of Curing Compounds: For tall columns and water-scarce sites, spray resin-based curing compound immediately after stripping formwork."
      ),
      standardNorm = "IS 456:2000 Section 13.5 (Curing Period)"
    ),
    SiteGuideline(
      id = "sg_storage",
      title = "Material Storage Best Practices",
      category = "Logistics & Storage",
      iconEmoji = "📦",
      summary = "Prevent costly damage and degradation of cement, steel, and timber during monsoon and storage.",
      detailedSteps = listOf(
        "Cement Shed: Build temporary raised wooden pallet floor minimum 150mm off bare soil. Stack bags maximum 10 layers high. Maintain 450mm gap from external walls.",
        "First-In, First-Out (FIFO): Arrange cement bags so older stock is used first before newly delivered batches.",
        "Steel Rebar: Stack rebars diameter-wise on timber sleepers minimum 200mm above ground. Cover with heavy waterproof tarpaulin during rainy season to avoid pitting rust.",
        "Bricks & Blocks: Stack on level compacted soil in rows 10 to 12 layers high with 600mm clear inspection passages.",
        "Tiles & Plywood: Store tile cartons vertically on edge. Stack plywood flat on minimum 3 equal height level wooden runners to avoid permanent warpage."
      ),
      standardNorm = "IS 4082: Recommendations on Stacking and Storage of Construction Materials"
    ),
    SiteGuideline(
      id = "sg_safety",
      title = "Construction Site Safety & PPE Guidelines",
      category = "Worker Safety",
      iconEmoji = "🦺",
      summary = "Zero accident safety protocols for residential building sites across India.",
      detailedSteps = listOf(
        "Head & Foot Protection: Strict rule: No worker or visitor allowed on site without certified hard hat (IS 2925) and steel-toed boots (IS 15298).",
        "Working at Heights (>2 meters): Fall arrest safety harness (IS 3521) tied to structural anchor line during roof shuttering, slab casting, and external painting.",
        "Eye & Dust Protection: Wear shatter-proof polycarbonate goggles and FFP2 dust respirator when cutting tiles, chasing electrical conduits, or spraying paint.",
        "Electrical Safety: All portable power tools (cutters, drills, vibrators) must be connected through an industrial ELCB/RCCB (30mA trip sensitivity).",
        "First Aid Box: Maintain a fully stocked site first aid box with antiseptic wash, burn cream, sterile bandages, and local hospital emergency numbers displayed."
      ),
      standardNorm = "IS 3696 / IS 3764 Safety Codes"
    ),
    SiteGuideline(
      id = "sg_gst",
      title = "GST Rates & Procurement Planning",
      category = "Tax & Procurement",
      iconEmoji = "🧾",
      summary = "Understand Goods & Services Tax (GST) slabs on construction items to optimize budget and claim input tax credit where eligible.",
      detailedSteps = listOf(
        "5% GST: River sand, M-sand, crushed aggregates, stone dust, stone rubble.",
        "12% GST: Clay wire-cut bricks, fly ash bricks, concrete solid blocks, natural marble and granite slabs, Mangalore clay roof tiles.",
        "18% GST: TMT steel rebars, structural steel angles/pipes, AAC blocks, vitrified & ceramic tiles, UPVC/CPVC pipes, electrical wires & switches, paints, putty, plywood, false ceiling gypsum, waterproofing chemicals.",
        "28% GST: Portland cement (OPC 53, OPC 43, PPC, White cement).",
        "Always demand GST tax invoices with supplier HSN codes. Check that the billing matches genuine brand certificates to avoid spurious counterfeit products."
      ),
      standardNorm = "GST Council of India Construction Tariff Classification"
    )
  )
}
