package util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA. User: Jonas Date: 05.05.12 Time: 01:21 To change
 * this template use File | Settings | File Templates.
 */
public class UnsignedByteAsShortTest {
    @Test
    public void testGetOriginalByteValue() throws Exception {
        byte b = -1;
        byte b2 = 127;
        Assert.assertEquals(b,
                new UnsignedByteAsShort(b).getOriginalByteValue());
        Assert.assertEquals(b2,
                new UnsignedByteAsShort(b2).getOriginalByteValue());
    }

    @Test
    public void testGetValue() throws Exception {
        int i = 255;
        byte b = ((Integer) 255).byteValue();

        Assert.assertEquals((short) i, new UnsignedByteAsShort(i).getValue());
        Assert.assertFalse((short) b == new UnsignedByteAsShort(b).getValue());
    }
}
