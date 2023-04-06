# Checksum CLI
This command-line interface (CLI) application calculates the checksum and block count for the provided files using either the BSD or System V algorithm, depending on the given option. The program is inspired by the Linux `coreutils` package and is built using Java.
## Installation
This program requires Java 1.8 or higher and Maven to be installed.
To install the program, run the following commands:
```
$ ./install.sh
```
This will build the program and install it on your system. The `install.sh` script will check if Java and Maven are installed, and prompt you to install them if necessary.
After installation, you can run the program using the `jsum` command.
## Usage
The basic syntax of the `jsum` command is:
```
$ jsum [OPTIONS]... [FILES]...
```
### Options
The following options are available:
- `-r` or `--bsd`: Use the BSD sum algorithm and use 1K blocks (default option)
- `-s` or `--sysv`: Use the System V sum algorithm and use 512 bytes blocks
### Examples
Calculate the checksum and block count of a single file:
```
$ jsum file.txt
```
Calculate the checksum and block count of multiple files:
```
$ jsum file1.txt file2.txt file3.txt
```
Calculate the checksum and block count of a file using the System V algorithm:
```
$ jsum -s file.txt
```
## Uninstallation
To uninstall the program, run the following command:
```
$ ./uninstall.sh
```
This will remove the program from your system and update the PATH.
## Credits
This program was developed by Aries in 2023.
