package no.xolan.awesomelib.lang;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Generated;

import no.xolan.awesomelib.script.FileEvaluator;
import no.xolan.awesomelib.util.Localization;
import no.xolan.awesomelib.util.listeners.CrackerAttackWithCallbackListener;

public class Cracker implements CrackerAttackWithCallbackListener {

    public enum METHOD {
        ALL, PLAIN, MD5, CUSTOM;
    }

    private List<String> targets;
    private List<String> inputs;
    private METHOD method;
    private ConcurrentHashMap<String, String> results;
    private volatile long runtime;
    private int numRunningThreads;

    public Cracker() {
        this.targets = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.results = new ConcurrentHashMap<>();
        this.method = METHOD.PLAIN;
        this.runtime = System.currentTimeMillis();
        this.numRunningThreads = 0;
    }

    private ConcurrentHashMap<String, String> dictionaryAttack(String input, String target, METHOD method) {

        if (method == METHOD.ALL || method == METHOD.PLAIN) {
            if (input.equals(target)) {
                this.results.put(input, target);
                Logger.getLogger(Localization.getInstance().get("name")).info(
                        "$" + Thread.currentThread().getId() + " " + Localization.getInstance().get("attack_hit") + input + " => " + target + ".");
            }
        }

        if (method == METHOD.ALL || method == METHOD.MD5) {
            if (toMd5(input).equals(target)) {
                this.results.put(input, target);
                Logger.getLogger(Localization.getInstance().get("name")).info(
                        "$" + Thread.currentThread().getId() + " " + Localization.getInstance().get("attack_hit") + input + " => " + target + ".");
            }
        }

        if (method == METHOD.CUSTOM) {
            //TODO implement script
        }
        
        return this.results;

    }

    public String toMd5(String in) {
        byte[] bytesOfMessage;
        MessageDigest md;

        try {
            bytesOfMessage = in.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        byte[] thedigest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1, thedigest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public void addTargets(List<String> targets) {
        this.targets.addAll(targets);
    }

    public void addInputs(List<String> inputs) {
        this.inputs.addAll(inputs);
    }

    public void setMethod(METHOD method) {
        this.method = method;
        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("method") + this.method + ".");
    }

    public ConcurrentHashMap<String, String> runSingleThreaded() {
        this.runtime = System.currentTimeMillis();
        ConcurrentHashMap<String, String> output = new ConcurrentHashMap<String, String>();

        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("thread_single"));
        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("dictionary_attack"));
        for (String i : this.inputs) {
            for (String t : this.targets) {
                output.putAll((this.dictionaryAttack(i, t, this.method)));
            }
        }

        System.out.println(output);
        this.runtime = System.currentTimeMillis() - this.runtime;
        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("runtime") + this.getRunTime() + ".");
        return output;
    }

    public void runMultiThreaded(int threadCount) {
        this.runtime = System.currentTimeMillis();
        this.numRunningThreads = threadCount;
        final int[] ids = new int[threadCount + 1];
        List<String> slice;
        boolean slicedInputs;

        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("thread_multi"));
        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("dictionary_attack"));

        for (int i = 0; i < threadCount; i++) {
            ids[i] = (inputs.size() > targets.size()) ? (inputs.size() / threadCount) * i : (targets.size() / threadCount) * i;
        }

        slice = (inputs.size() > targets.size()) ? inputs : targets;
        slicedInputs = (inputs.size() > targets.size()) ? true : false;
        ids[ids.length - 1] = (slicedInputs) ? inputs.size() : targets.size();
        this.runtime = System.currentTimeMillis() - this.runtime;
        for (int i = 0; i < threadCount; i++) {
            new CrackerAttackThread(ids[i], ids[i + 1] - 1, slice.subList(ids[i], ids[i + 1] - 1), slicedInputs, this).start();
        }
        while (this.numRunningThreads > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.results);
        Logger.getLogger(Localization.getInstance().get("name")).info(Localization.getInstance().get("runtime") + this.getRunTime() + ".");
    }

    public ConcurrentHashMap<String, String> getResults() {
        return this.results;
    }
    
    public void clearResults() {
        this.results.clear();
    }

    public long getRunTime() {
        return this.runtime;
    }

    @Override
    public void finished(long runtime, ConcurrentHashMap<String, String> output) {
        this.runtime += runtime;
        this.results.putAll(output);
        this.numRunningThreads = this.numRunningThreads - 1;
    }

    public static void main(String[] args) {


    }

    public class CrackerAttackThread extends Thread {

        private int min, max;
        private List<String> slice;
        private boolean slicedInputs;
        private long runtime;
        private ConcurrentHashMap<String, String> output;
        private CrackerAttackWithCallbackListener listener;

        public CrackerAttackThread(int min, int max, List<String> slice, boolean slicedInputs, CrackerAttackWithCallbackListener listener) {
            this.min = min;
            this.max = max;
            this.slice = slice;
            this.slicedInputs = slicedInputs;
            this.listener = listener;
            this.output = new ConcurrentHashMap<>();
        }

        public void run() {
            this.runtime = System.currentTimeMillis();
            Logger.getLogger(Localization.getInstance().get("name")).info(
                    "$" + Thread.currentThread().getId() + " " + Localization.getInstance().get("thread_spawn1") + this.min + ". "
                            + Localization.getInstance().get("thread_spawn2") + this.max + ".");
            try {
                if (slicedInputs) {
                    for (String in : slice) {
                        for (String ta : targets) {
                            this.output.putAll((dictionaryAttack(in, ta, method)));
                        }
                    }
                } else {
                    for (String in : inputs) {
                        for (String ta : slice) {
                            this.output.putAll((dictionaryAttack(in, ta, method)));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.runtime = System.currentTimeMillis() - this.runtime;
            this.listener.finished(this.runtime, this.output);
            Logger.getLogger(Localization.getInstance().get("name")).info("$" + Thread.currentThread().getId() + " " + Localization.getInstance().get("thread_done1"));
        }

        public long getRuntime() {
            return this.runtime;
        }

        public ConcurrentHashMap<String, String> getOutput() {
            return this.output;
        }

    }

}
