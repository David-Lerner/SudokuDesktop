/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author David
 */
public interface Cell {

    boolean containsPossibility(int number);

    SubSudoku getColumn();

    int getId();

    boolean[] getPossibilities();

    int getPossibilityCount();

    SubSudoku getRow();

    SubSudoku getSubgrid();

    int getValue();

    boolean isGiven();

    boolean isHighlighted();

    void setHighlighted(boolean highlighted);

    boolean setPossibile(int number, boolean possible);

    void setValue(int value);

    void togglePossibile(int number);
    
    Cell createCell(int id, int length, int value, SubSudoku row, SubSudoku column, 
            SubSudoku subgrid, boolean given);
    
}
