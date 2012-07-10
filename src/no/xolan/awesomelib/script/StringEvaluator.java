package no.xolan.awesomelib.script;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import no.xolan.awesomelib.util.Localization;

public class StringEvaluator extends Evaluator {

    public StringEvaluator() {
        super();
    }

    public StringEvaluator(ScriptEngine engine) {
        this.factory = new ScriptEngineManager();
        this.engine = engine;
    }

    public StringEvaluator(String engineName) {
        this.factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName(engineName);
    }

    @Override
    public void evaluate(String input) {
        try {
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.INFO, Localization.getInstance().get("s1") + " \"" + this.engine.NAME + "\".");
            this.engine.eval(input);
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.INFO, Localization.getInstance().get("s2"));
        } catch (ScriptException e) {
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.INFO, Localization.getInstance().get("s3"));
            e.printStackTrace();
        }
    }

    @Override
    public void expose(Object object, String id) {
        this.engine.put(id, object);
    }

    public static void main(String[] args) throws ScriptException {
        StringEvaluator se = new StringEvaluator();
        se.evaluate("print('Hello, World')");
    }

}
