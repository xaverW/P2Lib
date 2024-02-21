package de.p2tools.p2lib.dialogs;

import java.time.LocalDate;

public class WhatsNewInfo {
    private final LocalDate date;
    private final String image;
    private final String header;
    private final String text;
    private final int taHeight;

    public WhatsNewInfo(LocalDate date, String image, String header, String text, int taHeight) {
        this.date = date;
        this.image = image;
        this.header = header;
        this.text = text;
        this.taHeight = taHeight;
    }

    public LocalDate getDate() {
        return date;
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
