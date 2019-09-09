package rig.commons.utils;

import java.util.concurrent.atomic.AtomicLong;

public class GuidGenerator implements IDGenerator {

    private static AtomicLong LAST_TIME_MS = new AtomicLong();

    public long getId() {
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
