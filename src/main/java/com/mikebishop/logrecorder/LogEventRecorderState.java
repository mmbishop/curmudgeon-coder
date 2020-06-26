package com.mikebishop.logrecorder;

public enum LogEventRecorderState {

    IDLE {
        public LogEventRecorderState nextState(LogEventRecorderAction action) {
            return switch (action) {
                case PLAY -> PLAYING;
                case RECORD -> RECORDING;
                default -> IDLE;
            };
        }
    },
    PLAYING {
        public LogEventRecorderState nextState(LogEventRecorderAction action) {
            return switch (action) {
                case PAUSE -> PAUSED;
                case STOP -> IDLE;
                case PLAY -> PLAYING;
                default -> throw new IllegalActionException("You can only pause or stop while playing");
            };
        }
    },
    RECORDING {
        public LogEventRecorderState nextState(LogEventRecorderAction action) {
            return switch (action) {
                case STOP -> IDLE;
                default -> throw new IllegalActionException("You can't do anything else while recording until you stop the recording");
            };
        }
    },
    PAUSED {
        public LogEventRecorderState nextState(LogEventRecorderAction action) {
            return switch (action) {
                case PLAY, PAUSE -> PLAYING;
                case STOP -> IDLE;
                case RECORD -> throw new IllegalActionException("Can't record while paused");
                case REWIND, FAST_FORWARD, SET_POSITION -> PAUSED;
            };
        };
    };

    public abstract LogEventRecorderState nextState(LogEventRecorderAction action);

}
