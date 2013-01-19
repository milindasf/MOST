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

import bpi.most.obix.comparator.DpDataComparator;
import bpi.most.obix.contracts.HistoryRollupRecord;
import bpi.most.obix.objects.*;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryRollupRecordImpl extends Obj implements HistoryRollupRecord {

	protected Abstime start;
	protected Abstime end;
    protected Int count;
    protected Real min;
    protected Real max;
    protected Real avg;
    protected Real sum;

    public static final String HISTORY_RECORD_CONTRACT = "obix:HistoryRollupRecord";

	public HistoryRollupRecordImpl(ArrayList<DpData> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null!");
        }

        if (data.size() > 1) {

            start = new Abstime("start", data.get(0).getTimestamp().get());
            end = new Abstime("end", data.get(data.size()-1).getTimestamp().get());
            count = new Int("count", data.size());

            sortByValue(data);

            min = new Real("min", data.get(0).getValue().get());
            max = new Real("max", data.get(data.size()-1).getValue().get());

            double sum = 0;

            for (DpData d : data) {
                sum += d.getValue().get();
            }

            this.sum = new Real("sum", sum);
            this.avg = new Real("avg", this.sum.get() / this.count.get());

        } else {
            DpData d = data.get(0);
            double val = d.getValue().get();;

            start = new Abstime("start", d.getTimestamp().get());
            end = new Abstime("end", d.getTimestamp().get());
            count = new Int("count", data.size());
            min = new Real("min", val);
            max = new Real("max", val);
            this.sum = new Real("sum", val);
            this.avg = new Real("avg", val);
        }

        add(start());
        add(end());
        add(count());
        add(min());
        add(max());
        add(avg());
        add(sum());
	}

    private void sortByValue(ArrayList<DpData> data) {
        DpDataComparator comparator = new DpDataComparator();
        comparator.sortAscendingByValue();
        Collections.sort(data, comparator);
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
    public Int count() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real min() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real max() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real avg() {
        return avg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real sum() {
        return sum;
    }
}
