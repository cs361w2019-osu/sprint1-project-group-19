package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();
    @JsonProperty private String status = "";
    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful) {
            status = "The entire ship must be on the board and ships cannot overlap with each other.";
            return false;
        }

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        status = "";
        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            status = "You have already attacked that square.";
            return false;
        }

        /*
        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);
        */

        opponentAttack();

        status = "";
        initSonarPulse();
        initMoves();
        return true;
    }

    private void opponentAttack(){
        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);
    }

    private char randCol() {
        int random = new Random().nextInt(10);
        return (char) ('A' + random);
    }

    private int randRow() {
        return  new Random().nextInt(10) + 1;
    }

    private boolean randVertical() {
        return new Random().nextBoolean();
    }

    private void initSonarPulse(){
        if ((opponentsBoard.numSunken() >= 1) && (opponentsBoard.getSonarPulses() == -1)){
            opponentsBoard.setSonarPulses(2);
        }
    }

    private void initMoves(){
        if ((opponentsBoard.numSunken() >= 2) && (playersBoard.getMoves() == -1)){
            playersBoard.setMoves(2);
        }
    }

    public boolean scan(int x, char y){
        if (x >= 1 && x <= 10 && y >= 'A' && y <= 'J') { // check if coords are on the board
            boolean ret = opponentsBoard.useSonarPulse(x, y);
            if (ret){ // if sonar pulse use is valid
                opponentAttack();
            }
            return ret;
        } else {
            status = "you can't scan there";
            return false;
        }
    }

    public boolean moveShip(String shipType, char dir){
        Ship s = playersBoard.findShip(shipType);
        if (s == null){
            status = "Cannot find the selected ship";
            return false;
        }
        String ret = playersBoard.moveShip(s, dir);
        if (ret.isEmpty()){
            opponentAttack();
            return true;
        } else {
            status = ret;
            return false;
        }
    }

}