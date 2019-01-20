package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"),10,'A',true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"),1,'J',false));
    }

    @Test
    public void testValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"),1,'A',true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"),9,'A',false));
    }
}
