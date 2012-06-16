package print;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class PrintObject {

    private Object data;
    private Field[] fields;
    private Method[] methods;
    private HashMap<String, Integer> colLengths;
    private PrintObjectDecorator decorator;

    public PrintObject(Object data) {
        this.data = data;
        this.fields = this.data.getClass().getDeclaredFields();
        this.methods = this.data.getClass().getDeclaredMethods();
        this.colLengths = new HashMap<>();

        this.colLengths.put("name", 0);
        this.colLengths.put("type", 0);
        try {
            for(Field f : this.fields) {
                if(this.colLengths.get("name") < f.getName().toString().length()) colLengths.put("name", f.getName().toString().length());
                if(this.colLengths.get("type") < f.getType().toString().length()) colLengths.put("type", f.getType().toString().length());
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public PrintObject(Object data, PrintObjectDecorator decorator) {
        this(data);
        this.decorator = decorator;
    }

    public HashMap<String, Integer> getColumnsLengths() {
        return this.colLengths;
    }

    public int getColumnLength(String key) {
        return this.colLengths.get(key);
    }

    public Object getData() {
        return this.data;
    }

    public PrintObjectDecorator getDecorator() {
        return decorator;
    }

    public void setDecorator(PrintObjectDecorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public String toString() {
        if(this.decorator != null) {
            return this.decorator.getOutput();
        } else {
            try {
                throw new Exception("PrintObjectDecorator not set.");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }
    }

    public static void main(String[] args) {
        PrintObject derp = new PrintObject("derp");
        PrintObject po = new PrintObject(derp);
        po.setDecorator(new DefaultPrintObjectDecorator(po));
        System.out.println(po);
    }

}
