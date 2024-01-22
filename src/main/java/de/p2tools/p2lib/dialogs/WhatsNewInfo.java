package de.p2tools.p2lib.dialogs;

public class WhatsNewInfo {
    private String image;
    private String header;
    private String text;

    public WhatsNewInfo(String image, String header, String text) {
        this.image = image;
        this.header = header;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
