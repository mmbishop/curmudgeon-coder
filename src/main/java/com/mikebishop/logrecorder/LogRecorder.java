package com.mikebishop.logrecorder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogRecorder {

    private List<LogEntry> logEntries;
    private LogPlayer logPlayer;
    private int currentPosition;
    private LogRecorderState currentState;

    public LogRecorder() {
        logEntries = new ArrayList<>();
        currentPosition = 0;
        currentState = LogRecorderState.IDLE;
    }

    public void play() {
        if (actionIsValidInCurrentState(LogRecorderAction.PLAY)) {
            logPlayer = new LogPlayer();
            Thread playerThread = new Thread(logPlayer);
            playerThread.start();
        }
    }

    public void pause() {
        if (actionIsValidInCurrentState(LogRecorderAction.PAUSE)) {
            logPlayer.stop();
        }
    }

    public void stop() {
        if (actionIsValidInCurrentState(LogRecorderAction.STOP)) {
            logPlayer.stop();
        }
    }

    public void record() {
        try {
            setNextState(LogRecorderAction.RECORD);
        }
        catch (IllegalActionException e) {
            System.err.println(e.getMessage());
        }
    }

    public void rewind() {
        if (actionIsValidInCurrentState(LogRecorderAction.REWIND)) {
            currentPosition = 0;
        }
    }

    public void fastForward() {
        if (actionIsValidInCurrentState(LogRecorderAction.FAST_FORWARD)) {
            currentPosition = Math.min(currentPosition + 3, logEntries.size());
        }
    }

    public void recordLogEntry(LogEntry logEntry) {
        if (currentState.equals(LogRecorderState.RECORDING)) {
            logEntries.add(logEntry);
            currentPosition = logEntries.size();
        }
    }

    public void setCurrentPosition(LocalDateTime time) {
        if (actionIsValidInCurrentState(LogRecorderAction.SET_POSITION)) {
            Optional<LogEntry> nextLogEntry = logEntries.stream().filter(logEntry -> logEntry.time().isAfter(time)).findFirst();
            nextLogEntry.ifPresent(logEntry -> currentPosition = logEntries.indexOf(logEntry));
        }
    }

    private boolean actionIsValidInCurrentState(LogRecorderAction action) {
        try {
            setNextState(action);
            return true;
        }
        catch (IllegalActionException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void setNextState(LogRecorderAction action) {
        try {
            currentState = currentState.nextState(action);
        }
        catch (IllegalActionException e) {
            currentState = LogRecorderState.IDLE;
            throw e;
        }
    }

    private void playLogEntry() {
        System.out.println(logEntries.get(currentPosition++));
    }

    private boolean endOfLog() {
        return currentPosition == logEntries.size();
    }

    private class LogPlayer implements Runnable {

        private boolean stopped = false;

        @Override
        public void run() {
            while ((! endOfLog()) && (! stopped)) {
                playLogEntry();
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            stopped = true;
        }

    }

}
