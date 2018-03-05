package kr.co.aperturedev.petcommunity.modules.obj;

/**
 * Created by 5252b on 2018-03-05.
 */

public class PetTypeObject {
    String typeName;
    int typeUUID;

    public PetTypeObject(String typeName, int typeUUID) {
        this.typeName = typeName;
        this.typeUUID = typeUUID;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeUUID() {
        return typeUUID;
    }
}
