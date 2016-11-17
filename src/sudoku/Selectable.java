package sudoku;

/**
 *
 * @author David
 */
public interface Selectable {
    
    public void setSelected(boolean selected);
    public void resolve(Selectable s);
}
