package com.dkay229.swimlane.model;

import java.util.Date;

public class SwimlaneEvent {
    private String name;
    private String lane;
    private Date start;
    private Date end;
    private String color; // Add color attribute
    Object[][] elapsedSecThresholdToColorMap = {
            { 120, "darkblue"},
            { 180, "green"},
            { 300, "yellow"},
            { 900, "brown"},
            { 1800, "red"},
            { 3600, "purple"},
    };

    // Constructors
    public SwimlaneEvent(String name, String lane, Date start, Date end, String color) {
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

    public String getColor() {
        String color="black";
        for (int i=elapsedSecThresholdToColorMap.length-1;i>0;i--) {
            color=(String)elapsedSecThresholdToColorMap[i][1];
            if (getElapsedSecs()>(Integer)(elapsedSecThresholdToColorMap[i][0])) {
                break;
            }
        }
        return color;
    }
    public void setColor(String color) { this.color = color; }

    public int getElapsedSecs() {
        return (int)((end.getTime()-start.getTime())/1000L);
    }

    @Override
    public String toString() {
        return "SwimlaneTask{" +
                "name='" + name + '\'' +
                ", lane='" + lane + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", secs="+getElapsedSecs()+
                ", color='" + getColor() + '\'' +
                '}';
    }
}

