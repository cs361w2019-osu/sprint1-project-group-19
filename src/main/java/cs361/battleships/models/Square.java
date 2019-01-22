package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int row;
	private char column;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public boolean equals (Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Square)) {
			return false;
		}
		Square otherSquare = (Square)o;
		return (otherSquare.getRow()==this.getRow() && otherSquare.getColumn()==this.getColumn());
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
