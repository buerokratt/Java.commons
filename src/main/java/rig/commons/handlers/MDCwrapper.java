package rig.commons.handlers;

import org.slf4j.MDC;

public class MDCwrapper implements DynamicContent {

    @Override
    public String get(String key) {
        return MDC.get(key);
    }

    @Override
    public void put(String key, String val) {
        MDC.put(key, val);
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }
}
