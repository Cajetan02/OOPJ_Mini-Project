# OOPJ_Mini-Project

Development notes
-----------------

To run the JavaFX application inside a headless Linux environment (dev container/Codespaces) we use Xvfb
as a virtual X server. Optionally you can view the UI in your browser using noVNC.

Quick run (no GUI viewing):

1. Start Xvfb (if not already running):

```bash
pgrep Xvfb || Xvfb :99 -screen 0 1024x768x24 >/tmp/xvfb.log 2>&1 & sleep 1
export DISPLAY=:99
```

2. Run the app:

```bash
mvn -e javafx:run
```

You should see in the terminal:
- "Database initialized successfully!"
- "UI shown: primaryStage displayed"

Run and view GUI in a browser (noVNC)
------------------------------------

This script automates installing prerequisites (if missing), starting Xvfb, starting x11vnc and websockify/noVNC, and running the app.

Usage:

```bash
chmod +x scripts/run_with_vnc.sh
./scripts/run_with_vnc.sh
```

Open the UI in your browser at:

http://localhost:6080/vnc.html

If you're in Codespaces, forward port 6080 in the Ports panel and open the forwarded URL.

Security note: the script runs x11vnc with no password for convenience. For multi-user or public environments set a VNC password using `x11vnc -storepasswd` and point x11vnc at the password file with `-rfbauth`.
