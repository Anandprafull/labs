#!/usr/bin/env bash
set -euo pipefail

# deploy_without_ansible_auto_install.sh
# Non-Ansible deploy with:
# - subnet discovery (nmap)
# - optional ssh-copy-id (shared password)
# - auto-install remote tools (curl/wget) via sudo+apt if missing (Debian/Ubuntu)
# - concurrent runs, per-host logs, color output, retries

USAGE="Usage: $0 [subnet] [concurrency] [--no-install-missing-tools]
Example: $0 172.1.60.0/24 20
If subnet omitted, script auto-detects local subnet."

SUBNET="${1:-}"
CONCURRENCY="${2:-20}"
NO_INSTALL_FLAG="${3:-}"
INSTALL_MISSING_TOOLS=true
if [ "$NO_INSTALL_FLAG" = "--no-install-missing-tools" ]; then
  INSTALL_MISSING_TOOLS=false
fi

KEY_PUB="${HOME}/.ssh/ansible_rsa.pub"
KEY_PRI="${HOME}/.ssh/ansible_rsa"
MAX_RETRIES=3
LOG_ROOT="./logs_$(date +%Y%m%d_%H%M%S)"
DISCOVERED="${LOG_ROOT}/discovered_hosts.txt"
SUCCESS_LOG="${LOG_ROOT}/success_hosts.txt"
FAIL_LOG="${LOG_ROOT}/failed_hosts.txt"
PROGRESS_REFRESH=0.5

mkdir -p "$LOG_ROOT"
: > "$SUCCESS_LOG"
: > "$FAIL_LOG"

# Colors
GREEN="$(tput setaf 2 2>/dev/null || printf '\e[32m')"
RED="$(tput setaf 1 2>/dev/null || printf '\e[31m')"
YELLOW="$(tput setaf 3 2>/dev/null || printf '\e[33m')"
BLUE="$(tput setaf 4 2>/dev/null || printf '\e[34m')"
BOLD="$(tput bold 2>/dev/null || printf '\e[1m')"
NC="$(tput sgr0 2>/dev/null || printf '\e[0m')"

ts() { date +"%Y-%m-%d %H:%M:%S"; }
log_info()  { printf "%s %s[INFO]%s %s\n" "$(ts)" "$BLUE" "$NC" "$*"; }
log_warn()  { printf "%s %s[WARN]%s %s\n" "$(ts)" "$YELLOW" "$NC" "$*"; }
log_error() { printf "%s %s[ERROR]%s %s\n" "$(ts)" "$RED" "$NC" "$*"; }
log_ok()    { printf "%s %s[OK]%s %s\n" "$(ts)" "$GREEN" "$NC" "$*"; }

# prerequisites check on control node
if ! command -v nmap >/dev/null 2>&1; then
  log_error "nmap required. Install: sudo apt install nmap"
  exit 1
fi
if ! command -v ssh >/dev/null 2>&1; then
  log_error "ssh required."
  exit 1
fi

# prompt for admin username and the shared password (same for all systems)
read -p "Enter remote username (default: rit): " REMOTE_USER
REMOTE_USER="${REMOTE_USER:-rit}"
read -s -p "Enter shared password for user ${REMOTE_USER} on all hosts: " SHARED_PASS
echo
if [ -z "$SHARED_PASS" ]; then
  log_warn "Empty password entered — key-based operations will be assumed."
fi

# detect subnet if not provided
if [ -z "$SUBNET" ]; then
  SUBNET=$(ip -o -4 addr show scope global | awk '{print $4}' | head -n1 || true)
  if [ -z "$SUBNET" ]; then
    log_error "Cannot auto-detect subnet. Provide as first arg."
    exit 1
  fi
fi

log_info "Scanning subnet $SUBNET for SSH (port 22)..."
nmap -p22 --open -n "$SUBNET" -oG - | awk '/22\/open/ {print $2}' | sort -u > "$DISCOVERED"
if [ ! -s "$DISCOVERED" ]; then
  log_error "No SSH hosts discovered on $SUBNET"
  exit 2
fi
mapfile -t HOSTS < "$DISCOVERED"
TOTAL=${#HOSTS[@]}
log_info "Discovered $TOTAL hosts (saved to $DISCOVERED)."

# optionally install public key using sshpass + ssh-copy-id
read -p "Install public key $KEY_PUB to all discovered hosts now? [y/N] " do_copy
if [[ "$do_copy" =~ ^[Yy]$ ]]; then
  if ! command -v sshpass >/dev/null 2>&1; then
    log_error "sshpass required for automated key install. Install with: sudo apt install sshpass"
    exit 1
  fi
  if [ ! -f "$KEY_PUB" ]; then
    log_error "Public key $KEY_PUB not found. Create it: ssh-keygen -t ed25519 -f ~/.ssh/ansible_rsa"
    exit 1
  fi
  log_info "Installing public key on discovered hosts (this will use provided shared password)..."
  for h in "${HOSTS[@]}"; do
    printf "%s " "$h"
    if sshpass -p "$SHARED_PASS" ssh-copy-id -i "$KEY_PUB" -o StrictHostKeyChecking=no -o ConnectTimeout=8 "${REMOTE_USER}@${h}" >/dev/null 2>&1; then
      printf "%b\n" "${GREEN}KEY INSTALLED${NC}"
    else
      printf "%b\n" "${YELLOW}KEY INSTALL WARN${NC}"
      log_warn "ssh-copy-id failed for $h"
    fi
  done
fi

# helper: check remote for tools, return 0 if present, 1 if missing both
remote_has_downloader() {
  local host="$1"
  ssh -o BatchMode=yes -o ConnectTimeout=8 -o StrictHostKeyChecking=accept-new "${REMOTE_USER}@${host}" 'command -v curl >/dev/null 2>&1 || command -v wget >/dev/null 2>&1' >/dev/null 2>&1
}

# helper: attempt to install curl/wget via apt on remote host using sudo with password
attempt_install_tools() {
  local host="$1"
  # Only attempt if INSTALL_MISSING_TOOLS true
  if ! $INSTALL_MISSING_TOOLS ; then
    return 1
  fi
  log_info "Attempting to install curl/wget on ${host} via sudo (apt-get)."
  # Compose remote command that uses sudo -S and reads password from stdin
  # We send the password via sshpass with stdin to the remote shell which echoes it into sudo.
  # Note: This assumes Debian/Ubuntu (apt-get). For other distros, installation will fail.
  remote_cmd="echo '${SHARED_PASS}' | sudo -S sh -c 'apt-get update -y && apt-get install -y curl wget || true'"
  if sshpass -p "$SHARED_PASS" ssh -o StrictHostKeyChecking=accept-new -o ConnectTimeout=12 "${REMOTE_USER}@${host}" "$remote_cmd" >/dev/null 2>&1; then
    log_ok "Install attempt completed on ${host} (check logs)."
    return 0
  else
    log_warn "Auto-install attempt failed on ${host} (may not be Debian/Ubuntu or sudo policy differs)."
    return 1
  fi
}

# Build remote deploy script (idempotent-ish) to run via ssh
read -r -d '' REMOTE_SCRIPT <<'EOF' || true
#!/bin/bash
set -euo pipefail
BASE="/logs"
ACN="$BASE/acn"
CNS="$BASE/cns"
JAVA="$BASE/java"
mkdir -p "$ACN" "$CNS" "$JAVA"
# ACN
ACN_URLS=( "https://raw.githubusercontent.com/0elon/1/main/acn/1.txt" "https://raw.githubusercontent.com/0elon/1/main/acn/2.txt" "https://raw.githubusercontent.com/0elon/1/main/acn/3.txt" "https://raw.githubusercontent.com/0elon/1/main/acn/4.txt" "https://raw.githubusercontent.com/0elon/1/main/acn/5.txt" )
# CNS
CNS_URLS=( "https://raw.githubusercontent.com/0elon/1/main/cns/aes.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/des.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/rsa.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/CaesarCipher.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/PlayFair.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/b1.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/b2.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/b3.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/b4.txt" "https://raw.githubusercontent.com/0elon/1/main/cns/h.txt" )
# JAVA
JAVA_URLS=( "https://raw.githubusercontent.com/0elon/1/main/java/h.txt" "https://raw.githubusercontent.com/0elon/1/main/java/m.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P1.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P2.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P3.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P4.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P5.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P6.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P7.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P8.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P9.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P10.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P11.txt" "https://raw.githubusercontent.com/0elon/1/main/java/P12.txt" )
dl() {
  url="$1"; dest="$2"; fname=$(basename "$url")
  for i in 1 2 3; do
    if command -v curl >/dev/null 2>&1; then
      curl -fsSL --retry 2 --retry-delay 2 -o "${dest}/${fname}" "$url" && return 0
    elif command -v wget >/dev/null 2>&1; then
      wget -q -T 10 -t 2 -O "${dest}/${fname}" "$url" && return 0
    else
      echo "No downloader (curl/wget) on host"
      return 1
    fi
    sleep 1
  done
  return 1
}
for u in "${ACN_URLS[@]}"; do dl "$u" "$ACN" || echo "WARN: failed $u"; done
for u in "${CNS_URLS[@]}"; do dl "$u" "$CNS" || echo "WARN: failed $u"; done
for u in "${JAVA_URLS[@]}"; do dl "$u" "$JAVA" || echo "WARN: failed $u"; done
if [ -f "$JAVA/h.txt" ]; then
  echo "OK"
  exit 0
else
  echo "MISSING: $JAVA/h.txt"
  exit 2
fi
EOF

# Create semaphore FIFO
SEM="/tmp/deploy_sem.$$"
mkfifo "$SEM"
(
  for i in $(seq 1 "$CONCURRENCY"); do printf '%s\n' token; done > "$SEM"
) &

cleanup() { rm -f "$SEM"; }
trap cleanup EXIT

# progress refresher
print_progress() {
  local succ fail done pct bar filled
  succ=$(wc -l < "$SUCCESS_LOG" 2>/dev/null || echo 0)
  fail=$(wc -l < "$FAIL_LOG" 2>/dev/null || echo 0)
  done=$((succ + fail))
  pct=$(( done * 100 / (TOTAL>0?TOTAL:1) ))
  filled=$(( done * 40 / (TOTAL>0?TOTAL:1) ))
  bar="$(printf '%0.s#' $(seq 1 $filled))$(printf '%0.s-' $(seq 1 $((40-filled))))"
  printf "\rProgress: [%s] %3d%%  (%d/%d)  Success:%s%d%s Fail:%s%d%s" "$bar" "$pct" "$done" "$TOTAL" "$GREEN" "$succ" "$NC" "$RED" "$fail" "$NC"
}
refresh_loop() { while true; do print_progress; sleep "$PROGRESS_REFRESH"; done }
refresh_loop & REFRESH_PID=$!

run_host() {
  host="$1"
  hostlog="$LOG_ROOT/${host}.log"
  : > "$hostlog"
  attempt=1
  success=0

  # If downloader missing and INSTALL_MISSING_TOOLS allowed, attempt install before running remote script
  if ! remote_has_downloader "$host"; then
    log_warn "No downloader on ${host} (curl/wget missing)."
    if $INSTALL_MISSING_TOOLS ; then
      attempt_install_tools "$host"
      sleep 2
    else
      log_warn "Auto-install disabled; will attempt deploy and may fail."
    fi
  fi

  while [ $attempt -le $MAX_RETRIES ]; do
    printf "%s %s[%s]%s Attempt %d/%d\n" "$(ts)" "$BOLD" "$host" "$NC" "$attempt" "$MAX_RETRIES" | tee -a "$hostlog"
    # Attempt to run remote deploy via ssh. If key exists, use it; otherwise fallback to sshpass (password)
    if [ -f "$KEY_PRI" ]; then
      ssh -i "$KEY_PRI" -o BatchMode=yes -o ConnectTimeout=20 -o StrictHostKeyChecking=accept-new "${REMOTE_USER}@${host}" 'bash -s' 2>&1 <<< "$REMOTE_SCRIPT" | sed -u "s/^/[$host] /" | tee -a "$hostlog"
      rc=${PIPESTATUS[0]}
    else
      # use sshpass to login with password and run remote script (less secure)
      if ! command -v sshpass >/dev/null 2>&1; then
        log_error "sshpass not installed on controller; cannot ssh with password to $host"
        rc=2
      else
        sshpass -p "$SHARED_PASS" ssh -o StrictHostKeyChecking=accept-new -o ConnectTimeout=20 "${REMOTE_USER}@${host}" 'bash -s' 2>&1 <<< "$REMOTE_SCRIPT" | sed -u "s/^/[$host] /" | tee -a "$hostlog"
        rc=${PIPESTATUS[0]}
      fi
    fi

    if [ "$rc" -eq 0 ]; then
      printf "%s %b[%s]%b %bSUCCESS%b\n" "$(ts)" "$GREEN" "$host" "$NC" "$BOLD" "$NC" | tee -a "$hostlog"
      echo "$host" >> "$SUCCESS_LOG"
      success=1
      break
    else
      printf "%s %b[%s]%b Attempt %d failed (rc=%d)\n" "$(ts)" "$YELLOW" "$host" "$NC" "$attempt" "$rc" | tee -a "$hostlog"
      attempt=$((attempt+1))
      sleep $((attempt))  # backoff
    fi
  done

  if [ $success -ne 1 ]; then
    printf "%s %b[%s]%b FAILED after %d attempts\n" "$(ts)" "$RED" "$host" "$NC" "$MAX_RETRIES" | tee -a "$hostlog"
    echo "$host" >> "$FAIL_LOG"
  fi

  # release token
  printf '%s\n' token > "$SEM"
}

# dispatch
pids=()
for h in "${HOSTS[@]}"; do
  read -r _ < "$SEM"
  {
    run_host "$h"
  } &
  pids+=( $! )
done

wait "${pids[@]}" 2>/dev/null || true

kill "$REFRESH_PID" 2>/dev/null || true
print_progress
printf "\n\n"

# Summary table
echo "========================================"
echo -e "${BOLD}Deployment Summary (${ts()}):${NC}"
succ_count=$(wc -l < "$SUCCESS_LOG" 2>/dev/null || echo 0)
fail_count=$(wc -l < "$FAIL_LOG" 2>/dev/null || echo 0)
echo -e "${GREEN}Succeeded (${succ_count}):${NC}"
if [ "$succ_count" -gt 0 ]; then sed -n '1,500p' "$SUCCESS_LOG" | nl -ba; else echo "  None"; fi
echo
echo -e "${RED}Failed (${fail_count}):${NC}"
if [ "$fail_count" -gt 0 ]; then sed -n '1,500p' "$FAIL_LOG" | nl -ba; echo; echo "Per-host logs: $LOG_ROOT/*.log"; else echo "  None"; fi
echo "========================================"

if [ -s "$FAIL_LOG" ]; then echo "Failed hosts listed in: $FAIL_LOG"; fi
exit 0
