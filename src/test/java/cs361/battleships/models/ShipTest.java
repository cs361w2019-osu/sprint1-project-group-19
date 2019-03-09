package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void testPlaceMinesweeperHorizontaly() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        assertEquals(expected, occupiedSquares);
    }
    @Test
    public void testPlaceSubmarineHorizontaly() {
        Ship submarine = new Ship("SUBMARINE");
        submarine.place('A', 2, false, false);
        List<Square> occupiedSquares = submarine.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(2, 'A'));
        expected.add(new Square(2, 'B'));
        expected.add(new Square(2, 'C'));
        expected.add(new Square(2, 'D'));
        expected.add(new Square(1, 'C'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceSubmarineOverlapping() {
        Ship submarine = new Ship("SUBMARINE");
        Ship battleship = new Ship("BATTLESHIP");
        submarine.place('A', 2, false, true);
        battleship.place('B',2,false, false);
        List<Square> occupiedSquares1 = submarine.getOccupiedSquares();
        List<Square> occupiedSquares2 = battleship.getOccupiedSquares();
        ArrayList<Object> expected1 = new ArrayList<>();
        ArrayList<Object> expected2 = new ArrayList<>();
        expected1.add(new Square(2, 'A'));
        expected1.add(new Square(2, 'B'));
        expected1.add(new Square(2, 'C'));
        expected1.add(new Square(2, 'D'));
        expected1.add(new Square(1, 'C'));

        expected2.add(new Square(2, 'B'));
        expected2.add(new Square(2, 'C'));
        expected2.add(new Square(2, 'D'));
        expected2.add(new Square(2, 'E'));
        assertEquals(expected1, occupiedSquares1);
        assertEquals(expected2, occupiedSquares2);
    }

    @Test
    public void testPlaceSubmarineVertically() {
        Ship submarine = new Ship("SUBMARINE");
        submarine.place('A', 1, true, false);
        List<Square> occupiedSquares = submarine.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        expected.add(new Square(4, 'A'));
        expected.add(new Square(3, 'B'));
        assertEquals(expected, occupiedSquares);
    }
    @Test
    public void testPlaceMinesweeperVertically() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerHorizontaly() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerVertically() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipHorizontaly() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, false, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        expected.add(new Square(1, 'D'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipVertically() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testShipOverlaps() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true, false);

        assertTrue(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testShipsDontOverlap() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('C', 2, true, false);

        assertFalse(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testIsAtLocation() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);

        assertTrue(minesweeper.isAtLocation(new Square(1, 'A')));
        assertTrue(minesweeper.isAtLocation(new Square(2, 'A')));
    }

    @Test
    public void testHit() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true, false);

        Result result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
        assertEquals(new Square(1, 'A'), result.getLocation());
    }

    @Test
    public void testSink() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);

        minesweeper.attack(1, 'A');
        Result result = minesweeper.attack(2, 'A');

        assertEquals(AtackStatus.SUNK, result.getResult());
        assertEquals(minesweeper, result.getShip());
        assertEquals(new Square(2, 'A'), result.getLocation());
    }

    @Test
    public void testOverlapsBug() {
        Ship minesweeper = new Ship("MINESWEEPER");
        Ship destroyer = new Ship("DESTROYER");
        minesweeper.place('C', 5, false, false);
        destroyer.place('C', 5, false, false);
        assertTrue(minesweeper.overlaps(destroyer));
    }

    @Test
    public void testAttackSameSquareTwice() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true, false);
        var result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
        result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testEquals() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true, false);
        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true, false);
        assertTrue(minesweeper1.equals(minesweeper2));
        assertEquals(minesweeper1.hashCode(), minesweeper2.hashCode());
    }
}
