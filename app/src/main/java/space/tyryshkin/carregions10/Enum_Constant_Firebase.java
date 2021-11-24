package space.tyryshkin.carregions10;

public enum Enum_Constant_Firebase {

    FIREBASE_REF_LIST ("list"),
    FIREBASE_REF_VERSION ("version"),

    FIREBASE_LIST_CHILD_CODE ("code"),
    FIREBASE_LIST_CHILD_REGION ("region"),
    FIREBASE_LIST_CHILD_CITY ("city");

    private final String string;

    Enum_Constant_Firebase(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
