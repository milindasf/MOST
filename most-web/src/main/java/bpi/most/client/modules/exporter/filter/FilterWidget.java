package bpi.most.client.modules.exporter.filter;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class FilterWidget extends Composite {

	// TODO: onDelete feature

	String name;
	Filter filter;
	Panel formFrame;

	@UiField
	Label title;

	private static FilterWidgetUiBinder uiBinder = GWT
			.create(FilterWidgetUiBinder.class);

	interface FilterWidgetUiBinder extends UiBinder<Widget, FilterWidget> {
	}

	public FilterWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FilterWidget(String name, Filter filter, Panel formFrame) {
		initWidget(uiBinder.createAndBindUi(this));
		this.name = name;
		this.filter = filter;
		this.formFrame = formFrame;
		title.setText(name);
		formFrame.clear();
		formFrame.add(filter.getForm());
	}

	@UiHandler("title")
	void onClick(ClickEvent event) {
		formFrame.clear();
		formFrame.add(filter.getForm());
	}

	public Panel getFormFrame() {
		return formFrame;
	}

	public void setFormFrame(Panel formFrame) {
		this.formFrame = formFrame;
	}

	@Override
	protected void onUnload() {
		filter.getForm().removeFromParent();
		super.onUnload();
	}

}
