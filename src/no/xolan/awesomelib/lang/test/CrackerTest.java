package no.xolan.awesomelib.lang.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import junit.framework.Assert;

import no.xolan.awesomelib.lang.Cracker;
import no.xolan.awesomelib.lang.Cracker.METHOD;
import no.xolan.awesomelib.util.Localization;

import org.junit.Test;

public class CrackerTest {
    
    private ConcurrentHashMap<String, String> comparePlain = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> compareMD5 = new ConcurrentHashMap<>();
    private Cracker crack;
    
    private void setup() {
        crack = new Cracker();
        List<String> inputs = generateTestList((long) Math.pow(2, 4));
        crack.addInputs(inputs);

        ArrayList<String> targets = new ArrayList<>();
        for (int i = 2; i < 100; i++) {
            targets.add(inputs.get(inputs.size() / i));
            targets.add(crack.toMd5(inputs.get(inputs.size() / i)));
        }
        crack.addTargets(targets);
        
        //PLAIN
        comparePlain.put("0", "0");
        comparePlain.put("1", "1");
        comparePlain.put("2", "2");
        comparePlain.put("3", "3");
        comparePlain.put("4", "4");
        comparePlain.put("5", "5");
        comparePlain.put("8", "8");
        
        //MD5
        compareMD5.put("0", "cfcd208495d565ef66e7dff9f98764da");
        compareMD5.put("1", "c4ca4238a0b923820dcc509a6f75849b");
        compareMD5.put("2", "c81e728d9d4c2f636f067f89cc14862c");
        compareMD5.put("3", "eccbc87e4b5ce2fe28308fd9f2a7baf3");
        compareMD5.put("4", "a87ff679a2f3e71d9181a67b7542122c");
        compareMD5.put("5", "e4da3b7fbbce2345d7772b0674a318d5");
        compareMD5.put("8", "c9f0f895fb98ab9159f51fd0297e236d");
    }
    
    public List<String> generateTestList(long maxSize) {
        List<String> tests = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            if((100*((float)(i)/(maxSize-1))) % 1 == 0) {
                Logger.getLogger(Localization.getInstance().get("name")).info("Loading: " + (100*((float)(i)/(maxSize-1))) + "%");    
            }
            tests.add(Integer.toString(i));
        }
        return tests;
    }
    
    private void runAssertions(Cracker.METHOD method) {
        switch(method) {
        case PLAIN:
            Assert.assertEquals(comparePlain.size(), crack.getResults().size());
            for(String s : comparePlain.keySet()) {
                Assert.assertEquals(true, comparePlain.keySet().contains(s));
                Assert.assertEquals(true, crack.getResults().get(s).equals(comparePlain.get(s)));
            }
            break;
        case MD5:
            Assert.assertEquals(compareMD5.size(), crack.getResults().size());
            for(String s : compareMD5.keySet()) {
                Assert.assertEquals(true, compareMD5.keySet().contains(s));
                Assert.assertEquals(true, crack.getResults().get(s).equals(compareMD5.get(s)));
            }
            break;
        case ALL:
            break;
        case CUSTOM:
            break;
        default:
            break;
        }
    }
    
    @Test
    public void testSingleThreaded() {
        setup();

        crack.setMethod(Cracker.METHOD.PLAIN);
        crack.runSingleThreaded();
        runAssertions(Cracker.METHOD.PLAIN);
    }
    
    @Test
    public void testMultiThreaded() {
        setup();
        
        crack.setMethod(Cracker.METHOD.MD5);
        crack.runMultiThreaded(4);
        runAssertions(Cracker.METHOD.MD5);
    }
    
}
