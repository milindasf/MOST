package bpi.most.client.modules.exporter.filter;


import com.google.gwt.user.client.ui.Composite;

public abstract class FormWidget extends Composite {
	
	Filter filter;

	public FormWidget(Filter filter){
		this.filter = filter;
	}
	
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
