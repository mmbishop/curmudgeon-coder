package com.mikebishop.logrecorder;

import java.time.LocalDateTime;

public record LogEntry(LocalDateTime time, String action) {
}
