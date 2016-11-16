package sudoku;

/**
 *
 * @author David
 */
public class Cell {
    
    private int id;
    private int value;
    private boolean[] possibles;
    private int possibilityCount;
    private SubSudoku row;
    private SubSudoku column;
    private SubSudoku subgrid;
    private boolean highlighted;
    private boolean given;

    public Cell(int id, int length, SubSudoku row, SubSudoku column, 
            SubSudoku subgrid) {
        this(id, length, 0, row, column, subgrid, false);
    }

    public Cell(int id, int length, int value, SubSudoku row, SubSudoku column, 
            SubSudoku subgrid, boolean given) {
        this.id = id;
        this.value = value;
        possibles = new boolean[length];
        possibilityCount = 0;
        this.row = row;
        this.column = column;
        this.subgrid = subgrid;
        highlighted = false;
        this.given = given;
    }

    public int getId() {
        return id;
    }

    public SubSudoku getRow() {
        return row;
    }

    public SubSudoku getColumn() {
        return column;
    }

    public SubSudoku getSubgrid() {
        return subgrid;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
    
    public boolean isGiven() {
        return given;
    }

    public int getPossibilityCount() {
        return possibilityCount;
    }
    
    public boolean containsPossibility(int number) {
        return possibles[number-1];
    }
    
    public boolean setPossibile(int number, boolean possible) {
        if (possible == possibles[number-1])
            return false;
        possibles[number-1] = possible;
        
        return true;
    }
    
    public void togglePossibile(int number) {
        possibles[number-1] = !possibles[number-1];
    }
}
