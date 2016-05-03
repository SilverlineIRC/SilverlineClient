package gui.components;

/**
 * Enumeration for fonts that this theme supports.
 * @author kulttuuri
 */
public enum ThemeFont {
    HELVETICA_NEUE("Helvetica Neue");
    
    private final String text;

    private ThemeFont(final String text) {
	this.text = text;
    }

    @Override
    public String toString() {
	return text;
    }
}