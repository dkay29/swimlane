package com.dkay229.swimlane.controller;

import com.dkay229.swimlane.model.SwimlaneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SwimlaneController {
    private static final Logger logger = LoggerFactory.getLogger(SwimlaneController.class);

    AtomicInteger seq = new AtomicInteger();
    String colors[] = {"red", "yellow", "darkBlue", "brown"};

    private String nextColor() {
        return colors[seq.incrementAndGet() % colors.length];
    }

    static int[] distConcurrentReqs = {1, 1, 1, 3, 3,  4, 1, 11, 1, 1, 8};
    static int[] distTimeBetweenReqs = {1, 2, 4, 5, 5, 9, 14, 6, 9, 6, 2};
    static int[] distQuerySecs = {0,0,0,1, 2, 3, 5,5656 ,1700, 1000, 3, 30, 40, 40, 40, 40, 139,34,842, 234, 123, 433, 633, 34, 6, 4,5,34,64,64,64,64,64,64,64,9, 34, 65, 23};

    private int drawFromDist(int[] d) {
        int l=d.length;
        int r=Math.abs(random.nextInt());
        int i= r%l;
        return d[i];
    }

    Random random = new Random();

    private int minsReqWait() {
        return drawFromDist(distTimeBetweenReqs);
    }
    private int secsReqWait() {
        int mins=drawFromDist(distTimeBetweenReqs);
        return mins + randomBetween(0,88);
    }


    private int randomBetween(int a, int b) {
        return a+Math.abs(random.nextInt())%(b-a);
    }
    private int numConcurrent() {
        return drawFromDist(distConcurrentReqs);
    }

    private int querySecs() {
        int secs=drawFromDist(distQuerySecs)+randomBetween(2,200);
        return secs;
    }

    private Date add(Date t, int plusSecs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(t);
        calendar.add(Calendar.SECOND, plusSecs);
        return calendar.getTime();
    }


    @GetMapping("/wholeDay")
    public List<SwimlaneEvent> getWholeDay() {
        List<SwimlaneEvent> swimlaneEvents = new ArrayList<>();
        Date t0 = new Date(2024, 9, 1, 0, 0);
        Date t1 = new Date(2024, 9, 1, 12, 15);
        Date t = t0;
        List<SwimlaneEvent> laneTails = new ArrayList<>();
        AtomicInteger taskNumber = new AtomicInteger();
        while (t.before(t1)) {
            int minsBetween=minsReqWait();
            t = add(t, secsReqWait());
            List<SwimlaneEvent> batch = new ArrayList<>();
            int reqsInBatch = numConcurrent();
            for (int i = 0; i < reqsInBatch; i++) {
                SwimlaneEvent swimlaneEvent = new SwimlaneEvent("Task " + taskNumber.incrementAndGet(), null, t, add(t, querySecs()), "green");
                swimlaneEvents.add(swimlaneEvent);
                for (int laneNum = 0; laneNum < laneTails.size(); laneNum++) {
                    if (laneTails.get(laneNum).getEnd().before(swimlaneEvent.getStart())) {
                        swimlaneEvent.setLane("" + (laneNum + 1));
                        laneTails.set(laneNum, swimlaneEvent);
                        break;
                    }
                }
                if (swimlaneEvent.getLane() == null) {
                    swimlaneEvent.setLane("" + (laneTails.size() + 1));
                    laneTails.add(swimlaneEvent);
                }
            }
        }
        logger.info("Returning "+swimlaneEvents.size()+" events");
        return swimlaneEvents;
    }

    @GetMapping("/swimlaneData")
    public List<SwimlaneEvent> getSwimlaneData() {
        // Mocked data for swimlane tasks
        logger.info("swimlaneData");
        List<SwimlaneEvent> swimlaneEvents = new ArrayList<>();
        swimlaneEvents.add(new SwimlaneEvent("Task A", "Team 1", new Date(2024, 9, 1, 10, 0), new Date(2024, 9, 1, 10, 30), nextColor()));
        swimlaneEvents.add(new SwimlaneEvent("Task B", "Team 1", new Date(2024, 9, 1, 10, 45), new Date(2024, 9, 1, 11, 15), nextColor()));
        swimlaneEvents.add(new SwimlaneEvent("Task C", "Team 2", new Date(2024, 9, 1, 10, 10), new Date(2024, 9, 1, 11, 0), nextColor()));
        swimlaneEvents.add(new SwimlaneEvent("Task D", "Team 2", new Date(2024, 9, 1, 11, 5), new Date(2024, 9, 1, 11, 45), nextColor()));
        swimlaneEvents.add(new SwimlaneEvent("Task E", "Team 3", new Date(2024, 9, 1, 10, 20), new Date(2024, 9, 1, 11, 10), nextColor()));

        return swimlaneEvents;
    }
}
