package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testInvalidScan(){
        Board testBoard = new Board();
        assertFalse(testBoard.useSonarPulse(12,'Z'));
        testBoard.setSonarPulses(3);
        assertTrue(testBoard.useSonarPulse(5,'E'));
        assertFalse(testBoard.useSonarPulse(5,'E'));
        assertTrue(testBoard.useSonarPulse(5,'F'));
        assertTrue(testBoard.useSonarPulse(1,'A'));
    }


}
