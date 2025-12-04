#!/usr/bin/env bash
set -euo pipefail

# Usage: ./deploy_without_ansible.sh [subnet] [concurrency]
# Example: ./deploy_without_ansible.sh 172.1.60.0/24 20

SUBNET="${1:-}"
CONCURRENCY="${2:-20}"
KEY_PUB="${HOME}/.ssh/ansible_rsa.pub"
KEY_PRI="${HOME}/.ssh/ansible_rsa"
MAX_RETRIES=3
LOG_DIR="./logs_$(date +%Y%m%d_%H%M%S)"
DISCOVERED="${LOG_DIR}/discovered_hosts.txt"
SSH_ONLY="${LOG_DIR}/ssh_hosts.txt"
SUCCESS_LOG="${LOG_DIR}/success_hosts.txt"
FAIL_LOG="${LOG_DIR}/failed_hosts.txt"
TMPFIFO="/tmp/deploy_sem.$$"

mkdir -p "$LOG_DIR"
: > "$SUCCESS_LOG"
: > "$FAIL_LOG"

if ! command -v nmap >/dev/null 2>&1; then
  echo "[ERROR] nmap required. Install it (sudo apt install nmap)." >&2
  exit 1
fi

if [ ! -f "$KEY_PRI" ] || [ ! -f "$KEY_PUB" ]; then
  echo "[ERROR] SSH keypair not found. Create with: ssh-keygen -t ed25519 -f ~/.ssh/ansible_rsa" >&2
  exit 1
fi

# detect subnet if not provided
if [ -z "$SUBNET" ]; then
  SUBNET=$(ip -o -4 addr show scope global | awk '{print $4}' | head -n1 || true)
  if [ -z "$SUBNET" ]; then
    echo "Cannot auto-detect subnet. Provide as first argument, e.g. 172.1.60.0/24" >&2
    exit 1
  fi
fi

echo "[INFO] Scanning subnet $SUBNET for SSH (22)... this may take a few minutes."
nmap -p22 --open -n "$SUBNET" -oG - | awk '/22\/open/ {print $2}' | sort -u > "$DISCOVERED"
if [ ! -s "$DISCOVERED" ]; then
  echo "[ERROR] No SSH hosts discovered in $SUBNET" >&2
  exit 2
fi
cp "$DISCOVERED" "$SSH_ONLY"
HOSTS=( $(cat "$SSH_ONLY") )
TOTAL=${#HOSTS[@]}
echo "[INFO] Discovered $TOTAL SSH hosts (saved to $SSH_ONLY)."

# Optionally install public key using shared password (one-time)
read -p "Do you want to install your public key on these hosts now? [y/N] " do_copy
if [[ "$do_copy" =~ ^[Yy]$ ]]; then
  if ! command -v sshpass >/dev/null 2>&1; then
    echo "[ERROR] sshpass not found. Install it (sudo apt install sshpass) or manually run ssh-copy-id." >&2
    exit 1
  fi
  read -s -p "Enter shared SSH password for all hosts: " SHARED_PASS
  echo
  echo "[INFO] Installing public key to hosts (this will prompt/attempt for each host using sshpass)..."
  for h in "${HOSTS[@]}"; do
    echo "-> $h"
    sshpass -p "$SHARED_PASS" ssh-copy-id -i "$KEY_PUB" -o StrictHostKeyChecking=no -o ConnectTimeout=8 "rit@$h" 2>/dev/null || {
      echo "WARN: ssh-copy-id failed for $h (check password or reachability)"
    }
  done
  unset SHARED_PASS
fi

# Confirm key works
echo "[INFO] Checking key-based SSH to a sample host..."
sample="${HOSTS[0]}"
ssh -i "$KEY_PRI" -o BatchMode=yes -o ConnectTimeout=8 -o StrictHostKeyChecking=accept-new "rit@$sample" 'echo ok' >/dev/null 2>&1 || {
  echo "[WARN] Passwordless SSH to $sample failed. Ensure your public key is installed on hosts." >&2
  read -p "Continue anyway and attempt SSH (y/N)? " cont
  if [[ ! "$cont" =~ ^[Yy]$ ]]; then exit 1; fi
}

# Build the remote deploy script content (idempotent-ish)
read -r -d '' REMOTE_SCRIPT <<'EOF' || true
#!/bin/bash
set -euo pipefail
BASE="/logs"
ACN="$BASE/acn"
CNS="$BASE/cns"
JAVA="$BASE/java"
mkdir -p "$ACN" "$CNS" "$JAVA"
# ACN
ACN_URLS=(
  "https://raw.githubusercontent.com/0elon/1/main/acn/1.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/2.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/3.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/4.txt"
  "https://raw.githubusercontent.com/0elon/1/main/acn/5.txt"
)
# CNS
CNS_URLS=(
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
# JAVA
JAVA_URLS=(
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
# helper to download with retries
dl() {
  local url="$1" dest_dir="$2" fname
  fname=$(basename "$url")
  for i in 1 2 3; do
    if command -v curl >/dev/null 2>&1; then
      curl -fsSL --retry 2 --retry-delay 2 -o "${dest_dir}/${fname}" "$url" && return 0
    elif command -v wget >/dev/null 2>&1; then
      wget -q -T 10 -t 2 -O "${dest_dir}/${fname}" "$url" && return 0
    else
      echo "No downloader (curl/wget) on host" >&2
      return 1
    fi
    sleep 1
  done
  return 1
}
for u in "${ACN_URLS[@]}"; do dl "$u" "$ACN" || echo "WARN: failed $u"; done
for u in "${CNS_URLS[@]}"; do dl "$u" "$CNS" || echo "WARN: failed $u"; done
for u in "${JAVA_URLS[@]}"; do dl "$u" "$JAVA" || echo "WARN: failed $u"; done
echo "OK"
EOF

# semaphore FIFO for concurrency
mkfifo "$TMPFIFO"
# prime tokens
(
  for i in $(seq 1 "$CONCURRENCY"); do printf '\n' > "$TMPFIFO"; done
) &

cleanup() { rm -f "$TMPFIFO"; }
trap cleanup EXIT

run_host() {
  host="$1"
  hostlog="$LOG_DIR/${host}.log"
  : > "$hostlog"
  attempt=1
  while [ $attempt -le $MAX_RETRIES ]; do
    echo "[${host}] attempt $attempt" | tee -a "$hostlog"
    # send remote script and run it (uses the key-based ssh)
    if ssh -i "$KEY_PRI" -o BatchMode=yes -o ConnectTimeout=15 -o StrictHostKeyChecking=accept-new "rit@$host" 'bash -s' <<< "$REMOTE_SCRIPT" > >(tee -a "$hostlog") 2>&1; then
      echo "[${host}] SUCCESS" | tee -a "$hostlog"
      echo "$host" >> "$SUCCESS_LOG"
      return 0
    else
      echo "[${host}] WARN: attempt $attempt failed" | tee -a "$hostlog"
      attempt=$((attempt+1))
      sleep $((attempt))
    fi
  done
  echo "[${host}] FAILED after $MAX_RETRIES attempts" | tee -a "$hostlog"
  echo "$host" >> "$FAIL_LOG"
  return 1
}

# dispatch hosts with concurrency using FIFO tokens
pids=()
for h in "${HOSTS[@]}"; do
  # wait for token
  read -r _ < "$TMPFIFO"
  {
    run_host "$h"
    # release token
    printf '\n' > "$TMPFIFO"
  } &
  pids+=( $! )
done

# wait for all
wait "${pids[@]}"

echo "----------------------------------------"
echo "Summary:"
echo "  Successes: $(wc -l < "$SUCCESS_LOG" 2>/dev/null || echo 0)"
echo "  Failures:  $(wc -l < "$FAIL_LOG" 2>/dev/null || echo 0)"
echo "Logs: $LOG_DIR"
echo "----------------------------------------"

# print failed hosts file path for convenience
if [ -s "$FAIL_LOG" ]; then
  echo "Failed hosts are listed in: $FAIL_LOG"
fi

exit 0
