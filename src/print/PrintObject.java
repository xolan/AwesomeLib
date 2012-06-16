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

    public int getRowLength() {
        int rowLength = 0;
        for(Integer i : this.colLengths.values()) {
            rowLength += i;
        }
        rowLength += (this.colLengths.values().size())*3;
        rowLength += 1;
        return rowLength;
    }

    public String getRowSeparator() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.getRowLength(); i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    public int getColLength(String key) {
        return this.colLengths.get(key);
    }

    public Object getData() {
        return this.data;
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        int max = this.getRowLength();
        int start = (max - this.getData().getClass().getName().toString().length())/2;
        int end = start + this.getData().getClass().getName().toString().length();

        for (int i = 0; i < start-1 ; i++) {
            sb.append("#");
        }

        sb.append(" " + this.getData().getClass().getName().toString() + " ");

        for (int i = end; i < max-1 ; i++) {
            sb.append("#");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        //String derp = new String("derp");
        PrintObject derp = new PrintObject("hello");
        PrintObject po = new PrintObject(derp);

        String hdr = "| %"+po.getColLength("type")+"S | %-"+po.getColLength("name")+"S |%n";
        String row = "| %"+po.getColLength("type")+"s | %-"+po.getColLength("name")+"s |%n";

        System.out.println(po.getRowSeparator());
        System.out.println(po.getHeader());
        System.out.println(po.getRowSeparator());
        System.out.format(hdr, "type", "name");
        System.out.println(po.getRowSeparator());
        for(Field f : po.getData().getClass().getDeclaredFields()) {
            System.out.format(row, f.getType(), f.getName());
        }
        System.out.println(po.getRowSeparator());
        System.out.format(hdr, "type", "name");
        System.out.println(po.getRowSeparator());
        //System.out.print(new PrintObject(derp));
    }

}
