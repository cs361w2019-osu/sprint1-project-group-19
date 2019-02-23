package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testInvalidScan(){
        Board testBoard = new Board();
        assertFalse(testBoard.scanBoard(12,'Z'));
        assertTrue(testBoard.scanBoard(5,'E'));
        assertFalse(testBoard.scanBoard(5,'E'));
        assertTrue(testBoard.scanBoard(5,'F'));
        assertTrue(testBoard.scanBoard(1,'A'));
    }


}
