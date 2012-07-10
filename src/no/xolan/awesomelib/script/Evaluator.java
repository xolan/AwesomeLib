package no.xolan.awesomelib.script;

import java.nio.file.Path;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public abstract class Evaluator {

    protected ScriptEngineManager factory;
    protected ScriptEngine engine;

    public Evaluator() {
        this.factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("JavaScript");
    }

    public void evaluate(String input) {
    }

    public void evaluate(Path path) {
    }

    public void expose(Object object, String id) {
    }

}
