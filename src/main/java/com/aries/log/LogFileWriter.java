package com.aries.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFileWriter {

    private final String filename;

    public LogFileWriter(String filename) {
        this.filename = filename;
    }

    public void write(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
        }
    }

    public boolean isEmpty() {
        return new File(filename).length() == 0;
    }
}
