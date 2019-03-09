var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var underwater;
var attackType; // "" = regular attacks, "Sonar" = sonar pulse

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS") {className = "miss";}
        else if (attack.result === "HIT") {
            className = "hit";
            board.ships.forEach((ship) => {
                let sunk = true;
                let touching = false;
                ship.occupiedSquares.forEach((square) => {
                    if ((attack.location.row === square.row) && (attack.location.column === square.column)) {
                        touching = true;
                    }
                    if (square.hit === false) {
                        sunk = false;
                    }
                });
                if ((touching === true) && (sunk === true)) {
                    className = "sink";
                }
            });
        }
        else if (attack.result === "SUNK") {
            className = "sink";
        }
        else if (attack.result === "SURRENDER") {
            className = "sink";
            alert(surrenderText);
        }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
}

function markScan(board, elementId) {
    board.scannedSquares.forEach((square) => {
        document.getElementById(elementId).rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("scan");
    })
    board.ships.forEach((ship) => {
        ship.occupiedSquares.forEach((square) => {
            classNames = document.getElementById(elementId).rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList;
            if (classNames.contains("scan") && !classNames.contains("hit") && !classNames.contains("sink")) {
                document.getElementById(elementId).rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("hidden");
            }
        })
    })
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markScan(game.opponentsBoard, "opponent");
    markHits(game.playersBoard, "player", "You lost the game");

    var num_sonar_pulses = game.opponentsBoard.sonarPulses;
    if (num_sonar_pulses <= 0){ // 0/1/2 = number of sonar pulses left, -1 = sonar pulse not yet available
        document.getElementById("sonar_pulse_button").style.display = "none";
        document.getElementById("regular_wpn_button").style.display = "none";
        attackType = "";
    } else if (num_sonar_pulses > 0){
        if (attackType == "Sonar"){
            document.getElementById("sonar_pulse_button").style.display = "none";
            document.getElementById("regular_wpn_button").style.display = "initial"; // initial = default element display property
        } else {
            document.getElementById("sonar_pulse_button").style.display = "initial"; // initial = default element display property
            document.getElementById("regular_wpn_button").style.display = "none";
        }
        document.getElementById("sonar_pulse_button").innerHTML = "Use sonar pulse (" + num_sonar_pulses + " left)";
        attackType = "";
    }
}

var oldListener;
function registerCellListener(e, f) {
    let el = document.getElementById(e);
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical, isUnderwater: underwater}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 4) {
                isSetup = false;
                attackPhase();
                registerCellListener("player", (e) => {});
            }
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col, Atttype: attackType}, function(data) {
            game = data;
            redrawGrid();
        });
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Cannot complete the action");
            return;
        }
        response = JSON.parse(req.responseText);
        if (response.status != ""){
            alert(response.status);
            return;
        }
        handler(response);
        //handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        underwater = document.getElementById("is_underwater").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function scan() {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        let table = document.getElementById("opponent");
        let iStart = row-2;
        let iEnd = row+2;
        if (iStart < 0) {iStart = 0;};
        if (iEnd > 9) {iEnd = 9;};
        for (let i=iStart;i<=iEnd;i++){
            let jStart = col - (2-Math.abs(i-row));
            let jEnd = col + (2-Math.abs(i-row));
            if (jStart < 0) {jStart = 0;};
            if (jEnd > 9) {jEnd = 9;};
            for (let j=jStart;j<=jEnd;j++) {
                table.rows[i].cells[j].classList.toggle("placed");
            }
        }
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    attackType = ""; // defaults to regular attacks (empty string)
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener("player", place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener("player", place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener("player", place(4));
    });
    document.getElementById("place_submarine").addEventListener("click", function(e) {
                    shipType = "SUBMARINE";
                   registerCellListener(place(5));
                });
    document.getElementById("sonar_pulse_button").addEventListener("click", function(e) {
        attackType = "Sonar";
        document.getElementById("sonar_pulse_button").style.display = "none";
        document.getElementById("regular_wpn_button").style.display = "initial"; // initial = default element display property
        registerCellListener("opponent", scan());
    });
    document.getElementById("regular_wpn_button").addEventListener("click", function(e) {
        attackType = "";
        document.getElementById("sonar_pulse_button").style.display = "initial"; // initial = default element display property
        document.getElementById("regular_wpn_button").style.display = "none";
        registerCellListener("opponent", (e) => {});
    });
    document.getElementById("sonar_pulse_button").style.display = "none";
    document.getElementById("regular_wpn_button").style.display = "none";
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};

function attackPhase(){
    document.getElementById("setup_buttons").style.display = "none";
    document.getElementById("choose_weapon").style.display = "initial";
    attackType = "";
    document.getElementById("sonar_pulse_button").style.display = "initial"; // initial = default element display property
    document.getElementById("regular_wpn_button").style.display = "none";

    document.getElementById("status").innerHTML = "<h3>Attack phase</h3>Click on a square on the opponent's board to attack it.<br>The opponent will attack your board as you attack theirs."
}

