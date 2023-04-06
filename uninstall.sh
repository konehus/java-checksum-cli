#!/bin/bash

# Remove the target directory
mvn clean

# Locate the JAR file after building
jar_file=$(find target -name "*.jar" -type f 2> /dev/null)

# Remove the JAR file from the bin directory
bin_dir="$HOME/bin"
if [ -f "$bin_dir/$(basename "$jar_file")" ]; then
    rm "$bin_dir/$(basename "$jar_file")"
fi

# Remove the created script
if [ -f "$bin_dir/jsum" ]; then
    rm "$bin_dir/jsum"
fi

# Remove the added "export PATH" from the .bashrc or .zshrc file
shell_config="$HOME/.bashrc"
if [ -n "$ZSH_VERSION" ]; then
    shell_config="$HOME/.zshrc"
fi

# Remove the PATH addition
grep -v "export PATH=\"$bin_dir:\$PATH\"" "$shell_config" > "${shell_config}.tmp" && mv "${shell_config}.tmp" "$shell_config"

# Reload the shell configuration
eval "source $shell_config"

echo "Uninstallation complete. The target package, created directories, JAR file, and script have been removed. The PATH has been updated."
