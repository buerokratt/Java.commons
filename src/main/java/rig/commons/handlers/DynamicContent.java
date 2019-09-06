package rig.commons.handlers;

public interface DynamicContent {

    String get(String key);
    void put(String key, String val);
    void remove(String key);

}
