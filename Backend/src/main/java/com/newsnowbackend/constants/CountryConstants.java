package com.newsnow.constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Master list of the 195 countries supported by NewsNow
 * (193 UN member states + 2 UN observer states: Holy See & State of Palestine).
 *
 * Keys are lowercase ISO 3166-1 alpha-2 codes, which is the country-code format
 * expected by newsdata.io's /news endpoint (?country=us,in,gb ...).
 *
 * Values are display names shown in the country picker UI.
 */
public final class CountryConstants {

    private CountryConstants() {
    }

    public static final Map<String, String> COUNTRIES = new LinkedHashMap<>();

    static {
        COUNTRIES.put("af", "Afghanistan");
        COUNTRIES.put("al", "Albania");
        COUNTRIES.put("dz", "Algeria");
        COUNTRIES.put("ad", "Andorra");
        COUNTRIES.put("ao", "Angola");
        COUNTRIES.put("ag", "Antigua and Barbuda");
        COUNTRIES.put("ar", "Argentina");
        COUNTRIES.put("am", "Armenia");
        COUNTRIES.put("au", "Australia");
        COUNTRIES.put("at", "Austria");
        COUNTRIES.put("az", "Azerbaijan");
        COUNTRIES.put("bs", "Bahamas");
        COUNTRIES.put("bh", "Bahrain");
        COUNTRIES.put("bd", "Bangladesh");
        COUNTRIES.put("bb", "Barbados");
        COUNTRIES.put("by", "Belarus");
        COUNTRIES.put("be", "Belgium");
        COUNTRIES.put("bz", "Belize");
        COUNTRIES.put("bj", "Benin");
        COUNTRIES.put("bt", "Bhutan");
        COUNTRIES.put("bo", "Bolivia");
        COUNTRIES.put("ba", "Bosnia and Herzegovina");
        COUNTRIES.put("bw", "Botswana");
        COUNTRIES.put("br", "Brazil");
        COUNTRIES.put("bn", "Brunei");
        COUNTRIES.put("bg", "Bulgaria");
        COUNTRIES.put("bf", "Burkina Faso");
        COUNTRIES.put("bi", "Burundi");
        COUNTRIES.put("cv", "Cabo Verde");
        COUNTRIES.put("kh", "Cambodia");
        COUNTRIES.put("cm", "Cameroon");
        COUNTRIES.put("ca", "Canada");
        COUNTRIES.put("cf", "Central African Republic");
        COUNTRIES.put("td", "Chad");
        COUNTRIES.put("cl", "Chile");
        COUNTRIES.put("cn", "China");
        COUNTRIES.put("co", "Colombia");
        COUNTRIES.put("km", "Comoros");
        COUNTRIES.put("cg", "Congo (Republic)");
        COUNTRIES.put("cd", "Congo (DRC)");
        COUNTRIES.put("cr", "Costa Rica");
        COUNTRIES.put("ci", "Cote d'Ivoire");
        COUNTRIES.put("hr", "Croatia");
        COUNTRIES.put("cu", "Cuba");
        COUNTRIES.put("cy", "Cyprus");
        COUNTRIES.put("cz", "Czechia");
        COUNTRIES.put("dk", "Denmark");
        COUNTRIES.put("dj", "Djibouti");
        COUNTRIES.put("dm", "Dominica");
        COUNTRIES.put("do", "Dominican Republic");
        COUNTRIES.put("ec", "Ecuador");
        COUNTRIES.put("eg", "Egypt");
        COUNTRIES.put("sv", "El Salvador");
        COUNTRIES.put("gq", "Equatorial Guinea");
        COUNTRIES.put("er", "Eritrea");
        COUNTRIES.put("ee", "Estonia");
        COUNTRIES.put("sz", "Eswatini");
        COUNTRIES.put("et", "Ethiopia");
        COUNTRIES.put("fj", "Fiji");
        COUNTRIES.put("fi", "Finland");
        COUNTRIES.put("fr", "France");
        COUNTRIES.put("ga", "Gabon");
        COUNTRIES.put("gm", "Gambia");
        COUNTRIES.put("ge", "Georgia");
        COUNTRIES.put("de", "Germany");
        COUNTRIES.put("gh", "Ghana");
        COUNTRIES.put("gr", "Greece");
        COUNTRIES.put("gd", "Grenada");
        COUNTRIES.put("gt", "Guatemala");
        COUNTRIES.put("gn", "Guinea");
        COUNTRIES.put("gw", "Guinea-Bissau");
        COUNTRIES.put("gy", "Guyana");
        COUNTRIES.put("ht", "Haiti");
        COUNTRIES.put("va", "Holy See");
        COUNTRIES.put("hn", "Honduras");
        COUNTRIES.put("hu", "Hungary");
        COUNTRIES.put("is", "Iceland");
        COUNTRIES.put("in", "India");
        COUNTRIES.put("id", "Indonesia");
        COUNTRIES.put("ir", "Iran");
        COUNTRIES.put("iq", "Iraq");
        COUNTRIES.put("ie", "Ireland");
        COUNTRIES.put("il", "Israel");
        COUNTRIES.put("it", "Italy");
        COUNTRIES.put("jm", "Jamaica");
        COUNTRIES.put("jp", "Japan");
        COUNTRIES.put("jo", "Jordan");
        COUNTRIES.put("kz", "Kazakhstan");
        COUNTRIES.put("ke", "Kenya");
        COUNTRIES.put("ki", "Kiribati");
        COUNTRIES.put("kp", "Korea (North)");
        COUNTRIES.put("kr", "Korea (South)");
        COUNTRIES.put("kw", "Kuwait");
        COUNTRIES.put("kg", "Kyrgyzstan");
        COUNTRIES.put("la", "Laos");
        COUNTRIES.put("lv", "Latvia");
        COUNTRIES.put("lb", "Lebanon");
        COUNTRIES.put("ls", "Lesotho");
        COUNTRIES.put("lr", "Liberia");
        COUNTRIES.put("ly", "Libya");
        COUNTRIES.put("li", "Liechtenstein");
        COUNTRIES.put("lt", "Lithuania");
        COUNTRIES.put("lu", "Luxembourg");
        COUNTRIES.put("mg", "Madagascar");
        COUNTRIES.put("mw", "Malawi");
        COUNTRIES.put("my", "Malaysia");
        COUNTRIES.put("mv", "Maldives");
        COUNTRIES.put("ml", "Mali");
        COUNTRIES.put("mt", "Malta");
        COUNTRIES.put("mh", "Marshall Islands");
        COUNTRIES.put("mr", "Mauritania");
        COUNTRIES.put("mu", "Mauritius");
        COUNTRIES.put("mx", "Mexico");
        COUNTRIES.put("fm", "Micronesia");
        COUNTRIES.put("md", "Moldova");
        COUNTRIES.put("mc", "Monaco");
        COUNTRIES.put("mn", "Mongolia");
        COUNTRIES.put("me", "Montenegro");
        COUNTRIES.put("ma", "Morocco");
        COUNTRIES.put("mz", "Mozambique");
        COUNTRIES.put("mm", "Myanmar");
        COUNTRIES.put("na", "Namibia");
        COUNTRIES.put("nr", "Nauru");
        COUNTRIES.put("np", "Nepal");
        COUNTRIES.put("nl", "Netherlands");
        COUNTRIES.put("nz", "New Zealand");
        COUNTRIES.put("ni", "Nicaragua");
        COUNTRIES.put("ne", "Niger");
        COUNTRIES.put("ng", "Nigeria");
        COUNTRIES.put("mk", "North Macedonia");
        COUNTRIES.put("no", "Norway");
        COUNTRIES.put("om", "Oman");
        COUNTRIES.put("pk", "Pakistan");
        COUNTRIES.put("pw", "Palau");
        COUNTRIES.put("ps", "Palestine");
        COUNTRIES.put("pa", "Panama");
        COUNTRIES.put("pg", "Papua New Guinea");
        COUNTRIES.put("py", "Paraguay");
        COUNTRIES.put("pe", "Peru");
        COUNTRIES.put("ph", "Philippines");
        COUNTRIES.put("pl", "Poland");
        COUNTRIES.put("pt", "Portugal");
        COUNTRIES.put("qa", "Qatar");
        COUNTRIES.put("ro", "Romania");
        COUNTRIES.put("ru", "Russia");
        COUNTRIES.put("rw", "Rwanda");
        COUNTRIES.put("kn", "Saint Kitts and Nevis");
        COUNTRIES.put("lc", "Saint Lucia");
        COUNTRIES.put("vc", "Saint Vincent and the Grenadines");
        COUNTRIES.put("ws", "Samoa");
        COUNTRIES.put("sm", "San Marino");
        COUNTRIES.put("st", "Sao Tome and Principe");
        COUNTRIES.put("sa", "Saudi Arabia");
        COUNTRIES.put("sn", "Senegal");
        COUNTRIES.put("rs", "Serbia");
        COUNTRIES.put("sc", "Seychelles");
        COUNTRIES.put("sl", "Sierra Leone");
        COUNTRIES.put("sg", "Singapore");
        COUNTRIES.put("sk", "Slovakia");
        COUNTRIES.put("si", "Slovenia");
        COUNTRIES.put("sb", "Solomon Islands");
        COUNTRIES.put("so", "Somalia");
        COUNTRIES.put("za", "South Africa");
        COUNTRIES.put("ss", "South Sudan");
        COUNTRIES.put("es", "Spain");
        COUNTRIES.put("lk", "Sri Lanka");
        COUNTRIES.put("sd", "Sudan");
        COUNTRIES.put("sr", "Suriname");
        COUNTRIES.put("se", "Sweden");
        COUNTRIES.put("ch", "Switzerland");
        COUNTRIES.put("sy", "Syria");
        COUNTRIES.put("tj", "Tajikistan");
        COUNTRIES.put("tz", "Tanzania");
        COUNTRIES.put("th", "Thailand");
        COUNTRIES.put("tl", "Timor-Leste");
        COUNTRIES.put("tg", "Togo");
        COUNTRIES.put("to", "Tonga");
        COUNTRIES.put("tt", "Trinidad and Tobago");
        COUNTRIES.put("tn", "Tunisia");
        COUNTRIES.put("tr", "Turkey");
        COUNTRIES.put("tm", "Turkmenistan");
        COUNTRIES.put("tv", "Tuvalu");
        COUNTRIES.put("ug", "Uganda");
        COUNTRIES.put("ua", "Ukraine");
        COUNTRIES.put("ae", "United Arab Emirates");
        COUNTRIES.put("gb", "United Kingdom");
        COUNTRIES.put("us", "United States");
        COUNTRIES.put("uy", "Uruguay");
        COUNTRIES.put("uz", "Uzbekistan");
        COUNTRIES.put("vu", "Vanuatu");
        COUNTRIES.put("ve", "Venezuela");
        COUNTRIES.put("vn", "Vietnam");
        COUNTRIES.put("ye", "Yemen");
        COUNTRIES.put("zm", "Zambia");
        COUNTRIES.put("zw", "Zimbabwe");
    }

    public static boolean isSupported(String isoCode) {
        return isoCode != null && COUNTRIES.containsKey(isoCode.toLowerCase());
    }
}
