package in.jantamalik.Rajya;

public final class MP_2_MLA {
    public static final String[][] mapping = {
            {"आगरा", "एतमादपुर", "आगरा कैंट.", "आगरा दक्षिण", "आगरा उत्तर", "जलेसर"},
            {"अकबरपुर", "अकबरपुर रनिया", "बिठूर", "कल्याणपुर", "महाराजपुर", "घाटमपुर"},
            {"अलीगढ़", "खैर", "बरौली", "अतरौली", "कोइल", "अलीगढ़"},
            {"इलाहाबाद", "मेजा", "करचना", "इलाहाबाद दक्षिण", "बारा", "कोरों"},
            {"अंबेडकर नगर", "कटेहरी", "टांडा", "गोसाईगंज", "जलालपुर", "अकबरपुर"},
            {"अमेठी", "तिलोई", "सलोन", "जगदीशपुर", "गौरीगंज", "अमेठी"},
            {"अमरोहा", "धनौरा", "नौगावां सादात", "अमरोहा", "हसनपुर", "गढ़मुक्तेश्वर"},
            {"आंवला", "शेखूपुर", "दातागंज", "फरीदपुर", "बिथरी चैनपुर", "आंवला"},
            {"आजमगढ़", "गोपालपुर", "सगरी", "मुबारकपुर", "आजमगढ़", "मेहनगर"},
            {"बदायूं", "गुन्नौर", "बिसौली", "सहसवान", "बिल्सी", "बदायूं"},
            {"बागपत", "सिवालखास", "छपरौली", "बड़ौत", "बागपत", "मोदीनगर"},
            {"बहराइच", "बलहा", "नानपारा", "मटेरा", "महसी", "बहराइच"},
            {"बलिया", "फेफना", "बलिया नगर", "बैरिया", "जहूराबाद", "मोहम्मदाबाद"},
            {"बांदा", "बबेरू", "नरैनी", "बांदा", "चित्रकूट", "मानिकपुर"},
            {"बांसगांव", "चौरी-चौरा", "बांसगांव", "चिल्लूपार", "रुद्रपुर", "बरहज"},
            {"बाराबंकी", "कुर्सी", "रामनगर", "बाराबंकी", "जैदपुर", "हैदरगढ़"},
            {"बरेली", "मीरगंज", "भोजीपुरा", "नवाबगंज", "बरेली", "बरेली कैंट."},
            {"बस्ती", "हर्रैया", "कप्तानगंज", "रुधौली", "बस्ती सदर", "महादेवा"},
            {"भदोही", "प्रतापपुर", "हंडिया", "भदोही", "ज्ञानपुर", "औराई"},
            {"बिजनौर", "पुरकाजी", "मीरापुर", "बिजनौर", "चांदपुर", "हस्तिनापुर"},
            {"बुलंदशहर", "बुलंदशहर", "स्याना", "अनूपशहर", "डिबाई", "शिकारपुर"},
            {"चंदौली", "मुगलसराय", "सकलडीहा", "सैयदराजा", "अजगर", "शिवपुर"},
            {"देवरिया", "तमकुही राज", "फाजिलनगर", "देवरिया", "पथरदेवा", "रामपुर कारखाना"},
            {"धौरहरा", "धौरहरा", "कस्ता", "मोहम्मदी", "महोली", "हरगांव"},
            {"डुमरियागंज", "शोहरतगढ़", "कपिलवस्तु", "बांसी", "इटवा", "डुमरियागंज"},
            {"एटा", "कासगंज", "अमांपुर", "पटियाली", "एटा", "मारहरा"},
            {"इटावा", "इटावा", "भरथना", "दिबियापुर", "औरैया", "सिकंदरा"},
            {"फैजाबाद", "दरियाबाद", "रुदौली", "मिल्कीपुर", "बीकापुर", "अयोध्या"},
            {"फर्रुखाबाद", "अलीगंज", "कायमगंज", "अमृतपुर", "फर्रुखाबाद", "भोजपुर"},
            {"फतेहपुर", "जहानाबाद", "बिंदकी", "फतेहपुर", "अयाह शाह", "हुसैनगंज", "खागा"},
            {"फतेहपुर सीकरी", "आगरा ग्रामीण", "फतेहपुर सीकरी", "खेरागढ़", "फतेहाबाद", "बाह"},
            {"फिरोजाबाद", "टूंडला", "जसराना", "फिरोजाबाद", "शिकोहाबाद", "सिरसागंज"},
            {"गौतम बुद्ध नगर", "नोएडा", "दादरी", "जेवर", "सिकंदराबाद", "खुर्जा"},
            {"गाज़ियाबाद", "लोनी", "मुरादनगर", "साहिबाबाद", "गाज़ियाबाद", "धौलाना"},
            {"गाजीपुर", "जखनियां", "सैदपुर", "गाजीपुर", "जंगीपुर", "ज़मानिया"},
            {"घोसी", "मधुबन", "घोसी", "मुहम्मदाबाद-गोहना(एस सी)", "मऊ", "रसड़ा"},
            {"गोंडा", "उतरौला", "मेहनौन", "गोंडा", "मनकापुर", "गौरा"},
            {"गोरखपुर", "कैम्पियरगंज", "पिपराइच", "गोरखपुर शहरी", "गोरखपुर ग्रामीण", "सहजनवा"},
            {"हमीरपुर", "हमीरपुर", "राठ", "महोबा", "चरखारी", "तिंदवारी"},
            {"हरदोई", "सवायजपुर", "शाहाबाद", "हरदोई", "गोपामऊ", "सांडि"},
            {"हाथरस", "छर्रा", "इगलास", "हाथरस", "सादाबाद", "सिकंदरा राव"},
            {"जालौन", "भोगनीपुर", "माधौगढ़", "कालपी", "उरई", "गरौठा"},
            {"जौनपुर", "बदलापुर", "शाहगंज", "जौनपुर", "मल्हनी", "मुंगरा बादशाहपुर"},
            {"झांसी", "बबीना", "झांसी नगर", "मऊरानीपुर", "ललितपुर", "महरौनी"},
            {"कैराना", "नकुड़", "गंगोह", "कैराना", "थाना भवन", "शामली"},
            {"कैसरगंज", "पयागपुर", "कैसरगंज", "कटरा बाजार", "कर्नलगंज", "तरबगंज"},
            {"कन्नौज", "छिबरामऊ", "तिर्वा", "कन्नौज", "बिधूना", "रसूलाबाद"},
            {"कानपुर", "गोविंद नगर", "सीसामऊ", "आर्य नगर", "किदवई नगर", "कानपुर कैंट"},
            {"कौशाम्बी", "बाबागंज", "कुंडा", "सिराथू", "मंझनपुर", "चैल"},
            {"खेरी", "पलिया", "निघासन", "गोला गोकर्णनाथ", "श्री नगर", "लखीमपुर"},
            {"कुशी नगर", "खड्डा", "पडरौना", "कुशी नगर", "हाटा", "रामकोला"},
            {"लालगंज", "अतरौलिया", "निजामाबाद", "फूलपुर पवाई", "दीदारगंज", "लालगंज"},
            {"लखनऊ", "लखनऊ पश्चिम", "लखनऊ उत्तर", "लखनऊ पूर्व", "लखनऊ सेंट्रल", "लखनऊ कैंट."},
            {"मछलीशहर", "मछलीशहर", "मड़ियाहूं", "जफराबाद", "केराकत", "पिंडरा"},
            {"महाराजगंज", "फरेन्दा", "नौतनवा", "सिसवा", "महाराजगंज", "पनियरा"},
            {"मैनपुरी", "मैनपुरी", "भोगांव", "किशनी", "करहल", "जसवंतनगर"},
            {"मथुरा", "छटा", "मंत", "गोवर्धन", "मथुरा", "बलदेव"},
            {"मेरठ", "किठौर", "मेरठ कैंट.", "मेरठ", "मेरठ दक्षिण", "हापुड़"},
            {"मिर्जापुर", "छानबे", "मिर्जापुर", "मझवां", "चुनार", "मड़िहान"},
            {"मिसरिख", "मिसरिख", "बिलग्राम मल्लावां", "बालामऊ", "सण्डीला", "बिल्हौर"},
            {"मोहनलालगंज", "सिधौली", "मलिहाबाद", "बक्शी का तालाब", "सरोजिनी नगर", "मोहनलालगंज"},
            {"मुरादाबाद", "बढ़ापुर", "काँठ", "ठाकुरद्वारा", "मुरादाबाद ग्रामीण", "मुरादाबाद नगर"},
            {"मुजफ्फरनगर", "बुढ़ाना", "चरथावल", "मुजफ्फर नगर", "खतौली", "सरधना"},
            {"नगीना", "नजीबाबाद", "नगीना", "धामपुर", "नहटौर", "नूरपुर"},
            {"फूलपुर", "फाफामऊ", "सोराअों", "फूलपुर", "इलाहाबाद पश्चिम", "इलाहाबाद उत्तर"},
            {"पीलीभीत", "बहेरी", "पीलीभीत", "बरखेड़ा", "पूरनपुर", "बीसलपुर"},
            {"प्रतापगढ़", "रामपुर खास", "विश्वनाथ गंज", "प्रतापगढ़", "पट्टी", "रानीगंज"},
            {"रायबरेली", "बछरावां", "हरचंदपुर", "रायबरेली", "सरेनी", "ऊंचाहार"},
            {"रामपुर", "सूअर", "चमरौआ", "बिलासपुर", "रामपुर", "मिलक"},
            {"रोबर्टस गंज", "चकिया", "घोरावल", "रोबर्टस गंज", "ओबरा", "दुद्धी"},
            {"सहारनपुर", "बेहत", "सहारनपुर नगर", "सहारनपुर", "देवबन्द", "रामपुर मनिहारान"},
            {"सलेमपुर", "भाटपार रानी", "सलेमपुर", "बेलथरा रोड", "सिकंदरपुर", "बांसडीह"},
            {"संभल", "कुंदरकी", "बिलारी", "चन्दौसी", "असमोली", "संभल"},
            {"संत कबीर नगर", "अलापुर", "मेंहदावल", "खलीलाबाद", "धनघटा", "खजनी"},
            {"शाहजहांपुर", "कटरा", "जलालाबाद", "तिलहर", "पुवायां", "शाहजहांपुर", "ददरौल"},
            {"श्रावस्ती", "भिनगा", "श्रावस्ती", "तुलसीपुर", "गैसड़ी", "बलरामपुर"},
            {"सीतापुर", "सीतापुर", "लहरपुर", "बिसवाँ", "सेवता", "महमूदाबाद"},
            {"सुल्तानपुर", "इसौली", "सुल्तानपुर", "सदर", "लम्भुआ", "कादीपुर"},
            {"उन्नाव", "बांगरमऊ", "सफीपुर", "मोहन", "उन्नाव", "भगवंतनगर", "पूर्वा"},
            {"वाराणसी", "रोहनिया", "सेवापुरी", "वाराणसी कैंट.", "वाराणसी उत्तर", "वाराणसी दक्षिण"},
    };
}