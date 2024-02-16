package com.example.spacelab.service;

import com.example.spacelab.model.task.Task;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Hidden;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
@Hidden
public interface PDFService {

    File generatePDF(Task task, Locale locale) throws IOException, DocumentException, URISyntaxException;

}
