package space.tyryshkin.carregions10;

public enum Enum_Constant_Settings {

    APP_SETTING_MODE ("APP_SETTING_MODE"),
    APP_VERSION ("APP_VERSION"),
    APP_THEME ("APP_THEME");

    private final String string;

    Enum_Constant_Settings(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
