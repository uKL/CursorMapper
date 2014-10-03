package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class FlatMappedModel {

    @CursorName("aInt")
    public int aInt = 5;
    @CursorName("aLong")
    public long aLong = 10L;
    @CursorName("aShort")
    public short aShort = 5;
    @CursorName("aByte")
    public byte aByte = (byte) 5;
    @CursorName("aFloat")
    public float aFloat = 10.25f;
    @CursorName("aDouble")
    public double aDouble = 1234d;
    @CursorName("aBoolean")
    public boolean aBoolean = true;
    @CursorName("aString")
    public String aString = "string";
    @CursorName("aNullString")
    public String aNullString = null;
    @CursorName("aEnum")
    public SomeEnum aEnum = SomeEnum.TEST_VALUE;
    @CursorName("aByteArray")
    public byte[] aByteArray = "string".getBytes();
}
