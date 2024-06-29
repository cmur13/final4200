// this class represents a move (position) on the game board
public class Position
{
    private int marker; // computer =1, player = 2
    int row;
    int column;

    // constructs a new Move object
    public Position(int marker, int row, int column)
    {
        this.marker = marker;
        this.row = row;
        this.column = column;
    }

    // translates the move coordinates to a user-friendly string representation
    public String translator()
    {
        // returns in notation (A1, B3...)
        return (char) (65 + row - 1) + Integer.toString(column);
    }
}
