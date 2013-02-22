package bpi.most.client;


/**
 * 
 * The Class Config. default configuration constants
 * 
 * Will be replaced/moved soon. 
 * 
 * @author sg
 * @deprecated
 */
public class GConfig {
  
  /** The Constant ICON_PATH. */
  public static final String ICON_PATH = "public/img/icons/";
  /** The Constant DEFAULT_ICON. */
  public static final String DEFAULT_ICON = "icon_default.jpeg";
  public static final String UI_DRAG_PREFIX = "uid-";
  public static final String UI_PREFIX = "ui-";
  public static final String DWIDGET_PREFIX = "dWidget-";
  // public static final String splitter = "-";
  public static final String BLANK_SCREEN = "dragonsupermaster";
  public static final String DND_HIGHLIGHT_DRAGGED = "zindex";
  public static final String DND_HIGHLIGHT_DROPABLE = "dragonmaster"; 
  public static final String DND_HIGHLIGHT_HELPER = "dragonslave";
  public static final String LINK_DESKTOP = "ui-desktop-link";
  public static final String ACTIVE = "active";
  
  public static final String ID_DESKTOP = "desktop";
  public static final String ID_PERSON = "person";
  public static final String ID_EXPORT = "export";
  public static final String ID_LIVE_CHART = "livechart";
  public static final String ID_D3 = "d3";
  public static final String ID_NEW_AP = "newAP";
  
  /**
   * @return the idPerson
   */
  public static String getIdPerson() {

    return ID_PERSON;
  }
  
  /**
   * @return the idExport
   */
  public static String getIdExport() {

    return ID_EXPORT;
  }
  
  /**
   * @return the idLiveChart
   */
  public static String getIdLiveChart() {

    return ID_LIVE_CHART;
  }
  
  /**
   * @return the idNewAP
   */
  public static String getIdNewAP() {

    return ID_NEW_AP;
  }
  
  /**
   * @return the iddesktop
   */
  public static String getIdDesktop() {

    return ID_DESKTOP;
    // return DesktopModule.getInstance().getModuleName();
  }
  
  // public static final String UID_SUPERMASTER;
  public static GConfig ref = null;
  
  public static GConfig getInstance() {

    if (ref == null) {
      ref = new GConfig();
    }
    return ref;
  }
  
  /**
   * @return the defaultIcon
   */
  public static String getDefaultIcon() {

    return DEFAULT_ICON;
  }
  
  /**
   * @return the uiDragPrefix
   */
  public static String getUiDragPrefix() {

    return UI_DRAG_PREFIX;
  }
  
  /**
   * @return the uiPrefix
   */
  public static String getUiPrefix() {

    return UI_PREFIX;
  }
  
  /**
   * @return the splitter
   */
  // public static String getSplitter() {
  //
  // return splitter;
  // }
  /**
   * @return the dragonsupermaster
   */
  public static String getBlankScreen() {

    return BLANK_SCREEN;
  }
  

public static String getDndHighlightDragged() {
	return DND_HIGHLIGHT_DRAGGED;
}

public static String getDndHighlightDropable() {
	return DND_HIGHLIGHT_DROPABLE;
}

public static String getDndHighlightHelper() {
	return DND_HIGHLIGHT_HELPER;
}

public static String getDwidgetPrefix() {
	return DWIDGET_PREFIX;
}

public static String getIdD3() {
	return ID_D3;
}
}
