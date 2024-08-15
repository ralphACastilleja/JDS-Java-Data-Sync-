package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.util.Arrays;


public class _09_BrandList {
    //list of brands with predicted turnover times, unused
    public static Map<String, String> getBrandsWithTurnoverTimes() {
        Map<String, String> brandsWithTimes = new HashMap<>();
        brandsWithTimes.put("A. Lange & Söhne", "3-4 months");
        brandsWithTimes.put("Accutron", "4-6 months");
        brandsWithTimes.put("Akribos XXIV", "4-6 months");
        brandsWithTimes.put("Alpina", "3-4 months");
        brandsWithTimes.put("Anne Klein", "4-6 months");
        brandsWithTimes.put("Armani Exchange", "4-6 months");
        brandsWithTimes.put("Armitron", "4-6 months");
        brandsWithTimes.put("Arnold & Son", "3-4 months");
        brandsWithTimes.put("Audemars Piguet", "1-2 months");
        brandsWithTimes.put("Ball", "3-4 months");
        brandsWithTimes.put("Baume & Mercier", "3-4 months");
        brandsWithTimes.put("Bell & Ross", "3-4 months");
        brandsWithTimes.put("Bernhardt", "4-6 months");
        brandsWithTimes.put("Blancpain", "3-4 months");
        brandsWithTimes.put("Bovet", "3-4 months");
        brandsWithTimes.put("Breguet", "3-4 months");
        brandsWithTimes.put("Breitling", "2-3 months");
        brandsWithTimes.put("Bulova", "14-24 months");
        brandsWithTimes.put("Bvlgari", "3-4 months");
        brandsWithTimes.put("Camerer Cuss & Co", "4-6 months");
        brandsWithTimes.put("Cartier", "1-2 months");
        brandsWithTimes.put("Casio", "4-6 months");
        brandsWithTimes.put("Chanel", "3-4 months");
        brandsWithTimes.put("Chopard", "3-4 months");
        brandsWithTimes.put("Citizen", "3-4 months");
        brandsWithTimes.put("Claude Bernard", "4-6 months");
        brandsWithTimes.put("Concord", "4-6 months");
        brandsWithTimes.put("Corum", "3-4 months");
        brandsWithTimes.put("Curtis & Co", "4-6 months");
        brandsWithTimes.put("Daniel Wellington", "4-6 months");
        brandsWithTimes.put("Davosa", "4-6 months");
        brandsWithTimes.put("Deep Blue", "4-6 months");
        brandsWithTimes.put("Diesel", "4-6 months");
        brandsWithTimes.put("Dior", "3-4 months");
        brandsWithTimes.put("DKNY", "4-6 months");
        brandsWithTimes.put("Ebel", "4-6 months");
        brandsWithTimes.put("Edox", "4-6 months");
        brandsWithTimes.put("Elgin", "4-6 months");
        brandsWithTimes.put("Emporio Armani", "4-6 months");
        brandsWithTimes.put("Enicar", "4-6 months");
        brandsWithTimes.put("Eterna", "4-6 months");
        brandsWithTimes.put("F.P. Journe", "3-4 months");
        brandsWithTimes.put("Festina", "4-6 months");
        brandsWithTimes.put("Fortis", "4-6 months");
        brandsWithTimes.put("Fossil", "4-6 months");
        brandsWithTimes.put("Franck Muller", "3-4 months");
        brandsWithTimes.put("Frederique Constant", "3-4 months");
        brandsWithTimes.put("G-Shock", "4-6 months");
        brandsWithTimes.put("Garmin", "4-6 months");
        brandsWithTimes.put("Gavox", "4-6 months");
        brandsWithTimes.put("Geneva", "4-6 months");
        brandsWithTimes.put("Gerald Genta", "3-4 months");
        brandsWithTimes.put("Gevril", "4-6 months");
        brandsWithTimes.put("Girard-Perregaux", "3-4 months");
        brandsWithTimes.put("Glycine", "4-6 months");
        brandsWithTimes.put("Graham", "3-4 months");
        brandsWithTimes.put("Grand Seiko", "3-4 months");
        brandsWithTimes.put("Gucci", "3-4 months");
        brandsWithTimes.put("Hamilton", "24-32 months");
        brandsWithTimes.put("Harry Winston", "3-4 months");
        brandsWithTimes.put("Hautlence", "3-4 months");
        brandsWithTimes.put("Hublot", "2-3 months");
        brandsWithTimes.put("IWC", "2-3 months");
        brandsWithTimes.put("Jacob & Co", "3-4 months");
        brandsWithTimes.put("Jaeger-LeCoultre", "2-3 months");
        brandsWithTimes.put("Jaquet Droz", "3-4 months");
        brandsWithTimes.put("JeanRichard", "4-6 months");
        brandsWithTimes.put("Junghans", "4-6 months");
        brandsWithTimes.put("Juvenia", "4-6 months");
        brandsWithTimes.put("Konstantin Chaykin", "3-4 months");
        brandsWithTimes.put("Laco", "4-6 months");
        brandsWithTimes.put("Lange & Söhne", "3-4 months");
        brandsWithTimes.put("Lars Larsen", "4-6 months");
        brandsWithTimes.put("Longines", "3-4 months");
        brandsWithTimes.put("Lorus", "4-6 months");
        brandsWithTimes.put("Louis Erard", "4-6 months");
        brandsWithTimes.put("Louis Moinet", "4-6 months");
        brandsWithTimes.put("Luminox", "4-6 months");
        brandsWithTimes.put("Luzerne", "4-6 months");
        brandsWithTimes.put("Maurice Lacroix", "3-4 months");
        brandsWithTimes.put("MB&F", "3-4 months");
        brandsWithTimes.put("Michael Kors", "4-6 months");
        brandsWithTimes.put("Mido", "4-6 months");
        brandsWithTimes.put("Mondaine", "4-6 months");
        brandsWithTimes.put("Montblanc", "3-4 months");
        brandsWithTimes.put("Montres De Luxe", "4-6 months");
        brandsWithTimes.put("Movado", "4-6 months");
        brandsWithTimes.put("Nautica", "4-6 months");
        brandsWithTimes.put("Nixon", "4-6 months");
        brandsWithTimes.put("Nomos", "3-4 months");
        brandsWithTimes.put("Omega", "Varies Drasticly");
        brandsWithTimes.put("Orient", "4-6 months");
        brandsWithTimes.put("Oris", "4-6 months");
        brandsWithTimes.put("Panerai", "3-4 months");
        brandsWithTimes.put("Parmigiani Fleurier", "3-4 months");
        brandsWithTimes.put("Patek Philippe", "1-2 months");
        brandsWithTimes.put("Perrelet", "4-6 months");
        brandsWithTimes.put("Philip Stein", "4-6 months");
        brandsWithTimes.put("Philippe Dufour", "3-4 months");
        brandsWithTimes.put("Piaget", "3-4 months");
        brandsWithTimes.put("Porsche Design", "4-6 months");
        brandsWithTimes.put("Rado", "3-4 months");
        brandsWithTimes.put("Raymond Weil", "3-4 months");
        brandsWithTimes.put("Richard Mille", "3-4 months");
        brandsWithTimes.put("Roamer", "4-6 months");
        brandsWithTimes.put("Rolex", "1-2 months");
        brandsWithTimes.put("Rotary", "4-6 months");
        brandsWithTimes.put("Salvatore Ferragamo", "4-6 months");
        brandsWithTimes.put("Seiko", "3-4 months");
        brandsWithTimes.put("Sekonda", "4-6 months");
        brandsWithTimes.put("Shinola", "4-6 months");
        brandsWithTimes.put("Skagen", "4-6 months");
        brandsWithTimes.put("Speake-Marin", "3-4 months");
        brandsWithTimes.put("Stowa", "4-6 months");
        brandsWithTimes.put("Stuhrling", "4-6 months");
        brandsWithTimes.put("Tag Heuer", "2-3 months");
        brandsWithTimes.put("Technomarine", "4-6 months");
        
 
    
        brandsWithTimes.put("Tiffany & Co", "3-4 months");
        brandsWithTimes.put("Timex", "4-6 months");
        brandsWithTimes.put("Tissot", "3-4 months");
        brandsWithTimes.put("Tourneau", "4-6 months");
        brandsWithTimes.put("Traser", "4-6 months");
        brandsWithTimes.put("U-Boat", "4-6 months");
        brandsWithTimes.put("Ulysse Nardin", "3-4 months");
        brandsWithTimes.put("Union Glashütte", "4-6 months");
        brandsWithTimes.put("Universal Genève", "4-6 months");
        brandsWithTimes.put("Urwerk", "3-4 months");
        brandsWithTimes.put("Vacheron Constantin", "3-4 months");
        brandsWithTimes.put("Van Cleef & Arpels", "4-6 months");
        brandsWithTimes.put("Versace", "4-6 months");
        brandsWithTimes.put("Vincero", "4-6 months");
        brandsWithTimes.put("Vostok", "4-6 months");
        brandsWithTimes.put("Vulcain", "4-6 months");
        brandsWithTimes.put("Waltham", "4-6 months");
        brandsWithTimes.put("Wenger", "4-6 months");
        brandsWithTimes.put("Wittnauer", "4-6 months");
        brandsWithTimes.put("Xetum", "4-6 months");
        brandsWithTimes.put("Zenith", "3-4 months");
        brandsWithTimes.put("Zodiac", "4-6 months");
        brandsWithTimes.put("Zeno", "4-6 months");
        brandsWithTimes.put("Alessi", "4-6 months");
        brandsWithTimes.put("Apple", "4-6 months");
        brandsWithTimes.put("Aristo", "4-6 months");
        brandsWithTimes.put("Armand Nicolet", "4-6 months");
        brandsWithTimes.put("Arne Jacobsen", "4-6 months");
        brandsWithTimes.put("Askania", "4-6 months");
        brandsWithTimes.put("Atlantic", "4-6 months");
        brandsWithTimes.put("Balticus", "4-6 months");
        brandsWithTimes.put("Bamford", "4-6 months");
        brandsWithTimes.put("Beijing Watch Factory", "4-6 months");
        brandsWithTimes.put("Bohlin Cywinski Jackson", "4-6 months");
        brandsWithTimes.put("Boucheron", "4-6 months");
        brandsWithTimes.put("Braun", "4-6 months");
        brandsWithTimes.put("Bremont", "4-6 months");
        brandsWithTimes.put("Calvin Klein", "4-6 months");
        brandsWithTimes.put("Carl F. Bucherer", "4-6 months");
        brandsWithTimes.put("Carpenter", "4-6 months");
        brandsWithTimes.put("Certina", "4-6 months");
        brandsWithTimes.put("Christiaan van der Klaauw", "4-6 months");
        brandsWithTimes.put("Claude Meylan", "4-6 months");
        brandsWithTimes.put("Clerc", "4-6 months");
        brandsWithTimes.put("Coutura", "4-6 months");
        brandsWithTimes.put("CWC", "4-6 months");
        brandsWithTimes.put("Cyma", "4-6 months");
        brandsWithTimes.put("D1 Milano", "4-6 months");
        brandsWithTimes.put("Davidoff", "4-6 months");
        brandsWithTimes.put("De Bethune", "4-6 months");
        brandsWithTimes.put("Defakto", "4-6 months");
        brandsWithTimes.put("Delma", "4-6 months");
        brandsWithTimes.put("Dolce & Gabbana", "4-6 months");
        brandsWithTimes.put("Doxa", "4-6 months");
        brandsWithTimes.put("DWISS", "4-6 months");
        brandsWithTimes.put("Edifice", "4-6 months");
        brandsWithTimes.put("Elliot Brown", "4-6 months");
        brandsWithTimes.put("Elysee", "4-6 months");
        brandsWithTimes.put("Fabergé", "4-6 months");
        brandsWithTimes.put("Farer", "4-6 months");
        brandsWithTimes.put("Favre-Leuba", "4-6 months");
        brandsWithTimes.put("Ferrari", "4-6 months");
        brandsWithTimes.put("Fiyta", "4-6 months");
        brandsWithTimes.put("Franck Muller", "3-4 months");
        brandsWithTimes.put("Gallet", "4-6 months");
        brandsWithTimes.put("Garrick", "4-6 months");
        brandsWithTimes.put("Glashütte Original", "4-6 months");
        brandsWithTimes.put("Graf Zeppelin", "4-6 months");
        brandsWithTimes.put("Hanhart", "4-6 months");
        brandsWithTimes.put("Helson", "4-6 months");
        brandsWithTimes.put("Junkers", "4-6 months");
        brandsWithTimes.put("Kobold", "4-6 months");
        brandsWithTimes.put("Linde Werdelin", "4-6 months");
        brandsWithTimes.put("Marathon", "4-6 months");
        brandsWithTimes.put("MeisterSinger", "4-6 months");
        brandsWithTimes.put("NOMOS Glashütte", "4-6 months");
        brandsWithTimes.put("Perrelet", "4-6 months");
        brandsWithTimes.put("Polar", "4-6 months");
        brandsWithTimes.put("Q&Q", "4-6 months");
        brandsWithTimes.put("Seagull", "4-6 months");
        brandsWithTimes.put("Steinhart", "4-6 months");
        brandsWithTimes.put("Suunto", "4-6 months");
        brandsWithTimes.put("Swatch", "4-6 months");
        brandsWithTimes.put("Tudor", "2-3 months");
        brandsWithTimes.put("Victorinox", "4-6 months");
        brandsWithTimes.put("Vostok Europe", "4-6 months");
        brandsWithTimes.put("Xeric", "4-6 months");
        brandsWithTimes.put("Yema", "4-6 months");
        brandsWithTimes.put("Alain Silberstein", "4-6 months");
        brandsWithTimes.put("Alessandro Baldieri", "4-6 months");
        brandsWithTimes.put("Android", "4-6 months");
        brandsWithTimes.put("Andreas Strehler", "4-6 months");
        brandsWithTimes.put("Anonimo", "4-6 months");
        brandsWithTimes.put("Aquanautic", "4-6 months");
        brandsWithTimes.put("Aristo Vollmer", "4-6 months");
        brandsWithTimes.put("Azimuth", "4-6 months");
        brandsWithTimes.put("Ballast", "4-6 months");
        brandsWithTimes.put("Bertucci", "4-6 months");
        brandsWithTimes.put("Bruno Söhnle", "4-6 months");
        brandsWithTimes.put("Burberry", "4-6 months");
        brandsWithTimes.put("Charmex", "4-6 months");
        brandsWithTimes.put("Christophe Claret", "4-6 months");
        brandsWithTimes.put("Chronoswiss", "4-6 months");
        brandsWithTimes.put("Cuervo y Sobrinos", "4-6 months");
        brandsWithTimes.put("Damasko", "4-6 months");
        brandsWithTimes.put("DeWitt", "4-6 months");
        brandsWithTimes.put("Dietrich", "4-6 months");
        brandsWithTimes.put("Dubey & Schaldenbrand", "4-6 months");
        brandsWithTimes.put("Eberhard & Co.", "4-6 months");
        brandsWithTimes.put("Edison", "4-6 months");
        brandsWithTimes.put("Eloga", "4-6 months");
        brandsWithTimes.put("Evisu", "4-6 months");
        brandsWithTimes.put("Glycine", "4-6 months");
        brandsWithTimes.put("Gorilla", "4-6 months");
        brandsWithTimes.put("Graham", "4-6 months");
        brandsWithTimes.put("Habring²", "4-6 months");
        brandsWithTimes.put("Hublot", "2-3 months");
        brandsWithTimes.put("Ikepod", "4-6 months");
        brandsWithTimes.put("Invicta", "4-6 months");
        brandsWithTimes.put("J.N. Shapiro", "4-6 months");
        brandsWithTimes.put("Jacob & Co.", "3-4 months");
        brandsWithTimes.put("Jaquet Droz", "3-4 months");
        brandsWithTimes.put("Johan Eric", "4-6 months");
        brandsWithTimes.put("John Hardy", "4-6 months");
        brandsWithTimes.put("Jordi", "4-6 months");
        brandsWithTimes.put("Jorg Gray", "4-6 months");
       
        brandsWithTimes.put("Jules Audemars", "4-6 months");
        brandsWithTimes.put("Kari Voutilainen", "4-6 months");
        brandsWithTimes.put("Kentex", "4-6 months");
        brandsWithTimes.put("Kobold", "4-6 months");
        brandsWithTimes.put("Kronos", "4-6 months");
        brandsWithTimes.put("Lang & Heyne", "4-6 months");
        brandsWithTimes.put("Linde Werdelin", "4-6 months");
        brandsWithTimes.put("Lüm-Tec", "4-6 months");
        brandsWithTimes.put("Magellan", "4-6 months");
        brandsWithTimes.put("Marc & Sons", "4-6 months");
        brandsWithTimes.put("Martin Braun", "4-6 months");
        brandsWithTimes.put("MB&F", "3-4 months");
        brandsWithTimes.put("MCT", "4-6 months");
        brandsWithTimes.put("Meistersinger", "4-6 months");
        brandsWithTimes.put("Milus", "4-6 months");
        brandsWithTimes.put("Mondia", "4-6 months");
        brandsWithTimes.put("Monta", "4-6 months");
        brandsWithTimes.put("Montblanc", "3-4 months");
        brandsWithTimes.put("Moritz Grossmann", "4-6 months");
        brandsWithTimes.put("Mühle Glashütte", "4-6 months");
        brandsWithTimes.put("Nienaber Bünde", "4-6 months");
        brandsWithTimes.put("Oris", "4-6 months");
        brandsWithTimes.put("Paul Picot", "4-6 months");
        brandsWithTimes.put("Paul Smith", "4-6 months");
        brandsWithTimes.put("Pita Barcelona", "4-6 months");
        brandsWithTimes.put("Pro-Hunter", "4-6 months");
        brandsWithTimes.put("RGM", "4-6 months");
        brandsWithTimes.put("Romain Gauthier", "4-6 months");
        brandsWithTimes.put("Romain Jerome", "4-6 months");
        brandsWithTimes.put("Rössler", "4-6 months");
        brandsWithTimes.put("Rudolf", "4-6 months");
        brandsWithTimes.put("Sinn", "4-6 months");
        brandsWithTimes.put("Speake-Marin", "3-4 months");
        brandsWithTimes.put("Stowa", "4-6 months");
        brandsWithTimes.put("Tempus Computare", "4-6 months");
        brandsWithTimes.put("Tempvs Fvgit", "4-6 months");
        brandsWithTimes.put("Thomas Ninchritz", "4-6 months");
        brandsWithTimes.put("Thomas Prescher", "4-6 months");
        brandsWithTimes.put("Tutima", "4-6 months");
        brandsWithTimes.put("Urban Jürgensen", "4-6 months");
        brandsWithTimes.put("U-Boat", "4-6 months");
        brandsWithTimes.put("Ulysse Nardin", "3-4 months");
        brandsWithTimes.put("Urwerk", "3-4 months");
        brandsWithTimes.put("Vianney Halter", "4-6 months");
        brandsWithTimes.put("Voutilainen", "4-6 months");
        brandsWithTimes.put("Vulcain", "4-6 months");
        brandsWithTimes.put("Wempe", "4-6 months");
        brandsWithTimes.put("Wilson Watch Works", "4-6 months");
        brandsWithTimes.put("Xemex", "4-6 months");
        brandsWithTimes.put("Zeitwinkel", "4-6 months");
        //Change
        brandsWithTimes.put("Zodiac", "24-32 months");
        brandsWithTimes.put("Zürcher", "4-6 months");
        brandsWithTimes.put("Spartacus", "8-12 months");

        return brandsWithTimes;
    }


    //Gets the brand from searching ta string 
    public static String getBrandFromTitle(String title, List<String> brands) {
        for (String brand : brands) {
            if (title.toLowerCase().contains(brand.toLowerCase())) {
                return brand;
            }
        }
        return null;
    }

//Brands we have
    public static List<String> getBrands() {
        return Arrays.asList(
            "A. Lange & Söhne", "Accutron", "Akribos XXIV", "Alpina", "Anne Klein", "Armani Exchange", "Armitron", "Arnold & Son",
            "Audemars Piguet", "Ball", "Baume & Mercier", "Bell & Ross", "Bernhardt", "Blancpain", "Bovet", "Breguet", "Breitling",
            "Bulova", "Bvlgari", "Camerer Cuss & Co", "Cartier", "Casio", "Chanel", "Chopard", "Citizen", "Claude Bernard", "Concord",
            "Corum", "Curtis & Co", "Daniel Wellington", "Davosa", "Deep Blue", "Diesel", "Dior", "DKNY", "Ebel", "Edox", "Elgin",
            "Emporio Armani", "Enicar", "Eterna", "F.P. Journe", "Festina", "Fortis", "Fossil", "Franck Muller", "Frederique Constant",
            "G-Shock", "Garmin", "Gavox", "Geneva", "Gerald Genta", "Gevril", "Girard-Perregaux", "Glycine", "Graham", "Grand Seiko",
            "Gucci", "Hamilton", "Harry Winston", "Hautlence", "Hublot", "IWC", "Jacob & Co", "Jaeger-LeCoultre", "Jaquet Droz",
            "JeanRichard", "Junghans", "Juvenia", "Konstantin Chaykin",             "JeanRichard", "Junghans", "Juvenia", "Konstantin Chaykin", "Laco", "Lange & Söhne", "Lars Larsen", "Longines", "Lorus",
            "Louis Erard", "Louis Moinet", "Luminox", "Luzerne", "Maurice Lacroix", "MB&F", "Michael Kors", "Mido", "Mondaine", "Montblanc",
            "Montres De Luxe", "Movado", "Nautica", "Nixon", "Nomos", "Omega", "Orient", "Oris", "Panerai", "Parmigiani Fleurier", "Patek Philippe",
            "Perrelet", "Philip Stein", "Philippe Dufour", "Piaget", "Porsche Design", "Rado", "Raymond Weil", "Richard Mille", "Roamer",
            "Rolex", "Rotary", "Salvatore Ferragamo", "Seiko", "Sekonda", "Shinola", "Skagen", "Speake-Marin", "Stowa", "Stuhrling",
            "Tag Heuer", "Technomarine", "Tiffany & Co", "Timex", "Tissot", "Tourneau", "Traser", "U-Boat", "Ulysse Nardin", "Union Glashütte",
            "Universal Genève", "Urwerk", "Vacheron Constantin", "Van Cleef & Arpels", "Versace", "Vincero", "Vostok", "Vulcain", "Waltham",
            "Wenger", "Wittnauer", "Xetum", "Zenith", "Zodiac", "Zeno", "Alessi", "Alpina", "Apple", "Aristo", "Armand Nicolet", "Armitron",
            "Arne Jacobsen", "Askania", "Atlantic", "Audemars Piguet", "Ball", "Balticus", "Bamford", "Baume et Mercier", "Beijing Watch Factory",
            "Bell & Ross", "Blancpain", "Bohlin Cywinski Jackson", "Boucheron", "Braun", "Breitling", "Bremont", "Bulova", "Calvin Klein", "Carl F. Bucherer",
            "Carpenter", "Casio", "Certina", "Chanel", "Chopard", "Christiaan van der Klaauw", "Citizen", "Claude Meylan", "Clerc", "Concord",
            "Corum", "Coutura", "CWC", "Cyma", "D1 Milano", "Davidoff", "De Bethune", "Defakto", "Delma", "Dior", "DKNY", "Dolce & Gabbana",
            "Doxa", "DWISS", "Ebel", "Edifice", "Elliot Brown", "Elysee", "Emporio Armani", "Enicar", "Eterna", "Fabergé", "Farer", "Favre-Leuba",
            "Ferrari", "Fiyta", "Fortis", "Franck Muller", "Frederique Constant", "G-Shock", "Gallet", "Garrick", "Gevril", "Girard-Perregaux",
            "Glashütte Original", "Glycine", "Graf Zeppelin", "Grand Seiko", "Gucci", "Hamilton", "Hanhart", "Hautlence", "Helson", "Hublot",
            "IWC", "Jacob & Co", "Jaeger-LeCoultre", "JeanRichard", "Junghans", "Junkers", "Kobold", "Laco", "Linde Werdelin", "Longines",
            "Lorus", "Louis Erard", "Louis Moinet", "Luminox", "Marathon", "Maurice Lacroix", "MB&F", "MeisterSinger", "Mido", "Montblanc",
            "Movado", "Nautica", "NOMOS Glashütte", "Omega", "Orient", "Oris", "Panerai", "Parmigiani Fleurier", "Patek Philippe", "Perrelet",
            "Piaget", "Polar", "Porsche Design", "Q&Q", "Rado", "Raymond Weil", "Richard Mille", "Roamer", "Rolex", "Rotary", "Seagull", "Seiko",
            "Sekonda", "Shinola", "Skagen", "Speake-Marin", "Steinhart", "Stowa", "Suunto", "Swatch", "TAG Heuer", "Technomarine", "Tiffany & Co",
            "Timex", "Tissot", "Tourneau", "Traser", "Tudor", "U-Boat", "Ulysse Nardin", "Vacheron Constantin", "Van Cleef & Arpels", "Versace",
            "Victorinox", "Vostok Europe", "Vulcain", "Waltham", "Wenger", "Xeric", "Yema", "Zenith", "Zodiac", "Alain Silberstein", "Alessandro Baldieri",
            "Android", "Andreas Strehler", "Anonimo", "Aquanautic", "Aristo Vollmer", "Azimuth", "Ballast", "Bertucci", "Bremont", "Bruno Söhnle",
            "Burberry", "Carl F. Bucherer", "Charmex", "Christophe Claret", "Chronoswiss", "Concord", "Cuervo y Sobrinos", "Damasko", "Davidoff",
            "DeWitt", "Dietrich", "Dubey & Schaldenbrand", "Eberhard & Co.", "Edison", "Eloga", "Eterna", "Evisu", "Fortis", "Frédérique Constant",
            "Glashütte Original", "Glycine", "Gorilla", "Graham", "Habring²", "Hublot", "Ikepod", "Invicta", "J.N. Shapiro", "Jacob & Co.",
            "Jaquet Droz", "Johan Eric", "John Hardy", "Jordi", "Jorg Gray", "Jules Audemars", "Kari Voutilainen", "Kentex", "Kobold", "Kronos",
            "Lang & Heyne", "Linde Werdelin", "Lüm-Tec", "Magellan", "Marc & Sons", "Martin Braun", "MB&F", "MCT", "Meistersinger", "Milus",
            "Mondia", "Monta", "Montblanc", "Moritz Grossmann", "Mühle Glashütte", "Nienaber Bünde", "Oris", "Paul Picot", "Paul Smith", "Pita Barcelona",
            "Pro-Hunter", "RGM", "Romain Gauthier", "Romain Jerome", "Rössler", "Rudolf", "Sinn", "Speake-Marin", "Stowa", "Tempus Computare",
            "Tempvs Fvgit", "Thomas Ninchritz", "Thomas Prescher", "Tutima", "Urban Jürgensen", "U-Boat", "Ulysse Nardin", "Urwerk", "Vianney Halter",
            "Voutilainen","TEST123", "L & R",
       "Alain Silberstein", "Alessandro Baldieri", "Android", "Andreas Strehler", "Anonimo", "Aquanautic", "Aristo Vollmer", "Azimuth",
"Ballast", "Bertucci", "Bohlin Cywinski Jackson", "Boucheron", "Braun", "Bruno Söhnle", "Burberry", "Calvin Klein",
"Carpenter", "Certina", "Christiaan van der Klaauw", "Claude Meylan", "Clerc", "Coutura", "CWC", "Cyma",
"D1 Milano", "Davidoff", "De Bethune", "Defakto", "Delma", "Dolce & Gabbana", "Doxa", "DWISS",
"Edifice", "Elliot Brown", "Elysee", "Fabergé", "Farer", "Favre-Leuba", "Ferrari", "Fiyta",
"Gallet", "Garrick", "Glashütte Original", "Graf Zeppelin", "Hanhart", "Helson", "Ikepod", "Invicta",
"J.N. Shapiro", "Johan Eric", "John Hardy", "Jordi", "Jorg Gray", "Jules Audemars", "Kari Voutilainen", "Kentex",
"Kronos", "Lang & Heyne", "Linde Werdelin", "Lüm-Tec", "Magellan", "Marc & Sons", "Martin Braun", "MCT",
"Meistersinger", "Milus", "Mondia", "Monta", "Moritz Grossmann", "Mühle Glashütte", "Nienaber Bünde", "Paul Picot",
"Paul Smith", "Pita Barcelona", "Polar", "Q&Q", "RGM", "Romain Gauthier", "Romain Jerome", "Rössler",
"Rudolf", "Seagull", "Steinhart", "Suunto", "Swatch", "Tudor", "Urban Jürgensen", "Vianney Halter",
"Victorinox", "Vostok Europe", "Wempe", "Xeric", "Yema", "Zeitwinkel", "Zürcher", "Sherline Collet",
"ETA", "Calvin Klein", "Alba", "Anker", "Arbutus", "Bertolucci", "Borel", "Bovet Fleurier",
"Breguet Marine", "Cecil Purnell", "Chopard Mille Miglia", "Chronoswiss", "Christophe Claret", "Concord Mariner", "Cuervo y Sobrinos", "Damasko",
"DeWitt", "Dietrich", "Dubey & Schaldenbrand", "Eberhard & Co.", "Edison", "Eloga", "Eterna KonTiki", "Evisu",
"Fortis Spacematic", "Frédérique Constant Slimline", "Gevril Tribeca", "Gorilla Fastback", "Graham Chronofighter", "Habring²", "Hublot Big Bang", "Ikepod Hemipode",
"Invicta Pro Diver", "J.N. Shapiro Infinity", "Jacob & Co. Astronomia", "Jaquet Droz Grande Seconde", "Johan Eric Copenhagen", "John Hardy Classic Chain", "Jordi Icon", "Jorg Gray JG6500",
"Kari Voutilainen Vingt-8", "Kentex Marineman", "Kobold Phantom", "Kronos Nautilus", "Lang & Heyne Friedrich August I", "Linde Werdelin Spidolite", "Lüm-Tec Combat", "Magellan Magellano",
"Marc & Sons Automatic Diver", "Martin Braun EOS", "MB&F Legacy Machine", "Meistersinger Perigraph", "Milus Snow Star", "Mondia Madison", "Monta Triumph", "Moritz Grossmann Benu",
"Mühle Glashütte S.A.R. Rescue-Timer", "Nienaber Bünde Regulator", "Oris Aquis", "Paul Picot Plongeur", "Paul Smith Track", "Pita Barcelona Oceana", "Pro-Hunter Rolex", "Rado Captain Cook",
"Romain Gauthier Logical One", "Romain Jerome Titanic-DNA", "Rössler Stella", "Rudolf Flik Flak", "Sinn U1", "Speake-Marin One & Two", "Stowa Flieger", "Tempus Computare",
"Tempvs Fvgit Chrono", "Thomas Ninchritz T-Nich", "Thomas Prescher Triple Axis Tourbillon", "Tutima Glashütte", "Urban Jürgensen Jules", "Vianney Halter Antiqua", "Voutilainen Vingt-8", "Wilson Watch Works",
"Xemex Offroad", "Zeitwinkel 273°", "Zodiac Super Sea Wolf", "Zürcher Drive", "Sherline Collet Classic", "ETA Valjoux", "Spartacus Quartz", "Wilson Watch Works Project 99",
"Voutilainen Vingt-8 ISO", "Zodiac Astrographic", "Zürcher ZT3", "UNBRANDED", "VIGOR", "WALLER","ULTRASONIC", "Vulcain", "Wempe", "illinois", "Spartacus", "Wilson Watch Works", "Xemex", "Zeitwinkel", "Zodiac", "Zürcher", "Sherline Collet", "ETA", "Girard-Perregaux", " Girard-Perregaux"
        );
    }


  
    //categories we have 
    public static List<String> getCategories() {
        return Arrays.asList(
            "parts",   "Watch Movement Holder","band", "Collet", "Balance Assembly","Wristwatch Movement", "Wristwatch",
            "Pocket Watch Movement", "pocket watch case",
              "pocket watch", "tool"
        );
    }

//Categoreis that we discounted 
    public static List<String> getCategories2(){
        return Arrays.asList(
            "CLEANING BASKET", "parts",   "Watch Movement Holder","band", "Collet", "Balance Assembly","Wristwatch Movement", "Wristwatch",
            "Pocket Watch Movement", "pocket watch case",
              "pocket watch", "tool", "BASKET", "BALANCE SCREW HOLDER"
        );
    }


    //getmethod 
    public static Map<String, String> getPredictedTurnoverTimes() {
        return getBrandsWithTurnoverTimes();
    }
}



