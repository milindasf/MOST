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
import bpi.most.obix.contracts.HistoryQueryOut;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Implementation of the {@link HistoryQueryOut} contract.
 * It contains a list of history records, which are
 * implementations of {@link bpi.most.obix.contracts.HistoryRecord}.
 *
 * @author Alexej Strelzow
 */
public class HistoryQueryOutImpl extends Obj implements HistoryQueryOut {

	private List resultList;
	private Int count;
	private Abstime start;
	private Abstime end;
	
	public static final String HISTORY_QUERY_OUT_CONTRACT = "obix:HistoryQueryOut";
    public static final String RECORD_DEF = "#RecordDef";

    /**
     * Constructor.
     *
     * @param historyRecords The history records
     */
	public HistoryQueryOutImpl(ArrayList<HistoryRecordImpl> historyRecords) {

        if (historyRecords == null) {
            throw new IllegalArgumentException("Data must not be null!");
        }

        count = new Int("count");
        start = new Abstime("start");
        end = new Abstime("end");
		
		resultList = new List("data");
		resultList.setOf(new Contract(HistoryRecordImpl.HISTORY_RECORD_CONTRACT));

		for(HistoryRecordImpl historyRecord : historyRecords) {
			resultList.add(historyRecord);
		}

        count.set(historyRecords.size());
		
		if(historyRecords.size() > 0) {
			start.set(historyRecords.get(0).timestamp().get(), TimeZone.getDefault());
		}
		
		if(historyRecords.size() > 0) {
			end.set(historyRecords.get(historyRecords.size()-1).timestamp().get(), TimeZone.getDefault());
		}

		setIs(new Contract(HISTORY_QUERY_OUT_CONTRACT));
		
		add(count);
		add(start);
		add(end);
		add(resultList);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Int count() {
		return count;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Abstime start() {
		return start;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Abstime end() {
		return end;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public List data() {
		return resultList;
	}

    /**
     * Sets the Href of the history to <code>"http://x/" + name + "/history/query"</code>
     *
     * @param name The name of the data point, which contains the historic values
     */
    public void setDpName(String name) {
        setHref(new Uri("http://x/" + name + "/history/query"));
    }

   /**
    *  Sets the units of the records. Additionally an obj of #RecordDef
    *  will be appended as a sibling of the list element.
    *  <code>
    *  <obj href="#RecordDef" is="obix:HistoryRecord">
    *   <real name="value" units="obix:units/fahrenheit"/>
    *  </obj>
    *  </code>
    *
    *  @param unit The unit of each record, e.g. "celsius"
    */
    public void setUnits(String unit) {
        setUnits(new Uri("obix:units/"+unit));
    }

    /**
     *  Sets the units of the records. Additionally an obj of #RecordDef
     *  will be appended as a sibling of the list element.
     *  <code>
     *  <obj href="#RecordDef" is="obix:HistoryRecord">
     *   <real name="value" units="obix:units/fahrenheit"/>
     *  </obj>
     *  </code>
     *
     *  @param unit The unit as {@link Uri}.
     */
    public void setUnits(Uri unit) {
        resultList.setOf(new Contract(RECORD_DEF + " " + HistoryRecordImpl.HISTORY_RECORD_CONTRACT));

        Obj recordDef = new Obj();
        recordDef.setHref(new Uri(RECORD_DEF));
        recordDef.setIs(new Contract(HistoryRecordImpl.HISTORY_RECORD_CONTRACT));

        Real real = new Real("value");

        real.setUnit(unit);

        recordDef.add(real);

        add(recordDef);
    }
}