package com.dkay229.swimlane.model;

import java.util.Date;

public class SwimlaneTask {
    private String name;
    private String lane;
    private Date start;
    private Date end;
    private String color; // Add color attribute

    // Constructors
    public SwimlaneTask(String name, String lane, Date start, Date end, String color) {
        this.name = name;
        this.lane = lane;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLane() { return lane; }
    public void setLane(String lane) { this.lane = lane; }

    public Date getStart() { return start; }
    public void setStart(Date start) { this.start = start; }

    public Date getEnd() { return end; }
    public void setEnd(Date end) { this.end = end; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return "SwimlaneTask{" +
                "name='" + name + '\'' +
                ", lane='" + lane + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", color='" + color + '\'' +
                '}';
    }
}

