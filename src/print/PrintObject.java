package print;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class PrintObject {

    final Object data;
    private final Field[] fields;
    private final Method[] methods;
    private final HashMap<String, Integer> colLengths;
    private PrintObjectDecorator decorator;

    public PrintObject(Object data) {
        this.data = data;
        this.fields = this.data.getClass().getDeclaredFields();
        this.methods = this.data.getClass().getDeclaredMethods();
        this.colLengths = new HashMap<>();

        this.colLengths.put("name", 4);
        this.colLengths.put("type", 4);
        this.colLengths.put("value", 10);
        try {
            for(Field f : this.fields) {
                if(this.colLengths.get("name") < f.getName().length()) colLengths.put("name", f.getName().length());
                if(this.colLengths.get("type") < f.getType().toString().length()) colLengths.put("type", f.getType().toString().length());
               // if(this.colLengths.get("value") < f.get(data).toString().length()) colLengths.put("value", f.get(data).toString().length());
            }
            for(Method m : this.methods) {
                if(this.colLengths.get("name") < m.getName().length()) colLengths.put("name", m.getName().length());
                if(this.colLengths.get("type") < m.getReturnType().toString().length()) colLengths.put("type", m.getReturnType().toString().length());
            }
        } catch(Exception iae) {
            System.err.println(iae);
        }
    }

    public PrintObject(Object data, PrintObjectDecorator decorator) {
        this(data);
        this.decorator = decorator;
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
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
        PrintObject po = new PrintObject(System.out);
        po.setDecorator(new DefaultPrintObjectDecorator(po));
        System.out.println(po);
    }

}
