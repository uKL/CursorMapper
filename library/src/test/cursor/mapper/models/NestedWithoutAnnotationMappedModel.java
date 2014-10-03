package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class NestedWithoutAnnotationMappedModel {

    @CursorName("secondInt")
    public int aSecondInt = 5;
    public FlatMappedModel nested;
}
