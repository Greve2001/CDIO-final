package Utilities;

// TODO [] Write a method to trim down the array in size.

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

public class CSVReader {
    private final Scanner FILE_SCANNER;
    private final String DELIMITER;

    private String[] columnHeaders;

    public CSVReader(String fileName, String delimiter, boolean hasColumnHeaders) {
        this.DELIMITER = delimiter;

        // Reads the file to a scanner object, so it can be used for reading lines later on.
        ClassLoader classLoader = getClass().getClassLoader();
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)));
        FILE_SCANNER = new Scanner(reader);

        if(hasColumnHeaders)
            columnHeaders = FILE_SCANNER.nextLine().split(delimiter);
    }

    // Returns the line as an array, so it can be stored in a larger array.
    public String[] readLine() {
        String[] result = new String[0];

        if(FILE_SCANNER.hasNextLine())
            result = FILE_SCANNER.nextLine().split(DELIMITER);

        return  result;
    }

    // Takes the file and converts it to a 2D array.
    public String[][] fileAsArr() {
        String[][] result = new String[20][1];

        int line = 0;
        while(FILE_SCANNER.hasNextLine()) {

            // Increases the size of the array if the file is longer than 20 lines.
            if(result.length <= line) {
                String[][] temp = new String[result.length * 2][1];

                // Copies the old array to the new array with increased size.
                int i = 0;
                for (String[] s : result) {
                    temp[i++] = s;
                }

                result = temp;
            }

            result[line++] = readLine();
        }

        return result;
    }

    public void close() {
        FILE_SCANNER.close();
    }

    public String[] getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(String[] columnHeaders) {
        this.columnHeaders = columnHeaders;
    }
}
