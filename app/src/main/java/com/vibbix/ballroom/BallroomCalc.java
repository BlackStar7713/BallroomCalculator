package com.vibbix.ballroom;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The main math class
 */
public class BallroomCalc {
    //region constants
    public static final BigDecimal CUBCM_PER_CUBM = BigDecimal.valueOf(Math.pow(10, 6));
    public static final BigDecimal CUBIN_PER_CUBFT = BigDecimal.valueOf(1728D);
    public static final BigDecimal MAX_DENSITY = BigDecimal.valueOf(Math.PI)
            .divide(BigDecimal.valueOf(3.0D)
                    .multiply(BigDecimal.valueOf(Math.sqrt(2.0D))), BigDecimal.ROUND_HALF_EVEN)
            .multiply(BigDecimal.valueOf(100.0D));
    public static final double EASY_RADIUS_METRIC = 6.477D; //1.675 cm
    public static final double EASY_RADIUS_IMPERIAL = 2.55D; //2.55 in
    public static final double EASY_EFFICIENCY = 65.0D; //65.0%
    //endregion
    /**
     * Creates a new instance of the BallroomCalculator
     */
    public BallroomCalc() {
    }

    //endregion
    //region static methods

    public static int estimateBalls(double efficiency, double footage, double depth, double radius,
                                    BigDecimal measurementscale) {
        double value = 0.0D;
        if (efficiency == 0.0D || footage == 0.0D || depth == 0.0D || radius == 0.0D){
            return 0;
        }
        try {
            efficiency = efficiency / 100;
            BigDecimal wholevolume = BigDecimal.valueOf(footage * depth);
            BigDecimal usablevolume = wholevolume.multiply(BigDecimal.valueOf(efficiency));
            BigDecimal ballvolume = BigDecimal.valueOf(4).divide(BigDecimal.valueOf(3), 10,
                    RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.PI)).multiply(
                    BigDecimal.valueOf(Math.pow(radius, 3))).divide(BigDecimal.valueOf(1), 10,
                    RoundingMode.HALF_UP).divide
                    (measurementscale, 10, RoundingMode.HALF_UP);
            value = usablevolume.divide(ballvolume, 10, RoundingMode.HALF_UP).doubleValue();
            //this.balls = Math.floor(usablevolume/ballvolume);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (int) Math.floor(value);
    }
}
