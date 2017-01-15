package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author David
 */
public class Solver {
    
    public static Sudoku solve(Sudoku s) {
        int length = s.getLength();
        int[][] cells = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                cells[i][j] = s.getCell(i, j).getValue();
            }
        }
        if (solve(0, 0, cells))
            return new Sudoku(cells);
        else
            return null;
    }
    
    static boolean solve(int i, int j, int[][] cells) {
        if (i == 9) {
            i = 0;
            if (++j == 9)
                return true;
        }
        if (cells[i][j] != 0)  // skip filled cells
            return solve(i+1,j,cells);

        for (int val = 1; val <= 9; ++val) {
            if (legal(i,j,val,cells)) {
                cells[i][j] = val;
                if (solve(i+1,j,cells))
                    return true;
            }
        }
        cells[i][j] = 0; // reset on backtrack
        return false;
    }

    static boolean legal(int i, int j, int val, int[][] cells) {
        for (int k = 0; k < 9; ++k)  // row
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k) // col
            if (val == cells[i][k])
                return false;

        int boxRowOffset = (i / 3)*3;
        int boxColOffset = (j / 3)*3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset+k][boxColOffset+m])
                    return false;

        return true; // no violations, so it's legal
    }

    private Sudoku sudoku;
    
    public Solver(Sudoku s) {
        sudoku = s;
    }
    
    public boolean solve() {
        //System.out.println("Before:");
        //writeMatrix();
        int changed = 1;
        while (changed > 0) {
            update();
            changed = findHiddenSingles();
        }
        
        //System.out.println("After:");
        //writeMatrix();
        
        return true;
    }
    
    private void update() {
        int changed = 1;
        while (changed > 0) {
            changed = findPossibleValues();
        }
    }
    
    private int findPossibleValues() {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            for (int j = 0; j < sudoku.getLength(); ++j) {
                Cell c = sudoku.getCell(i, j);
                if (c.getValue() != 0)
                    continue;
                setPossibleValues(c);
                if (c.getPossibilityCount() == 1) {
                    //found a number
                    ++changes;
                    int n = 1;
                    while (!c.containsPossibility(n)) {
                        ++n;
                    }
                    c.setValue(n);
                    c.setPossibile(n, false);
                }
            }
        }
        return changes;
    }
    
    private void setPossibleValues(Cell c) {
        for (int i = 1; i <= sudoku.getLength(); ++i) {
            c.setPossibile(i, true);
        }
        
        //version 1
        /*search: for (int i = 1; i <= sudoku.getLength(); ++i) { 
            for (int j = 0; j < sudoku.getLength(); ++j) {
                if (c.getSubgrid().getCell(j).getValue() == i) {
                    c.setPossibile(i, false);
                    continue search;
                }
                if (c.getColumn().getCell(j).getValue() == i) {
                    c.setPossibile(i, false);
                    continue search;
                }
                if (c.getRow().getCell(j).getValue() == i) {
                    c.setPossibile(i, false);
                    continue search;
                }
            }
        }*/
        
        //version 2
        for (int i = 0; i < sudoku.getLength(); ++i) {
            if (c.getSubgrid().getCell(i).getValue() != 0) {
                c.setPossibile(c.getSubgrid().getCell(i).getValue(), false);
            }
            if (c.getColumn().getCell(i).getValue() != 0) {
                c.setPossibile(c.getColumn().getCell(i).getValue(), false);
            }
            if (c.getRow().getCell(i).getValue() != 0) {
                c.setPossibile(c.getRow().getCell(i).getValue(), false);
            }
        }
    }
    
    private int findHiddenSingles() {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            changes += setHiddenSingles(sudoku.getSubgrid(i));
            //update();
            changes += setHiddenSingles(sudoku.getRow(i));
            //update();
            changes += setHiddenSingles(sudoku.getColumn(i));
            //update();
        }
        return changes;
    }
    
    private int setHiddenSingles(SubSudoku s) {
        int changes = 0;
        for (int n = 1; n <= sudoku.getLength(); ++n) {
            int index = 0, count = 0;
            for (int i = 0; i < sudoku.getLength(); ++i) {
                if (s.getCell(i).containsPossibility(n)) {
                    ++count;
                    if (count > 1) 
                        break;
                    index = i;
                    update();
                }
            }
            if (count == 1) {
                //found a number
                ++changes;
                s.getCell(index).setValue(n);
                for (int i = 1; i <= sudoku.getLength(); ++i)
                    s.getCell(index).setPossibile(i, false);
            }
        }
        return changes;
    }
    
    private void writeMatrix() {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(sudoku.getCell(i, j).getValue() == 0 ? " " : 
                        Integer.toString(sudoku.getCell(i, j).getValue()));

                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }
    
    //for testing which update algorithm is faster
    public boolean testUpdate(int version) {        
        int changed = 1;
        while (changed == 1) {
            changed = checkColumnsAndRows2(version);
        }
        return true;
    }
    
    private int checkColumnsAndRows2(int version) {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            for (int j = 0; j < sudoku.getLength(); ++j) {
                Cell c = sudoku.getCell(i, j);
                if (c.getValue() != 0)
                    continue;
                setPossibleValues2(c, version);
                if (c.getPossibilityCount() == 1) {
                    //found a number
                    changes = 1;
                    int n = 1;
                    while (!c.containsPossibility(n)) {
                        ++n;
                    }
                    c.setValue(n);
                    c.setPossibile(n, false);
                }
            }
        }
        return changes;
    }
    
    private void setPossibleValues2(Cell c, int version) {
        for (int i = 1; i <= sudoku.getLength(); ++i) {
            c.setPossibile(i, true);
        }
        
        if (version == 1) {
            search: for (int i = 1; i <= sudoku.getLength(); ++i) { 
                for (int j = 0; j < sudoku.getLength(); ++j) {
                    if (c.getSubgrid().getCell(j).getValue() == i) {
                        c.setPossibile(i, false);
                        continue search;
                    }
                    if (c.getColumn().getCell(j).getValue() == i) {
                        c.setPossibile(i, false);
                        continue search;
                    }
                    if (c.getRow().getCell(j).getValue() == i) {
                        c.setPossibile(i, false);
                        continue search;
                    }
                }
            }
        } else if (version == 2) {
            for (int i = 0; i < sudoku.getLength(); ++i) {
                if (c.getSubgrid().getCell(i).getValue() != 0) {
                    c.setPossibile(c.getSubgrid().getCell(i).getValue(), false);
                }
                if (c.getColumn().getCell(i).getValue() != 0) {
                    c.setPossibile(c.getColumn().getCell(i).getValue(), false);
                }
                if (c.getRow().getCell(i).getValue() != 0) {
                    c.setPossibile(c.getRow().getCell(i).getValue(), false);
                }
            }
        }
    }
    //end test
}
