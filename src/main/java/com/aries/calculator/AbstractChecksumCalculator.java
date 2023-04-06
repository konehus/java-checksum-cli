package com.aries.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractChecksumCalculator implements ChecksumCalculator {
    protected long getFileSize(Path path) throws IOException {
        return Files.size(path);
    }
}