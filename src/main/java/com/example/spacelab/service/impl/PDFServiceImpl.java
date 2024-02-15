package com.example.spacelab.service.impl;

import com.example.spacelab.model.task.Task;
import com.example.spacelab.service.PDFService;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.TranslationService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

    private final TranslationService translationService;
    private final ResourceLoader resourceLoader;

    @Override
    public File generatePDF(Task task, Locale locale) throws IOException, DocumentException, URISyntaxException {
        try {
            FontFactory.register(resourceLoader.getResource("classpath:/fonts/arial.ttf").getURL().getPath(), "my_font");
            FontFactory.register(resourceLoader.getResource("classpath:/fonts/arialbd.ttf").getURL().getPath(), "my_font_bold");
        } catch (Exception e) {
            log.error("could not register pdf fonts");
            log.error(e.getMessage());
        }


        Document document = new Document();
        File file = new File(String.format("%s_%s.pdf", FilenameUtils.trimNameString(task.getName()), locale.getLanguage()));
        PdfWriter.getInstance(document, new FileOutputStream(file));

        document.open();

        document.add(generateTaskHeader(task));

        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);

        document.add(generateTaskSecondaryInfoTable(task, locale));

        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        document.add(generateTaskDescription(task));

        document.close();
        return file;
    }

    private Paragraph generateTaskHeader(Task task) {
        Font font = FontFactory.getFont("my_font_bold", "Cp1251", true, 22);
//        Font font = FontFactory.getFont(FontFactory.TIMES, "Cp1251", true, 22);
        Paragraph paragraph = new Paragraph(new Chunk(task.getName(), font));
        paragraph.setAlignment(1);
        return paragraph;
    }

    private PdfPTable generateTaskSecondaryInfoTable(Task task, Locale locale) throws IOException, DocumentException {
        Font font = FontFactory.getFont("my_font", "Cp1251", true);
        Font boldFont = FontFactory.getFont("my_font_bold", "Cp1251", true);
//        Font font = FontFactory.getFont(FontFactory.TIMES, "Cp1251", true);
//        Font boldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, "Cp1251", true);

        PdfPTable table = new PdfPTable(2);
        if(task.getParentTask() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.parent-task", locale), boldFont, Element.ALIGN_CENTER));
            table.addCell(createTableCell(task.getParentTask().getName(), font, Element.ALIGN_CENTER));
        }
        if(task.getCourse() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.course", locale), boldFont, Element.ALIGN_CENTER));
            table.addCell(createTableCell(task.getCourse().getName(), font, Element.ALIGN_CENTER));
        }
        if(task.getLevel() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.level", locale), boldFont, Element.ALIGN_CENTER));
            table.addCell(createTableCell(translationService.getMessage(task.getLevel().name(), locale), font, Element.ALIGN_CENTER));
        }
        if(task.getStatus() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.status", locale), boldFont, Element.ALIGN_CENTER));
            table.addCell(createTableCell(translationService.getMessage(task.getStatus().name(), locale), font, Element.ALIGN_CENTER));
        }
        if(task.getCompletionTime() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.completion-time", locale), boldFont, Element.ALIGN_CENTER));
            table.addCell(createTableCell(task.getCompletionTime().getValue() + " " + translationService.getMessage(task.getCompletionTime().getTimeUnit().name(), locale), font, Element.ALIGN_CENTER));
        }
        table.addCell(createTableCell(translationService.getMessage("task.skills-description", locale), boldFont, Element.ALIGN_CENTER));
        table.addCell(createTableCell(task.getSkillsDescription(), font, Element.ALIGN_CENTER));
        return table;
    }

    private Paragraph generateTaskDescription(Task task) {
        Paragraph p3 = new Paragraph(removeHTMLTags(task.getTaskDescription()));
        p3.setIndentationLeft(5.0f);
        return p3;
    }

    private PdfPCell createTableCell(String text) {
        return new PdfPCell(new Phrase(text));
    }

    private PdfPCell createTableCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private PdfPCell createTableCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private String removeHTMLTags(String string) {
        return string.replaceAll("<[ '\"=:\\-/\\w]+>", "");
    }
}
