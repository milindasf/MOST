package bpi.most.client.modules.exporter.filter;

import java.util.ArrayList;
import bpi.most.client.utils.ui.GeneralDropWidget;
import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDatasetDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ExportFilterWidget extends Composite {

	@UiField
	ListBox listBox;

	@UiField
	GeneralDropWidget dropWidget;

	@UiField
	FlowPanel formContent;

	public ArrayList<DpDatasetDTO> dpdatasetList = new ArrayList<DpDatasetDTO>();

	private String dataURI = "data:application/octet-stream,";

	private ArrayList<DpDTO> dpList;
	private int dpIndex = 0;

	private ArrayList<TimePeriodFilter> timeFilterList = new ArrayList<TimePeriodFilter>();
	private int timeFilterIndex = 0;

	private ArrayList<Filter> orderedFilterList = new ArrayList<Filter>();

	private String format = "";

	private DateTimeFormat csvformat = DateTimeFormat.getFormat("EEEE'%20'HH:mm:ss'%20'dd.MM.yyyy");

	private static ExportFilterWidgetUiBinder uiBinder = GWT.create(ExportFilterWidgetUiBinder.class);

	interface ExportFilterWidgetUiBinder extends UiBinder<Widget, ExportFilterWidget> {
	}

	public ExportFilterWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		dropWidget.getElement().getStyle().setOverflowY(Overflow.AUTO);
		dropWidget.getElement().getStyle().setHeight(100, Unit.PCT);
	}

	private ExportFilterWidget getThis() {
		return this;
	}

	@UiHandler("listBox")
	void changeHandler(ChangeEvent e) {
		if (listBox.getValue(listBox.getSelectedIndex()).equalsIgnoreCase("DateTimePeriode")) {
			FilterWidget dummy = new FilterWidget(listBox.getItemText(listBox.getSelectedIndex()), new TimePeriodFilter(
					listBox.getValue(listBox.getSelectedIndex()), getThis()), formContent);
			ExportOptionsDragWidget exportDragWidget = new ExportOptionsDragWidget(dummy, new String[] { "dragWidget",
					"dWidget-uid-eOption" }, null, null);
			dropWidget.addDraggable(exportDragWidget);
		} else if (listBox.getValue(listBox.getSelectedIndex()).equalsIgnoreCase("InternalConditions")) {
			FilterWidget dummy = new FilterWidget(listBox.getItemText(listBox.getSelectedIndex()), new InternalConditionsFilter(
					listBox.getValue(listBox.getSelectedIndex()), getThis()), formContent);
			ExportOptionsDragWidget exportDragWidget = new ExportOptionsDragWidget(dummy, new String[] { "dragWidget",
					"dWidget-uid-eOption" }, null, null);
			dropWidget.addDraggable(exportDragWidget);
		} else if (listBox.getValue(listBox.getSelectedIndex()).equalsIgnoreCase("ExternalConditions")) {
			FilterWidget dummy = new FilterWidget(listBox.getItemText(listBox.getSelectedIndex()), new ExternalConditionsFilter(
					listBox.getValue(listBox.getSelectedIndex()), getThis()), formContent);
			ExportOptionsDragWidget exportDragWidget = new ExportOptionsDragWidget(dummy, new String[] { "dragWidget",
					"dWidget-uid-eOption" }, null, null);
			dropWidget.addDraggable(exportDragWidget);
		} else if (listBox.getValue(listBox.getSelectedIndex()).equalsIgnoreCase("DaySelect")) {
			FilterWidget dummy = new FilterWidget(listBox.getItemText(listBox.getSelectedIndex()), new DaySelectFilter(
					listBox.getValue(listBox.getSelectedIndex()), getThis()), formContent);
			ExportOptionsDragWidget exportDragWidget = new ExportOptionsDragWidget(dummy, new String[] { "dragWidget",
					"dWidget-uid-eOption" }, null, null);
			dropWidget.addDraggable(exportDragWidget);
		} else {
			Label dummy = new Label(listBox.getValue(listBox.getSelectedIndex()));
			ExportOptionsDragWidget exportDragWidget = new ExportOptionsDragWidget(dummy, new String[] { "dragWidget",
					"dWidget-uid-eOption" }, null, null);
			dropWidget.addDraggable(exportDragWidget);
		}

		listBox.setItemSelected(0, true);
	}

	public void startExecute(ArrayList<DpDTO> dpList, String format) {
		dpIndex = 0;
		timeFilterIndex = 0;
		dpdatasetList.clear();
		timeFilterList.clear();
		orderedFilterList.clear();
		// collect all filter in the TimeFilterList and the
		// otherFiltersList
		for (int i = 0; i < dropWidget.getDropPanel().getWidgetCount(); i++) {
			if (((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter instanceof TimePeriodFilter) {
				timeFilterList
						.add((TimePeriodFilter) ((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter);
			}
		}
		for (int i = 0; i < dropWidget.getDropPanel().getWidgetCount(); i++) {
			if (((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter instanceof DaySelectFilter) {
				orderedFilterList.add(((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter);
			}
		}
		for (int i = 0; i < dropWidget.getDropPanel().getWidgetCount(); i++) {
			if (((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter instanceof InternalConditionsFilter) {
				orderedFilterList.add(((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter);
			}
		}
		for (int i = 0; i < dropWidget.getDropPanel().getWidgetCount(); i++) {
			if (((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter instanceof ExternalConditionsFilter) {
				orderedFilterList.add(((ExportOptionsDragWidget) dropWidget.getDropPanel().getWidget(i)).getFilter().filter);
			}
		}
		this.dpList = dpList;
		this.format = format;

		processDPList();

	}

	private void processDPList() {
		if (dpIndex < dpList.size()) {
			if (timeFilterIndex < timeFilterList.size()) {
				fetch();
			} else {
				dpIndex++;
				timeFilterIndex = 0;
				processDPList();
				return;
			}
		} else {
			if (format.equalsIgnoreCase("csv")) {
				printCSV();
			} else {
				Window.alert("wrong format");
			}
		}

	}

	private void fetch() {
		// set all fetchFlags to all involved Filter
		timeFilterList.get(timeFilterIndex).setFinishedFetch(false);
		for (int i = 0; i < orderedFilterList.size(); i++) {
			orderedFilterList.get(i).setFinishedFetch(false);
		}
		timeFilterList.get(timeFilterIndex).fetchData(timeFilterList.get(timeFilterIndex), dpList.get(dpIndex));
		for (int i = 0; i < orderedFilterList.size(); i++) {
			orderedFilterList.get(i).fetchData(timeFilterList.get(timeFilterIndex), dpList.get(dpIndex));
		}
	}

	private void execute() {
		DpDatasetDTO dpdataset = new DpDatasetDTO();
		dpdataset = timeFilterList.get(timeFilterIndex).execute(dpdataset);
		for (int i = 0; i < orderedFilterList.size(); i++) {
			dpdataset = orderedFilterList.get(i).execute(dpdataset);
		}
		timeFilterIndex++;
		if (dpdataset != null && dpdataset.getDatapointName() != null) { // &&
																			// !dpdataset.isEmpty()
																			// removed
			dpdatasetList.add(dpdataset);
		}
		processDPList();
	}

	private void printCSV() {
		// print dataList and create csv, afterwards delete all data
		if (dpdatasetList.size() > 0) {
			String dataLink = "";
			dataLink = dataURI;
			int longest = 0;
			// search dpdatasetlist for longest size
			for (DpDatasetDTO current : dpdatasetList) {
				if (current.size() > longest)
					longest = current.size();
			}
			// print datapoint names with 2 commas in the middle
			for (int j = 0; j < dpdatasetList.size(); j++) {
				dataLink = dataLink.concat(dpdatasetList.get(j).getDatapointName());
				dataLink = dataLink.concat("%2C"); // comma
				dataLink = dataLink.concat("%2C"); // comma
				dataLink = dataLink.concat("%2C"); // comma
			}
			dataLink = dataLink.concat("%0A");
			// for loop (longest size)
			for (int i = 0; i < longest; i++) {
				for (int j = 0; j < dpdatasetList.size(); j++) {
					if (dpdatasetList.get(j).size() > i) {
						dataLink = dataLink.concat(csvformat.format(dpdatasetList.get(j).get(i).getTimestamp()));
						dataLink = dataLink.concat("%2C"); // comma
						dataLink = dataLink.concat(String.valueOf(dpdatasetList.get(j).get(i).getValue()));
						dataLink = dataLink.concat("%2C"); // comma
						dataLink = dataLink.concat(String.valueOf(dpdatasetList.get(j).get(i).getQuality()));
						dataLink = dataLink.concat("%2C"); // comma
					} else {
						dataLink = dataLink.concat("%2C"); // comma
						dataLink = dataLink.concat("%2C"); // comma
						dataLink = dataLink.concat("%2C"); // comma
					}
				}
				dataLink = dataLink.concat("%0A");
			}

			setWindowHref(dataLink);
		} else {
			Window.alert("No data available!");
		}
	}

	public void fetchReady() {
		// TODO start execute phase
		boolean flag = true;
		if (timeFilterList.get(timeFilterIndex).isFinishedFetch()) {
			for (int i = 0; i < orderedFilterList.size(); i++) {
				flag = orderedFilterList.get(i).isFinishedFetch();
			}
		} else {
			flag = false;
		}

		if (flag == true) {
			execute();
		}
	}

	public static native void setWindowHref(String url)/*-{
														$wnd.location.href = url;
														}-*/;

}
