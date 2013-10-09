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

import bpi.most.obix.objects.Abstime;
import bpi.most.obix.objects.Obj;
import bpi.most.obix.contracts.HistoryRecord;

/**
 * Implementation of the {@link HistoryRecord} contract.
 *
 * @author Alexej Strelzow
 */
public class HistoryRecordImpl extends Obj implements HistoryRecord {
	protected Obj value;
	protected Abstime abstime;
	
	public static final String HISTORY_RECORD_CONTRACT = "obix:HistoryRecord";

    /**
     * Constructor, which sets the time to the current system time.
     *
     * @param value The value of the record
     */
	public HistoryRecordImpl(Obj value) {
		this.value = value;
		abstime = new Abstime(System.currentTimeMillis());

        add(value());
		add(timestamp());
	}

    /**
     * Constructor.
     *
     * @param value The value of the record
     * @param timestamp The timestamp of the record
     */
    public HistoryRecordImpl(Obj value, Abstime timestamp) {
        this.value = value;
        this.abstime = timestamp;

        add(value());
        add(timestamp());
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public Abstime timestamp() {
		return abstime;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Obj value() {
		return value;
	}

}
