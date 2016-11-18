/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
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
import javafx.scene.text.Text;
import sudoku.Sudoku;
import sudoku.Cell;
import sudoku.Selectable;

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
    
    public static final int BASE = 3;
    private static final int CELL_SIZE = 40;
    public static final int LENGTH = BASE*BASE;
    private static final int BORDER_SIZE = 1;

    private Sudoku sudoku;
    private CellTile[][] cellGrid;
    private Selectable currentSelected;
    private GridPane[] subGrids;
    private boolean clicked = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sudokuScene.setOnMouseClicked((MouseEvent event) -> {
            if (!clicked) {
                System.out.println("mouse click detected on scene");
                if (currentSelected != null)
                    currentSelected.setSelected(false);
                currentSelected = null;
            }
            clicked = false;
        });
        sudoku = new Sudoku(BASE);
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
                System.out.println("Creating Tile "+(i*LENGTH+j));
                final CellTile ct = new CellTile(sudoku.getCell(i,j));
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
    }
    
    private class CellTile extends StackPane implements Selectable{
        private Cell cell;
        private boolean selected = false;

        private Rectangle inner = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private Text text = new Text();
        private GridPane possibleGrid = new GridPane();
        private Label possibles[] = new Label[LENGTH];

        public CellTile(Cell cell) {
            this.cell = cell;

            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            inner.setStroke(Color.LIGHTGRAY);
            inner.setFill(cell.isHighlighted()? Color.CYAN : Color.WHITE);

            text.setFont(Font.font((CELL_SIZE - 2*BORDER_SIZE)/2));
            text.setText(String.valueOf(cell.getValue()));
            text.setVisible(cell.getValue()!=0);
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
                label.setVisible(cell.containsPossibility(i+1));
                possibles[i] = label;
                possibleGrid.add(pane, i%BASE, i/BASE);
            }

            getChildren().addAll(border, inner, text, possibleGrid);
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
            if (s == null) {
                currentSelected = this;
                setSelected(true);
            }
            else if (s == this) {
                currentSelected = null;
                setSelected(false);
                setHighlighted(false);
            }
            else if (s instanceof CellTile) {
                currentSelected = this;
                s.setSelected(false);
                setSelected(true);
            }
            else if (s instanceof ValueTile) {
                if (!cell.isGiven()) {
                    setValue(((ValueTile) s).getValue());
                }
                currentSelected = null;
                s.setSelected(false);
            }
            else if (s instanceof PossibilityTile) {
                if (!cell.isGiven()) {
                    togglePossible(((PossibilityTile) s).getPossibility());
                }
                //currentSelected = null;
                //s.setSelected(false);
            }
            else if (s instanceof HighlightTile) {
                setHighlighted(!cell.isHighlighted());
                //HighlightTile t = ((HighlightTile) s);
                //currentSelected = null;
                //t.setSelected(false);
            }
        }
        
        public void setValue(int value){
            text.setText(String.valueOf(value));
            text.setVisible(value!=0);
            cell.setValue(value);
            if (cell.getPossibilityCount() > 0) {
                for (int i = 0; i < LENGTH; i++) {
                    if (cell.containsPossibility(i+1)) {
                        cell.setPossibile(i+1, false);
                        possibles[i].setVisible(false);
                    }
                }
            }
        }
        
        public int getValue() {
            return cell.getValue();
        }
        
        
        public boolean containsPossibility(int i) {
            return cell.containsPossibility(i);
        }
        
        public boolean isGiven() {
            return cell.isGiven();
        }
        
        public void togglePossible(int value) {
            if (cell.getValue() != 0)
                setValue(0);
            cell.togglePossibile(value);
            possibles[value-1].setVisible(cell.containsPossibility(value));
        }
        
        public void setHighlighted(boolean highlighted) {
            inner.setFill(highlighted? Color.CYAN : Color.WHITE);
            cell.setHighlighted(highlighted);
        }
        
        public boolean isHighlighted() {
            return cell.isHighlighted();
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
                if (!t.isGiven()) {
                    t.setValue(value);
                }
                currentSelected = null;
                t.setSelected(false);
            }
            else if (s instanceof HighlightTile) {
                HighlightTile t = ((HighlightTile) s);
                setHighlighted();
                currentSelected = null;
                t.setSelected(false);
            }
        }
        
        public int getValue() {
            return value;
        }
        
        public void setHighlighted() {
            for (int i = 0; i < LENGTH; i++) {
                for (int j = 0; j < LENGTH; j++) {
                    if (cellGrid[i][j].getValue() == value) {
                        cellGrid[i][j].setHighlighted(true);
                    }
                }
            }
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
                if (!t.isGiven()) {
                    t.togglePossible(possibility);
                }
                //currentSelected = null;
                //t.setSelected(false);
            }
            else if (s instanceof HighlightTile) {
                HighlightTile t = ((HighlightTile) s);
                setHighlighted();
                currentSelected = null;
                t.setSelected(false);
            }
        }
        
        public int getPossibility() {
            return possibility;
        }
        
        public void setHighlighted() {
            for (int i = 0; i < LENGTH; i++) {
                for (int j = 0; j < LENGTH; j++) {
                    if (cellGrid[i][j].containsPossibility(possibility)) {
                        cellGrid[i][j].setHighlighted(true);
                    }
                }
            }
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
                t.setHighlighted();
                currentSelected = null;
                t.setSelected(false);
            }
            else if (s instanceof PossibilityTile) {
                PossibilityTile t = ((PossibilityTile) s);
                t.setHighlighted();
                currentSelected = null;
                t.setSelected(false);
            }
            else if (s instanceof CellTile) {
                CellTile t = ((CellTile) s);
                t.setHighlighted(!t.isHighlighted());
                currentSelected = null;
                t.setSelected(false);
            }
        }
        
    }
}
