package Utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {
    @BeforeEach
    void setup() {
        System.setProperty("user.language", "en");
        Language.getInstance();
    }

    @Test
    void getNotTranslatedString() {
        String expected = "This string is not translated";
        String actual = Language.get("notTranslated");

        assertEquals(expected, actual);
    }

    @Test
    void emptyLineInTranslateFile() {
        String expected = "the line above is empty";
        String actual = Language.get("emptyLineAbove");

        assertEquals(expected, actual);
    }
}