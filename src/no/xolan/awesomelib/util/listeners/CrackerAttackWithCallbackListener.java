package no.xolan.awesomelib.util.listeners;

import java.util.concurrent.ConcurrentHashMap;

public interface CrackerAttackWithCallbackListener {
    public void finished(long runtime, ConcurrentHashMap<String, String> output);
}
