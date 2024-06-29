import java.util.ArrayList;

public class Board
{
    private ArrayList<Move> moveHistory = new ArrayList<>(); // stores the history of moves
    public State currentState = new State();
    private boolean isComputerFirst;

    // constructs a new board
    public Board(boolean isComputerFirst)
    {
        this.isComputerFirst = isComputerFirst;
    }

    // generates a formatted string representation of the game board
    private String generateBoardString()
    {
        StringBuilder result = new StringBuilder();
        result.append("  ");

        for (int i = 1; i <= 8; i++)
        {
            result.append(i + " ");
        }

        result.append("\n");

        int letter = 65;
        for (int i = 0; i < 8; i++)
        {
            result.append((char) letter + " ");

            for (int j = 0; j < 8; j++)
            {
                char marker = getMove(currentState.gameBoard[i][j]);
                result.append(marker + " ");
            }
            result.append("\n");
            letter++;
        }

        result.append("\n");

        return result.toString();
    }

    @Override
    public String toString()
    {
        return combineStrings(generateBoardString(), generateMoveList(isComputerFirst));
    }


    // generates a formattted string representing the move list
    private String generateMoveList(boolean isComputerFirst)
    {
        StringBuilder result = new StringBuilder();

        if (!isComputerFirst)
        {
            result.append("Player vs. Opponent");
        }
        else
        {
            result.append("Opponent vs. Player");
        }

        for (int i = 0; i < moveHistory.size(); i++)
        {
            String listOfMoves = moveHistory.get(i).translator();
            if (i % 2 == 0)
            {
                result.append("\n  " + (i / 2 + 1) + ". " + listOfMoves);
            }

            else
            {
                result.append(" " + listOfMoves);
            }
        }

        return result.toString();
    }

    // combines the board string and move list into a single formatted string
    private String combineStrings(String string1, String string2)
    {
        String splitter = "\n";
        String inserter = "  ";

        String[] string1Split = string1.split(splitter);
        String[] string2Split = string2.split(splitter);
        int maxLength = 0;
        for (String s : string1Split)
        {
            if (maxLength < s.length())
            {
                maxLength = s.length();
            }
        }
        StringBuilder b = new StringBuilder();
        int largerLength = Math.max(string1Split.length, string2Split.length);
        for (int i = 0; i < largerLength; i++)
        {
            if (i < string1Split.length)
            {
                b.append(string1Split[i]);
            }
            if (i < string2Split.length)
            {
                if (i >= string1Split.length)
                {
                    b.append("                  ");
                }
                b.append(inserter).append(string2Split[i]);
            }

            b.append("\n");
        }

        return b.toString();
    }
    // displays the current game board on the terminal
    public void displayBoard()
    {
        System.out.println(toString());
    }

    // if any moves are added, it records a move in the move history
    public void recordMove(Move move)
    {
        moveHistory.add(move);
    }

    // maps player codes to their corresponding piece symbols
    private static char getMove(int player)
    {
        char result;
        if (player == 0)
        {
            result = '-';
            return result;
        }

        // 1= computer
        if (player == 1)
        {
            result = 'X';
            return result;
        }

        // 2= player
        if (player == 2)
        {
            result = 'O';
            return result;
        }
        else
        {
            result = ' ';
            return result;
        }

    }
    public void reset(){
        moveHistory.clear();
        currentState = new State();
    }

} // end board