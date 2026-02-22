#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

EMULATOR_PID=""

cleanup() {
    if [ -n "$EMULATOR_PID" ]; then
        echo "Stopping Firebase Emulator (PID: $EMULATOR_PID)..."
        kill "$EMULATOR_PID" 2>/dev/null || true
        wait "$EMULATOR_PID" 2>/dev/null || true
    fi
}
trap cleanup EXIT

echo "Starting Firebase Emulator..."
cd "$PROJECT_DIR"
firebase emulators:start --project firebase-kotlin-sdk-test &
EMULATOR_PID=$!

echo "Waiting for Firestore Emulator to be ready..."
for i in $(seq 1 30); do
    if curl -s http://127.0.0.1:8080/ > /dev/null 2>&1; then
        echo "Emulator is ready."
        break
    fi
    if [ "$i" -eq 30 ]; then
        echo "Error: Emulator did not start within 60 seconds."
        exit 1
    fi
    sleep 2
done

echo "Running integration tests..."
cd "$PROJECT_DIR"
./gradlew :firebase-firestore:connectedAndroidTest

echo "Done."
