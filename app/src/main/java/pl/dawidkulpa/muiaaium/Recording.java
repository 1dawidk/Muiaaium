package pl.dawidkulpa.muiaaium;

import java.util.ArrayList;

public class Recording {
    private String name;
    private long length;    //in milliseconds

    public Recording(String name, long length){
        this.name= name;
        this.length= length;
    }

    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }

    public static void loadFromFile(String file, ArrayList<Recording> list){
        list.add(new Recording("Test1", 150000));
        list.add(new Recording("Test2", 150000));
        list.add(new Recording("Test3", 150000));
        list.add(new Recording("Test4", 150050));
        list.add(new Recording("Test5", 150200));
        list.add(new Recording("Test6", 150055));
        list.add(new Recording("Test7", 158000));
        list.add(new Recording("Test8", 150900));
        list.add(new Recording("Test9", 157200));
        list.add(new Recording("Test10", 245634));
        list.add(new Recording("Test11", 700000));
        list.add(new Recording("Test12", 50000));
        list.add(new Recording("Test13", 150000));
    }
}
