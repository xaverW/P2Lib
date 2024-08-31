package de.p2tools.p2lib.icons;

import de.p2tools.p2lib.P2LibConst;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class P2ImageView extends ImageView {

    private final P2Image p2Image;

    public P2ImageView(P2Image p2Image, Image image) {
        super(image);
        this.p2Image = p2Image;
        setColor();
    }

    public void setColor() {
        Image img = p2Image.getImage();
        if (img == null) {
            return;
        }

        if (P2LibConst.blackWhite.get()) {
            Image imgBw = p2Image.getBwImage();
            if (imgBw != null) {
                setImage(imgBw);
                setClip(new ImageView(imgBw));
            }
        } else {
            setImage(img);
            setClip(new ImageView(img));
        }
    }
}
