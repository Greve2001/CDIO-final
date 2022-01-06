package Utilities;

// TODO: Implement error handling

public class Language {
    private static Language instance;
    private static CSVReader language;

    private Language(){
        try {
            language = new CSVReader("languages/" + System.getProperty("user.language") + ".csv",
                    ",", true);
        } catch (NullPointerException e) {
            language = new CSVReader("languages/da.csv",
                    ",", true);
        }
    }

    public static Language getInstance() {
        if(instance == null)
            instance = new Language();

        return instance;
    }

    public static String get(String textToRetrieve) {
        for (int i = 0; i < language.getFile().length; i++) {
            String varName = language.getFile()[i][1];

            if(varName.equals(textToRetrieve)) {
                return language.getFile()[i][0];
            }
        }

        // Default if no matches found.
        return "";
    }
}
