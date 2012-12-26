package bpi.most.client.modules.d3viewer;

import java.util.ArrayList;
import java.util.Date;
import bpi.most.client.mainlayout.RootModule;
import bpi.most.client.model.Datapoint;
import bpi.most.client.model.DatapointHandler;
import bpi.most.client.modules.ModuleController;
import bpi.most.client.modules.ModuleInterface;
import bpi.most.client.modules.ModuleWidget;
import bpi.most.client.modules.charts.ChartWrapper;
import bpi.most.client.utils.JavaScriptInjector;
import bpi.most.client.utils.dnd.DNDController;
import bpi.most.client.utils.dnd.DropWidget;
import bpi.most.client.utils.dnd.MostDragEndEvent;
import bpi.most.client.utils.ui.DateTimePickerBox;
import bpi.most.client.utils.ui.DeleteableDragWidget;
import bpi.most.client.utils.ui.DpWidget;
import bpi.most.client.utils.ui.jSlider.Slider;
import bpi.most.client.utils.ui.jSlider.SliderSlideHandler;
import bpi.most.shared.DpDatasetDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * 
 * The Class D3ModuleWidget. Creates the 3D Module
 * 
 * @author sg
 */
public final class D3ModuleWidget extends ModuleWidget {

	private static D3ModuleWidget ref = null;

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private static final int ZOOM_BAR_MAX_VALUE = 20;
	private static final int ZOOM_BAR_VALUE = 15;
	private static final int TRANSPARANCY_BAR_MAX_VALUE = 100;
	private static final int TRANSPARANCY_BAR_VALUE = 100;
	private static final int EXPOSE_BAR_MAX_VALUE = 150;
	private static final int VIEW_TOP_LEVEL = 3;
	private static final int ZOOM_LEVEL_ABSOLUTE = 15;
	private static final int TRIM_BEGIN_INDEX = 3;
	private static final long START_TIME_DATE = 1304589600000L;			
	private static final long END_TIME_DATE = 86400000L;
	
	private static ArrayList<DpWidget> activeDpWidgets = new ArrayList<DpWidget>();
	private static int zoomLevel = 0;
	private static int transparencyLevel = 0;
	private static Slider zoomBar = new Slider("zoomBar", 0, ZOOM_BAR_MAX_VALUE);
	private static Slider transparencyBar = new Slider("transparencyBar", 0,
			TRANSPARANCY_BAR_MAX_VALUE);
	private static Slider exposeBar = new Slider("exposeBar", 0, EXPOSE_BAR_MAX_VALUE);
	private boolean injected = false;

	/**
	 * The unique module panel. everything is rendered in this panel. originally
	 * included to ensure dnd functionality.
	 */
	@UiField
	AbsolutePanel demPanel;
	@UiField
	Anchor resetView;
	@UiField
	static FlowPanel panviewchanger;
	@UiField
	Anchor pan;
	@UiField
	Anchor rotate;
	@UiField
	static FlowPanel mainViewControlsLeft;
	@UiField
	static FlowPanel controlsDp;
	@UiField
	static FlowPanel controlsError;
	@UiField
	public static DropWidget dpPreview;
	@UiField
	FlowPanel transparencySlider;
	@UiField
	FlowPanel exposeSlider;
	@UiField
	FlowPanel zoomSlider;
	// @UiField
	// static FlowPanel mainViewControlsLeft1;
	@UiField
	static FlowPanel mainViewControls;
	@UiField
	static FlowPanel controlsView;
	@UiField
	Anchor viewDefault;
	@UiField
	Anchor viewFront;
	@UiField
	Anchor viewSide;
	@UiField
	Anchor viewTop;

	// @UiField
	// FlowPanel controlsAccordion;
	// @UiField
	// FlowPanel controlsAccordionObjects;
	// @UiField
	// FormPanel controlsRelationships;
	// @UiField
	// FormPanel controlsLayers;

	interface Binder extends UiBinder<Widget, D3ModuleWidget> {
	}

	/**
	 * Instantiates a new d3 module widget.
	 * 
	 * @param module
	 *            the module
	 */
	private D3ModuleWidget() {
		super();
	}

	private D3ModuleWidget(ModuleInterface module) {
		super(module);

		initWidget(BINDER.createAndBindUi(this));
		initZoomLevel();
		initClickEventDatapointMarked();
		initZoomLevelAbsolute();
		initControls();
		initClickEventDatapointMove();
		initcallbackAddDpWidget();

		zoomBar.setWidth("400px");
		zoomBar.setHorizontal();
		zoomBar.setRange("min");
		zoomBar.setValue(ZOOM_BAR_VALUE);

		transparencyBar.setWidth("400px");
		transparencyBar.setHorizontal();
		transparencyBar.setRange("min");
		transparencyBar.setValue(TRANSPARANCY_BAR_VALUE);
		exposeBar.setWidth("400px");
		exposeBar.setHorizontal();
		exposeBar.setRange("max");

		panviewchanger.getElement().setId("pan-view-changer");
		mainViewControlsLeft.getElement().setId("main-view-controls-left");
		// mainViewControlsLeft1.getElement().setId("main-view-controls-left1");
		controlsDp.getElement().setId("controls-dp");
		controlsError.getElement().setId("controls-error");
		mainViewControls.getElement().setId("main-view-controls");
		controlsView.getElement().setId("controls-view");

		transparencySlider.add(transparencyBar);
		exposeSlider.add(exposeBar);
		zoomSlider.add(zoomBar);
		// mainViewControlsLeft1.add(zoomBar);

		// setNavigationMode Pan
		pan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					rotate.removeStyleName("active");
					pan.removeStyleName("active");
					pan.addStyleName("active");
				} catch (Exception e) {
				}

				setNavigationMode(1);
			}

		});
		// setNavigationMode Rotate
		rotate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					pan.removeStyleName("active");
					rotate.removeStyleName("active");
					rotate.addStyleName("active");
				} catch (Exception e) {
				}

				setNavigationMode(0);

			}

		});

		resetView.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setViewToDatapoint(" dp_con17");

			}
		});
		viewDefault.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setView(0);
			}
		});
		viewFront.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setView(1);
			}
		});
		viewSide.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setView(2);
			}
		});
		viewTop.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setView(VIEW_TOP_LEVEL);
			}
		});

		// panviewchanger.setVisible(false);
		// // mainViewControlsLeft.setVisible(false);
		// // mainViewControlsLeft1.setVisible(false);
		// controlsDp.setVisible(false);
		// controlsError.setVisible(false);
		// mainViewControls.setVisible(false);
		// controlsView.setVisible(false);

		transparencyBar.addSliderSlideHandler(new SliderSlideHandler() {

			@Override
			public void onSlide(Event event, int value, JsArrayInteger values) {
				setTransparentLevel(value);
			}
		});
		zoomBar.addSliderSlideHandler(new SliderSlideHandler() {

			@Override
			public void onSlide(Event event, int value, JsArrayInteger values) {
				// Window.alert("slide");
				setZoomLevelAbsolute(zoomBar.getValue());
				// if (zoomLevel < value) {
				//
				// // setZoomLevel(1);
				// System.out.println("+");
				// zoomLevel++;
				// } else {
				// setZoomLevel(-1);
				// System.out.println("-");
				// zoomLevel--;
				// }
			}
		});
		exposeBar.addSliderSlideHandler(new SliderSlideHandler() {

			@Override
			public void onSlide(Event event, int value, JsArrayInteger values) {
				setExposeLevel(value);
			}
		});

		transparencyBar.enable();
		exposeBar.enable();
		zoomBar.enable();

		demPanel.addDomHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				// demPanel.add(DNDController.getCurrentDrag());
			}
		}, DragOverEvent.getType());

		demPanel.addDomHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				// setViewToDatapoint(" dp_"+((DpWidget)DNDController.getCurrentDrag()).getName());
				String dpName = null;
				try {
					// dpName =
					// " dp_"+((DpWidget)DNDController.getCurrentDrag()).getName();
					dpName = ((DpWidget) DNDController.getCurrentDrag())
							.getName();
				} catch (Exception e) {

				}

				DNDController.EVENT_BUS.fireEvent(new MostDragEndEvent());
				if (dpName == null) {
					Window.alert("Data Point not found");
				} else {
					setViewToDatapoint(dpName);
				}

			}
		}, DropEvent.getType());

		demPanel.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				removeActiveDpWidgets();
			}
		}, DropEvent.getType());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach()
	 */
	/**
	 * Injects the Bim surfer Javascript to the end of the body tag. Best
	 * practice to use the BIM surfer
	 * 
	 * replaces
	 * 
	 * {@link bimInit}
	 * 
	 * @see BimSurferInjection
	 * @see JavaScriptInjector
	 */
	public void onAttach() {
		super.onAttach();
		if (!injected) {
			BimSurferInjection js = GWT.create(BimSurferInjection.class);
			JavaScriptInjector.inject(js.scenejsMath().getText());
			JavaScriptInjector.inject(js.scenejsMathExtra().getText());
			JavaScriptInjector.inject(js.scenejs().getText());
			JavaScriptInjector.inject(js.bimSurfer().getText());
			injected = true;
			setZoomLevelAbsolute(ZOOM_LEVEL_ABSOLUTE);
		}
		removeActiveDpWidgets();
	}

	private static void removeActiveDpWidgets() {
		if (activeDpWidgets.size() == 1) {
			activeDpWidgets.get(0).removeFromParent();
			activeDpWidgets.remove(0);
		}
	}

	public void onLoad() {
		super.onLoad();

	}

	/**
	 * Gets the single instance of D3ModuleWidget.
	 * 
	 * @param module
	 *            the module
	 * @return single instance of D3ModuleWidget
	 */
	public static D3ModuleWidget getInstance(ModuleInterface module) {
		if (ref == null) {
			ref = new D3ModuleWidget(module);
		}
		return ref;
	}

	public static D3ModuleWidget getInstance() {
		if (ref == null) {
			ref = new D3ModuleWidget();
		}
		return ref;
	}

	/**
	 * Bim init.
	 * 
	 * @deprecated
	 */
	public static native void bimInit() /*-{
		var bimInit = $wnd.bimInit();
	}-*/;

	/**
	 * The native setView function sets predefined perspective of the model.
	 * 
	 * @param level
	 *            default, top, side, bottom
	 */
	public static native void setView(int level) /*-{
		$wnd.setView(level);

	}-*/;

	// TODO:
	// JavaScript Function @
	// returns DP String
	// trigger DP Preview when dnd applied
	// public static void callbackClickEventDatapointMove(String)

	/**
	 * callback when marking a dp. generates a new preview chart. will be moved
	 * to dpmoveevent
	 * 
	 * @param dpName
	 */
	public static void callbackClickEventDatapointMarked(String dpName) {
		Window.alert(dpName);
		String dp = dpName.trim().substring(TRIM_BEGIN_INDEX);
		int win = dp.indexOf("(");
		if (win > 0) {
			dp = dp.substring(0, win);
		}
		final ChartWrapper cwtemp = new ChartWrapper(new DateTimePickerBox(
				new Date(START_TIME_DATE)).date);
		DeleteableDragWidget drag = new DeleteableDragWidget(cwtemp,
				new String[] { "dWidget-uid-livechart", "dWidget-uid-desktop",
						"dWidget-uid-3d" }, null, null);
		Datapoint dptemp = ModuleController.DPCC.getDatapoint(dp);
		dptemp.getData(new Date(START_TIME_DATE), new Date(
				START_TIME_DATE + END_TIME_DATE), new DatapointHandler(
				getInstance(), dptemp) {

			@Override
			public void onSuccess(DpDatasetDTO result) {
				cwtemp.addCurve(result, true);
			}
		});
		dpPreview.addDraggable(drag);
	}

	/**
	 * attaches the callbackClickEventDatapointMarked function to the window
	 * object
	 */
	public static native void initClickEventDatapointMarked() /*-{
																$wnd.callbackClickEventDatapointMarked = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackClickEventDatapointMarked(Ljava/lang/String;));
																}-*/;

	/**
	 * sets the navigation mode to pan or rotate
	 * 
	 * @param mode
	 *            pan = 0 rotate = 1
	 */
	public static native void setNavigationMode(int mode) /*-{
		$wnd.setNavigationMode(mode);
	}-*/;

	// ZOOM
	// slider
	// callbackZoomLevelAbsolute (0-20)
	public static void callbackZoomLevelAbsolute(int factor) {

	}

	/**
	 * attaches the callbackZoomLevelAbsolute function to the window object
	 */
	public static native void initZoomLevelAbsolute() /*-{
		$wnd.callbackZoomLevelAbsolute = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackZoomLevelAbsolute(I));
	}-*/;

	public static void callbackZoomLevel(int level) {
		// zoomBar.setValue(level);
	}

	public static native void setZoomLevelAbsolute(int level) /*-{
		$wnd.setZoomLevelAbsolute(level);
	}-*/;

	/**
	 * sets the zoomlevel
	 * 
	 * @param level
	 *            zoom in -1 zoom out 1
	 */
	public static native void setZoomLevel(int level) /*-{
		$wnd.setZoomLevel(level);
	}-*/;

	/**
	 * attaches the callbackZoomLevel function to the window object
	 */
	public static native void initZoomLevel() /*-{
		$wnd.callbackZoomLevel = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackZoomLevel(I));

	}-*/;

	/**
	 * attaches the callbackInitControls function to the window object
	 */
	public static native void initControls() /*-{
		$wnd.callbackInitControls = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackInitControls());
	}-*/;

	/**
	 * displays the controls when a model is loaded
	 */
	public static void callbackInitControls() {

		// panviewchanger.setVisible(true);
		// // mainViewControlsLeft.setVisible(true);
		// // mainViewControlsLeft1.setVisible(true);
		// controlsDp.setVisible(true);
		// controlsError.setVisible(true);
		// mainViewControls.setVisible(true);
		// controlsView.setVisible(true);
		// zoomBar.setVisible(true);
		// transparencyBar.setVisible(true);
		// exposeBar.setVisible(true);
	}

	// TODO: drag; mousewheel

	public static native void initcallbackAddDpWidget() /*-{
														$wnd.callbackAddDpWidget = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackAddDpWidget(Ljava/lang/String;II));

														}-*/;

	/**
	 * sets the expose level of the model
	 * 
	 * @param factor
	 *            [0;150]
	 */
	public static native void setExposeLevel(int factor) /*-{
		$wnd.setExposeLevel(factor);
	}-*/;

	/**
	 * sets the transparency level
	 * 
	 * @param transparentFactor
	 *            [0;100]
	 */
	public static native void setTransparentLevel(int transparentFactor) /*-{
		$wnd.setTransparentLevel(transparentFactor);
	}-*/;

	/**
	 * sets the view to a data point and selects the data point
	 * 
	 * @param dpName
	 */
	public static native void setViewToDatapoint(String dpName) /*-{
		$wnd.setViewToDatapoint(dpName);
	}-*/;

	public static native void triggerJSMouseUpEvent() /*-{
		$wnd.getMouseUpEvent();
	}-*/;

	/**
	 * functions for dragging dp
	 */
	public static native void initClickEventDatapointMove() /*-{
															$wnd.callbackClickEventDatapointMove = $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackClickEventDatapointMove(Ljava/lang/String;));
															}-*/;

	// public static native void initMouseMoveCallback() /*-{
	// $wnd.callbackMouseMove =
	// $entry(@bpi.most.client.modules.d3viewer.D3ModuleWidget::callbackMouseMove(II));
	// }-*/;
	//
	// public static void callbackMouseMove(int X, int Y) {
	//
	// }
	// BRAUCHST DAWEIL NET
	public static native void mouseMove(Element element) /*-{
		$wnd.$($doc).mousemove(function(e) {
			//	     $wnd.$(element).position({ top: e.PageY, left: e.PageX});
			//		 clone = $wnd.$(element).clone(true);
			//		 $wnd.$(body).append($(cone));

			//	     $wnd.$(element).css({ top: e.PageY, left: e.PageX});
			////	     callbackMouseMove(e.pageX,e.pageY);
			//	     console.log(e.pageX);
			//	     console.log($wnd.$(element));
		});

	}-*/;

	/**
	 * Adds a new DP widget to the mouseposition when a DP is selected in
	 * Bimsurfer.
	 * 
	 * @param dpName
	 * @param X
	 * @param Y
	 */
	public static void callbackAddDpWidget(String dpName, int x, int y) {
		String dp = dpName.trim().substring(TRIM_BEGIN_INDEX);
		int win = dp.indexOf("(");
		if (win > 0) {
			dp = dp.substring(0, win);
		}
		removeActiveDpWidgets();
		DpWidget dpWidget = new DpWidget(dp);
		getInstance().demPanel.add(dpWidget, x, y);
		// dpWidget.getElement().setAttribute("style", "");
		dpWidget.getElement().setAttribute(
				"style",
				"position:absolute;left:" + x + "px;top:" + y
						+ "px;z-index:600;background-color:#fff;");
		activeDpWidgets.add(dpWidget);

	}

	public static void callbackClickEventDatapointMove(String dpName) {
		// TODO: fix callback String
		// TODO: fix highlighting

		// DNDController.getInstance();
		// // mouseMove(dpWidget.getElement());

		// DNDController.setCurrentDrag(dpWidget);
		// DNDController.setDragitem(dpWidget);
		// DNDController.EVENT_BUS.fireEvent(new MostDragStartEvent(dpWidget
		// .getElement()));
		// DNDController.getCurrentDrag().addStyleName("uid-zindex");
		// // DragStartEvent.fireNativeEvent(Document.get().createClickEvent(1,
		// dpWidget.getAbsoluteLeft(), dpWidget.getAbsoluteTop(),
		// dpWidget.getAbsoluteLeft(), dpWidget.getAbsoluteTop(), false, false,
		// false, false), DNDController.getCurrentDrag());
		// // DomEvent.fireNativeEvent(Document.get().createMouseDownEvent(1, 1,
		// 1, 1, 1, false, false, false, false, 0), dpWidget);
		// // final HandlerRegistration mouseMoveHandler =
		// DNDController.getBackgroundWidget().addDomHandler(new
		// MouseMoveHandler() {
		// //
		// // @Override
		// // public void onMouseMove(MouseMoveEvent event) {
		// // // delete to enable drag functionality
		// // dpWidget.getElement().setAttribute("style", "");
		// // dpWidget.getElement().setAttribute("style",
		// "position:absolute;left:" +
		// (event.getClientX()-RootModule.getSidebar().getOffsetWidth()) +
		// "px;top:" +
		// (event.getClientY()-RootModule.getRootNorth().getOffsetHeight()) +
		// "px;z-index:600;");
		// //
		// //
		// // }
		// // }, MouseMoveEvent.getType());
		// final HandlerRegistration mouseMoveHandler1 =
		// RootLayoutPanel.get().addDomHandler(new MouseMoveHandler() {
		//
		// @Override
		// public void onMouseMove(MouseMoveEvent event) {
		// // delete to enable drag functionality
		// dpWidget.getElement().setAttribute("style", "");
		// dpWidget.getElement().setAttribute("style", "position:absolute;left:"
		// + (event.getClientX()-RootModule.getSidebar().getOffsetWidth()) +
		// "px;top:" +
		// (event.getClientY()-RootModule.getRootNorth().getOffsetHeight()) +
		// "px;z-index:600;");
		//
		//
		// }
		// }, MouseMoveEvent.getType());

		RootModule.getRootWidget().addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				// mouseMoveHandler.removeHandler();
				// mouseMoveHandler1.removeHandler();

			}
		}, MouseUpEvent.getType());
		// RootModule.getRootWidget().addMouseMoveHandler(new MouseMoveHandler()
		// {
		//
		// @Override
		// public void onMouseMove(MouseMoveEvent event) {
		// Window.alert("mousemove");
		// dpWidget.getElement().setAttribute("style",
		// "top" + event.getClientY());
		// }
		// });
		// NativeEvent ev = Document.get().createMouseDownEvent(1, 1, 1, 1, 1,
		// false, false, false, false, 1);
		// DomEvent.fireNativeEvent(ev, dpWidget);

	}

	// TODO: Warning
	// dummybutton
	// setWarning(String dpName)

	// TODO: dummybutton
	// setViewToDataPoint(String dpName)

	// TODO: dummybutton
	// setProperty(String[])

	// TODO: brauche: setZoomLevelAbsolute

}
