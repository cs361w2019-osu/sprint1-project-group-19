package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String shipName;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {
		occupiedSquares = new ArrayList<>();
		shipName = kind;
		//TODO implement
	}

	public List<Square> getOccupiedSquares() {
		//TODO implement
		return null;
	}

	public String getShipName() {
		return shipName;
	}
}
