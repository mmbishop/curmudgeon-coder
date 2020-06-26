package com.mikebishop.logrecorder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogEventRecorder {

    private List<LogEvent> logEvents;
    private LogPlayer logPlayer;
    private int currentPosition;
    private LogEventRecorderState currentState;

    public LogEventRecorder() {
        logEvents = new ArrayList<>();
        currentPosition = 0;
        currentState = LogEventRecorderState.IDLE;
    }

    public void play() {
        if (actionIsValidInCurrentState(LogEventRecorderAction.PLAY)) {
            logPlayer = new LogPlayer();
            Thread playerThread = new Thread(logPlayer);
            playerThread.start();
        }
    }

    public void pause() {
        if (actionIsValidInCurrentState(LogEventRecorderAction.PAUSE)) {
            logPlayer.stop();
        }
    }

    public void stop() {
        if (actionIsValidInCurrentState(LogEventRecorderAction.STOP)) {
            logPlayer.stop();
        }
    }

    public void record() {
        try {
            setNextState(LogEventRecorderAction.RECORD);
        }
        catch (IllegalActionException e) {
            System.err.println(e.getMessage());
        }
    }

    public void rewind() {
        if (actionIsValidInCurrentState(LogEventRecorderAction.REWIND)) {
            currentPosition = 0;
        }
    }

    public void fastForward() {
        if (actionIsValidInCurrentState(LogEventRecorderAction.FAST_FORWARD)) {
            currentPosition = Math.min(currentPosition + 3, logEvents.size());
        }
    }

    public void recordLogEvent(LogEvent LogEvent) {
        if (currentState.equals(LogEventRecorderState.RECORDING)) {
            logEvents.add(LogEvent);
            currentPosition = logEvents.size();
        }
    }

    public void setCurrentPosition(LocalDateTime time) {
        if (actionIsValidInCurrentState(LogEventRecorderAction.SET_POSITION)) {
            Optional<LogEvent> nextLogEvent = logEvents.stream().filter(LogEvent -> LogEvent.time().isAfter(time)).findFirst();
            nextLogEvent.ifPresent(LogEvent -> currentPosition = logEvents.indexOf(LogEvent));
        }
    }

    private boolean actionIsValidInCurrentState(LogEventRecorderAction action) {
        try {
            setNextState(action);
            return true;
        }
        catch (IllegalActionException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void setNextState(LogEventRecorderAction action) {
        try {
            currentState = currentState.nextState(action);
        }
        catch (IllegalActionException e) {
            currentState = LogEventRecorderState.IDLE;
            throw e;
        }
    }

    private void playLogEvent() {
        System.out.println(logEvents.get(currentPosition++));
    }

    private boolean endOfLog() {
        return currentPosition == logEvents.size();
    }

    private class LogPlayer implements Runnable {

        private boolean stopped = false;

        @Override
        public void run() {
            while ((! endOfLog()) && (! stopped)) {
                playLogEvent();
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
