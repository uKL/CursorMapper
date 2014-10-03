package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class NestedMappedModel {

    @CursorName("secondInt")
    public int aSecondInt = 5;
    @CursorName
    public FlatMappedModel nested = new FlatMappedModel();
}
