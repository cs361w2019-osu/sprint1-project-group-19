package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int size;
	@JsonProperty private int numHits;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {
		this();
		this.kind = kind;
		switch(kind) {
			case "MINESWEEPER":
				size = 2;
				numHits = 1;
				break;
			case "DESTROYER":
				size = 3;
				numHits = 2;
				break;
			case "BATTLESHIP":
				size = 4;
				numHits = 2;
				break;
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public int getNumHits() { return numHits; }
	public void setNumHits(int numHits) { this.numHits = numHits; }

	public void place(char col, int row, boolean isVertical) {
		for (int i=0; i<size; i++) {
			if (isVertical) {
				var newSquare = new Square(row+i, col);
				if(i == size-2) {
					newSquare.setIsCapQuarters(true);
				}
				occupiedSquares.add(newSquare);
			} else {
				var newSquare = new Square(row, (char) (col + i));
				if(i == size-2) {
					newSquare.setIsCapQuarters(true);
				}
				occupiedSquares.add(newSquare);
			}
		}
	}

	public boolean overlaps(Ship other) {
		Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
		Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
		Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
		return intersection.size() != 0;
	}

	public boolean isAtLocation(Square location) {
		return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
	}

	public String getKind() {
		return kind;
	}

	public Result attack(int x, char y) {
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			return new Result(attackedLocation);
		}
		var attackedSquare = square.get();
		if (attackedSquare.isHit()) {
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.INVALID);
			return result;
		}
		attackedSquare.hit();
		var result = new Result(attackedLocation);
		result.setShip(this);
		if(getNumHits() > 0 && result.getLocation().getIsCapQuarters()) {
			result.setResult(AtackStatus.MISS);
			setNumHits(getNumHits()-1);
		}
		if (isSunk()) {
			result.setResult(AtackStatus.SUNK);
		} else {
			result.setResult(AtackStatus.HIT);
		}
		return result;
	}

	@JsonIgnore
	public boolean isSunk() {
		return ((getNumHits() == 0) || getOccupiedSquares().stream().allMatch(s -> s.isHit()));
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Ship)) {
			return false;
		}
		var otherShip = (Ship) other;

		return this.kind.equals(otherShip.kind)
				&& this.size == otherShip.size
				&& this.occupiedSquares.equals(otherShip.occupiedSquares);
	}

	private Pair getDiff(char dir){
		Pair p = new Pair();
		p.a = 0;
		p.b = 0;
		switch (dir){
			case 'N':
				p.a = -1;
				break;
			case 'E':
				p.b = -1;
				break;
			case 'S':
				p.a = 1;
				break;
			case 'W':
				p.b = 1;
				break;
		}
		return p;
	}

	public boolean move(char dir){
		// step 1: get direction
		Pair diff = getDiff(dir);
		int rowDiff = diff.b;
		int colDiff = diff.a;

		// step 2: check
		for (Square sq : occupiedSquares){
			if (!(sq.moveCheck(rowDiff, colDiff))){
				return false;
			}
		}

		// step 3: move
		for (Square sq : occupiedSquares){
			sq.move(rowDiff, colDiff);
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
	}

	@Override
	public String toString() {
		return kind + occupiedSquares.toString();
	}
}
