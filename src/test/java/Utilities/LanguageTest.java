package Utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void getNotTranslatedString() {
        System.setProperty("user.language", "en");
        Language.getInstance();

        String expected = "This string is not translated";
        String actual = Language.get("notTranslated");

        assertEquals(expected, actual);
    }
}