package in.filternet.jantamalik.Rajya;

public final class MP_2_MLA {

    public static final String[][] mapping = {

            //  1               2                   3                4              5                  6                    7               8                   9                   10                  11                 12
            {"Uttar Pradesh", "Agra",        "Etmadpur",     "Agra Cantt. (SC)", "Agra South",   "Agra North",   "Agra Rural (SC)",  "Fatehpur Sikri",   "Kheragarh",        "Fatehabad",        "Bah"},
            {"उत्तर प्रदेश", "आगरा",        "एतमादपुर",       "आगरा कैंट. (एस सी)", "आगरा दक्षिण",   "आगरा उत्तर",      "आगरा ग्रामीण (एस सी)", "फतेहपुर सीकरी",     "खेरागढ़",             "फतेहाबाद",          "बाह"},
            {"Uttar Pradesh", "Aligarh",     "Khair (SC)",   "Barauli",          "Atrauli",      "Chharra",      "Koil",             "Aligarh",          "Iglas (SC)"},
            {"उत्तर प्रदेश", "अलीगढ़",       "खैर (एस सी)",   "बरौली",              "अतरौली",       "छर्रा",           "कोइल",             "अलीगढ़",           "इगलास (एस सी)"},
            {"Uttar Pradesh", "Allahabad",   "Phaphamau",    "Soraon (SC)",      "Phulpur",      "Pratappur",    "Handia",           "Meja",             "Karachhana",       "Allahabad West",   "Allahabad North", "Allahabad South",   "Bara (SC)",     "Koraon (SC)"},
            {"उत्तर प्रदेश", "इलाहाबाद",     "फाफामऊ",        "सोराअों (एस सी)",    "फूलपुर",         "प्रतापपुर",        "हंडिया",             "मेजा",             "करचना",            "इलाहाबाद पश्चिम",      "इलाहाबाद उत्तर",       "इलाहाबाद दक्षिण",      "बारा (एस सी)",     "कोरों (एस सी)"},
            {"Uttar Pradesh", "Ambedkar Nagar",     "Katehari",   "Tanda",          "Alapur (SC)",      "Jalalpur",      "Akbarpur"},
            {"उत्तर प्रदेश", "अंबेडकर नगर",           "कटेहरी",      "टांडा",            "अलापुर (एस सी)",    "जलालपुर",         "अकबरपुर"},
            {"Uttar Pradesh", "Amethi", "Tiloi" , "Salon (SC)", "Jagdishpur (SC)", "Gauriganj", "Amethi"},
            {"उत्तर प्रदेश", "अमेठी", "तिलोई", "सलोन (एस सी)", "जगदीशपुर (एस सी)", "गौरीगंज", "अमेठी"},
            {"Uttar Pradesh", "Amroha", "Dhanaura (SC)" , "Naugawan Sadat", "Amroha", "Hasanpur", "Garhmukteshwar"},
            {"उत्तर प्रदेश", "अमरोहा", "धनौरा (एस सी)" , "नौगावां सादात", "अमरोहा", "हसनपुर", "गढ़मुक्तेश्वर"},
            {"Uttar Pradesh", "Aonla", "Shekhupur", "Dataganj", "Faridpur (SC)", "Bithari Chainpur", "Aonla"},
            {"उत्तर प्रदेश", "आंवला", "शेखूपुर", "दातागंज", "फरीदपुर (एस सी)", "बिथरी चैनपुर", "आंवला"},
            {"Uttar Pradesh", "Azamgarh", "Gopalpur", "Sagri", "Mubarakpur", "Azamgarh", "Mehnagar (SC)"},
            {"उत्तर प्रदेश", "आजमगढ़", "गोपालपुर", "सगरी", "मुबारकपुर", "आजमगढ़", "मेहनगर (एस सी)"},
            {"Uttar Pradesh", "Badaun", "Gunnaur", "Bisauli (SC)", "Sahaswan", "Bilsi", "Badaun"},
            {"उत्तर प्रदेश", "बदायूं", "गुन्नौर", "बिसौली (एस सी)", "सहसवान", "बिल्सी", "बदायूं"},
            {"Uttar Pradesh", "Baghpat", "Siwalkhas", "Chhaprauli", "Baraut", "Baghpat", "Modi Nagar"},
            {"उत्तर प्रदेश", "बागपत", "सिवालखास", "छपरौली", "बड़ौत", "बागपत", "मोदीनगर"},
            {"Uttar Pradesh", "Bahraich", "Balha (SC)", "Nanpara", "Matera", "Mahasi", "Bahraich"},
            {"उत्तर प्रदेश", "बहराइच", "बलहा (एस सी)", "नानपारा", "मटेरा", "महसी", "बहराइच"},
            {"Uttar Pradesh", "Ballia", "Phephana", "Ballia Nagar", "Bairia", "Zahoorabad", "Mohammadabad"},
            {"उत्तर प्रदेश", "बलिया", "फेफना", "बलिया नगर", "बैरिया", "जहूराबाद", "मोहम्मदाबाद"},
            {"Uttar Pradesh", "Mirzapur",    "Chhanbey (SC)",     "Mirzapur",         "Majhawan",     "Chunar",       "Marihan"},
            {"उत्तर प्रदेश", "मिर्जापुर",       "छानबे (एस सी)",          "मिर्जापुर",            "मझवां",          "चुनार",         "मड़िहान"},
            {"Uttar Pradesh", "Varanasi",    "Pindra",       "Ajagara (SC)",     "Rohaniya",     "Sevapuri",     "Varanasi Cantt.",  "Varanasi North",   "Varanasi South",   "Shivpur"},
            {"उत्तर प्रदेश", "वाराणसी",       "पिंडरा",         "अजगर (एस सी)",     "रोहनिया",      "सेवापुरी",         "वाराणसी कैंट.",        "वाराणसी उत्तर",        "वाराणसी दक्षिण",       "शिवपुर"}
    };
}
