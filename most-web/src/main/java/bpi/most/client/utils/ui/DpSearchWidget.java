package bpi.most.client.utils.ui;

import java.util.ArrayList;

import bpi.most.client.model.DpController;
import bpi.most.shared.DpDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class DpSearchWidget extends Composite {
	
	private static DpSearchWidgetUiBinder uiBinder = GWT
			.create(DpSearchWidgetUiBinder.class);

	interface DpSearchWidgetUiBinder extends UiBinder<Widget, DpSearchWidget> {
	}

	@UiField
	FlowPanel contentArea;

	@UiField
	SuggestBox sugbox;
	@UiField
	Button searchbutton;
	@UiField
	Button selectallbutton;
	@UiField
	CheckBox chkboxtemp;
	@UiField
	CheckBox chkboxhum;
	@UiField
	CheckBox chkboxair;
	@UiField
	CheckBox chkboxstate;
	@UiField
	CheckBox chkboxmeter;
	@UiField
	CheckBox chkboxpower;
	@UiField
	CheckBox chkboxocc;
	@UiField
	CheckBox chkboxbright;
	@UiField
	CheckBox chkboxother;

	ArrayList<CheckBox> searcharea = new ArrayList<CheckBox>();

	public DpSearchWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		DOM.setStyleAttribute(contentArea.getElement(), "overflow", "auto");
		// add all options in an arraylist in order of appearance
		searcharea.add(chkboxair);
		searcharea.add(chkboxbright);
		searcharea.add(chkboxhum);
		searcharea.add(chkboxmeter);
		searcharea.add(chkboxocc);
		searcharea.add(chkboxother);
		searcharea.add(chkboxpower);
		searcharea.add(chkboxstate);
		searcharea.add(chkboxtemp);
		DpController.DP_SERVICE
				.getDatapoints(new AsyncCallback<ArrayList<DpDTO>>() {

					@Override
					public void onSuccess(ArrayList<DpDTO> result) {
						if (!result.isEmpty()) {
							contentArea.clear();
							DpWidget sl;
							for (DpDTO entity : result) {
								sl = new DpWidget(entity.getName(), entity.getType());
								contentArea.add(sl);
							}
						} else {
							Window.alert("Empty");
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// Window.alert("Failure on get Entities!");
					}
				});

		selectallbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean flag = false;
				for (int i = 0; i < searcharea.size(); i++) {
					flag = false;
					if (!searcharea.get(i).getValue()) {
						break;
					}
					flag = true;
				}
				if (flag) {
					for (int i = 0; i < searcharea.size(); i++) {
						searcharea.get(i).setValue(false);
					}
				} else {
					for (int i = 0; i < searcharea.size(); i++) {
						searcharea.get(i).setValue(true);
					}
				}
			}
		});

		searchbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = false;
				for (int i = 0; i < searcharea.size(); i++) {
					if (searcharea.get(i).getValue()) {
						checked = true;
					}
				}
				if (checked) {
					DpController.DP_SERVICE.getDatapoints(sugbox.getText().trim()
							.toLowerCase(),
							new AsyncCallback<ArrayList<DpDTO>>() {

								@Override
								public void onSuccess(ArrayList<DpDTO> result) {
									if (!result.isEmpty()) {
										contentArea.clear();
										DpWidget sl;
										for (DpDTO entity : result) {
											// absolutely bad solution
											boolean add = false;
											if (chkboxtemp.getValue()
													&& (entity.getType()
															.equalsIgnoreCase(
																	"tcon") || entity
															.getType()
															.equalsIgnoreCase(
																	"tem"))) {
												add = true;
											} else if (chkboxhum.getValue()
													&& entity.getType()
															.equalsIgnoreCase(
																	"rhu")) {
												add = true;
											} else if (chkboxair.getValue()
													&& (entity.getType()
															.equalsIgnoreCase(
																	"cdi") || entity
															.getType()
															.equalsIgnoreCase(
																	"voc"))) {
												add = true;
											} else if (chkboxstate.getValue()
													&& entity.getType()
															.equalsIgnoreCase(
																	"con")) {
												add = true;
											} else if (chkboxmeter.getValue()
													&& entity.getType()
															.equalsIgnoreCase(
																	"ele-met")) {
												add = true;
											} else if (chkboxpower.getValue()
													&& entity.getType()
															.equalsIgnoreCase(
																	"ele-pow")) {
												add = true;
											} else if (chkboxocc.getValue()
													&& entity.getType()
															.equalsIgnoreCase(
																	"occ")) {
												add = true;
											} else if (chkboxbright.getValue()
													&& (entity.getType()
															.equalsIgnoreCase(
																	"illum") || entity
															.getType()
															.equalsIgnoreCase(
																	"ele-light"))) {
												add = true;
											} else if (chkboxother.getValue()
													&& !(entity.getType()
															.equalsIgnoreCase(
																	"tcon")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"tem")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"rhu")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"cdi")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"voc")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"con")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"ele-met")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"ele-pow")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"occ")
															|| entity
																	.getType()
																	.equalsIgnoreCase(
																			"illum") || entity
															.getType()
															.equalsIgnoreCase(
																	"ele-light"))) {
												add = true;
											}
											if (add) {
												sl = new DpWidget(entity
														.getName(), entity.getType());
												
												contentArea.add(sl);
											}

										}
									}
								}

								@Override
								public void onFailure(Throwable caught) {
									// Window.alert("Failure on get Entities!");
								}
							});
				}
			}
		});
	}
	
	public FlowPanel getContentArea(){
		FlowPanel temp = contentArea;
		contentArea.removeFromParent();
		return temp;
	}
}
