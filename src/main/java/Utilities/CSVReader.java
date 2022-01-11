package Utilities;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

public class CSVReader {
    private final Scanner FILE_SCANNER;
    private final String DELIMITER;
    private String[][] file;
    private int size;

    private String[] columnHeaders;

    public CSVReader(String fileName, String delimiter, boolean hasColumnHeaders) {
        this.DELIMITER = delimiter;

        // Reads the file to a scanner object, so it can be used for reading lines later on.
        ClassLoader classLoader = getClass().getClassLoader();
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)));
        FILE_SCANNER = new Scanner(reader);

        if (hasColumnHeaders)
            columnHeaders = FILE_SCANNER.nextLine().split(delimiter);

        file = fileAsArr();
        file = trimArrLength();
    }

    // Returns the line as an array, so it can be stored in a larger array.
    private String[] readLine() {
        String[] result = new String[0];

        if (FILE_SCANNER.hasNextLine())
            result = FILE_SCANNER.nextLine().split(DELIMITER);

        return result;
    }

    // Takes the file and converts it to a 2D array.
    private String[][] fileAsArr() {
        String[][] result = new String[20][];

        size = 0;
        while (FILE_SCANNER.hasNextLine()) {

            // Increases the size of the array if the file is longer than 20 lines.
            if (result.length <= size) {
                String[][] temp = new String[result.length * 2][];

                // Copies the old array to the new array with increased size.
                int i = 0;
                for (String[] s : result) {
                    temp[i++] = s;
                }

                result = temp;
            }

            result[size++] = readLine();
        }

        return result;
    }

    private String[][] trimArrLength() {
        String[][] trimmedArray = new String[size][];

        for (int i = 0; i < size; i++) {
            trimmedArray[i] = file[i];
        }

        return trimmedArray;
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

    public String[][] getFile() {
        return file;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (String[] row : file) {
            for (String col : row) {
                result.append(col).append(", ");
            }
            result.append("\n");
        }

        return result.toString();
    }
}
