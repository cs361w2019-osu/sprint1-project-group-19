package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	@JsonProperty private AtackStatus res = AtackStatus.INVALID;
	@JsonProperty private Ship ship;
	@JsonProperty private Square square;

	public AtackStatus getResult() {
		//TODO implement
		return this.res;
	}

	public void setResult(AtackStatus result) {
		//TODO implement
		this.res = result;
	}

	public Ship getShip() {
		//TODO implement
		return this.ship;
	}

	public void setShip(Ship ship) {
		//TODO implement
		this.ship = ship;
	}

	public Square getLocation() {
		//TODO implement
		return this.square;
	}

	public void setLocation(Square square) {
		//TODO implement
		this.square = square;
	}
}
