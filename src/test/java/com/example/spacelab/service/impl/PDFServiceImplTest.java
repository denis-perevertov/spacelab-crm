package com.example.spacelab.service.impl;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.task.CompletionTime;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.service.PDFService;
import com.example.spacelab.util.TimeUnit;
import com.example.spacelab.util.TranslationService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PDFServiceImplTest {

    @Autowired
    private PDFService pdfService;

    @Test
    void contextLoads() {
        assertThat(pdfService).isNotNull();
    }

    @Test
    void generatePDFTest() throws DocumentException, IOException, URISyntaxException {
        Task task = new Task();
        task.setName("TestTasknaфывфывфыввфывфыфывфывфвыфвфывфывывфывфыфывфыфвыфвфвфыфывыфвфвывфывme");
        task.setCourse(new Course().setName("courseName"));
        task.setParentTask(new Task().setName("ParentTфывфывфывфывфывфыывфывфыввфвфывфывфывфывфывфыфвфывфвфывфывфывывфaskName"));
        task.setLevel(TaskLevel.ADVANCED);
        task.setStatus(TaskStatus.INACTIVE);
        task.setSkillsDescription("Skills description epta");
        task.setCompletionTime(new CompletionTime().setValue("6-8").setTimeUnit(TimeUnit.DAYS));
        task.setTaskDescription("<div class='lololol'><p>SpamSpamSpamSpam<b>Spam</b>Spam<i style='font-weight: bold'>Spam</i>Spam</p></div>");
        pdfService.generatePDF(task, TranslationService.UA);
        pdfService.generatePDF(task, TranslationService.RU);
        pdfService.generatePDF(task, TranslationService.EN);
    }
}
