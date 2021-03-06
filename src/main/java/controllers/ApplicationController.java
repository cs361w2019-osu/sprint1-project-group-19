package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.Game;
import cs361.battleships.models.Ship;
import ninja.Context;
import ninja.Result;
import ninja.Results;

@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result newGame() {
        Game g = new Game();
        return Results.json().render(g);
    }

    public Result placeShip(Context context, PlacementGameAction g) {
        Game game = g.getGame();
        Ship ship = new Ship(g.getShipType());
        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical(), g.isUnderwater());
        /*
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }*/
        return Results.json().render(game);
    }

    public Result attack(Context context, AttackGameAction g) {
        Game game = g.getGame();
        boolean result;
        if (g.getAttackType().equals("Sonar")){
            result = game.scan(g.getActionRow(), g.getActionColumn());
        } else {
            result = game.attack(g.getActionRow(), g.getActionColumn());
        }

        /*
        if (result) {
            return Results.json().render(game);
        } else
            return Results.badRequest();
        }
        */
        return Results.json().render(game);
    }

    public Result move(Context context, MoveGameAction g){
        Game game = g.getGame();
        String shipType = g.getShipType();
        char dir = g.getDir();
        game.moveShip(shipType, dir);

        return Results.json().render(game);
    }
}
