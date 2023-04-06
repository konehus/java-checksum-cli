package com.aries.calculator;

import com.aries.log.ChecksumLogger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
public abstract class AbstractChecksumCalculator implements ChecksumCalculator {
    private Path path = Paths.get("");

    @Override
    public String calculateChecksumAndBlockSize(String file) throws IOException {
        path = path.resolve(file);
        int checksum;
        long totalBytes;
        long blockSize;
        Instant start = Instant.now();

        // Calculate checksum and total bytes
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            ChecksumResult result = calculateChecksumAndTotalBytes(in);
            checksum = result.getChecksum();
            totalBytes = result.getTotalBytes();
        }

        blockSize = calculateBlockSize(totalBytes);
        Instant end = Instant.now();
        double elapsedSeconds = Duration.between(start, end).toMillis() / 1000.0;

        // Log the results
        String fileType = Files.probeContentType(path);
        ChecksumLogger.log(path, getAlgorithmName(), checksum, blockSize, totalBytes, fileType, elapsedSeconds);

        return String.format(Locale.ROOT, "%05d %5d ", checksum, blockSize) + file;
    }

    private long calculateBlockSize(long totalBytes) {
        return (totalBytes + getBlockSizeAdjustment() - 1) / getBlockSizeAdjustment();
    }

    protected abstract String getAlgorithmName();

    protected abstract ChecksumResult calculateChecksumAndTotalBytes(InputStream in) throws IOException;

    protected abstract int getBlockSizeAdjustment();

}