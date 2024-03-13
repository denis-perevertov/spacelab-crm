package com.example.spacelab.service.impl;

import com.example.spacelab.model.task.Task;
import com.example.spacelab.service.PDFService;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.TranslationService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

    private final TranslationService translationService;
    private final ResourceLoader resourceLoader;

    private final static String REGULAR_FONT_PATH = "fonts/arial.ttf";
    private final static String BOLD_FONT_PATH = "fonts/arialbd.ttf";

    @Override
    public File generatePDF(Task task, Locale locale) throws IOException {

        String filename = String.format("%s_%s.pdf", FilenameUtils.trimNameString(task.getName()), locale.getLanguage());

        try(PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument)) {
            document.add(generateHeader(task.getName(), 22.0f));
            document.add(new LineSeparator(new SolidLine()).setMarginBottom(15.0f));
            document.add(generateTaskSecondaryInfoTable(task, locale));
            document.add(new LineSeparator(new SolidLine()).setMarginTop(15.0f));
            document.add(generateHeader(translationService.getMessage("task.description", locale), 16.0f));
            document.add(generateTaskDescription(task));
        }

        return new File(filename);
    }

    private Paragraph generateHeader(String content, float fontSize) throws IOException {
        PdfFont boldFont = getBoldFont();
        Text text = new Text(content);
        text.setFont(boldFont);
        text.setFontSize(fontSize);
        text.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph paragraph = new Paragraph(text);
        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        return paragraph;
    }

    private Table generateTaskSecondaryInfoTable(Task task, Locale locale) throws IOException {
        PdfFont regularFont = getRegularFont();
        PdfFont boldFont = getBoldFont();

        Table table = new Table(2);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setMaxWidth(UnitValue.createPercentValue(100.0f));

//        PdfPTable table = new PdfPTable(2);
        if(task.getParentTask() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.parent-task", locale), boldFont, HorizontalAlignment.CENTER));
            table.addCell(createTableCell(task.getParentTask().getName(), regularFont, HorizontalAlignment.CENTER));
        }
        if(task.getCourse() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.course", locale), boldFont, HorizontalAlignment.CENTER));
            table.addCell(createTableCell(task.getCourse().getName(), regularFont, HorizontalAlignment.CENTER));
        }
        if(task.getLevel() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.level", locale), boldFont, HorizontalAlignment.CENTER));
            table.addCell(createTableCell(translationService.getMessage(task.getLevel().name(), locale), regularFont, HorizontalAlignment.CENTER));
        }
        if(task.getStatus() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.status", locale), boldFont, HorizontalAlignment.CENTER));
            table.addCell(createTableCell(translationService.getMessage(task.getStatus().name(), locale), regularFont, HorizontalAlignment.CENTER));
        }
        if(task.getCompletionTime() != null) {
            table.addCell(createTableCell(translationService.getMessage("task.completion-time", locale), boldFont, HorizontalAlignment.CENTER));
            table.addCell(createTableCell(task.getCompletionTime().getValue() + " " + translationService.getMessage(task.getCompletionTime().getTimeUnit().name(), locale), regularFont, HorizontalAlignment.CENTER));
        }
        table.addCell(createTableCell(translationService.getMessage("task.skills-description", locale), boldFont, HorizontalAlignment.CENTER));
        table.addCell(createTableCell(task.getSkillsDescription(), regularFont, HorizontalAlignment.CENTER));
        return table;
    }

    private Paragraph generateTaskDescription(Task task) {
        Paragraph p3 = new Paragraph(removeHTMLTags(task.getTaskDescription()));
        return p3;
    }

    private Cell createTableCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text));
        return cell;
    }

    private Cell createTableCell(String text, HorizontalAlignment alignment) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text));
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private Cell createTableCell(String text, PdfFont font, HorizontalAlignment alignment) {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font);
        paragraph.setHorizontalAlignment(alignment);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        cell.setPaddingLeft(10.0f).setPaddingRight(10.0f);
        cell.add(paragraph);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private PdfFont getRegularFont() throws IOException {
        return PdfFontFactory.createFont(
                IOUtils.toByteArray(
                        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(REGULAR_FONT_PATH))
                ),
                "cp1251",
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED,
                true
        );
    }

    private PdfFont getBoldFont() throws IOException {
        return PdfFontFactory.createFont(
                IOUtils.toByteArray(Objects.requireNonNull(
                                this.getClass().getClassLoader().getResourceAsStream(BOLD_FONT_PATH))
                        ),
                "cp1251",
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED,
                true
        );
    }

    private String removeHTMLTags(String string) {
        return string.replaceAll("<[ '\"=:\\-/\\w]+>", "");
    }
}
