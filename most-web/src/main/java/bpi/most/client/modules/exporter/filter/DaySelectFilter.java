package bpi.most.client.modules.exporter.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;

import bpi.most.dto.DpDTO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;

public class DaySelectFilter extends Filter implements FilterInterface {

	public HashMap<String, Boolean> days = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> daystime = new HashMap<String, Boolean>();

	public int monFromInMinute = 0;
	public int monToInMinute = 0;

	public int tueFromInMinute = 0;
	public int tueToInMinute = 0;

	public int wedFromInMinute = 0;
	public int wedToInMinute = 0;

	public int thuFromInMinute = 0;
	public int thuToInMinute = 0;

	public int friFromInMinute = 0;
	public int friToInMinute = 0;

	public int satFromInMinute = 0;
	public int satToInMinute = 0;

	public int sunFromInMinute = 0;
	public int sunToInMinute = 0;

	public DaySelectFilter() {
		super();
		initHashMap();
	}

	public DaySelectFilter(String name) {
		super(name);
		createForm();
		initHashMap();
	}

	public DaySelectFilter(String name, ExportFilterWidget parent) {
		super(name, parent);
		createForm();
		initHashMap();
	}

	@Override
	public DpDatasetDTO execute(DpDatasetDTO dpdataset) {
		// TODO Auto-generated method stub
		DpDatasetDTO datasetwork = new DpDatasetDTO(
				dpdataset.getDatapointName());
		for (int i = 0; i < dpdataset.size(); i++) {
			if (validateValue(dpdataset.get(i))) {
				datasetwork.add(dpdataset.get(i));
			}
		}
		return datasetwork;
	}

	private void createForm() {
		setForm(new DaySelectFormWidget(this));
	}

	private void initHashMap() {
		days.put("Monday", true);
		days.put("Tuesday", true);
		days.put("Wednesday", true);
		days.put("Thursday", true);
		days.put("Friday", true);
		days.put("Saturday", true);
		days.put("Sunday", true);
		daystime.put("Monday", false);
		daystime.put("Tuesday", false);
		daystime.put("Wednesday", false);
		daystime.put("Thursday", false);
		daystime.put("Friday", false);
		daystime.put("Saturday", false);
		daystime.put("Sunday", false);
	}

	@Override
	public void fetchData(TimePeriodFilter timeFilter, DpDTO dp) {
		// no need to fetch data here yet
		setFinishedFetch(true);
		getParent().fetchReady();
	}

	private boolean validateValue(DpDataDTO dpDataDTO) {

		boolean validate = false;
		if (days.get(dayOfTheWeek(dpDataDTO.getTimestamp()))) {
			if (daystime.get(dayOfTheWeek(dpDataDTO.getTimestamp()))) {
				if (betweenTime(dpDataDTO.getTimestamp())) {
					validate = true;
				}
			} else {
				validate = true;
			}
		}

		return validate;
	}

	private boolean betweenTime(Date timestamp) {
		boolean isBetween = false;
		String dayOfTheWeek = dayOfTheWeek(timestamp);
		int timeInMinutes = (timestamp.getHours() * 60)
				+ timestamp.getMinutes();
		if (dayOfTheWeek.equalsIgnoreCase("Monday")) {
			if (timeInMinutes >= monFromInMinute
					&& timeInMinutes <= monToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
			if (timeInMinutes >= tueFromInMinute
					&& timeInMinutes <= tueToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
			if (timeInMinutes >= wedFromInMinute
					&& timeInMinutes <= wedToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Thursday")) {
			if (timeInMinutes >= thuFromInMinute
					&& timeInMinutes <= thuToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Friday")) {
			if (timeInMinutes >= friFromInMinute
					&& timeInMinutes <= friToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Saturday")) {
			if (timeInMinutes >= satFromInMinute
					&& timeInMinutes <= satToInMinute) {
				isBetween = true;
			}
		} else if (dayOfTheWeek.equalsIgnoreCase("Sunday")) {
			if (timeInMinutes >= sunFromInMinute
					&& timeInMinutes <= sunToInMinute) {
				isBetween = true;
			}
		} else {
			Window.alert("Ups, something went wrong! Sorry! (betweenTime)");
		}
		return isBetween;
	}

	private String dayOfTheWeek(Date date) {
		DateTimeFormat dotw = DateTimeFormat.getFormat("EEEE");
		return dotw.format(date);

	}
}
