/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.swing.JFileChooser;
import com.david.completesudoku.ACell;
import com.david.completesudoku.BCell;
import com.david.completesudoku.Sudoku;
import com.david.completesudoku.Cell;
import com.david.completesudoku.Selectable;
import com.david.completesudoku.SudokuGame;
import com.david.completesudoku.SudokuGame.OnChangeListener;
import com.david.completesudoku.SudokuGenerator;
import com.david.completesudoku.SudokuSolver;

/**
 *
 * @author David
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private BorderPane sudokuScene;
    
    @FXML
    private GridPane sudokuGrid;
    
    @FXML
    private GridPane buttonGrid;
    
    @FXML
    private Label elapsed;
    
    @FXML
    private Label name;
    
    @FXML
    private Label difficulty;
    
    public static final int BASE = 3;
    private static final int CELL_SIZE = 40;
    public static final int LENGTH = BASE*BASE;
    private static final int BORDER_SIZE = 1;

    private SudokuGame sudokuGame;
    private SudokuGenerator sudokuGenerator;
    private CellTile[][] cellGrid;
    private Selectable currentSelected;
    private GridPane[] subGrids;
    private boolean clicked = false;
    private boolean showScore = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int[][] model = new int[9][9] ;

      // Create the initial situation

      model[0][0] = 9 ;
      model[0][4] = 2 ;
      model[0][6] = 7 ;
      model[0][7] = 5 ;

      model[1][0] = 6 ;
      model[1][4] = 5 ;
      model[1][7] = 4 ;

      model[2][1] = 2 ;
      model[2][3] = 4 ;
      model[2][7] = 1 ;

      model[3][0] = 2 ;
      model[3][2] = 8 ;

      model[4][1] = 7 ;
      model[4][3] = 5 ;
      model[4][5] = 9 ;
      model[4][7] = 6 ;

      model[5][6] = 4 ;
      model[5][8] = 1 ;

      model[6][1] = 1 ;
      model[6][5] = 5 ;
      model[6][7] = 8 ;

      model[7][1] = 9 ;
      model[7][4] = 7 ;
      model[7][8] = 4 ;

      model[8][1] = 8 ;
      model[8][2] = 2 ;
      model[8][4] = 4 ;
      model[8][8] = 6 ;

        sudokuGenerator = new SudokuGenerator();
        sudokuGame = new SudokuGame(new Sudoku(model));
        
        resetLabels();
        sudokuGame.begin();
        /*int sample[][] = {{ 3, 0, 6, 5, 0, 8, 4, 0, 0 }, //
                        { 5, 2, 0, 0, 0, 0, 0, 0, 0 }, //
                        { 0, 8, 7, 0, 0, 0, 0, 3, 1 }, //
                        { 0, 0, 3, 0, 1, 0, 0, 8, 0 }, //
                        { 9, 0, 0, 8, 6, 3, 0, 0, 5 }, //
                        { 0, 5, 0, 0, 9, 0, 6, 0, 0 }, //
                        { 1, 3, 0, 0, 0, 0, 2, 5, 0 }, //
                        { 0, 0, 0, 0, 0, 0, 0, 7, 4 }, //
                        { 0, 0, 5, 2, 0, 6, 3, 0, 0 } };
        sudoku = new Sudoku(sample);*/
        /*sudoku = new Sudoku(BASE);
        try {
            BufferedReader br = new BufferedReader(new FileReader(DEFAULT_GAME));
            String line ="";
            int lineCount = 0;
            char[][] initialBoard = new char[9][9];
            while((line = br.readLine()) != null && lineCount < 9){
                System.out.println(line);
                initialBoard[lineCount++] = line.toCharArray() ;
            }
            br.close();
            int[][] grid = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    grid[i][j] = Character.getNumericValue(initialBoard[i][j]);
                }
            }
            sudokuGame = new SudokuGame(new Sudoku(grid));
            resetLabels();
            sudokuGame.begin();
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + DEFAULT_GAME + "'");                
        } catch(IOException ex) {
            System.out.println("Error reading file '" + DEFAULT_GAME + "'");                   
        }*/
        sudokuScene.setOnMouseClicked((MouseEvent event) -> {
            undoCurrentSelected();
        });
        //sudokuGrid.setStyle("-fx-background-color: black;");
        //sudokuGrid.setAlignment(Pos.CENTER);
        //sudokuGrid.setHgap(2);
        //sudokuGrid.setVgap(2);
        //sudokuGrid.setPadding(new Insets(1, 1, 1, 1));
        cellGrid = new CellTile[LENGTH][LENGTH];
        subGrids = new GridPane[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            subGrids[i] = new GridPane();
            sudokuGrid.add(subGrids[i], i%BASE, i/BASE);
        }
        currentSelected = null;
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                final CellTile ct = new CellTile(i,j);
                ct.setId(String.valueOf(i*LENGTH+j));
                ct.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("mouse click detected on tile "+ct.getId());
                    clicked = true;
                    ct.resolve(currentSelected);
                });
                subGrids[j/BASE+(i/BASE)*BASE].add(ct, j%BASE, i%BASE);
                cellGrid[i][j] = ct;
            }
        }
        
        for (int i = 0; i < LENGTH; i++) {
            final ValueTile vt = new ValueTile(i+1);
            vt.setOnMouseClicked((MouseEvent event) -> {
                System.out.println("mouse click detected on number button "+vt.getValue());
                clicked = true;
                vt.resolve(currentSelected);
            });
            buttonGrid.add(vt, i, 0);
            final PossibilityTile ct = new PossibilityTile(i+1);
            ct.setOnMouseClicked((MouseEvent event) -> {
                System.out.println("mouse click detected on possibility button "+ct.getPossibility());
                clicked = true;
                ct.resolve(currentSelected);
            });
            buttonGrid.add(ct, i, 1);
        }
        final ValueTile vt = new ValueTile(0);
        vt.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("mouse click detected on the number erase button ");
            clicked = true;
            vt.resolve(currentSelected);
        });
        buttonGrid.add(vt, 9, 0);
        final HighlightTile ht = new HighlightTile();
        ht.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("mouse click detected on the highlight button ");
            clicked = true;
            ht.resolve(currentSelected);
        });
        buttonGrid.add(ht, 9, 1);
        
        //add a listener to update GUI when game state changes
        sudokuGame.addOnChangeListener(new OnChangeListener() {
            @Override
            public void onChange() {
                for (int i = 0; i < LENGTH; i++) {
                    for (int j = 0; j < LENGTH; j++) {
                        cellGrid[i][j].update();
                    }
                }
            }
        });
        
        //add timer
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        elapsed.setText(sudokuGame.getElapsedFormatted());
                        if (sudokuGame.getStatus().equals(SudokuGame.COMPLETED) && !showScore) {
                            showScore = true;
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Your score");
                            alert.setHeaderText("Your score");
                            alert.setContentText(sudokuGame.getScore()+" points");

                            alert.showAndWait();
                        }
                    }
                });
             }
        }, 0, 1000);
    }
    
    private void resetLabels() {
        name.setText(sudokuGame.getName());
        difficulty.setText(sudokuGame.getDifficulty());
        showScore = false;
    }
    
    private class CellTile extends StackPane implements Selectable{
        int targetI, targetJ;
        private boolean selected = false;

        private Rectangle inner = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private Text text = new Text();
        private GridPane possibleGrid = new GridPane();
        private Label possibles[] = new Label[LENGTH];

        public CellTile(int targetI, int targetJ) {
            this.targetI = targetI;
            this.targetJ = targetJ;

            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            inner.setStroke(Color.LIGHTGRAY);
            inner.setFill(sudokuGame.isHighlighted(targetI, targetJ)? Color.CYAN : Color.WHITE);
            
            text.setFont(Font.font(null, sudokuGame.isGiven(targetI, targetJ) ? FontWeight.EXTRA_BOLD : FontWeight.NORMAL, CELL_SIZE/2));
            text.setFill(sudokuGame.isGiven(targetI, targetJ) ? Color.BLUE : Color.BLACK);
            text.setFill(sudokuGame.isErrorShownInCell(targetI, targetJ) ? Color.RED : Color.BLACK);
            text.setText(String.valueOf(sudokuGame.getValue(targetI, targetJ)));
            text.setVisible(sudokuGame.getValue(targetI, targetJ) != 0);
            border.setVisible(selected);
            
            possibleGrid.setPrefSize(CELL_SIZE, CELL_SIZE);
            for (int i = 0; i < LENGTH; i++) {
                Label label = new Label(String.valueOf(i+1));
                label.setFont(Font.font((CELL_SIZE-2*BORDER_SIZE)/(BASE)));
                label.setPadding(new Insets(-BASE, -BASE, -BASE, -BASE));
                StackPane pane = new StackPane();
                pane.setMaxWidth(CELL_SIZE/BASE);
                pane.setMaxHeight(CELL_SIZE/BASE);
                Rectangle r = new Rectangle(CELL_SIZE/BASE, CELL_SIZE/BASE);
                r.setVisible(false);
                pane.getChildren().add(r);
                pane.setClip(new Rectangle(CELL_SIZE/BASE, CELL_SIZE/BASE));
                pane.getChildren().add(label);
                label.setVisible(sudokuGame.containsPossibility(targetI, targetJ, i+1));
                possibles[i] = label;
                possibleGrid.add(pane, i%BASE, i/BASE);
            }

            getChildren().addAll(border, inner, text, possibleGrid);
        }

        public int getTargetI() {
            return targetI;
        }

        public int getTargetJ() {
            return targetJ;
        }
        
        @Override
        public void setSelected(boolean selected){
            this.selected = selected;

            border.setVisible(selected);
            if(selected){ //Decrease the white cell size, to increase the selected border size
                inner.setWidth(CELL_SIZE - 6*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 6*BORDER_SIZE);
            } else { //Increase the white cell size, to revert the border size
                inner.setWidth(CELL_SIZE - 2*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 2*BORDER_SIZE);
            }
        }
        
        @Override
        public void resolve(Selectable s) {
            System.out.println("resolving cell tile");
            //undo error coloring
            text.setFill(sudokuGame.isGiven(targetI, targetJ)? Color.BLUE : Color.BLACK);
            text.setFill(sudokuGame.isErrorShownInCell(targetI, targetJ) ? Color.RED : Color.BLACK);
            
            if (s == null) {
                currentSelected = this;
                setSelected(true);
            }
            else if (s == this) {
                currentSelected = null;
                setSelected(false);
                sudokuGame.setHighlightedAction(targetI, targetJ);
            }
            else if (s instanceof CellTile) {
                currentSelected = this;
                s.setSelected(false);
                setSelected(true);
            }
            else if (s instanceof ValueTile) {
                sudokuGame.setValueAction(targetI, targetJ, ((ValueTile) s).getValue());
            }
            else if (s instanceof PossibilityTile) {
                sudokuGame.setPossibleAction(targetI, targetJ, ((PossibilityTile) s).getPossibility());
            }
            else if (s instanceof HighlightTile) {
                sudokuGame.setHighlightedAction(targetI, targetJ);
            }
        }
        
        public void update() {
            inner.setFill(sudokuGame.isHighlighted(targetI, targetJ)? Color.CYAN : Color.WHITE);
            text.setText(String.valueOf(sudokuGame.getValue(targetI, targetJ)));
            text.setVisible(sudokuGame.getValue(targetI, targetJ) != 0);
            text.setFont(Font.font(null, sudokuGame.isGiven(targetI, targetJ)? FontWeight.EXTRA_BOLD : FontWeight.NORMAL, CELL_SIZE/2));
            text.setFill(sudokuGame.isGiven(targetI, targetJ)? Color.BLUE : Color.BLACK);
            text.setFill(sudokuGame.isErrorShownInCell(targetI, targetJ) ? Color.RED : Color.BLACK);
            for (int i = 0; i < LENGTH; i++)
                possibles[i].setVisible(sudokuGame.containsPossibility(targetI, targetJ, i+1));
        }
        
    }
    
    private class ValueTile extends StackPane implements Selectable {
        
        private int value;
        private boolean selected = false;
        private Rectangle inner = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private Text text = new Text();
        
        public ValueTile(int value) {
            this.value = value;
            
            text.setFont(Font.font((CELL_SIZE - 2*BORDER_SIZE)/2));
            text.setText(String.valueOf(value));
            text.setVisible(value!=0);
            border.setVisible(selected);
            
            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            inner.setStroke(Color.LIGHTGRAY);
            inner.setFill(Color.WHITE);

            getChildren().addAll(border, inner, text);
        }

        @Override
        public void setSelected(boolean selected) {
            
            this.selected = selected;
            border.setVisible(selected);
            if(selected){ //Decrease the white cell size, to increase the selected border size
                inner.setWidth(CELL_SIZE - 6*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 6*BORDER_SIZE);
            } else { //Increase the white cell size, to revert the border size
                inner.setWidth(CELL_SIZE - 2*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 2*BORDER_SIZE);
            }
        }

        @Override
        public void resolve(Selectable s) {
            if (s == null) {
                currentSelected = this;
                setSelected(true);
            }
            else if (s == this) {
                currentSelected = null;
                setSelected(false);
            }
            else if (s instanceof ValueTile || s instanceof PossibilityTile) {
                currentSelected = this;
                s.setSelected(false);
                setSelected(true);
            }
            else if (s instanceof CellTile) {
                CellTile t = ((CellTile) s);
                sudokuGame.setValueAction(t.getTargetI(), t.getTargetJ(), value);
                currentSelected = null;
                t.setSelected(false);
            }
            else if (s instanceof HighlightTile) {
                sudokuGame.setHighlightedValueAction(value);
            }
        }
        
        public int getValue() {
            return value;
        }
    
    }
    
    private class PossibilityTile extends StackPane implements Selectable {
        
        private int possibility;
        private boolean selected = false;
        private Rectangle inner = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private GridPane possibleGrid = new GridPane();
        private Label possibles[] = new Label[LENGTH];
        
        public PossibilityTile(int possibility) {
            this.possibility = possibility;
            
            possibleGrid.setPrefSize(CELL_SIZE, CELL_SIZE);
            //possibleGrid.setAlignment(Pos.CENTER);
            this.setClip(new Rectangle(CELL_SIZE, CELL_SIZE));
            for (int i = 0; i < LENGTH; i++) {
                Label label = new Label(String.valueOf(i+1));
                label.setFont(Font.font((CELL_SIZE-2*BORDER_SIZE)/(BASE)));
                label.setPadding(new Insets(-BASE, -BASE, -BASE, -BASE));
                StackPane pane = new StackPane();
                pane.setMaxWidth(CELL_SIZE/BASE);
                pane.setMaxHeight(CELL_SIZE/BASE);
                Rectangle r = new Rectangle(1*CELL_SIZE/BASE, 1*CELL_SIZE/BASE);
                r.setVisible(false);
                pane.getChildren().add(r);
                pane.setClip(new Rectangle(1*CELL_SIZE/BASE, 1*CELL_SIZE/BASE));
                pane.getChildren().add(label);
                label.setVisible(i+1==possibility);
                possibles[i] = label;
                possibleGrid.add(pane, i%BASE, i/BASE);
            }
            border.setVisible(selected);        
            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            inner.setStroke(Color.LIGHTGRAY);
            inner.setFill(Color.WHITE);

            getChildren().addAll(border, inner, possibleGrid);
        }

        @Override
        public void setSelected(boolean selected) {
            
            this.selected = selected;
            border.setVisible(selected);
            if(selected){ //Decrease the white cell size, to increase the selected border size
                inner.setWidth(CELL_SIZE - 6*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 6*BORDER_SIZE);
            } else { //Increase the white cell size, to revert the border size
                inner.setWidth(CELL_SIZE - 2*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 2*BORDER_SIZE);
            }
        }

        @Override
        public void resolve(Selectable s) {
            if (s == null) {
                currentSelected = this;
                setSelected(true);
            }
            else if (s == this) {
                currentSelected = null;
                setSelected(false);
            }
            else if (s instanceof ValueTile || s instanceof PossibilityTile) {
                currentSelected = this;
                s.setSelected(false);
                setSelected(true);
            }
            else if (s instanceof CellTile) {
                CellTile t = ((CellTile) s);
                sudokuGame.setPossibleAction(t.getTargetI(), t.getTargetJ(), possibility);
                currentSelected = null;
                t.setSelected(false);
            }
            else if (s instanceof HighlightTile) {
                sudokuGame.setHighlightedPossibilityAction(possibility);
            }
        }
        
        public int getPossibility() {
            return possibility;
        }
        
    }
    
    private class HighlightTile extends StackPane implements Selectable {
        
        private boolean selected = false;
        private Rectangle inner = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        
        public HighlightTile() {            
            
            border.setVisible(selected);
            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            inner.setStroke(Color.LIGHTGRAY);
            inner.setFill(Color.CYAN);

            getChildren().addAll(border, inner);
        }

        @Override
        public void setSelected(boolean selected) {
            
            this.selected = selected;
            border.setVisible(selected);
            if(selected){ //Decrease the white cell size, to increase the selected border size
                inner.setWidth(CELL_SIZE - 6*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 6*BORDER_SIZE);
            } else { //Increase the white cell size, to revert the border size
                inner.setWidth(CELL_SIZE - 2*BORDER_SIZE);
                inner.setHeight(CELL_SIZE - 2*BORDER_SIZE);
            }
        }

        @Override
        public void resolve(Selectable s) {
            if (s == null) {
                currentSelected = this;
                setSelected(true);
            }
            else if (s == this) {
                currentSelected = null;
                setSelected(false);
            }
            else if (s instanceof ValueTile) {
                ValueTile t = ((ValueTile) s);
                sudokuGame.setHighlightedValueAction(t.getValue());
            }
            else if (s instanceof PossibilityTile) {
                PossibilityTile t = ((PossibilityTile) s);
                sudokuGame.setHighlightedPossibilityAction(t.getPossibility());
            }
            else if (s instanceof CellTile) {
                CellTile t = ((CellTile) s);
                sudokuGame.setHighlightedAction(t.getTargetI(), t.getTargetJ());
                currentSelected = null;
                t.setSelected(false);
            }
        }
        
    }
    
    @FXML
    public void newGame() {
        undoCurrentSelected();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Select Difficulty");
        alert.setHeaderText("Select Difficulty");

        ButtonType buttonTypeOne = new ButtonType(SudokuGenerator.EASY);
        ButtonType buttonTypeTwo = new ButtonType(SudokuGenerator.MEDIUM);
        ButtonType buttonTypeThree = new ButtonType(SudokuGenerator.HARD);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Sudoku sudoku = null;
        String diff;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            sudoku = sudokuGenerator.getSudoku(SudokuGenerator.EASY);
            diff = SudokuGenerator.EASY;
        } else if (result.get() == buttonTypeTwo) {
            diff = SudokuGenerator.MEDIUM;
        } else if (result.get() == buttonTypeThree) {
            diff = SudokuGenerator.HARD;
        } else {
            // ... user chose CANCEL or closed the dialog
            diff = null;
        }
        if (diff != null) {
            sudoku = sudokuGenerator.getSudoku(diff);
        }
        if (sudoku != null) {
            sudokuGame.clearOnChangeListeners();
            sudokuGame = new SudokuGame(sudoku);
            sudokuGame.setDifficulty(diff);
            sudokuGame.addOnChangeListener(new OnChangeListener() {
                @Override
                public void onChange() {
                    for (int i = 0; i < LENGTH; i++) {
                        for (int j = 0; j < LENGTH; j++) {
                            cellGrid[i][j].update();
                        }
                    }
                }
            });
            resetLabels();
            sudokuGame.begin();
        }
    }
    
    @FXML
    public void loadGame() {
        undoCurrentSelected();
        File recordsDir = new File(System.getProperty("user.home"), "CompleteSudoku");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(recordsDir);
        fileChooser.setTitle("Select Sudoku game");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            FileModel fileModel = new FileModel(file.getPath());
            sudokuGame.clearOnChangeListeners();
            sudokuGame = fileModel.loadGame(null);
            sudokuGame.addOnChangeListener(new OnChangeListener() {
                @Override
                public void onChange() {
                    for (int i = 0; i < LENGTH; i++) {
                        for (int j = 0; j < LENGTH; j++) {
                            cellGrid[i][j].update();
                        }
                    }
                }
            });
            resetLabels();
            sudokuGame.start();
            sudokuGame.update();
        }
        
    }
    
    @FXML
    public void saveGame() {
        undoCurrentSelected();       
        /*TextInputDialog dialog = new TextInputDialog(sudokuGame.getName());
        dialog.setTitle("Save a Sudoku puzzle");
        dialog.setContentText("Please enter a name:");       
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            FileModel fileModel = new FileModel(result.get());
            fileModel.saveGame(sudokuGame, null);
        }*/
        File recordsDir = new File(System.getProperty("user.home"), "CompleteSudoku");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(recordsDir);
        fileChooser.setTitle("Select Sudoku game");
        fileChooser.setInitialFileName(sudokuGame.getName());
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            sudokuGame.setName(file.getName().substring(0,file.getName().lastIndexOf(".")));
            FileModel fileModel = new FileModel(file.getPath());
            fileModel.saveGame(sudokuGame, null);
            resetLabels();
        }
               
    }
    
    @FXML
    public void solveGame() {
        undoCurrentSelected();
        sudokuGame.showAllAnswers();
            
    }
    
    @FXML
    public void undoMove() {
        sudokuGame.undo();
    }
    
    @FXML
    public void redoMove() {
        sudokuGame.redo();
    }
    
    @FXML
    public void showAllErrors() {
        undoCurrentSelected();
        sudokuGame.showAllErrors();
    }
    
    @FXML
    public void showError() {
        if (currentSelected instanceof CellTile) {
            CellTile tile = (CellTile) currentSelected;
            sudokuGame.showError(tile.getTargetI(), tile.getTargetJ());
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No cell selected");
            alert.setHeaderText(null);
            alert.setContentText("No cell selected");

            alert.showAndWait();
        }
    }
    
    @FXML
    public void showValue() {
        if (currentSelected instanceof CellTile) {
            CellTile tile = (CellTile) currentSelected;
            sudokuGame.showAnswer(tile.getTargetI(), tile.getTargetJ());
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No cell selected");
            alert.setHeaderText(null);
            alert.setContentText("No cell selected");

            alert.showAndWait();
        }
    }
    
    @FXML
    public void restartGame() {
        undoCurrentSelected();
        sudokuGame.reset();
    }
    
    @FXML
    public void fillNotes() {
        undoCurrentSelected();
        sudokuGame.showPossibilities();
    }
    
    @FXML
    public void removeNotes() {
        undoCurrentSelected();
        sudokuGame.removePossibilities();
    }
    
    @FXML
    public void test() {
        //change the test function to whatever appropriate functionality you are testing
        test5();
    }
    
    //compares 2 different update implementations
    public void test1() {
        List<int[][]> sudokus = sudokuGenerator.getTestSudokus();
        ArrayList<SudokuSolver> s1 = new ArrayList<>();
        ArrayList<SudokuSolver> s2 = new ArrayList<>();
        for (int[][] puzzle : sudokus) {
            s1.add(new SudokuSolver(new Sudoku(puzzle)));
            s2.add(new SudokuSolver(new Sudoku(puzzle)));
        }
        long start, time;
        System.out.println("Starting Test:");
        start = System.currentTimeMillis();
        for (SudokuSolver s : s1)
            s.testUpdate(1);
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for s1: %,14d%n", time);
        start = System.currentTimeMillis();
        for (SudokuSolver s : s2)
            s.testUpdate(2);
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for s2: %,14d%n", time);
        System.out.println("End Test:");
    }
    
    //tests validity and timing of solvers
    public void test2() {
        List<int[][]> sudokus = sudokuGenerator.getTestSudokus();
        ArrayList<Sudoku> s1 = new ArrayList<>();
        ArrayList<SudokuSolver> s2 = new ArrayList<>();
        for (int[][] puzzle : sudokus) {
            s1.add(new Sudoku(puzzle));
            s2.add(new SudokuSolver(new Sudoku(puzzle)));
        }
        long start, time;
        System.out.println("Starting Test:");
        int correct = 0;
        start = System.currentTimeMillis();
        for (Sudoku s : s1) {
            if (SudokuSolver.checkValidity(SudokuSolver.solve(s)))
                correct++;
        }
        time = System.currentTimeMillis() - start;
        System.out.printf("Advanced solver: %d/%d correctly in %,14d%n", correct, s1.size(), time);
        correct = 0;
        start = System.currentTimeMillis();
        for (SudokuSolver s : s2)
            if (s.solve())
                correct++;
        time = System.currentTimeMillis() - start;
        System.out.printf("Base solver: %d/%d correctly in %,14d%n", correct, s2.size(), time);
        System.out.println("End Test:");
    }
    
    //compares 2 different cell implementations
    public void test3() {
        List<int[][]> sudokus = sudokuGenerator.getTestSudokus();
        ArrayList<SudokuSolver> s1 = new ArrayList<>();
        ArrayList<SudokuSolver> s2 = new ArrayList<>();
        for (int[][] puzzle : sudokus) {
            s1.add(new SudokuSolver(new Sudoku(puzzle, new ACell())));
            s2.add(new SudokuSolver(new Sudoku(puzzle, new BCell())));
        }
        long start, time;
        System.out.println("Starting Test:");
        start = System.currentTimeMillis();
        for (SudokuSolver s : s1)
            s.solve();
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for ACell Implementation: %,14d%n", time);
        start = System.currentTimeMillis();
        for (SudokuSolver s : s2)
            s.solve();
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for BCell Implementation: %,14d%n", time);
        System.out.println("End Test:");
    }
    
    //compares 2 different naked pair implementations
    public void test4() {
        List<int[][]> sudokus = sudokuGenerator.getTestSudokus();
        ArrayList<SudokuSolver> s1 = new ArrayList<>();
        ArrayList<SudokuSolver> s2 = new ArrayList<>();
        for (int[][] puzzle : sudokus) {
            s1.add(new SudokuSolver(new Sudoku(puzzle)));
            s2.add(new SudokuSolver(new Sudoku(puzzle)));
        }
        long start, time;
        System.out.println("Starting Test:");
        start = System.currentTimeMillis();
        for (SudokuSolver s : s1)
            s.solve();
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for findNakedPairs(): %,14d%n", time);
        start = System.currentTimeMillis();
        for (SudokuSolver s : s2)
            s.solve2();
        time = System.currentTimeMillis() - start;
        System.out.printf("Time taken for findNakedPairs2(): %,14d%n", time);
        System.out.println("End Test:");
    }
    
    //tests occurrences of strategies
    public void test5() {
        List<int[][]> sudokus = sudokuGenerator.getTestSudokus();
        ArrayList<SudokuSolver> solvers = new ArrayList<>();
        for (int[][] puzzle : sudokus) {
            solvers.add(new SudokuSolver(new Sudoku(puzzle)));
        }
        long start, time;
        int[] strategyCounts = new int[SudokuSolver.STRATEGY_NUMBER];
        int[] strategyByCellCounts = new int [SudokuSolver.STRATEGY_NUMBER];
        int[] strategyUsed = new int [SudokuSolver.STRATEGY_NUMBER];
        int correct = 0;
        System.out.println("Starting Test:");
        start = System.currentTimeMillis();
        for (SudokuSolver s : solvers) {
            if (s.solve()) {
                correct++;
                for (int i = 0; i < SudokuSolver.STRATEGY_NUMBER; i++) {
                    strategyCounts[i] += s.getStrategyCount(i);
                    strategyByCellCounts[i] += s.getStrategyCountByCell(i);
                    strategyUsed[i] += s.isStrategyUsed(i) ? 1 : 0;
                }
            }
        }
        time = System.currentTimeMillis() - start;
        System.out.printf("Solved and rated %d/%d puzzles in %,14d%n", correct, solvers.size(), time);
        System.out.printf("%-21s %10s %10s %10s%n", "", "avg. count", "by cell", "used in");
        for (int i = 0; i < SudokuSolver.STRATEGY_NUMBER; i++) {
            System.out.printf("%-21s %10.3f %10.3f %10s%n", SudokuSolver.getStrategyName(i)+":", 
                    (strategyCounts[i]+0.0)/correct, (strategyByCellCounts[i]+0.0)/correct, strategyUsed[i]+"/"+correct);
        }
        System.out.println("End Test:");
    }
    
    private void undoCurrentSelected() {
        if (!clicked) {
            if (currentSelected != null)
                currentSelected.setSelected(false);
            currentSelected = null;
            }
        clicked = false;
    }
    
}
