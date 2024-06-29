import java.util.ArrayList;
import java.util.Arrays;

public class State {
    int[][] gameBoard; // represents the game board as a 2D array

    // default constructor
    public State() {
        gameBoard = new int[8][8];
    }

    // used for creating a copy of the game board
    private State(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public State(State copy) {
        this.gameBoard = Arrays.stream(copy.gameBoard).map(r -> r.clone()).toArray(int[][]::new);
    }

    // Calculates the difference in heuristic scores between the computer player and the opponent
    public int compareComputerScore() {
        return (evaluateHeuristic(true) - evaluateHeuristic(false));
    }

    //     // Returns the player code (1 for computer, 2 for opponent)
    public int getPlayerCode(boolean isComputer) {
        if (isComputer) {
            return 1;
        }
        else {
            return 2;
        }
    }

    // calculates the heuristic value for the given player (computer or user)
    private int evaluateHeuristic(boolean isComputer) {
        int heuristic = 0;

        int playerCode = getPlayerCode(isComputer); // 1 for computer, 2 for user

        // check if the player has already won
        if (hasWinner(isComputer)) {
            heuristic += 1000000000;
        }

        // iterate through the game board
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int space = gameBoard[row][column]; // get value of the current space

                // if empty
                if (space == 0) {
                    int emptySpace = 0;
                    int userSpace = 0;
                    int opponentSpace = 0;
                    boolean isOccupied = false;

                    // check adjacent spaces upwards
                    for (int i = 1; i <= 3; i++) {
                        if (row - i >= 0) {
                            int next = gameBoard[row - i][column];
                            if (next == 0) {
                                emptySpace++;
                            }

                            else if (next != playerCode) {
                                opponentSpace++;
                                isOccupied = true;
                            }
                            else {
                                userSpace++;
                            }
                        }
                    }
                    // Calculate the heuristic value based on the occupied, empty, and user spaces
                    heuristic += calculateH(isOccupied, emptySpace, userSpace, opponentSpace);

                    // Reset counters for the next direction
                    emptySpace = 0;
                    userSpace = 0;
                    opponentSpace = 0;
                    isOccupied = false;

                    // check adjacent spaces downwards
                    for (int i = 1; i <= 3; i++) {
                        if (row + i < 8) {
                            int next = gameBoard[row + i][column];
                            if (next == 0) {
                                emptySpace++;
                            }

                            else if (next != playerCode) {
                                opponentSpace++;
                                isOccupied = true;
                            }
                            else {
                                userSpace++;
                            }
                        }
                    }
                    heuristic += calculateH(isOccupied, emptySpace, userSpace, opponentSpace);

                    // Reset counters for the next direction
                    emptySpace = 0;
                    userSpace = 0;
                    opponentSpace = 0;
                    isOccupied = false;

                    // Check adjacent spaces to the left
                    for (int i = 1; i <= 3; i++) {
                        if (column - i >= 0) {
                            int next = gameBoard[row][column - i];
                            if (next == 0) {
                                emptySpace++;
                            }
                            else if (next != playerCode) {
                                opponentSpace++;
                                isOccupied = true;
                            }
                            else
                            {
                                userSpace++;
                            }
                        }
                    }
                    heuristic += calculateH(isOccupied, emptySpace, userSpace, opponentSpace);
                    // reset counters for the next direction
                    emptySpace = 0;
                    userSpace = 0;
                    opponentSpace = 0;
                    isOccupied = false;

                    // check adjacent spaces to the right
                    for (int i = 1; i <= 3; i++) {
                        if (column + i < 8) {
                            int next = gameBoard[row][column + i];
                            if (next == 0) {
                                emptySpace++;
                            }
                            else if (next != playerCode) {
                                opponentSpace++;
                                isOccupied = true;
                            }
                            else{
                                userSpace++;
                            }
                        }
                    }
                    heuristic += calculateH(isOccupied, emptySpace, userSpace, opponentSpace);
                }
            }
        }

        return heuristic; // return the final heuristic value
    }
    private static int calculateH(boolean isOccupied, int emptySpace, int userSpace, int opponentSpace) {
        if (userSpace == 3) {
            return 1000000;
        }
        else if (userSpace == 2) {
            if (opponentSpace == 0) {
                return 1000;
            }
            else {
                return 100;
            }
        }

        else if (userSpace == 1) {
            if (opponentSpace == 0) {
                return 250;
            }
            else {
                return 25;
            }
        }

        if (opponentSpace == 3) {
            return 10000;
        }

        else if (opponentSpace == 2){
            return 1500;
        }

        else if (opponentSpace == 1) {
            return 100;
        }
        return 25; // default value if none of the conditions match
    }

    // returns the next move based on the provided next state
    public Move getNextMove(State next) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int currentSpace = gameBoard[row][column];

                if (currentSpace == 0 && next.gameBoard[row][column] == 1)
                    return new Move(1, row + 1, column + 1);
            }
        }
        return null; // no valid move found
    }

    // checks if the game board is empty
    public boolean isEmpty() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (gameBoard[row][column] != 0)
                    return false;
            }
        }

        return true;
    }

    // generates successor states for the specified player
    public ArrayList<State> generateSuccessorStates(boolean isComputer) {
        ArrayList<State> result = new ArrayList<>();

        int playerCode = getPlayerCode(isComputer);

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int currentSpace = gameBoard[row][column];

                if (currentSpace == 0){
                    int[][] newState = Arrays.stream(gameBoard).map(r -> r.clone()).toArray(int[][]::new);

                    newState[row][column] = playerCode;
                    result.add(new State(newState));
                }
            }
        }
        return result;
    }

    // checks if the given state is a child state fo the current state
    public boolean isChildState(State state, boolean isComputer) {
        int diff = 0;

        int playerCode = getPlayerCode(isComputer);

        // iterate through the game board
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (state.gameBoard[row][column] != gameBoard[row][column]) {
                    // Check if the differing space changes from empty to the player's piece
                    if (state.gameBoard[row][column] == 0 && gameBoard[row][column] == playerCode) {
                        diff += 1;
                    }
                    else {
                        return false; // Invalid child state (more than one differing space or incorrect change)
                    }
                }
            }
        }

        if (diff == 1) {
            return true;
        }
        else {
            return false;
        }
    }


    // checks if there is a winner on the board for the specified player
    public boolean hasWinner(boolean isComputer) {
        boolean isWinner = false;
        int playerCode = getPlayerCode(isComputer);

        // iterate through the game board
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int currentSpace = gameBoard[row][column];

                if (currentSpace == playerCode) {
                    // check for vertical win
                    if (row <= 8 - 4 && gameBoard[row + 1][column] == playerCode && gameBoard[row + 2][column] == playerCode && gameBoard[row + 3][column] == playerCode) {
                        isWinner = true;
                    }
                    // check for horizontal win
                    if (column <= 8 - 4 && gameBoard[row][column + 1] == playerCode && gameBoard[row][column + 2] == playerCode && gameBoard[row][column + 3] == playerCode) {
                        isWinner = true;
                    }
                }
            }
        }
        return isWinner; // return true is a win condition is met, otherwise false
    }

    public boolean hasWinner() {
        return hasWinner(true) || hasWinner(false);
    }
} // end state