package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String shipName;
	@JsonProperty private int len;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {
		occupiedSquares = new ArrayList<>();
		shipName = kind;
		this.setLength(kind);
		//TODO implement
	}

	public int getLength() {
		return this.len;
	}

	private void setLength(String kind) {
		switch (kind) {
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
				len = 0;
		}
	}

	public void setOccupiedSquares(List<Square> shipSquares) {
		if (shipSquares.size() > 0) {
			occupiedSquares.clear();
			occupiedSquares.addAll(shipSquares);
		}
	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return occupiedSquares;
	}
}
