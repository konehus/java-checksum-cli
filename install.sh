#!/bin/bash



# Check if Java is installed and if the version is above 1.8
if ! command -v java >/dev/null 2>&1; then
    echo "Java is not installed. Please install Java."
    exit 1
else
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    major_version=$(echo "$java_version" | cut -d '.' -f 1)
    minor_version=$(echo "$java_version" | cut -d '.' -f 2)
    patch_version=$(echo "$java_version" | cut -d '.' -f 3)

    if [[ "$major_version" -eq 1 && "$minor_version" -le 8 ]]; then
        echo "Java version higher than 1.8 is required. Please update Java."
        exit 1
    else
        echo "Java $major_version.$minor_version.$patch_version is already installed."
    fi

fi

# Check if Maven is installed
if ! command -v mvn >/dev/null 2>&1; then
    echo "Maven is not installed."

    # Prompt user to install Maven
    read -pr "Do you want to download and install Maven? [Y/n]: " user_choice

    if [[ "$user_choice" =~ ^[Yy]$ ]]; then
        echo "Downloading and installing Maven..."
        curl -sL https://downloads.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz | tar xz
        export PATH=$PWD/apache-maven-3.8.4/bin:$PATH
        echo "Maven has been installed for this session."
    else
        echo "Maven was not installed. Please install Maven to continue."
        exit 1
    fi
else
    echo "Maven is already installed."
fi


# Build the project using Maven
mvn clean package

# Locate the JAR file after building
jar_file=$(find target -name "*.jar" -type f)

# Create the bin directory in the user's home directory if it doesn't exist
bin_dir="$HOME/bin"
mkdir -p "$bin_dir"

# Copy the JAR file to the bin directory
cp "$jar_file" "$bin_dir"

# Create a new bash file for execution
cat > "$bin_dir/jsum" <<EOF
#!/bin/bash

# Run the JAR file with the provided arguments
java -jar "$bin_dir/$(basename "$jar_file")" "\$@"
EOF

# Make the generated script executable
chmod +x "$bin_dir/jsum"

# Add the bin directory to the PATH
export PATH="$bin_dir:$PATH"


# Add the bin directory to the PATH in the .bashrc or .zshrc file
shell_config="$HOME/.bashrc"
if [ -n "$ZSH_VERSION" ]; then
    shell_config="$HOME/.zshrc"
fi

# Check if the bin directory is already in the PATH
if ! grep -q "$bin_dir" "$shell_config"; then
    echo "export PATH=\"$bin_dir:\$PATH\"" >> "$shell_config"
    echo "The 'bin' directory was added to the PATH in $shell_config."
fi

# Reload the shell configuration
eval "source $shell_config"

echo "Installation complete. You can now use the 'jsum' command to run the Checksum CLI."
