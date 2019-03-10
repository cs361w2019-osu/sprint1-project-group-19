package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit = false;
	@JsonProperty private boolean isCapQuarters = false;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public boolean getIsCapQuarters() { return isCapQuarters; }
	public void setIsCapQuarters(boolean isCapQuarters) { this.isCapQuarters = isCapQuarters; }


	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > 10 || row < 1 || column > 'J' || column < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}

	// checks if the target coordinates are on the board
	public boolean moveCheck(int diffX, int diffY){
		if (row + diffX < 1 || row + diffX > 10 || column + diffY < 'A' || column + diffY > 'J'){
			return false;
		} else {
			return true;
		}
	}

	public boolean move(int diffX, int diffY){ // diffY is int because char is unsigned
		if (moveCheck(diffX,  diffY)) {
			row += diffX;
			column += diffY;
			return true;
		} else {
			return false;
		}
	}
}
