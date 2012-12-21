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
  public static final String blankScreen = "dragonsupermaster";
  public static final String dndHighlightDragged = "zindex";
  public static final String dndHighlightDropable = "dragonmaster"; 
  public static final String dndHighlightHelper = "dragonslave";
  public static final String linkDesktop = "ui-desktop-link";
  public static final String active = "active";
  
  public static final String idDesktop = "desktop";
  public static final String idPerson = "person";
  public static final String idExport = "export";
  public static final String idLiveChart = "livechart";
  public static final String idD3 = "d3";
  public static final String idNewAP = "newAP";
  
  /**
   * @return the idPerson
   */
  public static String getIdPerson() {

    return idPerson;
  }
  
  /**
   * @return the idExport
   */
  public static String getIdExport() {

    return idExport;
  }
  
  /**
   * @return the idLiveChart
   */
  public static String getIdLiveChart() {

    return idLiveChart;
  }
  
  /**
   * @return the idNewAP
   */
  public static String getIdNewAP() {

    return idNewAP;
  }
  
  /**
   * @return the iddesktop
   */
  public static String getIdDesktop() {

    return idDesktop;
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

    return blankScreen;
  }
  

public static String getDndHighlightDragged() {
	return dndHighlightDragged;
}

public static String getDndHighlightDropable() {
	return dndHighlightDropable;
}

public static String getDndHighlightHelper() {
	return dndHighlightHelper;
}

public static String getDwidgetPrefix() {
	return DWIDGET_PREFIX;
}

public static String getIdD3() {
	return idD3;
}
}
