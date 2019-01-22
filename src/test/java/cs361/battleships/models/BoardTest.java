package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testAttackLocation() {
        Board board = new Board();
        assertTrue(board.attack(1,'A').getResult() == AtackStatus.MISS);
        assertTrue(board.attack(11,'K').getResult() == AtackStatus.INVALID);
    }

    @Test
    public void testAttackOverlap() {
        Board board = new Board();
        assertTrue(board.attack(1,'A').getResult() == AtackStatus.MISS);
        assertTrue(board.attack(1,'A').getResult() == AtackStatus.INVALID);
    }

    @Test
    public void testShipAttack() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"),1,'A',true);
        board.placeShip(new Ship("MINESWEEPER"),3,'C',false);
        assertTrue(board.attack(1,'A').getResult() == AtackStatus.HIT);
        assertTrue(board.attack(2,'A').getResult() == AtackStatus.SUNK);
        assertTrue(board.attack(3,'C').getResult() == AtackStatus.HIT);
        assertTrue(board.attack(3,'C').getResult() == AtackStatus.INVALID);
        assertTrue(board.attack(3,'D').getResult() == AtackStatus.SURRENDER);
    }
}