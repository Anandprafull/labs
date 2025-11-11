#!/bin/bash

# Define the destination directory
DEST_DIR="/code"

# Create the directory if it doesn't exist
if [ ! -d "$DEST_DIR" ]; then
  sudo mkdir -p "$DEST_DIR"
  echo "Directory $DEST_DIR created."
fi

# List of URLs to download
URLs=(
    "https://anandprafull.github.io/labs/5/aes.txt"
    "https://anandprafull.github.io/labs/5/des.txt"
    "https://anandprafull.github.io/labs/5/rsa.txt"
    "https://anandprafull.github.io/labs/5/CaesarCipher.txt"
    "https://anandprafull.github.io/labs/5/PlayFair.txt"
    
    "https://anandprafull.github.io/labs/5/tcp.txt"
    "https://anandprafull.github.io/labs/5/udp.txt"
    "https://anandprafull.github.io/labs/5/crc.txt"
    "https://anandprafull.github.io/labs/5/ford.txt"


)

# Download each file and save it to the destination directory with sudo
for URL in "${URLs[@]}"; do
    sudo wget -P "$DEST_DIR" "$URL"
done

echo "All files downloaded successfully to $DEST_DIR"



# sudo chown -R $(whoami):$(whoami) /code
# sudo ./1.sh or bash 1.sh
# cp /code/aes.txt ~/Desktop/aes.java
