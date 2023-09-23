package com.example.spacelab.job;

import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.service.LessonService;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Log
@Data
public class LessonMonitor {

    @Autowired private LessonService lessonService;
    private List<Lesson> lessons = new ArrayList<>();
    private List<Thread> activeThreads = new ArrayList<>();
    private int threadsAmount = 10;

    public void start() {

        // 1. Получить занятие из списка
        // 2. Взять его дату-время
        // 3. если это текущий момент - стартануть занятие
        // 4. После прохождения по списку подождать 1 минуту
        // 5. Повторить

//        log.info("Starting monitor for automatic lesson start!");

        if(lessons != null) {
            Runnable runnable = () -> {
                while(true) {
//                    log.info("Checking lessons to start...");
//                    log.info(lessons.toString());
                    Iterator<Lesson> iterator = lessons.iterator();
                    while(iterator.hasNext()) {
                        Lesson lesson = iterator.next();
                        if(lesson.getDatetime().isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                        {
                            log.info("Automatic start of lesson(ID: " + lesson.getId() + ")");
                            try {
                                lessonService.startLesson(lesson.getId());
//                                lessons.remove(lesson);
                                iterator.remove();
                            } catch (Exception e) {
                                log.warning("Encountered some error with automatic start, not stopping the monitor");
                            }
                        }
                    }
                    try {
                        Thread.sleep(15 * 1000);
                    } catch (InterruptedException ignored) {
                        log.warning("Monitor thread interrupted");
                        break;
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
            activeThreads.add(thread);
        }
        else log.severe("Can't start lessons monitor - lesson list is null!");
    }

    public void stop() {
        log.info("Stopping lessons monitor now");
        if(lessons != null) {
            for(Thread thread : activeThreads) thread.interrupt();
        }
        else log.severe("Can't stop lessons monitor - lesson list is null!");
    }

    public boolean isMonitored(Lesson lesson) {
        return lessons.stream().anyMatch(l -> Objects.equals(l.getId(), lesson.getId()));
    }

    public void removeFromMonitor(Lesson lesson) {
        Lesson lessonToRemove = lessons.stream().filter(l -> Objects.equals(l.getId(), lesson.getId())).findFirst().orElseThrow();
        lessons.remove(lessonToRemove);
    }

    public void addToMonitor(Lesson lesson) {
        lessons.add(lesson);
    }
}
