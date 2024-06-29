// first player to get 4 in a line wins (row column; diagonals are not counted as wins
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.toLowerCase;

public class Main
{
    private static ArrayList<Pair> validMoves = new ArrayList<>();
    private static ArrayList<Pair> finalMoves = new ArrayList<>();
    private static Board board;
    private static boolean hasWinner = false;
    private static int timeLimit;
    private static long startTime;


    public static void main(String[] args) {
        boolean playAgain = true;
        Scanner sc = new Scanner(System.in);
        while(playAgain) {
            boolean computerStarts = true;
            // ask the user if they want to go first
            System.out.print("Would you like to go first? (y/n): ");
            String userChoice = sc.nextLine();
            while (!(userChoice.equalsIgnoreCase("y") || userChoice.equalsIgnoreCase("n"))) {
                System.out.print("Only y or n is accepted. Please try again.\n");
                System.out.print("Would you like to go first? (y/n): ");
                userChoice = sc.nextLine();
            }
            if (userChoice.equals("y") || userChoice.equals("Y")) {
                computerStarts = false;
            }

            boolean isValidInt = false;
            sc = new Scanner(System.in);

            // get the time limit for the computers moves
            while (!isValidInt) {
                System.out.print("How long should the computer think about its moves (in seconds)? ");
                String input = sc.nextLine();
                try {
                    timeLimit = Integer.parseInt(input);
                    isValidInt = true;
                } catch (NumberFormatException e) {
                    System.out.print("Only integers are accepted. Please try again.\n");
                }

            }

            // create the game board
            board = new Board(computerStarts);
            board.displayBoard();

            boolean hasComputerWon = false;
            while (!board.currentState.hasWinner()) {
                if (computerStarts) {
                    // get the computer's move
                    Move computerMove = getComputerMove();
                    board.currentState.gameBoard[computerMove.row - 1][computerMove.column - 1] = 1;
                    board.recordMove(computerMove);

                    board.displayBoard();
                    if (board.currentState.hasWinner(true)) {
                        hasComputerWon = true;
                        break;
                    }
                } else if (!board.currentState.isEmpty()) {
                    Move computerMove = getComputerMove();
                    board.currentState.gameBoard[computerMove.row - 1][computerMove.column - 1] = 1;
                    board.recordMove(computerMove);

                    board.displayBoard();
                    if (board.currentState.hasWinner(true)) {
                        hasComputerWon = true;
                        break;
                    }
                }
                // get the player's move
                int[] playerMove = getPlayerMove(board.currentState.gameBoard);
                board.currentState.gameBoard[playerMove[0]][playerMove[1]] = 2;
                board.recordMove(new Move(2, playerMove[0] + 1, playerMove[1] + 1));
                board.displayBoard();
            }

            // prints the winner
            if (hasComputerWon) {
                System.out.println("Computer wins!");
            } else {
                System.out.println("Player wins!");
            }
            System.out.println("Game Over!");

            // ask if the user wants to play again
            System.out.print("\nPlay again? (y/n): ");
            String playAgainChoice = sc.nextLine();
            while (!(playAgainChoice.equalsIgnoreCase("y") || playAgainChoice.equalsIgnoreCase("n"))) {
                System.out.println("Only y or n is accepted. Please try again.");
                System.out.print("Play again? (y/n): ");
                playAgainChoice = sc.nextLine();
            }
            playAgain = playAgainChoice.equalsIgnoreCase("y");
            if(playAgain){
                board.reset();
            }
        }
        sc.close();
    }

    // prompts the player to choose their next move
    private static int[] getPlayerMove(int[][] board)
    {
        int row = -1;
        int column = -1;

        while (!isValid(row, column, board))
        {
            String pMove = "";
            while(pMove.length() != 2)
            {
                System.out.print("Choose your next move: ");
                pMove = new Scanner(System.in).nextLine();
            }
            System.out.println();
            row = (int) toLowerCase(pMove.charAt(0)) - 97;
            column = getNumericValue(pMove.charAt(1)) - 1;
        }
        // return array containing the chosen row and column indices
        return new int[]{row, column};
    }

    // checks whether a given row and column are valid coordinate on the board
    private static boolean isValid(int row, int column, int[][] board)
    {
        if ((row < 0 || row >= 8 || column < 0 || column >= 8) && (row == -1 && column == -1))
        {
            return false;
        }
        if (row < 0 || row >= 8 || column < 0 || column >= 8)
        {
            System.out.println("Not a legal move!");
            return false;
        }
        if (board[row][column] == 1 || board[row][column] == 2)
        {
            System.out.println("Space occupied. Please try again.");
            return false;
        }

        return true;
    }

    // finds the best option based for the computer's move
    private static Move findBestOption()
    {
        State bestOption = finalMoves.get(0).state;
        int bestPair = finalMoves.get(0).value;
        for (Pair pair : finalMoves)
        {
            if (pair.value > bestPair)
            {
                bestPair = pair.value;
                bestOption = pair.state;
            }

            else if (pair.value == bestPair && pair.state.compareComputerScore() > bestOption.compareComputerScore())
            {
                bestOption = pair.state;
            }
        }

        Move bestMove = board.currentState.getNextMove(bestOption);

        return bestMove;
    }

    // performs iterative deepening search to find the optimal move
    private static void search(State root)
    {
        startTime = System.nanoTime();
        for (int i = 1; i <= 500; i++)
        {
            validMoves.clear();

            int result = alphaBetaAlgorithm(root, root, i, Integer.MIN_VALUE, Integer.MAX_VALUE, true);


            if (result == -1)
            {
                System.out.println("Solution found");
                break;
            }


            finalMoves.clear();
            for (Pair pair : validMoves)
                finalMoves.add(new Pair(pair));
            if (hasWinner) {
                hasWinner = false;
                break;
            }
        }
        System.out.printf("Optimal move found in: %.3f Seconds\n", getTime());
        System.out.println();
    }

    // implements the alpha-beta pruning algorithm for the game
    private static int alphaBetaAlgorithm(State initialState, State currentState, int depth, int alpha, int beta, boolean computer)
    {
        if (currentState.hasWinner(!computer))
        {
            hasWinner = true;
            if (currentState.isChildState(initialState, !computer))
            {
                validMoves.add(new Pair(999999999, currentState));
            }

            return currentState.compareComputerScore();
        }

        if (depth == 0)
        {
            if (currentState.isChildState(initialState, !computer))
            {
                validMoves.add(new Pair(currentState.compareComputerScore(), currentState));
            }

            return currentState.compareComputerScore();
        }

        ArrayList<State> next = currentState.generateSuccessorStates(computer);

        int value;
        if (computer)
        {
            value = Integer.MIN_VALUE;
            for (State child : next)
            {
                value = Math.max(value, alphaBetaAlgorithm(initialState, child, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, value);

                if (timeReached())
                    return -1;

                if (alpha >= beta)
                    break;
            }
        }
        else
        {
            value = Integer.MAX_VALUE;
            for (State child : next)
            {
                value = Math.min(value, alphaBetaAlgorithm(initialState, child, depth - 1, alpha, beta, true));
                beta = Math.min(beta, value);

                if (timeReached())
                    return -1;

                if (alpha >= beta)
                    break;
            }
        }

        if (currentState.isChildState(initialState, !computer))
            validMoves.add(new Pair(value, currentState));

        return value;
    }
    // computes the computer's next move by searching the game board
    private static Move getComputerMove() {
        search(board.currentState);
        return findBestOption();
    }

    // calculates the elapsed time in seconds
    private static double getTime() {
        return (((System.nanoTime() - startTime))/1000000000.0);
    }

    // checks if the time limit has been reached
    private static boolean timeReached() {
        return getTime() > timeLimit;
    }
} // end Main