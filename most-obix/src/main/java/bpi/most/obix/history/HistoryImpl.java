/*******************************************************************************
 * Copyright (c) 2013
 * Institute of Computer Aided Automation, Automation Systems Group, TU Wien.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * This file is part of the IoTSyS project.
 ******************************************************************************/

package bpi.most.obix.history;

import bpi.most.obix.objects.*;
import bpi.most.obix.contracts.History;
import bpi.most.obix.contracts.HistoryFilter;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Generic history implementation. Should only be used for basic value types
 * (bool, int, real, str).
 */
public class HistoryImpl extends Obj implements History {
	private int historyCountMax = HistoryHelper.HISTORY_COUNT_DEFAULT; 
	private LinkedList<HistoryRecordImpl> history = new LinkedList<HistoryRecordImpl>();

	private Int count = new Int();
	private Abstime start = new Abstime();
	private Abstime end = new Abstime();
	private Op query = new Op();
	private Op rollup = new Op();
	private Feed feed = new Feed();

	private Obj observedDatapoint;

	public HistoryImpl(Obj observedDatapoint, int historyCountMax) {
		this.observedDatapoint = observedDatapoint;
		
		this.historyCountMax = historyCountMax;

		this.setName("history");
		this.setHref(new Uri("history"));
				
		count.setName("count");
		count.setHref(new Uri("count"));

		start.setName("start");
		start.setHref(new Uri("start"));

		end.setName("end");
		end.setHref(new Uri("end"));

		//observedDatapoint.attach(this);

		add(count);
		add(start);
		add(end);
	}

	//@Override
	public void initialize() {
		//this.setHref(new Uri(observedDatapoint.getFullContextPath() + "/history"));
		//ObjectBroker.getInstance().addObj(this, false);
		//String queryHref = observedDatapoint.getFullContextPath() + "/history/query";

//		ObjectBroker.getInstance().addOperationHandler(
//
//		new Uri(queryHref), new OperationHandler() {
//			public Obj invoke(Obj in) {
//				return HistoryImpl.this.query(in);
//			}
//		});

		// add history reference in the parent element
		if (observedDatapoint.getParent() != null) {

			observedDatapoint.getParent().add(
					new Ref(observedDatapoint.getName() + " history", new Uri(
							observedDatapoint.getHref() + "/history")));
		}
	}

	private Obj query(Obj in) {

		long limit = 0;
		Abstime start = new Abstime();
		Abstime end = new Abstime();
		if (in != null && in instanceof HistoryFilter) {
			HistoryFilter historyFilter = (HistoryFilter) in;
			limit = historyFilter.getInt();
			start = historyFilter.start();
			end = historyFilter.end();
		}

		ArrayList<HistoryRecordImpl> filteredRecords = new ArrayList<HistoryRecordImpl>();

		for (HistoryRecordImpl record : history) {
			boolean addRecord = true;

			if (limit != 0) { // unlimited
				if (filteredRecords.size() < limit) {
					addRecord = false;
				} else {
					break; // stop iterating
				}
			}

			if (start.get() != end.get()) {
				if (start != null && record.timestamp().get() < start.get()) {
					addRecord = false;
				}

				if (end != null && record.timestamp().get() > end.get()) {
					addRecord = false;
				}
			}

			if (addRecord) {
				filteredRecords.add(record);
			}
		}

		HistoryQueryOutImpl queryOut = new HistoryQueryOutImpl(filteredRecords);
		return queryOut;
	}

	public Int count() {
		return count;
	}

	public Abstime start() {
		return start;
	}

	public Abstime end() {
		return end;
	}

	public Op query() {
		return query;
	}

	public Feed feed() {
		return feed;
	}

	public Op rollup() {
		return rollup;
	}

	/**
	 * Observer method, that is called if the parent object changes
	 */
	//@Override
	public void update(Object state) {
		if (state instanceof Obj) {
			HistoryRecordImpl historyRecordImpl = new HistoryRecordImpl(
					new Obj());
			// only allow basic value types
			if (state instanceof Bool) {
				historyRecordImpl = new HistoryRecordImpl(new Bool(
						((Bool) state).get()));
			}

			if (state instanceof Real) {
				historyRecordImpl = new HistoryRecordImpl(new Real(
						((Real) state).get()));
			}

			if (state instanceof Int) {
				historyRecordImpl = new HistoryRecordImpl(new Int(
						((Int) state).get()));
			}

			if (state instanceof Str) {
				historyRecordImpl = new HistoryRecordImpl(new Str(
						((Str) state).get()));
			}

			history.add(historyRecordImpl);
			if (history.size() > historyCountMax) {
				history.removeFirst();
			}
			//count.setSilent(history.size());
		}
	}

}
