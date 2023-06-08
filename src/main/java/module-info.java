module de.p2tools.p2lib {

    exports de.p2tools.p2lib;
    exports de.p2tools.p2lib.alert;
    exports de.p2tools.p2lib.checkforactinfos;
    exports de.p2tools.p2lib.checkforupdates;
    exports de.p2tools.p2lib.configfile;
    exports de.p2tools.p2lib.configfile.config;
    exports de.p2tools.p2lib.configfile.configlist;
    exports de.p2tools.p2lib.configfile.pdata;
    exports de.p2tools.p2lib.data;
    exports de.p2tools.p2lib.dialogs;
    exports de.p2tools.p2lib.dialogs.accordion;
    exports de.p2tools.p2lib.dialogs.dialog;
    exports de.p2tools.p2lib.guitools;
    exports de.p2tools.p2lib.guitools.pcheckcombobox;
    exports de.p2tools.p2lib.guitools.pclosepane;
    exports de.p2tools.p2lib.guitools.pfiltercontrol;
    exports de.p2tools.p2lib.guitools.pmask;
    exports de.p2tools.p2lib.guitools.pnotification;
    exports de.p2tools.p2lib.guitools.prange;
    exports de.p2tools.p2lib.guitools.ptable;
    exports de.p2tools.p2lib.guitools.ptipofday;
    exports de.p2tools.p2lib.guitools.ptoggleswitch;
    exports de.p2tools.p2lib.hash;
    exports de.p2tools.p2lib.icons;
    exports de.p2tools.p2lib.image;
    exports de.p2tools.p2lib.mtdownload;
    exports de.p2tools.p2lib.mtfilm.film;
    exports de.p2tools.p2lib.mtfilm.loadfilmlist;
    exports de.p2tools.p2lib.mtfilm.readwritefilmlist;
    exports de.p2tools.p2lib.mtfilm.tools;
    exports de.p2tools.p2lib.mtfilter;
    exports de.p2tools.p2lib.tools;
    exports de.p2tools.p2lib.tools.date;
    exports de.p2tools.p2lib.tools.duration;
    exports de.p2tools.p2lib.tools.events;
    exports de.p2tools.p2lib.tools.file;
    exports de.p2tools.p2lib.tools.log;
    exports de.p2tools.p2lib.tools.net;
    exports de.p2tools.p2lib.tools.shortcut;

    requires javafx.controls;

    requires java.logging;
    requires java.desktop;

    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    
    requires org.tukaani.xz;
    requires com.fasterxml.jackson.core;
    
    requires okhttp3;
    requires okio;
    requires kotlin.stdlib;    
}
