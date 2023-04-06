package com.aries.log;

import com.aries.utility.ChecksumUtility;

import java.io.*;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;

public class ChecksumLogger {

    public static void log(Path path, String algorithm, int checksum, long blockSize, long totalBytes, String fileType, double elapsedSeconds) throws IOException {
        String fileSize = ChecksumUtility.humanReadableByteCountSI(totalBytes);
        Instant end = Instant.now();
        String gitRevision = getGitRevision();

        String header = String.format(Locale.ROOT, "%-24s %-20s %-10s %-20s %-11s %-10s %-12s %-42s %s%n", "Timestamp", "File", "Size", "Type", "Algorithm", "Checksum", "BlockSize", "GitRevision", "ElapsedTime");
        String log = String.format(Locale.ROOT, "\033[1;34m%-24s\033[0m \033[1;32m%-20s\033[0m \033[1;33m%-10s\033[0m \033[1;35m%-20s\033[0m \033[1;36m%-11s\033[0m \033[1;31m%-10d\033[0m \033[1;31m%-12d\033[0m \033[1;37m%-42s\033[0m \033[1;37m%.2f seconds\033[0m%n",
                end, path.getFileName(), fileSize, fileType, algorithm, checksum, blockSize, gitRevision, elapsedSeconds);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../log.txt", true))) {
            if (isNewLogFile()) {
                writer.write(header);
            }
            writer.write(log);
        }
    }

    private static boolean isNewLogFile() {
        File logFile = new File("../log.txt");
        return logFile.length() == 0;
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
