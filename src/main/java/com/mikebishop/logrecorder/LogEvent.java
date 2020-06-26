package com.mikebishop.logrecorder;

import java.time.LocalDateTime;

public record LogEvent(LocalDateTime time, String action) {
}
