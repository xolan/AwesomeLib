package no.xolan.awesomelib.print;

public interface PrintObjectDecorator {

    public int getRowLength();

    public String getRowSeparator();

    public String getHeader();

    public String getSubHeader(String subHeader);

    public String getOutput();

}
