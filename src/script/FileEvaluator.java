package script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;

public class FileEvaluator {

    private String engineName;
    private StringEvaluator evaluator;
    private Charset charset;

    public FileEvaluator() {
        this.engineName = "JavaScript";
        this.evaluator = new StringEvaluator(this.engineName);
        this.setCharset(Charset.defaultCharset());
    }

    public FileEvaluator(Charset charset) {
        this.engineName = "JavaScript";
        this.evaluator = new StringEvaluator(this.engineName);
        this.setCharset(charset);
    }

    public void expose(Object object, String id) {
        this.evaluator.expose(object, id);
    }

    public void evaluate(Path file) throws ScriptException {
        file = file.toAbsolutePath();
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = Files.newBufferedReader(file, this.charset);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Reading file: \"" + file + "\".");
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "File successfully read.");
        } catch (IOException ioe) {
            Logger.getLogger(this.getClass().getName()).log(
                    Level.SEVERE,
                    "Failed to read file: \"" + file
                            + "\". Are you sure is exists?");
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
        try {
            fe.evaluate(Paths.get("./scriptTest.js"));
            System.out.println(multipleBy);
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
