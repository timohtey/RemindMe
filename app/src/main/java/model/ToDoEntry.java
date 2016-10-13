package model;

/**
 * Created by timohtey on 13/10/2016.
 *
 * This class serves as the model for the To Do Entry.
 */
public class ToDoEntry {
    private int toDoEntryId;
    private String toDoEntryName;
    private int toDoEntryState;

    public ToDoEntry(String toDoEntryName, int toDoEntryState){
        this.toDoEntryName = toDoEntryName;
        this.toDoEntryState = toDoEntryState;
    }

    public int getToDoEntryId() {
        return toDoEntryId;
    }

    public void setToDoEntryId(int toDoEntryId) {
        this.toDoEntryId = toDoEntryId;
    }

    public String getToDoEntryName() {
        return toDoEntryName;
    }

    public void setToDoEntryName(String toDoEntryDesc) {
        this.toDoEntryName = toDoEntryDesc;
    }

    public int getToDoEntryState() {
        return toDoEntryState;
    }

    public void setToDoEntryState(int toDoEntryState) {
        this.toDoEntryState = toDoEntryState;
    }
}