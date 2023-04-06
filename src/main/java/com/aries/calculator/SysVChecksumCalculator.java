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

public class SysVChecksumCalculator extends AbstractChecksumCalculator {

    private Path path = Paths.get("");

    @Override
    public String calculateChecksumAndBlockSize(String file) throws IOException {
        path = path.resolve(file);
        int s = 0;
        long totalBytes = 0;
        byte[] buffer = new byte[8192];
        Instant start = Instant.now();

        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    s += buffer[i] & 0xff;
                }
                totalBytes += bytesRead;
            }
        }

        int r = (s & 0xffff) + (s >> 16);
        int checksum = (r & 0xffff) + (r >> 16);
        long blockSize = (totalBytes + 511) / 512;
        Instant end = Instant.now();
        double elapsedSeconds = Duration.between(start, end).toMillis() / 1000.0;

        // Write log to file
        String fileSize = humanReadableByteCountSI(totalBytes);
        String fileType = Files.probeContentType(path);
        String log = String.format(Locale.ROOT, "\033[1;34m[%s]\033[0m File: \033[1;32m%-20s\033[0m | Size: \033[1;33m%8s\033[0m | Type: \033[1;35m%-20s\033[0m | Algorithm: \033[1;36m%-7s\033[0m | Checksum: \033[1;31m%d\033[0m | Block Size: \033[1;31m%5d\033[0m | Elapsed Time: \033[1;37m%.2f seconds\033[0m%n",
                end, path.getFileName(), fileSize, fileType, "SysV", checksum, blockSize, elapsedSeconds);

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
