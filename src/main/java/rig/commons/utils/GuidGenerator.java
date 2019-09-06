package rig.commons.utils;

import java.util.concurrent.atomic.AtomicLong;

public class GuidGenerator {

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();
    private Long uniqueCurrentTimeMS;

    public long getUniqueCurrentTimeMS() {
        if (uniqueCurrentTimeMS == null) {
            this.uniqueCurrentTimeMS = generateUniqueCurrentTimeMS();
        }
        return uniqueCurrentTimeMS;
    }

    private long generateUniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }
}
