package de.p2tools.p2lib.dialogs;

public class WhatsNewInfo {
    private final String image;
    private final String header;
    private final String text;
    private final int taHeight;

    public WhatsNewInfo(String image, String header, String text, int taHeight) {
        this.image = image;
        this.header = header;
        this.text = text;
        this.taHeight = taHeight;
    }

    public String getImage() {
        return image;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public int getTaHeight() {
        return taHeight;
    }
}
