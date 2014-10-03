package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class FlatMappedModelWithPrivateFields {

    @CursorName("aInt")
    private int aInt = 5;

    public int getPrivateFieldValue() {
        return aInt;
    }
}
