package Utilities;

// TODO: Implement error handling

public class Language {
    private static CSVReader language;

    public Language(){
        try {
            language = new CSVReader("languages/" + System.getProperty("user.language") + ".csv",
                    ",", true);
        } catch (NullPointerException e) {
            language = new CSVReader("languages/da.csv",
                    ",", true);
        }
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
