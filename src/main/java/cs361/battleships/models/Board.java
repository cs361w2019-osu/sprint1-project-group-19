package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty private List<Square> emptySquares;
	@JsonProperty private List<Ship> placedShips;
	@JsonProperty private List<Square> missedSquares;
	@JsonProperty private List<Square> hitSquares;
	@JsonProperty private List<Result> attackResults;
  
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */

	public Board() { // board constructor
		emptySquares = new ArrayList<>(); // should contain all squares on the board
		hitSquares = new ArrayList<>(); // should be empty
		missedSquares = new ArrayList<>(); // should also be empty
		placedShips = new ArrayList<>();
		attackResults = new ArrayList<>();

		// adds all squares to the emptySquares list
		for (char y = 'A'; y <= 'J'; y++){ // column names are in uppercase
			for (int x = 1; x <= 10; x++){
				emptySquares.add(new Square(x, y));
			}
		}
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
		Result result = new Result();
		Square newSquare = new Square(x, y);
		result.setLocation(newSquare);
		if (x < 1 || x > 10 || y < 'A' || y > 'J') {
			result.setResult(AtackStatus.INVALID);
		} else {
			if (missedSquares.contains(newSquare) || hitSquares.contains(newSquare)) {
				result.setResult(AtackStatus.INVALID);
			} else {
				for (Ship existingShip:this.placedShips) {
					List<Square> existingSquares = new ArrayList<>(existingShip.getOccupiedSquares());
					if (existingSquares.contains(newSquare)) {
						result.setResult(AtackStatus.HIT);
						result.setShip(existingShip);
						existingSquares.remove(newSquare);
						existingShip.setOccupiedSquares(existingSquares);
						if (existingSquares.isEmpty()) {
							result.setResult(AtackStatus.SUNK);
							placedShips.remove(existingShip);
							if (placedShips.isEmpty()) {
								result.setResult(AtackStatus.SURRENDER);
							}
						}
						hitSquares.add(newSquare);
						List<Result> oldAtk = new ArrayList<>(this.getAttacks());
						oldAtk.add(result);
						this.setAttacks(oldAtk);
						return result;
					}
				}
				result.setResult(AtackStatus.MISS);
				missedSquares.add(newSquare);
			}
		}
		List<Result> oldAtk = new ArrayList<>(this.getAttacks());
		oldAtk.add(result);
		this.setAttacks(oldAtk);
		return result;
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
		return this.attackResults;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
		if (attacks.size() > 0) {
			attackResults.clear();
			attackResults.addAll(attacks);
		}
	}
}
