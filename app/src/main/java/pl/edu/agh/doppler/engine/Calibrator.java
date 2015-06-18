package pl.edu.agh.doppler.engine;

public class Calibrator {

    private final static int CYCLE_SIZE = 20;

    private final static int UP_THRESHOLD = 5;

    private final static int DOWN_THRESHOLD = 0;

    private final static double UP_AMOUNT = 1.1;

    private final static double DOWN_AMOUNT = 0.9;

    private final static double MAX = 0.95;

    private final static double MIN = 0.0001;

    private int i = 0;

    /** Direction in which move was detected last time. Used to detect direction changes. */
    private int previousDirection = 0;

    /** Counter for direction changes. */
    private int directionChanges = 0;

    /**
     * Calibrates volume ratio.
     *
     * @param maxVolRatio actual volume ratio
     * @param leftBandwidth left bandwidth value
     * @param rightBandwidth right bandwidth value
     *
     * @return new maximum volume ration
     */
    public double calibrate(double maxVolRatio, final int leftBandwidth, final int rightBandwidth) {
        //Log.d("DOPPLER","Start calibrating.");
        //calculate difference between bandwidths
        int difference = leftBandwidth - rightBandwidth;
        //calculate direction
        int direction = (int) Math.signum(difference);

        //if direction change occurred take appropriate steps
        if(previousDirection != direction) {
            directionChanges++;
            previousDirection = direction;
        }

        //if cycle finished
        i = (i + 1) % CYCLE_SIZE;
        if(i == 0) {
            if(directionChanges >= UP_THRESHOLD) {
                maxVolRatio *= UP_AMOUNT;
            } else if (directionChanges == DOWN_THRESHOLD) {
                maxVolRatio *= DOWN_AMOUNT;
            }

            //apply boundaries
            maxVolRatio = maxVolRatio > MAX ? MAX : maxVolRatio;
            maxVolRatio = maxVolRatio < MIN ? MIN : maxVolRatio;

            //reset direction changes counter
            directionChanges = 0;
        }

        //Log.d("DOPPLER","End calibrating.");
        return maxVolRatio;
    }
}
