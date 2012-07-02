package script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class StringEvaluator {

    private ScriptEngineManager factory;
    private ScriptEngine engine;

    public StringEvaluator() {
        this.factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("JavaScript");
    }

    public StringEvaluator(ScriptEngine engine) {
        this.factory = new ScriptEngineManager();
        this.engine = engine;
    }

    public StringEvaluator(String engineName) {
        this.factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName(engineName);
    }

    public void evaluate(String input) throws ScriptException {
        this.engine.eval(input);
    }

    public void expose(Object object, String id) {
        this.engine.put(id, object);
    }

    public static void main(String[] args) throws ScriptException {
        StringEvaluator se = new StringEvaluator("JavaScript");
        se.evaluate("print('Hello, World');");
    }

}
