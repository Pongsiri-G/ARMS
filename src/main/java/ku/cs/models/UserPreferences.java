package ku.cs.models;

public class UserPreferences {
    private String theme;
    private String fontSize;
    private String fontFamily;

    public UserPreferences() {
        this.theme = "Light"; 
        this.fontSize = "Medium"; 
        this.fontFamily = "Noto Sans Thai"; 
    }

    public UserPreferences(String theme, String fontSize, String fontFamily) {
        this.theme = theme;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }

    
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}
