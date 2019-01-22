package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	@JsonProperty private AtackStatus res = AtackStatus.INVALID;

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
		return null;
	}

	public void setShip(Ship ship) {
		//TODO implement
	}

	public Square getLocation() {
		//TODO implement
		return null;
	}

	public void setLocation(Square square) {
		//TODO implement
	}
}
