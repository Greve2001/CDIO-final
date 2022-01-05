package Board;

public class Square {
    private final String NAME;
    private final int POSITION;

    public Square (String name, int position)
    {
        this.NAME = name;
        this.POSITION = position;
    }

    public String getNAME() {
        return NAME;
    }

    public int getPOSITION() {
        return POSITION;
    }
}
