package com.vibbix.ballroom;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The main math class
 */
public class BallroomCalc {
    //region constants
    public static final BigDecimal cubcmpercubm = BigDecimal.valueOf(Math.pow(10, 6));
    public static final BigDecimal cubinpercubft = BigDecimal.valueOf(1728D);
    public static final BigDecimal maxdensity = BigDecimal.valueOf(Math.PI)
            .divide(BigDecimal.valueOf(3.0D)
                    .multiply(BigDecimal.valueOf(Math.sqrt(2.0D))), BigDecimal.ROUND_HALF_EVEN)
            .multiply(BigDecimal.valueOf(100.0D));
    public static final double EASY_RADIUS_METRIC = 6.477D; //1.675 cm
    public static final double EASY_RADIUS_IMPERIAL = 2.55D; //2.55 in
    public static final double EASY_EFFICIENCY = 65.0D; //65.0%
    //endregion
    private boolean isMetric = false;
    private int balls = 0;
    private double area = 0.0D;
    private double radius = 0.0D;
    private double cost = 0.0D;
    private double price = 0.0D;
    private double depth = 0.0D;
    private double efficiency = 0.0D;
    private boolean easymode = false;

    /**
     * Creates a new instance of the BallroomCalculator
     */
    public BallroomCalc() {
    }

    //endregion
    //region static methods
    public static int estimateBalls(double efficiency, double footage, double depth, double radius,
                                    double price, BigDecimal measurementscale) {
        double value = 0.0D;
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
    //region Getters/Setters

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Updates the computed values of the class
     */
    public void updateValues() {
        this.balls = estimateBalls(this.easymode ? EASY_EFFICIENCY : this.efficiency, this.area,
                this.depth, this.easymode ?
                        (this.isMetric ? EASY_RADIUS_METRIC : EASY_RADIUS_IMPERIAL) : this.radius,
                this.cost, this.isMetric ? cubcmpercubm : cubinpercubft);
        this.cost = this.balls * this.price;
    }

    /**
     * returns the estimated number of balls that could fit in the designated volume
     *
     * @return number of balls
     */
    public int getBalls() {
        return this.balls;
    }

    /**
     * Calculates the total cost
     *
     * @return Total cost
     */
    public double getCost() {
        return this.cost;
    }

    public double getArea() {
        return this.area;
    }

    public void setArea(double area) {
        this.area = clamp(area, 0.0D, Double.MAX_VALUE);
        this.updateValues();
    }

    public double getDepth() {
        return this.depth;
    }

    public void setDepth(double depth) {
        this.depth = clamp(depth, 0, Double.MAX_VALUE);
        this.updateValues();
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = clamp(radius, 0, Double.MAX_VALUE);
        this.updateValues();
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = clamp(price, 0, Double.MAX_VALUE);
        this.cost = this.balls * this.price;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = clamp(efficiency, 0, maxdensity.doubleValue());
        this.updateValues();
    }

    public boolean isMetric() {
        return this.isMetric;
    }

    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        this.updateValues();
    }

    public boolean isEasyMode() {
        return this.easymode;
    }

    public void setEasymode(boolean easymode) {
        this.easymode = easymode;
        this.updateValues();
    }

    //endregion
}
