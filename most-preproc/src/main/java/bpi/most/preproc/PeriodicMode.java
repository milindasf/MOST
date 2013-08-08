package bpi.most.preproc;

/**
 *
 * different available modes to generate periodic data
 *
 * //TODO the values are from the paper; aks rainer if they correlate to those used in the stored procedures
 *
 */
public enum PeriodicMode {

    WEIGHTED_AVG_LINEAR_INTERPOLATION(1),
    WEIGHTED_AVG_SAMPLE_AND_HOLD(2),
    MAJORITY_SAMPLE_AND_HOLD(3),
    DOMINATING_ZERO_DEFAULT_ONE(4),
    DOMINATING_ONE_DEFAULT_ZERO(5);

    private PeriodicMode(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
