/*
 * This code licensed to public domain
 */
package bpi.most.obix.net;

import bpi.most.obix.objects.Err;

/**
 * ErrException is thrown by ObixSession when a request
 * returns an unexpected err object.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 12 Sept 05
 */
public class ErrException
        extends RuntimeException {

    private final Err err;

    public ErrException(Err err) {
        super(err.format());
        this.err = err;
    }

    public Err getErr() {
        return err;
    }
}
