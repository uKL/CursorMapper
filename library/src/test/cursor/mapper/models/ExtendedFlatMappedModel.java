package cursor.mapper.models;

import cursor.mapper.annotation.CursorName;

public class ExtendedFlatMappedModel extends FlatMappedModel {

    @CursorName("addtitionalValue")
    public String aAdditionalString = "value";
}
