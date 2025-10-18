#!/usr/bin/env bash
set -euo pipefail

# Script to start Xvfb, x11vnc, websockify/noVNC and run the JavaFX app.
# Logs: /tmp/xvfb.log, /tmp/x11vnc.log, /tmp/websockify.log, ./run.log

LOG_DIR=/tmp
NO_VNC_DIR=/opt/noVNC
XVFB_DISPLAY=:99
RFB_PORT=5900
HTTP_PORT=6080

echo "Starting run_with_vnc.sh"

# 1) Install basics if missing (requires sudo)
if ! command -v Xvfb >/dev/null 2>&1; then
  echo "Installing Xvfb and x11vnc... (requires sudo)"
  sudo apt-get update
  sudo apt-get install -y xvfb x11vnc git python3-pip
  sudo pip3 install websockify
fi

# 2) Ensure noVNC is present
if [ ! -d "$NO_VNC_DIR" ]; then
  echo "Cloning noVNC to $NO_VNC_DIR (requires sudo)"
  sudo git clone https://github.com/novnc/noVNC.git "$NO_VNC_DIR"
fi

# 3) Start Xvfb if not running
if ! pgrep -f "Xvfb .*${XVFB_DISPLAY}" >/dev/null 2>&1; then
  echo "Starting Xvfb on $XVFB_DISPLAY"
  Xvfb ${XVFB_DISPLAY} -screen 0 1024x768x24 >"${LOG_DIR}/xvfb.log" 2>&1 &
  sleep 1
fi

export DISPLAY=${XVFB_DISPLAY}

# 4) Start x11vnc attached to DISPLAY
if ! pgrep -f "x11vnc .*${XVFB_DISPLAY}" >/dev/null 2>&1; then
  echo "Starting x11vnc on RFB port ${RFB_PORT}"
  x11vnc -display ${XVFB_DISPLAY} -nopw -forever -shared -rfbport ${RFB_PORT} >"${LOG_DIR}/x11vnc.log" 2>&1 &
  sleep 1
fi

# 5) Start websockify to serve noVNC
if ! pgrep -f "websockify .*${HTTP_PORT}" >/dev/null 2>&1; then
  echo "Starting websockify (noVNC) on http port ${HTTP_PORT}"
  if command -v websockify >/dev/null 2>&1; then
    echo "Using system websockify"
    websockify --web "${NO_VNC_DIR}" ${HTTP_PORT} localhost:${RFB_PORT} >"${LOG_DIR}/websockify.log" 2>&1 &
  elif [ -x "${NO_VNC_DIR}/utils/websockify/run" ]; then
    echo "Using bundled noVNC websockify runner"
    # Use python3 to run the bundled websockify runner
    python3 "${NO_VNC_DIR}/utils/websockify/run" --web "${NO_VNC_DIR}" ${HTTP_PORT} localhost:${RFB_PORT} >"${LOG_DIR}/websockify.log" 2>&1 &
  else
    echo "ERROR: websockify not found and noVNC bundled runner missing. Install websockify or ensure ${NO_VNC_DIR} exists." >&2
  fi
  sleep 1
fi

# 6) Run the JavaFX app and capture output
echo "Launching JavaFX app... logs -> run.log"
mvn -e javafx:run > run.log 2>&1 || true

# Tail logs for convenience
echo "--- tail of run.log ---"
tail -n +1 run.log | sed -n '1,200p'

echo "noVNC should be available at http://localhost:${HTTP_PORT}/vnc.html"
echo "Xvfb log: ${LOG_DIR}/xvfb.log"
echo "x11vnc log: ${LOG_DIR}/x11vnc.log"
echo "websockify log: ${LOG_DIR}/websockify.log"

exit 0
