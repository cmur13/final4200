// represents a pair of value and state
public class Pair
{
    int value;
    State state;

    // constructs a new Pair with the specified value and state
    public Pair(int value, State state)
    {
        this.value = value;
        this.state = state;
    }

    // constructs a new Pair by copying the values from another Pair
    public Pair(Pair copiedPair)
    {
        this.value = copiedPair.value;
        this.state = new State(copiedPair.state);
    }
}