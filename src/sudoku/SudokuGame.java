package sudoku;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 *
 * @author David
 */
public class SudokuGame {

    private Sudoku sudoku;
    private int length;
    private boolean[][] highlighted;
    private Deque<ActionPair> undo;
    private Deque<ActionPair> redo;
    
    private long currentTime;
    private long elapsed;
    
    private String status;
    private int score;

    public SudokuGame(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
        this.highlighted = new boolean[length][length];
        this.undo = new ArrayDeque<>();
        this.redo = new ArrayDeque<>();
        this.elapsed = 0;
        this.status = "New";
        this.score = 0;
    }
    
    public Sudoku getSudoku() {
        return sudoku;
    }
    
    public class ActionPair {
        Action action;
        Action reverse;

        public ActionPair(Action action, Action reverse) {
            this.action = action;
            this.reverse = reverse;
        }
        
    }
    
    public abstract class Action {  
        private ArrayList<Integer> targets;
        private boolean highlighted;
        
        public abstract void apply();
    }
    
    public class SetCellAction extends Action {
        private int targetI;
        private int targetJ;
        private int value;

        public SetCellAction(int targetI, int targetJ, int value) {
            this.targetI = targetI;
            this.targetJ = targetJ;
            this.value = value;
        }
        
        @Override
        public void apply() {         
            sudoku.getCell(targetI, targetJ).removePossibilities();
            sudoku.getCell(targetI, targetJ).setValue(value);
        }
        
    }
    
    public class SetPossibilityAction extends Action {
        private int targetI;
        private int targetJ;
        private int value;
        private boolean possible;

        public SetPossibilityAction(int targetI, int targetJ, int value, boolean possible) {
            this.targetI = targetI;
            this.targetJ = targetJ;
            this.value = value;
            this.possible = possible;
        }
        
        @Override
        public void apply() {         
            sudoku.getCell(targetI, targetJ).setPossibile(value, possible);
            sudoku.getCell(targetI, targetJ).setValue(0);
        }
        
    }
    
    public class FillCellAction extends Action {
        private int targetI;
        private int targetJ;
        private int value;
        private boolean[] possibles;

        public FillCellAction(int targetI, int targetJ, int value, boolean[] possibles) {
            this.targetI = targetI;
            this.targetJ = targetJ;
            this.value = value;
            this.possibles = possibles;
        }
        
        @Override
        public void apply() {
            Cell c = sudoku.getCell(targetI, targetJ);
            for (int n = 1; n <= length; ++n) {
                c.setPossibile(n, possibles[n-1]);
            }
            c.setValue(value);
        }
        
    }
    
    public class SetHighlightedAction extends Action {  
        private ArrayList<Integer> targets;
        private boolean isHighlighted;

        public SetHighlightedAction(int target, boolean isHighlighted) {
            this(new ArrayList<>(), isHighlighted);
            this.targets.add(target);
        }
        
        public SetHighlightedAction(ArrayList<Integer> targets, boolean isHighlighted) {
            this.targets = targets;
            this.isHighlighted = isHighlighted;
        }
        
        @Override
        public void apply() {
            for (int t : targets) {
                setHighlighted(t/sudoku.getLength(), t%sudoku.getLength(), isHighlighted);
            }
        }
    }
    
    public void setValueAction(int targetI, int targetJ, int value) {
        if (targetI < 0 || targetJ < 0 || value < 0 || 
                targetI >= length || targetJ >= length || value > length) {
            throw new IllegalArgumentException();
        }
        Cell c = sudoku.getCell(targetI, targetJ);
        if (c.isGiven() || (value == 0 && c.getValue() == 0)) {
            return;
        }
        Action action, reverse;      
        if (c.getValue() == value) {
            action = new SetCellAction(targetI, targetJ, 0);
            reverse = new SetCellAction(targetI, targetJ, value);
        } else if (c.getPossibilityCount() == 0) {
            action = new SetCellAction(targetI, targetJ, value);
            reverse = new SetCellAction(targetI, targetJ, c.getValue()); 
        } else {
            action = new SetCellAction(targetI, targetJ, value);
            reverse = new FillCellAction(targetI, targetJ, c.getValue(), c.getPossibilities());
        }
        action.apply();
        undo.push(new ActionPair(action, reverse));
        redo.clear();
    }
    
    public void setPossibleAction(int targetI, int targetJ, int value) {
        if (targetI < 0 || targetJ < 0 || value < 1 || 
                targetI >= length || targetJ >= length || value > length) {
            throw new IllegalArgumentException();
        }
        Cell c = sudoku.getCell(targetI, targetJ);
        if (c.isGiven()) {
            return;
        }
        Action action, reverse;      
        if (c.getValue() != 0) {
            action = new SetPossibilityAction(targetI, targetJ, value, true);
            reverse = new SetCellAction(targetI, targetJ, c.getValue());
        } else if (c.containsPossibility(value)) {
            action = new SetPossibilityAction(targetI, targetJ, value, false);
            reverse = new SetPossibilityAction(targetI, targetJ, value, true);
        } else {
            action = new SetPossibilityAction(targetI, targetJ, value, true);
            reverse = new SetPossibilityAction(targetI, targetJ, value, false);
        }
        action.apply();
        undo.push(new ActionPair(action, reverse));
        redo.clear();
    }
    
    public void setHighlightedAction(int targetI, int targetJ) {
        if (targetI < 0 || targetJ < 0 || targetI >= length || targetJ >= length) {
            throw new IllegalArgumentException();
        }
        Action action = new SetHighlightedAction(targetI*length+targetJ, !isHighlighted(targetI, targetJ));
        Action reverse = new SetHighlightedAction(targetI*length+targetJ, isHighlighted(targetI, targetJ));       
        action.apply();
        undo.push(new ActionPair(action, reverse));
        redo.clear();
    }
    
    public void setHighlightedValueAction(int value) {
        if (value < 0 || value > length) {
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> newTargets = new ArrayList<>();
        ArrayList<Integer> oldTargets = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (sudoku.getCell(i, j).getValue() == value) {
                    if (isHighlighted(i, j)) {
                        oldTargets.add(i*length+j);
                    } else {
                        newTargets.add(i*length+j);
                    }
                }
            }
        }
        Action action;
        Action reverse;
        if (newTargets.size() > 0) {
            action = new SetHighlightedAction(newTargets, true);
            reverse = new SetHighlightedAction(newTargets, false);
        } else if (oldTargets.size() > 0) {
            action = new SetHighlightedAction(oldTargets, false);
            reverse = new SetHighlightedAction(oldTargets, true);
        } else {
            return;
        }
        action.apply();
        undo.push(new ActionPair(action, reverse));
        redo.clear();
    }
    
    public void setHighlightedPossibilityAction(int possibility) {
        if (possibility < 1 || possibility > length) {
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> newTargets = new ArrayList<>();
        ArrayList<Integer> oldTargets = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (sudoku.getCell(i, j).containsPossibility(possibility)) {
                    if (isHighlighted(i, j)) {
                        oldTargets.add(i*length+j);
                    } else {
                        newTargets.add(i*length+j);
                    }
                }
            }
        }
        Action action;
        Action reverse;
        if (newTargets.size() > 0) {
            action = new SetHighlightedAction(newTargets, true);
            reverse = new SetHighlightedAction(newTargets, false);
        } else if (oldTargets.size() > 0) {
            action = new SetHighlightedAction(oldTargets, false);
            reverse = new SetHighlightedAction(oldTargets, true);
        } else {
            return;
        }
        action.apply();
        undo.push(new ActionPair(action, reverse));
        redo.clear();
    }
    
    public void undo() {
        if (undo.isEmpty()) {
            return;
        }
        ActionPair actionPair = undo.pop();
        actionPair.reverse.apply();
        redo.push(actionPair);
    }
    
    public void redo() {
        if (redo.isEmpty()) {
            return;
        }
        ActionPair actionPair = redo.pop();
        actionPair.action.apply();
        undo.push(actionPair);
    }
    
    public void setHighlighted(int i, int j, boolean highlighted) {  
        this.highlighted[i][j] = highlighted;
    }
    
    public boolean isHighlighted(int i, int j) {
        if (i < 0 || j < 0 || i >= length || j >= length) {
            throw new IllegalArgumentException();
        }    
        return highlighted[i][j];
    }
    
    public boolean isGiven(int i, int j) {
        if (i < 0 || j < 0 || i >= length || j >= length) {
            throw new IllegalArgumentException();
        }    
        return sudoku.getCell(i, j).isGiven();
    }
    
    public int getValue(int i, int j) {
        if (i < 0 || j < 0 || i >= length || j >= length) {
            throw new IllegalArgumentException();
        }    
        return sudoku.getCell(i, j).getValue();
    }
    
    public boolean containsPossibility(int i, int j, int value) {
        if (i < 0 || j < 0 || i >= length || j >= length || value < 1 || value > length) {
            throw new IllegalArgumentException();
        }    
        return sudoku.getCell(i, j).containsPossibility(value);
    }

    public void begin() {
        start();
        status = "In progress";  
    }
    
    public void end() {
        stop();
        status = "Completed";
        score = calculateScore();
    }
    
    public void start() {
        currentTime = System.currentTimeMillis();
    }
    
    public void stop() {
        long now = System.currentTimeMillis();
        elapsed += now-currentTime;
        currentTime = now;
    }

    public long getCurrentTime() {
        return currentTime;
    }
    
    public long getElapsed() {
        long now = System.currentTimeMillis();
        elapsed += now-currentTime;
        currentTime = now;
        return elapsed;
    }
    
    public String getElapsedFormatted() {
        long seconds = getElapsed() / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = seconds / (60 * 60);
        return String.format("%d:%02d:%02d", h,m,s);
    }

    public int getScore() {
        return score;
    }
    
    public int calculateScore() {
        return 0; //calculate score
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
