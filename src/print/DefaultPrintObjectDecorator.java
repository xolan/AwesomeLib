package print;


import java.lang.reflect.Field;

public class DefaultPrintObjectDecorator implements PrintObjectDecorator {

    private PrintObject po;

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
        int start = (max - po.getData().getClass().getName().toString().length())/2;
        int end = start + po.getData().getClass().getName().toString().length();

        for (int i = 0; i < start-1 ; i++) {
            sb.append("#");
        }

        sb.append(" " + po.getData().getClass().getName().toString() + " ");

        for (int i = end; i < max-1 ; i++) {
            sb.append("#");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String getOutput() {

        StringBuilder output = new StringBuilder();

        String hdr = "| %"+po.getColumnLength("type")+"S | %-"+po.getColumnLength("name")+"S |%n";
        String row = "| %"+po.getColumnLength("type")+"s | %-"+po.getColumnLength("name")+"s |%n";

        output.append(this.getRowSeparator());
        output.append(this.getHeader());
        output.append(this.getRowSeparator());
        output.append(String.format(hdr, "type", "name"));
        output.append(this.getRowSeparator());
        for(Field f : po.getData().getClass().getDeclaredFields()) {
            output.append(String.format(row, f.getType(), f.getName()));
        }
        output.append(this.getRowSeparator());
        output.append(String.format(hdr, "type", "name"));
        output.append(this.getRowSeparator());

        return output.toString();
    }
}
