package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class FlatMappedModel {

    @CursorName("aInt")
    public int aInt;
    @CursorName("aLong")
    public long aLong;
    @CursorName("aShort")
    public short aShort;
    @CursorName("aByte")
    public byte aByte;
    @CursorName("aFloat")
    public float aFloat;
    @CursorName("aDouble")
    public double aDouble;
    @CursorName("aBoolean")
    public boolean aBoolean;
    @CursorName("aStringBoolean")
    public boolean aStringBoolean;
    @CursorName("aString")
    public String aString;
    @CursorName("aNullString")
    public String aNullString;
    @CursorName("aEnum")
    public SomeEnum aEnum;
    @CursorName("aByteArray")
    public byte[] aByteArray;

    public void populate() {
        aInt = 5;
        aLong = 10L;
        aShort = 5;
        aByte = (byte) 5;
        aFloat = 10.25f;
        aDouble = 1234d;
        aBoolean = true;
        aStringBoolean = true;
        aString = "string";
        aNullString = null;
        aEnum = SomeEnum.TEST_VALUE;
        aByteArray = "string".getBytes();
    }
}
