package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Square> emptySquares;
	private List<Square> hitSquares;
	private List<Square> missedSquares;
	private List<Ship> placedShips;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
		placedShips = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		int len = ship.getLength();
		if (len == 0) {
			return false;
		}

		for (int i = 0; i < len; i++) {
			int a = x;
			char b = y;

			if (isVertical) {
				a += i;
			} else {
				b += i;
			}

			Square newSquare = new Square(a,b);

			if ( a > 10 || a < 1 || b > 'J' || b < 'A') {
				return false;
			} else {
				for (Ship existingShip:this.placedShips) {
					List<Square> shipSquares = new ArrayList<>(existingShip.getOccupiedSquares());
					if (shipSquares.contains(newSquare)) {
						return false;
					}
				}
				List<Square> existingSquares = new ArrayList<>(ship.getOccupiedSquares());
				existingSquares.add(newSquare);
				ship.setOccupiedSquares(existingSquares);
			}
		}

		List<Ship> newShips = new ArrayList<>(this.getShips());
		newShips.add(ship);
		this.setShips(newShips);

		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		//TODO implement
		return this.placedShips;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
		if (ships.size() > 0) {
			placedShips.clear();
			placedShips.addAll(ships);
		}
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
