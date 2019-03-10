package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private List<Square> scans;
	@JsonProperty private List<Square> scannedSquares;
	@JsonProperty private int sonarPulses; // -1 means not yet available, 0-2 means the number of sonar pulses left
	@JsonProperty private int moves; // -1 means not yet available, 0-2 means the number of moves left

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
		scans = new ArrayList();
		scannedSquares = new ArrayList();
		sonarPulses = -1;
		moves = -1;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		if (ships.size() >= 4) {

			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		placedShip.place(y, x, isVertical);

		if ((!ship.getKind().equals("SUBMARINE"))&&ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		ships.add(placedShip);
		return true;
	}

	public boolean useSonarPulse(int x, char y) {
		if (sonarPulses > 0) {
			Square s = new Square(x, y);
			if (x > 10 || x < 1 || y > 'J' || y < 'A') {
				return false;
			} else if (scans.contains(s)) {
				return false;
			} else {
				scans.add(s);
				addRadius(s);
				sonarPulses -= 1;
				return true;
			}
		}
		else {
			return false;
		}
	}

	private void addRadius(Square s) {
		int xMid = s.getRow();
		int xStart = xMid-2;
		int xEnd = xMid+2;
		if (xStart < 1) {
			xStart = 1;
		}
		if (xEnd > 10) {
			xEnd = 10;
		}
		int yMid = s.getColumn();
		for (int i=xStart;i<=xEnd;i++) {
			int yStart = yMid - (2-abs(i-xMid));
			int yEnd = yMid + (2-abs(i-xMid));
			if (yStart < 'A') {
				yStart = 'A';
			}
			if (yEnd > 'J') {
				yEnd = 'J';
			}

			for (int j=yStart;j<=yEnd;j++) {
				Square newS = new Square(i,(char)j);
				if(!scannedSquares.contains(newS)) {
					scannedSquares.add(newS);
				}
			}
		}
	}

	public List<Square> getScannedSquares() {
		return scannedSquares;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attackResult = attack(new Square(x, y));
		attacks.add(attackResult);
		return attackResult;
	}

	/*
	number of ships sunken ON THE BOARD, NOT BY THE PLAYER
	which means that # of ships you sunk = # of sunken ships on the enemy board
	 */
	public int numSunken(){
		int num = 0;
		for (int x = 0; x < ships.size(); x++){
			if (ships.get(x).isSunk()){
				num++;
			}
		}
		return num;
	}

	public int getSonarPulses(){
		return sonarPulses;
	}

	public void setSonarPulses(int num){
	    sonarPulses = num;
    }

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s))) {
			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.INVALID);
			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());
		if (attackResult.getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		return attackResult;
	}

	public List<Ship> getShips() {
		return ships;

	}

	private char opposite(char dir){
		char oppositeDir = ' ';
		switch (dir){
			case 'N':
				oppositeDir = 'S';
				break;
			case 'S':
				oppositeDir = 'N';
				break;
			case 'E':
				oppositeDir = 'W';
				break;
			case 'W':
				oppositeDir = 'E';
				break;
		}
		return oppositeDir;
	}

	// returns an empty string if move is successful, returns a status message otherwise
	public String moveShip(Ship s, char dir){
		// check if the player has moves left
		if (moves == 0){
			return "You have moved your ships twice";
		} else if (moves < 0){
			return "You have to sink at least 2 enemy ships before moving your ships";
		}

		// check if the ship is sunken
		if (s.isSunk()){
			return "The ship you are trying to move is sunken";
		}

		// create an array of all ships except the one we are trying to move, for overlap checks
		ArrayList<Ship> allButTarget = new ArrayList<>(); // contains all ships on the board except the one we are trying to move
		for (Ship cur : ships){
			if (!(cur.equals(s))){
				allButTarget.add(cur);
			}
		}

		// move the ship
		if (s.move(dir)){ // if move successful (does not result in the ship being out of the board)
			// check overlaps
			for (Ship check : allButTarget){
				if (s.overlaps(check)){ // if ship overlaps
					// move the opposite direction (undo move)
					char oppositeDir = opposite(dir);
					s.move(oppositeDir);
					return "The destination space is already occupied by another ship";
				}
			}
		} else { // if move results in the ship being out of the board
			return "You cannot move the ship out of the board";
		}
		// if everything went right
		moves--;
		return "";
	}

	public int getMoves(){
		return moves;
	}

	public void setMoves(int m){
		moves = m;
	}

	// find a ship on the board of a given type
	public Ship findShip(String kind){
		String shipType = kind.toUpperCase();

		for (int x = 0; x < ships.size(); x++){
			if (ships.get(x).getKind().equals(shipType)){
				return ships.get(x);
			}
		}
		return null;
	}
}
