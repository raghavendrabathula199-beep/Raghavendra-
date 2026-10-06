package com.example.model

enum class MaterialCategory(
  val displayNameEn: String,
  val displayNameHi: String,
  val displayNameTe: String,
  val displayNameTa: String,
  val displayNameKn: String,
  val displayNameMl: String,
  val iconEmoji: String
) {
  ALL("All Materials", "सभी सामग्री", "అన్ని పదార్థాలు", "அனைத்து பொருட்கள்", "ಎಲ್ಲಾ ಸಾಮಗ್ರಿಗಳು", "എല്ലാ സാമഗ്രികളും", "🏗️"),
  FOUNDATION_STRUCTURE("Structure & Steel", "संरचना और स्टील", "నిర్మాణం & స్టీల్", "கட்டமைப்பு & எஃகு", "ರಚನೆ ಮತ್ತು ಉಕ್ಕು", "ഘടനാപരമായ & സ്റ്റീൽ", "🏛️"),
  CEMENT_CONCRETE("Cement & Concrete", "सीमेंट और कंक्रीट", "సిమెంట్ & కాంక్రీట్", "சிமெண்ட் & கான்கிரீட்", "ಸಿಮೆಂಟ್ & ಕಾಂಕ್ರೀಟ್", "സിമന്റ് & കോൺക്രീറ്റ്", "🧱"),
  SAND_AGGREGATE("Sand & Aggregate", "रेत और गिट्टी", "ఇసుక & కంకర", "மணல் & ஜல்லி", "ಮರಳು ಮತ್ತು ಜಲ್ಲಿ", "മണൽ & മെറ്റൽ", "⏳"),
  BRICKS_BLOCKS("Bricks & Blocks", "ईंटें और ब्लॉक", "ఇటుకలు & బ్లాకులు", "செங்கற்கள் & பிளாக்குகள்", "ಇಟ್ಟಿಗೆಗಳು & ಬ್ಲಾಕ್ಗಳು", "ഇഷ്ടിക & ബ്ലോക്കുകൾ", "🧱"),
  FLOORING_TILES("Flooring & Marble", "फर्श, टाइल्स व मार्बल", "ఫ్లోరింగ్ & టైల్స్", "தரைத்தளம் & டைல்ஸ்", "ನೆಲಹಾಸು & ಟೈಲ್ಸ್", "ഫ്ലോറിങ് & ടൈലുകൾ", "✨"),
  PLUMBING_PIPES("Plumbing & Pipes", "प्लंबिंग, पाइप व टैंक", "ప్లంబింగ్ & పైపులు", "பிளம்பிங் & குழாய்கள்", "ಪ್ಲಂಬಿಂಗ್ & ಪೈಪ್ಗಳು", "പ്ലംബിംഗ് & പൈപ്പുകൾ", "🚿"),
  ELECTRICAL_POWER("Electrical & Wires", "बिजली, तार व स्विच", "విద్యుత్ & వైర్లు", "மின்சாரம் & கம்பிகள்", "ವಿದ್ಯುತ್ & ತಂತಿಗಳು", "ഇലക്ട്രിക്കൽ & വയറുകൾ", "⚡"),
  PAINT_FINISHING("Paint & Putty", "पेंट, पुट्टी व प्राइमर", "పెయింట్ & పుట్టీ", "பெயிண்ட் & புட்டி", "ಬಣ್ಣ & ಪುಟ್ಟಿ", "പെയിന്റ് & പുട്ടി", "🎨"),
  TIMBER_PLYWOOD("Timber & Plywood", "लकड़ी, प्लाईवुड व दरवाजे", "కలప & ప్లైవుడ్", "மரம் & ப்ளைவுட்", "ಮರ ಮತ್ತು ಪ್ಲೈವುಡ್", "തടി & പ്ലൈവുഡ്", "🚪"),
  ROOFING_CEILING("Roofing & False Ceiling", "छत व फॉल्स सीलिंग", "రూఫింగ్ & ఫాల్స్ సీలింగ్", "கூரை & ஃபால்ஸ் சீலிங்", "ಮೇಲ್ಛಾವಣಿ & ಸೀಲಿಂಗ್", "റൂഫിംഗ് & ഫോൾസ് സീലിംഗ്", "🏠"),
  CHEMICALS_WATERPROOFING("Chemicals & Adhesives", "केमिकल्स व वाटरप्रूफिंग", "రసాయనాలు & వాటర్‌ప్రూఫింగ్", "ரசாயனங்கள் & நீர்ப்புகாப்பு", "ರಾಸಾಯನಿಕಗಳು & ಜಲನಿರೋಧಕ", "കെമിക്കലുകൾ & വാട്ടർപ്രൂഫിംഗ്", "🧪"),
  HARDWARE_FASTENERS("Hardware & Fittings", "हार्डवेयर व फिटिंग्स", "హార్డ్‌వేర్ & ఫిట్టింగ్స్", "ஹார்டுவேர் & ஃபிட்டிங்ஸ்", "ಹಾರ್ಡ್ವೇರ್ & ಫಿಟ್ಟಿಂಗ್ಸ್", "ഹാർഡ്‌വെയർ & ഫിറ്റിംഗ്സ്", "🔩"),
  GLASS_WINDOWS("Glass & Aluminium", "ग्लास व एल्यूमीनियम", "గాజు & అల్యూమినియం", "கண்ணாடி & அலுமினியம்", "ಗಾಜು & ಅಲ್ಯೂಮಿನಿಯಂ", "ഗ്ലാസ്സ് & അലുമിനിയം", "🪟");

  fun getLocalizedName(lang: AppLanguage): String {
    return when (lang) {
      AppLanguage.EN -> displayNameEn
      AppLanguage.HI -> displayNameHi
      AppLanguage.TE -> displayNameTe
      AppLanguage.TA -> displayNameTa
      AppLanguage.KN -> displayNameKn
      AppLanguage.ML -> displayNameMl
    }
  }
}
