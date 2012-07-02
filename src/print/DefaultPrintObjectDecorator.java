package print;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultPrintObjectDecorator implements PrintObjectDecorator {

    private final PrintObject po;

    public DefaultPrintObjectDecorator(PrintObject po) {
        this.po = po;
    }

    @Override
    public int getRowLength() {
        int rowLength = 0;
        for(Integer i : po.getColumnsLengths().values()) {
            rowLength += i;
        }
        rowLength += (po.getColumnsLengths().values().size())*3;
        rowLength += 1;
        return rowLength;
    }

    @Override
    public String getRowSeparator() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.getRowLength(); i++) {
            sb.append("-");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        int max = this.getRowLength();
        int start = (max - po.getData().getClass().getName().length())/2;
        int end = start + po.getData().getClass().getName().length();

        for (int i = 0; i < start-1 ; i++) {
            sb.append("#");
        }

        sb.append(" ").append(po.getData().getClass().getName()).append(" ");

        for (int i = end; i < max-1 ; i++) {
            sb.append("#");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String getSubHeader(String subHeader) {
        StringBuilder sb = new StringBuilder();
        int max = this.getRowLength();
        int start = (max - subHeader.length())/2;
        int end = start + subHeader.length();

        for (int i = 0; i < start-1 ; i++) {
            sb.append("=");
        }

        sb.append(" ").append(subHeader).append(" ");

        for (int i = end; i < max-1 ; i++) {
            sb.append("=");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String getOutput() {

        try {
            StringBuilder output = new StringBuilder();

            //TODO: Clean this up. Remember to remove the +13 once everything works correctly.
            String fhdr = "| %"+po.getColumnLength("type")+"S | %-"+po.getColumnLength("name")+"S | %-"+po.getColumnLength("value")+"S |%n";
            String frow = "| %"+po.getColumnLength("type")+"s | %-"+po.getColumnLength("name")+"s | %-"+po.getColumnLength("value")+"s |%n";
            String mhdr = "| %"+po.getColumnLength("type")+"S | %-"+(po.getColumnLength("name")+13)+"S |%n";
            String mrow = "| %"+po.getColumnLength("type")+"s | %-"+(po.getColumnLength("name")+13)+"s |%n";

            output.append(this.getRowSeparator());
            output.append(this.getHeader());
            output.append(this.getRowSeparator());
            output.append(this.getSubHeader("Fields"));
            output.append(String.format(fhdr, "type", "name", "value"));
            output.append(this.getRowSeparator());
            for(Field f : po.getFields()) {
                output.append(String.format(frow, f.getType(), f.getName(), "value here"));
            }
            output.append(this.getSubHeader("Methods"));
            output.append(String.format(mhdr, "return type", "name"));
            output.append(this.getRowSeparator());
            for(Method m : po.getMethods()) {
                output.append(String.format(mrow, m.getReturnType(), m.getName()));
            }
            output.append(this.getRowSeparator());

            return output.toString();
        } catch (Exception e) {
            System.err.println(e);
            return "";
        }

    }
}
