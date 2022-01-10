package Board;

import Interface.GUIController;
import Utilities.Language;
import gui_main.GUI;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Logic.Player;
import javax.swing.*;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class BoardCSVTest {
    Board board;
    Player player;
    Square[] ALL_SQUARES;

    @Test
    void exceptionCSV(){
        System.setProperty("user.language","en");
        assertEquals("en", System.getProperty("user.language"));
        board = new Board();
    }



}
