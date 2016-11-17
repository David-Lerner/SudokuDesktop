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
    private GridPane sudokuGrid;
    
    @FXML
    private GridPane buttonGrid;
    
    public static final int SIDE_LENGTH = 432;
    
    public static final int BASE = 3;
    private static final int CELL_SIZE = 40;
    public static final int LENGTH = BASE*BASE;
    private static final int BORDER_SIZE = 1;

    private Sudoku sudoku;
    private Tile[][] tileGrid;
    private Selectable currentSelected;
    private GridPane[] subGrids;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //int length = BASE*BASE;
        sudoku = new Sudoku(BASE);
        //sudokuGrid.setStyle("-fx-background-color: black;");
        //sudokuGrid.setStyle("-fx-background-radius: 300px, 3px, 2px, 200px;");
        //sudokuGrid.setStyle("-fx-background-insets: 2.0 5.0 4.0 5.0");
        //sudokuGrid.setAlignment(Pos.CENTER);
        sudokuGrid.setHgap(2);
        sudokuGrid.setVgap(2);
        //sudokuGrid.setPadding(new Insets(1, 1, 1, 1));
        //int cellWidth = SIDE_LENGTH/length;
        /*for (int i = 0; i < length; i++) {
             ColumnConstraints col = new ColumnConstraints(cellWidth);
             sudokuGrid.getColumnConstraints().add(col);
             RowConstraints row = new RowConstraints(cellWidth);
             sudokuGrid.getRowConstraints().add(row);
        }*/
        tileGrid = new Tile[LENGTH][LENGTH];
        subGrids = new GridPane[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            subGrids[i] = new GridPane();
            sudokuGrid.add(subGrids[i], i%BASE, i/BASE);
        }
        currentSelected = null;
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                System.out.println("Creating Tile "+(i*LENGTH+j));
                int value = sudoku.getCell(i,j).getValue();
                final Tile tile = new Tile(value, sudoku.getCell(i, j).isGiven());
                tile.setId(String.valueOf(i*LENGTH+j));
                tile.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("mouse click detected on tile "+tile.getId());
                    tile.setSelected(true);
                    if (currentSelected != null)
                        currentSelected.setSelected(false);
                    currentSelected = tile;
                });
                //sudokuGrid.getChildren().add(tile);
                //sudokuGrid.add(tile, j, i);
                subGrids[j/BASE+(i/BASE)*BASE].add(tile, j%BASE, i%BASE);
                tileGrid[i][j] = tile;
            }
        }
        
        for (int i = 0; i <= LENGTH; i++) {
            NumberButton btn = new NumberButton(i);
            btn.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("mouse click detected on tile "+btn.getText());
                    btn.setSelected(true);
                    if (currentSelected != null)
                        currentSelected.setSelected(false);
                    currentSelected = btn;
                });
            buttonGrid.add(btn, i, 0);
        }
    }
    
    private class Tile extends StackPane implements Selectable{
        private boolean selected = false;
        private boolean given = false;

        private Rectangle cell = new Rectangle(CELL_SIZE - 2*BORDER_SIZE, CELL_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        private Text text = new Text();

        public Tile(int value, boolean given) {
            this.given = given;

            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            cell.setStroke(Color.LIGHTGRAY);
            cell.setFill(Color.WHITE);

            text.setFont(Font.font(18));
            text.setText(String.valueOf(value));
            text.setVisible(value!=0);
            border.setVisible(selected);

            getChildren().addAll(border, cell, text);
        }
        
        @Override
        public void setSelected(boolean selected){
            this.selected = selected;

            border.setVisible(selected);
            if(selected){ //Decrease the white cell size, to increase the selected border size
                cell.setWidth(CELL_SIZE - 6*BORDER_SIZE);
                cell.setHeight(CELL_SIZE - 6*BORDER_SIZE);
            } else { //Increase the white cell size, to revert the border size
                cell.setWidth(CELL_SIZE - 2*BORDER_SIZE);
                cell.setHeight(CELL_SIZE - 2*BORDER_SIZE);
            }
        }

        @Override
        public boolean isSelected() {
            return selected;
        }
        
        public void setValue(int value){
            text.setText(String.valueOf(value));
            text.setVisible(value!=0);
        }
        
        public boolean isGiven() {
            return given;
        }
    }
    
    private class NumberButton extends Button implements Selectable {
        
        private int value;
        private boolean selected = false;
        private Text text = new Text();
        
        public NumberButton(int value) {
            this.value = value;
            //setStyle("-fx-background-radius: 3px, 3px, 2px, 1px;");
            //setStyle("-fx-background-color: transparent;");
            
            text.setFont(Font.font(18));
            text.setText(String.valueOf(value));
            text.setVisible(value!=0);
            //border.setVisible(selected);
        }

        @Override
        public void setSelected(boolean selected) {
            
            this.selected = selected;
            requestFocus();
        }

        @Override
        public boolean isSelected() {
            return selected;
        }
    
    }
}
