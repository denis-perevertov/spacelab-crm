package com.example.spacelab.job;

import com.example.spacelab.model.lesson.Lesson;
import lombok.Data;

import java.util.List;

@Data
class LessonMonitorThread extends Thread {

    private List<Lesson> lessons;

    public LessonMonitorThread() {
    }

    public LessonMonitorThread(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
