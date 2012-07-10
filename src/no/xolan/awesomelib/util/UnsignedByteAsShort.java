package no.xolan.awesomelib.util;

/**
 * Created with IntelliJ IDEA. User: Jonas Date: 05.05.12 Time: 00:56 To change
 * this template use File | Settings | File Templates.
 */
public class UnsignedByteAsShort {

    private final byte value;
    private final short result;

    public UnsignedByteAsShort(byte value) {
        this.value = value;
        this.result = convert(value);
    }

    public UnsignedByteAsShort(int value) {
        this.value = ((Integer) value).byteValue();
        this.result = convert(this.value);
    }

    private short convert(byte value) {
        return (short) ((short) value & (short) 0xFF);
    }

    public byte getOriginalByteValue() {
        return this.value;
    }

    public short getValue() {
        return this.result;
    }

    public String toString() {
        return Short.toString(this.result);
    }

}
