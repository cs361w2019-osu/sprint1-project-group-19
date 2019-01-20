package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		int len;
		switch (ship.getShipName()) {
			case "MINESWEEPER":
				len = 2;
				break;
			case "DESTROYER":
				len = 3;
				break;
			case "BATTLESHIP":
				len = 4;
				break;
			default:
				return false;
		}
		for (int i = 0; i < len; i++) {
			if (isVertical) {
				if (x + i > 10 || x + i <= 0 || y > 'J' || y <= 0) {
					return false;
				}
			}
			else {
				if (x > 10 || x <= 0 || y + i > 'J' || y + i <= 0) {
					return false;
				}
			}
		}
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
		return null;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
