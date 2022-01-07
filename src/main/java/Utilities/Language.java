package Utilities;

public class Language {
    private static Language instance;
    private static CSVReader language;
    private static String langInUse;

    private Language(){
        langInUse = System.getProperty("user.language");
        chooseLanguage(langInUse);
    }

    public static Language getInstance() {
        if(instance == null)
            instance = new Language();

        return instance;
    }

    public static String get(String textToRetrieve) {
        for (int i = 0; i < language.getFile().length; i++) {
            if(language.getFile()[i].length >= 2) {
                String varName = language.getFile()[i][1];

                if (varName.equals(textToRetrieve)) {
                    return language.getFile()[i][0];
                }
            }
        }

        // Falls back to Danish and looks if the missing string is present.
        String result = "";
        if(!langInUse.equalsIgnoreCase("da")) {
            chooseLanguage("da");
            result = get(textToRetrieve);

            chooseLanguage(System.getProperty("user.language"));
        }

        // Default to empty string if no matches found.
        return result;
    }

    private static void chooseLanguage(String lang) {
        try {
            language = new CSVReader("languages/" + lang + ".csv",
                    ",", true);
            langInUse = lang;
        } catch (NullPointerException e) {
            language = new CSVReader("languages/da.csv",
                    ",", true);
            langInUse = "da";
        }
    }
}
