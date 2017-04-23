package sudoku;

/**
 *
 * @author David
 */
public class SudokuGame {

    private Sudoku sudoku;
    
    private long currentTime;
    private long elapsed;
    
    private String status;
    private int score;

    public SudokuGame(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.elapsed = 0;
        this.status = "New";
        this.score = 0;
    }
    
    public Sudoku getSudoku() {
        return sudoku;
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
