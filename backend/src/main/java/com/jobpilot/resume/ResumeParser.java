package com.jobpilot.resume;

import com.jobpilot.common.error.BusinessException;
import java.io.InputStream;
import java.util.Locale;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ResumeParser {
    private final Tika tika = new Tika();

    public ParsedResume parse(MultipartFile file) {
        String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String normalized = originalFilename.toLowerCase(Locale.ROOT);
        String fileType = detectSupportedType(normalized);
        try (InputStream inputStream = file.getInputStream()) {
            String content = tika.parseToString(inputStream).trim();
            if (content.isBlank()) {
                throw unsupported("Unable to extract text from this resume. Scanned PDFs and image resumes are not supported.");
            }
            return new ParsedResume(fileType, content);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw unsupported("Unable to parse this resume. Please upload a text PDF or DOCX file.");
        }
    }

    private String detectSupportedType(String filename) {
        if (filename.endsWith(".pdf")) {
            return "pdf";
        }
        if (filename.endsWith(".docx")) {
            return "docx";
        }
        throw unsupported("Unsupported resume file. Only text PDF and DOCX files are supported.");
    }

    private BusinessException unsupported(String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message);
    }

    public static class ParsedResume {
        private final String fileType;
        private final String content;

        public ParsedResume(String fileType, String content) {
            this.fileType = fileType;
            this.content = content;
        }

        public String getFileType() {
            return fileType;
        }

        public String getContent() {
            return content;
        }
    }
}
