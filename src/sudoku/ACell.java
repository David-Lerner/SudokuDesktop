package sudoku;

import java.util.Arrays;

/**
 *
 * @author David
 */
public class ACell implements Cell {
    
    private int id;
    private int value;
    private boolean[] possibles;
    private int possibilityCount;
    private SubSudoku row;
    private SubSudoku column;
    private SubSudoku subgrid;
    private boolean highlighted;
    private boolean given;

    public ACell() {}; //as factory
    
    public ACell(int id, int length, SubSudoku row, SubSudoku column, 
            SubSudoku subgrid) {
        this(id, length, 0, row, column, subgrid, false);
    }

    public ACell(int id, int length, int value, SubSudoku row, SubSudoku column, 
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public SubSudoku getRow() {
        return row;
    }

    @Override
    public SubSudoku getColumn() {
        return column;
    }

    @Override
    public SubSudoku getSubgrid() {
        return subgrid;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
    
    @Override
    public boolean isGiven() {
        return given;
    }

    @Override
    public int getPossibilityCount() {
        return possibilityCount;
    }
    
    @Override
    public boolean containsPossibility(int number) {
        return possibles[number-1];
    }
    
    @Override
    public boolean[] getPossibilities() {
        return Arrays.copyOf(possibles, possibles.length);
    }
    
    @Override
    public boolean setPossibile(int number, boolean possible) {
        if (possible == possibles[number-1])
            return false;
        possibles[number-1] = possible;
        if (possibles[number-1])
            possibilityCount++;
        else
            possibilityCount--;
        return true;
    }
    
    @Override
    public void togglePossibile(int number) {
        if (possibles[number-1])
            possibilityCount--;
        else
            possibilityCount++;
        possibles[number-1] = !possibles[number-1];
    }
    
    @Override
    public Cell createCell(int id, int length, int value, SubSudoku row, SubSudoku column, SubSudoku subgrid, boolean given) {
        return new ACell(id, length, value, row, column, subgrid, given);
    }
}
