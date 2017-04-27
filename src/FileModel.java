
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.david.completesudoku.SudokuGame;
import com.david.completesudoku.SudokuModel;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TargetInfo;
import com.david.completesudoku.Cell;
import com.david.completesudoku.Sudoku;
import com.david.completesudoku.SudokuGame.Action;
import com.david.completesudoku.SudokuGame.ActionPair;
import com.david.completesudoku.SudokuGame.FillCellAction;
import com.david.completesudoku.SudokuGame.SetCellAction;
import com.david.completesudoku.SudokuGame.SetHighlightedAction;
import com.david.completesudoku.SudokuGame.SetPossibilityAction;



/**
 *
 * @author David
 */
public class FileModel implements SudokuModel{
    
    private String fileLocation;

    public FileModel(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    
    /*public class Staff {

	private String name;
	private int age;
	private String position;
	private BigDecimal salary;
	private List<Skill> skills;
        private Map<String, Object> other;

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        public String getPosition() {
            return position;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public List<Skill> getSkills() {
            return skills;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public void setSkills(List<Skill> skills) {
            this.skills = skills;
        }

        public Map<String, Object> getOther() {
            return other;
        }

        public void setOther(Map<String, Object> other) {
            this.other = other;
        }
        
    }
    
    
    public class Skill {
        
        private String name;
        private String proficiency;

        public String getName() {
            return name;
        }

        public String getProficiency() {
            return proficiency;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProficiency(String proficiency) {
            this.proficiency = proficiency;
        }
        
    } 
    
    public class CertifiedSkill extends Skill {
        private String agency;

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }
        
    }
    
    private Staff createDummyObject() {

        Staff staff = new Staff();

        staff.setName("mkyong");
        staff.setAge(35);
        staff.setPosition("Founder");
        staff.setSalary(new BigDecimal("10000"));

        List<Skill> skills = new ArrayList<>();
        Skill skill = new Skill();
        skill.setName("java");
        skill.setProficiency("expert");
        skills.add(skill);
        
        CertifiedSkill cskill = new CertifiedSkill();
        cskill.setName("python");
        cskill.setProficiency("basic");
        cskill.setAgency("python certification");
        skills.add(cskill);
        
        skill = new Skill();
        skill.setName("shell");
        skill.setProficiency("basic");
        skills.add(skill);

        staff.setSkills(skills);
        
        Map<String, Object> other = new HashMap<>();
        other.put("Gender", "male");
        other.put("Sex", "male");
        List<String> food = new ArrayList<>();
        food.add("bugs");
        food.add("bees");
        other.put("Food", food);
        other.put("age", 19);
        skill = cskill;
        other.put("pyskill", skill);
        int[] array = {1,2,3};
        other.put("array", array);
        staff.setOther(other);

        return staff;

    }*/
    
    private class SaveGame {
        private String name;
        private String difficulty;
        private String status;
        
        private int score;
        private boolean[] answers; 
        private boolean[][] errors; 

        private long currentTime;
        private long elapsed;

        private boolean[][] highlighted;
        
        private int[] value;
        private boolean[] given; 
        private boolean[][] possible; 
        
        private List<Map<String, Map<String, String>>> undo;
        private List<Map<String, Map<String, String>>> redo;

        public long getCurrentTime() {
            return currentTime;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public long getElapsed() {
            return elapsed;
        }

        public boolean[] getGiven() {
            return given;
        }

        public boolean[][] getHighlighted() {
            return highlighted;
        }

        public String getName() {
            return name;
        }

        public boolean[][] getPossible() {
            return possible;
        }

        public List<Map<String, Map<String, String>>> getRedo() {
            return redo;
        }

        public int getScore() {
            return score;
        }

        public boolean[][] getErrors() {
            return errors;
        }

        public boolean[] getAnswers() {
            return answers;
        }

        public String getStatus() {
            return status;
        }

        public List<Map<String, Map<String, String>>> getUndo() {
            return undo;
        }

        public int[] getValue() {
            return value;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public void setElapsed(long elapsed) {
            this.elapsed = elapsed;
        }

        public void setGiven(boolean[] given) {
            this.given = given;
        }

        public void setHighlighted(boolean[][] highlighted) {
            this.highlighted = highlighted;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPossible(boolean[][] possible) {
            this.possible = possible;
        }

        public void setRedo(List<Map<String, Map<String, String>>> redo) {
            this.redo = redo;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setErrors(boolean[][] errors) {
            this.errors = errors;
        }

        public void setAnswers(boolean[] answers) {
            this.answers = answers;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setUndo(List<Map<String, Map<String, String>>> undo) {
            this.undo = undo;
        }

        public void setValue(int[] value) {
            this.value = value;
        }
        
    }
    
    private SaveGame createSaveGame(SudokuGame sg) {
        SaveGame game = new SaveGame();
        
        game.setName(sg.getName());
        game.setDifficulty(sg.getDifficulty());
        game.setStatus(sg.getStatus());
        
        game.setScore(sg.getScore());
        game.setAnswers(sg.getAnswers());
        game.setErrors(sg.getErrors());
        
        game.setCurrentTime(sg.getCurrentTime());
        game.setElapsed(sg.getElapsed());
        
        game.setHighlighted(sg.getHighlighted());
        
        Sudoku sudoku = sg.getSudoku();
        int length = sudoku.getLength();
        int[] value = new int[length*length];
        boolean[] given = new boolean[length*length];
        boolean[][] possible = new boolean[length*length][length];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                int id = i*length+j;
                Cell c = sudoku.getCell(i, j);
                value[id] = c.getValue();
                given[id] = c.isGiven();
                possible[id] = c.getPossibilities();
            }
        }
        game.setValue(value);
        game.setGiven(given);
        game.setPossible(possible);
        
        List<Map<String, Map<String, String>>> undo = getListFromActions(sg.getUndo());
        List<Map<String, Map<String, String>>> redo = getListFromActions(sg.getRedo());
        game.setUndo(undo);
        game.setRedo(redo);
        
        return game;
    }
    
    private List<Map<String, Map<String, String>>> getListFromActions(Deque<ActionPair> stack) {
        List<Map<String, Map<String, String>>> map = new ArrayList<>();
        Iterator<ActionPair> it = stack.iterator();
        while (it.hasNext()) {
            ActionPair ap = it.next();
            Map<String, Map<String, String>> pair = new HashMap<>();
            Map<String, String> action = getMapFromAction(ap.getAction());
            pair.put("action", action);
            action = getMapFromAction(ap.getReverse());
            pair.put("reverse", action);
            map.add(pair);
        }
        return map;
    }
    
    private Map<String, String> getMapFromAction(Action action) {
        Map<String, String> map = new HashMap<>();
        if (action instanceof SetCellAction) {
            SetCellAction a = (SetCellAction)action;
            map.put("name", "SetCellAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
        } else if (action instanceof SetPossibilityAction) {
            SetPossibilityAction a = (SetPossibilityAction)action;
            map.put("name", "SetPossibilityAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
            map.put("possible", String.valueOf(a.isPossible()));
        } else if (action instanceof FillCellAction) {
            FillCellAction a = (FillCellAction)action;
            map.put("name", "FillCellAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
            map.put("possibles", Arrays.toString(a.getPossibles()).replaceAll("[,\\[\\]]", ""));
        } else if (action instanceof SetHighlightedAction) {
            SetHighlightedAction a = (SetHighlightedAction)action;
            map.put("name", "SetHighlightedAction");
            map.put("targets", Arrays.toString(a.getTargets().toArray()).replaceAll("[,\\[\\]]", ""));
            map.put("isIsHighlighted", String.valueOf(a.isIsHighlighted()));
        }        
        return map;
    }
    
    private SudokuGame createSudokuGame(SaveGame sg) {
        SudokuGame game = new SudokuGame(new Sudoku(sg.getValue(), sg.getGiven(), sg.getPossible()), 
                sg.getHighlighted(), sg.getCurrentTime(), sg.getElapsed(), sg.getName(), 
                sg.getDifficulty(), sg.getStatus(), sg.getScore(), sg.getAnswers(), sg.getErrors());
        game.setUndo(getActionsFromList(sg.getUndo(), game));
        game.setRedo(getActionsFromList(sg.getRedo(), game));
        return game;
    }
    
    private Deque<ActionPair> getActionsFromList(List<Map<String, Map<String, String>>> list, SudokuGame game) {
        Deque<ActionPair> stack = new ArrayDeque<>();
        Iterator<Map<String, Map<String, String>>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Map<String, String>> map = it.next();
            Action action = getActionFromMap(map.get("action"), game);
            Action reverse = getActionFromMap(map.get("reverse"), game);
            stack.add(game.new ActionPair(action, reverse));
        }
        return stack;
    }
    
    private Action getActionFromMap(Map<String, String> map, SudokuGame game) {
        Action a = null;
        String name = map.get("name");
        if (name.equals("SetCellAction")) {
            a = game.new SetCellAction(Integer.parseInt(map.get("targetI")), 
                    Integer.parseInt(map.get("targetJ")), 
                    Integer.parseInt(map.get("value")));
        } else if (name.equals("SetPossibilityAction")) {
            a = game.new SetPossibilityAction(Integer.parseInt(map.get("targetI")), 
                    Integer.parseInt(map.get("targetJ")), 
                    Integer.parseInt(map.get("value")), 
                    Boolean.parseBoolean(map.get("possible")));
        } else if (name.equals("FillCellAction")) {
            String[] bools = map.get("possibles").split(" ");
            boolean[] possibles = new boolean[bools.length];
            for (int i = 0; i < possibles.length; ++i) {
                possibles[i] = Boolean.parseBoolean(bools[i]);
            }
            a = game.new FillCellAction(Integer.parseInt(map.get("targetI")), 
                    Integer.parseInt(map.get("targetJ")), 
                    Integer.parseInt(map.get("value")), possibles);
        } else if (name.equals("SetHighlightedAction")) {
            String[] ints = map.get("targets").split(" ");
            List<Integer> targets = new ArrayList<>(ints.length);
            for (int i = 0; i < ints.length; ++i) {
                targets.add(Integer.parseInt(ints[i]));
            }
            a = game.new SetHighlightedAction(targets, 
                    Boolean.parseBoolean(map.get("isIsHighlighted")));
        }        
        return a;
    }
    
    @Override
    public void saveGame(SudokuGame sudokuGame, Object param) {
        //Staff staff = createDummyObject();

        //1. Convert object to JSON string
        Gson gson = new Gson();
        //String json = gson.toJson(createSaveGame(sudokuGame));
        //System.out.println(json);

        //2. Convert object to JSON string and save into a file directly
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation))) {

            gson.toJson(createSaveGame(sudokuGame), out);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SudokuGame loadGame(Object param) {
        Gson gson = new Gson();
        SudokuGame sudokuGame = null;
        try (BufferedReader in = new BufferedReader(new FileReader(fileLocation));) {

			// Convert JSON to Java Object
            sudokuGame = createSudokuGame(gson.fromJson(in, SaveGame.class));
            //System.out.println(staff);

			// Convert JSON to JsonElement, and later to String
            /*JsonElement json = gson.fromJson(reader, JsonElement.class);
            String jsonInString = gson.toJson(json);
            System.out.println(jsonInString);*/

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sudokuGame;
    }
    
}
