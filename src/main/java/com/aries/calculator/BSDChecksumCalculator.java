package com.aries.calculator;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

public class BSDChecksumCalculator extends AbstractChecksumCalculator {

    private Path path = Paths.get("");

    @Override
    public String calculateChecksumAndBlockSize(String file) throws IOException {
        path  = path.resolve(file);
        int checksum = 0;
        long totalBytes = 0;
        int ch;
        Instant start = Instant.now();

        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            while ((ch = in.read()) != -1) {
                totalBytes++;
                checksum = (checksum >> 1) + ((checksum & 1) << 15);
                checksum += ch;
                checksum &= 0xffff;
            }
        }

        long blockSize = (totalBytes + 1023) / 1024;
        Instant end = Instant.now();
        double elapsedSeconds = Duration.between(start, end).toMillis() / 1000.0;

        // Write log to file
        String fileSize = humanReadableByteCountSI(totalBytes);
        String fileType = Files.probeContentType(path);
        String log = String.format(Locale.ROOT, "\033[1;34m[%s]\033[0m File: \033[1;32m%-20s\033[0m | Size: \033[1;33m%8s\033[0m | Type: \033[1;35m%-20s\033[0m | Algorithm: \033[1;36m%-7s\033[0m | Checksum: \033[1;31m%05d\033[0m | Block Size: \033[1;31m%5d\033[0m | Elapsed Time: \033[1;37m%.2f seconds\033[0m%n",
                end, path.getFileName(), fileSize, fileType, "BSD", checksum, blockSize, elapsedSeconds);

        BufferedWriter writer = new BufferedWriter(new FileWriter("../log.txt", true));
        writer.write(log);
        writer.close();

        return String.format(Locale.ROOT, "%05d %5d ", checksum, blockSize) + file;
    }

    // Utility method for getting human-readable file sizes
    private String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String[] units = {"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        return df.format(bytes / Math.pow(1000, exp)) + " " + units[exp];
    }
}