#!/bin/bash

BASE="/c"
ACN="$BASE/acn"
CNS="$BASE/cns"
JAVA="$BASE/java"

sudo mkdir -p "$ACN" "$CNS" "$JAVA"

URLs_ACN=(
  "https://raw.githubusercontent.com/0elon/1/main/acn/1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/5.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/6.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/7.txt"
)

URLs_CNS=(
  "https://raw.githubusercontent.com/0elon/1/main/cns/aes.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/des.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/rsa.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/CaesarCipher.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/PlayFair.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/b1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/b2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/b3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/b4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/b5.txt"
  "https://raw.githubusercontent.com/0elon/1/main/cns/h.txt"
)

URLs_JAVA=(
  "https://raw.githubusercontent.com/0elon/1/main/java/h.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/m.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P5.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P6.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P7.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P8.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P9.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P10.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P11.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/P12.txt"
  
  "https://raw.githubusercontent.com/0elon/1/main/java/1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/5.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/6.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/7.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/8.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/9.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/10.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/11.txt"
  "https://raw.githubusercontent.com/0elon/1/main/java/12.txt"

  
)

for u in "${URLs_ACN[@]}";  do sudo wget -P "$ACN" "$u";  done
for u in "${URLs_CNS[@]}";  do sudo wget -P "$CNS" "$u";  done
for u in "${URLs_JAVA[@]}"; do sudo wget -P "$JAVA" "$u"; done

echo "All files downloaded into structured folders under $BASE"

echo "Enabling SSH access..."
sudo ufw allow ssh
sudo ufw allow 22/tcp
sudo ufw --force enable
sudo systemctl enable ssh
sudo systemctl start ssh
echo "SSH is now enabled and port 22 is open."



# sudo chown -R $(whoami):$(whoami) /code
# sudo ./1.sh or bash 1.sh
# cp /code/aes.txt ~/Desktop/aes.java
