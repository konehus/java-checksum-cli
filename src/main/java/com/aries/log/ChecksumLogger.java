package com.aries.log;

import com.aries.utility.ByteSizeFormatter;
import com.aries.utility.StringUtility;
import com.aries.utility.TextColoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ChecksumLogger {

    private static final LogFileWriter logFileWriter = new LogFileWriter("../log.txt");

    public static void log(Path path, String algorithm, int checksum, long blockSize, long totalBytes, String fileType, double elapsedSeconds) throws IOException {
        String fileSize = ByteSizeFormatter.humanReadableByteCountSI(totalBytes);
        Instant end = Instant.now();
        String gitRevision = getGitRevision();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String header = StringUtility.formatWithLocale("%-24s %-20s %-10s %-20s %-11s %-10s %-12s %-42s %s%n",
                "Timestamp", "File", "Size", "Type", "Algorithm", "Checksum", "BlockSize", "GitRevision", "ElapsedTime");
        String log = StringUtility.formatWithLocale("%-24s %-20s %-10s %-20s %-11s %-10s %-12s %-42s %s%n",
                TextColoring.cyan(String.format("%-24s", formatter.format(end.atZone(ZoneId.of("America/New_York"))))),
                TextColoring.green(String.format("%-20s", path.getFileName())),
                TextColoring.yellow(String.format("%-10s", fileSize)),
                TextColoring.purple(String.format("%-20s", fileType)),
                TextColoring.blue(String.format("%-11s", algorithm)),
                TextColoring.red(String.format("%-10d", checksum)),
                TextColoring.red(String.format("%-12d", blockSize)),
                TextColoring.white(String.format("%-42s", gitRevision)),
                TextColoring.white(String.format("%.2f seconds", elapsedSeconds))
        );

        if (logFileWriter.isEmpty()) {
            logFileWriter.write(header);
        }
        logFileWriter.write(log);
    }

    private static String getGitRevision() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "rev-parse", "HEAD");
            Process process = processBuilder.start();
            process.waitFor();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            return "unknown";
        }
    }
}
