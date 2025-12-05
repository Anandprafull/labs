#!/usr/bin/env bash
set -euo pipefail

# Run this script directly in a terminal on each target machine:
# curl -sSL https://example.org/your/script.sh | bash
# or save as run_downloads.sh and run: bash run_downloads.sh

BASE="/j"
ACN="$BASE/acn"
CNS="$BASE/cns"
JAVA="$BASE/java"

# Tools
_have_cmd() { command -v "$1" >/dev/null 2>&1; }

# Try to create directories, with sudo if needed
_make_dirs() {
  if mkdir -p "$ACN" "$CNS" "$JAVA" 2>/dev/null; then
    return 0
  fi
  echo "[INFO] Need sudo to create $BASE directories..."
  sudo mkdir -p "$ACN" "$CNS" "$JAVA"
  sudo chown "$(id -u):$(id -g)" "$ACN" "$CNS" "$JAVA" || true
}

# Downloader wrapper: prefers curl, falls back to wget
download_file() {
  local url="$1"
  local dest_dir="$2"
  local fname
  fname="$(basename "$url")"
  mkdir -p "$dest_dir"
  if _have_cmd curl; then
    curl -fsSL --retry 3 --retry-delay 2 -o "${dest_dir}/${fname}" "$url"
  elif _have_cmd wget; then
    wget -q -T 15 -t 3 -O "${dest_dir}/${fname}" "$url"
  else
    return 2
  fi
}

print_header() {
  printf "\n==== %s ====\n" "$1"
}

# Lists
URLs_ACN=(
  "https://raw.githubusercontent.com/0elon/1/main/acn/1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/5.txt"
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
)

# Start
echo "[INFO] Preparing to download files into $BASE"
_make_dirs

# Check downloader availability and inform user
if _have_cmd curl; then
  echo "[INFO] Using curl for downloads"
elif _have_cmd wget; then
  echo "[INFO] Using wget for downloads"
else
  echo "[WARN] Neither curl nor wget found. Attempting to install (only supported on Debian/RedHat families)."
  if _have_cmd apt-get; then
    echo "[INFO] Installing curl via apt-get (requires sudo)..."
    sudo apt-get update -y && sudo apt-get install -y curl
  elif _have_cmd yum || _have_cmd dnf; then
    echo "[INFO] Installing curl via yum/dnf (requires sudo)..."
    if _have_cmd yum; then sudo yum -y install curl; else sudo dnf -y install curl; fi
  else
    echo "[ERROR] No package manager detected to install curl/wget. Please install curl or wget and re-run."
    exit 1
  fi
fi

# Download helper with progress and logging
download_and_report() {
  local url="$1" dest="$2"
  local fname
  fname="$(basename "$url")"
  printf "[%s] -> %s/%s ... " "$(date +'%H:%M:%S')" "$dest" "$fname"
  if download_file "$url" "$dest"; then
    printf "OK\n"
  else
    local rc=$?
    if [ $rc -eq 2 ]; then
      printf "FAILED (no downloader)\n"
    else
      printf "FAILED\n"
    fi
  fi
}

print_header "Downloading ACN files"
for u in "${URLs_ACN[@]}"; do
  download_and_report "$u" "$ACN"
done

print_header "Downloading CNS files"
for u in "${URLs_CNS[@]}"; do
  download_and_report "$u" "$CNS"
done

print_header "Downloading JAVA files"
for u in "${URLs_JAVA[@]}"; do
  download_and_report "$u" "$JAVA"
done

echo
echo "[DONE] All files attempted. Check directories under $BASE"
