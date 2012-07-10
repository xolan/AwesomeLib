package no.xolan.awesomelib.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;

import no.xolan.awesomelib.util.Localization;

public class FileEvaluator extends Evaluator {

    private String engineName;
    private Evaluator evaluator;
    private Charset charset;

    public FileEvaluator() {
        this.evaluator = new StringEvaluator();
        this.setCharset(Charset.defaultCharset());
    }

    public FileEvaluator(Charset charset) {
        this.evaluator = new StringEvaluator();
        this.setCharset(charset);
    }

    public FileEvaluator(String engineName) {
        this.evaluator = new StringEvaluator(engineName);
        this.setCharset(Charset.defaultCharset());
    }

    public FileEvaluator(String engineName, Charset charset) {
        this.evaluator = new StringEvaluator(engineName);
        this.setCharset(charset);
    }

    @Override
    public void expose(Object object, String id) {
        this.evaluator.expose(object, id);
    }

    @Override
    public void evaluate(Path file) {
        file = file.toAbsolutePath();
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = Files.newBufferedReader(file, this.charset);
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.INFO, Localization.getInstance().get("f1") + ": \"" + file + "\".");
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.INFO, Localization.getInstance().get("f2"));
        } catch (IOException ioe) {
            Logger.getLogger(Localization.getInstance().get("name")).log(Level.SEVERE, Localization.getInstance().get("f3") + " \"" + file + "\". " + Localization.getInstance().get("f4"));
            ioe.printStackTrace();
        }

        evaluator.evaluate(content.toString());
    }

    private void setCharset(Charset charset) {
        this.charset = charset;
    }

    public static void main(String[] args) {
        FileEvaluator fe = new FileEvaluator();

        int multipleBy = 2;
        fe.expose(multipleBy, "multipleBy");
        fe.evaluate(Paths.get("./scriptTest.js"));
        System.out.println(multipleBy);
    }

}
