/*
 * Checksum CLI - Calculates the checksum and block count for the provided files
 * using either the BSD or System V algorithm, depending on the given option.
 *
 * Copyright (C) 2023 Aries
 *
 * Copy of the GNU General Public License, see <https://www.gnu.org/licenses/>.
 */

package com.aries;

import com.aries.calculator.BSDChecksumCalculator;
import com.aries.calculator.ChecksumCalculator;
import com.aries.calculator.SysVChecksumCalculator;

import java.io.IOException;

public class CheckSumCLI {

    private static final String USAGE = "Usage: java ChecksumCLI [OPTION]... [FILE]...\n" +
            "  -r      use BSD sum algorithm, use 1K blocks\n" +
            "  -s, --sysv  use System V sum algorithm, use 512 bytes blocks\n";

    public static void main(String[] args) {
        ChecksumCalculator calculator;
        boolean useSysV = false;

        if (args.length == 0) {
            System.err.println(USAGE);
            System.exit(1);
        }

        int fileArgStartIndex = 0;

        label:
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-s":
                case "--sysv":
                    useSysV = true;
                    fileArgStartIndex = i + 1;
                    break;
                case "-r":
                case "--bsd":
                    useSysV = false;
                    fileArgStartIndex = i + 1;
                    break;
                case "--revision":
                    System.out.print("Git Revision: ");
                    break;
                default:
                    break label;
            }
        }

        calculator = useSysV ? new SysVChecksumCalculator() : new BSDChecksumCalculator();

        try {
            for (int i = fileArgStartIndex; i < args.length; i++) {
                String file = args[i];
                System.out.println(calculator.calculateChecksumAndBlockSize(file));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
