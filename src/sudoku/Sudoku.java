package sudoku;

/**
 *
 * @author David
 */
public class Sudoku {
    
    private int length;
    private Cell[][] cells;
    private SubSudoku[] rows;
    private SubSudoku[] columns;
    private SubSudoku[] subgrids;
    
    /**
     * Create a new Sudoku with the default base of 3
     */
    public Sudoku() {
        this(3);
    }

    /**
     * Create a new Sudoku with a given base
     * @param base the base the of the Sudoku
     */
    public Sudoku(int base) {
        length = base*base;
        cells = new Cell[length][length];
        rows = new SubSudoku[length];
        columns = new SubSudoku[length];
        subgrids = new SubSudoku[length];
        for (int i = 0; i < length; i++) {
            rows[i] = new SubSudoku(length);
            columns[i] = new SubSudoku(length);
            subgrids[i] = new SubSudoku(length);
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                SubSudoku row = rows[i];
                SubSudoku column = columns[j];
                SubSudoku subgrid = subgrids[(j/base+(i/base)*base)];
                Cell cell = new Cell(j+i*length, length, row, column, subgrid);
                cells[i][j] = cell;
                row.addcell(cell);
                column.addcell(cell);
                subgrid.addcell(cell);
            }
        }
    }
    
    /**
     * Create a Sudoku based on a given configuration
     * @param given a given n x n Sudoku with empty cells as 0
     */
    public Sudoku (int[][] given) {
        length = given.length;
        cells = new Cell[length][length];
        rows = new SubSudoku[length];
        columns = new SubSudoku[length];
        subgrids = new SubSudoku[length];
        for (int i = 0; i < length; i++) {
            rows[i] = new SubSudoku(length);
            columns[i] = new SubSudoku(length);
            subgrids[i] = new SubSudoku(length);
        }
        int base = (int)Math.sqrt(9);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                SubSudoku row = rows[i];
                SubSudoku column = columns[j];
                SubSudoku subgrid = subgrids[(j/base+(i/base)*base)];
                Cell cell = new Cell(j+i*length, length, given[i][j], row, column, subgrid, true);
                cells[i][j] = cell;
                row.addcell(cell);
                column.addcell(cell);
                subgrid.addcell(cell);
            }
        }
    }

    public int getLength() {
        return length;
    }
    
    public Cell getCell(int i, int j) {
        return cells[i][j];
    }
}
