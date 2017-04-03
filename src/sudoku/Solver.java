package sudoku;

import java.util.ArrayList;
import java.util.BitSet;
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
    
    public static boolean solve(int i, int j, int[][] cells) {
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

    public static boolean legal(int i, int j, int val, int[][] cells) {
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
        initializeCells();
        int changed = 1;
        while (changed > 0) {
            update();
            changed = findHiddenSingles();
            if (changed > 0)
                continue;
            changed = findNakedPairs();
            if (changed > 0)
                continue;
            changed = findNakedTriples();
        }
        if (!checkValidity(Solver.solve(sudoku)))
            System.out.println("ERROR!");
        //System.out.println("After:");
        //writeMatrix();
        
        return checkValidity(sudoku);
    }
    
    private void initializeCells() {
        int length = sudoku.getLength();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (!sudoku.getCell(i, j).isGiven()) {
                    sudoku.getCell(i, j).setValue(0);
                    for (int n = 1; n <= length; ++n)
                        sudoku.getCell(i, j).setPossibile(n, true);
                }
            }
        }
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
        
        //version 1
        /*search: for (int i = 1; i <= sudoku.getLength(); ++i) { 
            for (int j = 0; j < sudoku.getLength(); ++j) {
                if (c.getBox().getCell(j).getValue() == i) {
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
            if (c.getBox().getCell(i).getValue() != 0) {
                c.setPossibile(c.getBox().getCell(i).getValue(), false);
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
            changes += setHiddenSingles(sudoku.getBox(i));
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
                    //update();
                }
            }
            if (count == 1) {
                //found a number
                //System.out.println("hidden single");
                ++changes;
                /*s.getCell(index).setValue(n);*/
                for (int i = 1; i <= sudoku.getLength(); ++i)
                    s.getCell(index).setPossibile(i, false);
                s.getCell(index).setPossibile(n, true);
            }
        }
        return changes;
    }
    
    private int findNakedPairs() {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            for (int j = 0; j < sudoku.getLength(); ++j) {
                Cell c = sudoku.getCell(i, j);
                if (c.getPossibilityCount() == 2) {
                    //cell contains a pair
                    int n = 1;
                    while (!c.containsPossibility(n)) {
                        ++n;
                    }
                    int a = n++;
                    while (!c.containsPossibility(n)) {
                        ++n;
                    }
                    int b = n;
                    changes += setNakedPairs(c, c.getBox(), a, b);
                    changes += setNakedPairs(c, c.getColumn(), a, b);
                    changes += setNakedPairs(c, c.getRow(), a, b);
                }
            }
        }
        return changes;
    }
    
    private int setNakedPairs(Cell c, SubSudoku s, int a, int b) {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            Cell other = s.getCell(i);
            if (other.getPossibilityCount() == 2 && other.getId() != c.getId() && other.containsPossibility(a) && other.containsPossibility(b)) {
                //if a pair is found, remove the pair from the other cells
                for (int j = 0; j < sudoku.getLength(); j++) {
                    Cell temp = s.getCell(j);
                    if (temp.getValue() == 0 && i != j && temp.getId() != c.getId()) {
                        //increment change count only if possibilty(s) were set
                        //System.out.println("naked pair reduction");
                        changes += (temp.setPossibile(a, false) | temp.setPossibile(b, false)) ? 1 : 0;
                    }
                }
                break;
            }
        }
        return changes;
    }
    
    private int findNakedTriples() {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            changes += setNakedTriples(sudoku.getBox(i));
            changes += setNakedTriples(sudoku.getRow(i));
            changes += setNakedTriples(sudoku.getColumn(i));
        }
        return changes;
    }
    
    private int setNakedTriples(SubSudoku s) {
        int changes = 0;
        //find number of cells with 2-3 cells
        int setSize = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            int count = s.getCell(i).getPossibilityCount();
            if (count == 2 || count == 3) {
                setSize++;
            }
        }
        //return if < 3 cells with 2-3 cells
        if (setSize < 3) {
            return changes;
        }
        
        //create/fill set data structures for finding triples
        int[] indexes = new int[setSize];
        BitSet[] possibilities = new BitSet[setSize];
        int index = 0;
        for (int i = 0; i < setSize; ++i) {
            Cell c = s.getCell(index);
            while (c.getPossibilityCount() != 3 && c.getPossibilityCount() != 2 ) {
                c = s.getCell(++index);
            }
            //add index to triples set
            indexes[i] = index++;
            //create bitset representing possibilities
            possibilities[i] = new BitSet(sudoku.getLength());
            for (int n = 1; n <= sudoku.getLength(); n++) {
                if (c.containsPossibility(n)) {
                    possibilities[i].set(n-1);
                }
            }
        }
        //check if the union of all combinations of three cells form a set of size 3 
        for (int i = 0; i < setSize; ++i) {
            for (int j = i+1; j < setSize; ++j) {
                for (int k = j+1; k < setSize; ++k) {
                    BitSet union = possibilities[i].get(0, sudoku.getLength());
                    union.or(possibilities[j]);
                    union.or(possibilities[k]);
                    if (union.cardinality() == 3) {
                        //triple found
                        int a = union.nextSetBit(0)+1;
                        int b = union.nextSetBit(a)+1;
                        int c = union.nextSetBit(b)+1;
                        //remove possibilities from other cells
                        for (int n = 0; n < sudoku.getLength(); n++) {
                            Cell temp = s.getCell(n);
                            if (temp.getValue() == 0 && n != indexes[i] && n != indexes[j] && n != indexes[k]) {
                                //increment change count only if possibilty(s) were set
                                //System.out.println("naked triple reduction");
                                changes += (temp.setPossibile(a, false) | temp.setPossibile(b, false) | temp.setPossibile(c, false)) ? 1 : 0;
                            }
                        }
                    }
                }
            }
        }

        return changes;
    }
    
    public static boolean checkValidity(Sudoku s) {
        int length = s.getLength();
        //create box, row and column sets implented via boolean arrays
        boolean[][] boxes = new boolean[length][length];
        boolean[][] rows = new boolean[length][length];
        boolean[][] columns = new boolean[length][length];
        try {
        for (int i = 0; i < length; i++) {
            //check each box, row, and column for duplicate numbers
            for (int j = 0; j < length; j++) {
                if (boxes[i][s.getBox(i).getCell(j).getValue()-1])
                    return false;
                else
                    boxes[i][s.getBox(i).getCell(j).getValue()-1] = true;
                
                if (rows[i][s.getRow(i).getCell(j).getValue()-1])
                    return false;
                else
                    rows[i][s.getRow(i).getCell(j).getValue()-1] = true;
                
                if (columns[i][s.getColumn(i).getCell(j).getValue()-1])
                    return false;
                else
                    columns[i][s.getColumn(i).getCell(j).getValue()-1] = true;
            }
        }
        } catch (IndexOutOfBoundsException e) {
            //if contains number outside of valid range, return false
            return false;
        }
        return true;
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
                    if (c.getBox().getCell(j).getValue() == i) {
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
                if (c.getBox().getCell(i).getValue() != 0) {
                    c.setPossibile(c.getBox().getCell(i).getValue(), false);
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
    
    //testing naked pair implementations
    public boolean solve2() {
        initializeCells();
        int changed = 1;
        while (changed > 0) {
            update();
            changed = findHiddenSingles();
            if (changed > 0)
                continue;
            changed = findNakedPairs2();
        }
        
        return checkValidity(sudoku);
    }
    
    private int findNakedPairs2() {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            changes += setNakedPairs2(sudoku.getBox(i));
            changes += setNakedPairs2(sudoku.getRow(i));
            changes += setNakedPairs2(sudoku.getColumn(i));
        }
        return changes;
    }
    
    private int setNakedPairs2(SubSudoku s) {
        int changes = 0;
        for (int i = 0; i < sudoku.getLength(); ++i) {
            Cell c = s.getCell(i);
            //check if cell contains a pair
            if (c.getPossibilityCount() != 2)
                continue;
            //pair is a, b
            int n = 1;
            while (!c.containsPossibility(n))
                ++n;
            int a = n++;
            while (!c.containsPossibility(n))
                ++n;
            int b = n;
            //check if other cells contain the pair a, b
            for (int j = i+1; j < sudoku.getLength(); ++j) {
                Cell other = s.getCell(j);
                if (other.getPossibilityCount() == 2 && other.containsPossibility(a) && other.containsPossibility(b)) {
                    //if a match is found, remove the pair from all other cells
                    for (int k = 0; k < sudoku.getLength(); k++) {
                        Cell temp = s.getCell(k);
                        if (temp.getValue() == 0 && j != k && temp.getId() != c.getId()) {
                            //increment change count only if possibilty(s) were set
                            //System.out.println("naked pair reduction");
                            changes += (temp.setPossibile(a, false) | temp.setPossibile(b, false)) ? 1 : 0;
                        }
                    }
                    break;
                }
            }
        }
        return changes;
    }
    //end test
}
