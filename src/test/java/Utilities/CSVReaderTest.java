package Utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {
    CSVReader reader;

    @BeforeEach
    void setup(){
        reader = new CSVReader("csvTest.csv", ",", true);
    }

    @Test
    void columnHeaderTest() {
        String[] expected = {"Header1", "Header2", "Header3", "Header4", "Header5", "Header6"};
        String[] actual = reader.getColumnHeaders();

        assertArrayEquals(expected, actual);
    }

    @Test
    void toStringTest() {
        String expected = "Value1, Value2, Value3, Value4, Value5, Value6, \n";
        String actual = reader.toString();

        assertEquals(expected, actual);
    }

    @AfterEach
    void tearDown() {
        reader.close();
    }
}