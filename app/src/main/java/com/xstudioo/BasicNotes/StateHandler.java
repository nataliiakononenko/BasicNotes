package com.xstudioo.BasicNotes;

public class StateHandler {
    private int position;
    public int getPosition() {return position;}
    public void savePosition(int position) {this.position = position;}

    private static final StateHandler holder = new StateHandler();
    public static StateHandler getInstance() {return holder;}
}
