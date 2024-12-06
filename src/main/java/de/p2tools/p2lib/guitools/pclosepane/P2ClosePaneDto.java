package de.p2tools.p2lib.guitools.pclosepane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

public class P2ClosePaneDto {
    public Pane infoPane;
    public BooleanProperty PANE_INFO_IS_RIP;
    public StringProperty DIALOG_INFO_SIZE;
    public BooleanProperty tabOn;
    public String textDialog;
    public String textInfo;
    public boolean vertical;
    public BooleanProperty maskerVis;

    public P2ClosePaneDto(Pane infoPane /* ist die Pane des Infos/Filters */,
                          BooleanProperty PANE_INFO_IS_RIP /* Info/Filter ist extern sichtbar */,
                          StringProperty DIALOG_INFO_SIZE /* Größe des externen Dialogs */,
                          BooleanProperty tabOn /* im Programm ist der Tab (Filme, ..) ausgewählt bei dem das angezeigt wird */,
                          String textDialog /* ist der Titel */,
                          String textInfo /* ist der Text im Tab */,
                          boolean vertical /* vertical/horizontal */,
                          BooleanProperty maskerVis /* masker ist sichtbar */) {
        this.infoPane = infoPane;
        this.PANE_INFO_IS_RIP = PANE_INFO_IS_RIP;
        this.DIALOG_INFO_SIZE = DIALOG_INFO_SIZE;
        this.tabOn = tabOn;
        this.textDialog = textDialog;
        this.textInfo = textInfo;
        this.vertical = vertical;
        this.maskerVis = maskerVis;
    }

    public P2ClosePaneDto(Pane infoPane,
                          BooleanProperty PANE_INFO_IS_RIP,
                          StringProperty DIALOG_INFO_SIZE, BooleanProperty tabOn,
                          String textDialog, String textInfo, boolean vertical) {
        this.infoPane = infoPane;
        this.PANE_INFO_IS_RIP = PANE_INFO_IS_RIP;
        this.DIALOG_INFO_SIZE = DIALOG_INFO_SIZE;
        this.tabOn = tabOn;
        this.textDialog = textDialog;
        this.textInfo = textInfo;
        this.vertical = vertical;
        this.maskerVis = null;
    }
}
