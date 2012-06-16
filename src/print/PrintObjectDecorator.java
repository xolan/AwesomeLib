package print;

public interface PrintObjectDecorator {

    public int getRowLength();
    public String getRowSeparator();
    public String getHeader();
    public String getOutput();

}
