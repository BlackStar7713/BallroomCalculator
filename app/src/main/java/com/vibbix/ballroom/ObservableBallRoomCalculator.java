package com.vibbix.ballroom;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableInt;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * A ballroom calculator, but used in conjunction with databinding
 */
@Parcel
public class ObservableBallRoomCalculator extends BaseObservable {
    public static final BigDecimal MAX_DENSITY = BigDecimal.valueOf(Math.PI)
            .divide(BigDecimal.valueOf(3.0D).multiply(BigDecimal.valueOf(Math.sqrt(2.0D))),
                    BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(100.0D));
    private static final BigDecimal CUBCM_PER_CUBM = BigDecimal.valueOf(Math.pow(10, 6));
    private static final BigDecimal CUBIN_PER_CUBFT = BigDecimal.valueOf(1728D);
    private static final double EASY_RADIUS_METRIC = 7.6D; //~3.0cm
    private static final double EASY_RADIUS_IMPERIAL = 3.0D; //2.55 in
    private static final double EASY_EFFICIENCY = 64.0D;
    public ObservableBoolean isEasy;
    public ObservableBoolean isMetric;
    public ObservableDouble area;
    public ObservableDouble radius;
    public ObservableDouble cost;
    public ObservableDouble price;
    public ObservableDouble depth;
    public ObservableInt efficiency;
    public ObservableInt balls;

    public ObservableBallRoomCalculator() {
        this.isEasy = new ObservableBoolean(false);
        this.isMetric = new ObservableBoolean(false);
        this.area = new ObservableDouble(0.0D);
        this.radius = new ObservableDouble(0.0D);
        this.cost = new ObservableDouble(0.0D);
        this.price = new ObservableDouble(0.0D);
        this.depth = new ObservableDouble(0.0D);
        this.efficiency = new ObservableInt(0);
        this.balls = new ObservableInt(0);
        OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                recalculateBalls();
            }
        };
        this.isEasy.addOnPropertyChangedCallback(callback);
        this.isMetric.addOnPropertyChangedCallback(callback);
        this.area.addOnPropertyChangedCallback(callback);
        this.radius.addOnPropertyChangedCallback(callback);
        this.cost.addOnPropertyChangedCallback(callback);
        this.price.addOnPropertyChangedCallback(callback);
        this.depth.addOnPropertyChangedCallback(callback);
        this.efficiency.addOnPropertyChangedCallback(callback);
        this.balls.addOnPropertyChangedCallback(callback);
    }

    public ObservableBallRoomCalculator(boolean isEasy, boolean isMetric, double area,
                                        double radius, double price, double cost,
                                        double depth, int efficiency) {
        this();
        this.isEasy.set(isEasy);
        this.isMetric.set(isMetric);
        this.area.set(area);
        this.radius.set(radius);
        this.price.set(price);
        this.cost.set(cost);
        this.depth.set(depth);
        this.efficiency.set(efficiency);
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getText(EditText view) {
        try {
            return Double.parseDouble(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0D;
        }
    }

    @BindingAdapter("android:text")
    public static void setText(EditText view, double value) {
        boolean setValue = view.getText().length() == 0;
        try {
            if (!setValue) {
                setValue = Double.parseDouble(view.getText().toString()) != value;
            }
        } catch (NumberFormatException e) {
        }
        if (setValue) {
            view.setText(String.format(Locale.getDefault(), "%.02f", value));
        }
    }

    @InverseBindingAdapter(attribute = "android:progress")
    public static int getProgress(ProgressBar view) {
        return view.getProgress();
    }

    @BindingAdapter("android:progress")
    public static void setProgress(ProgressBar view, int progress) {
        view.setProgress(progress);
    }

    public void recalculateBalls() {
        this.balls.set(BallroomCalc.estimateBalls(
                this.isEasy.get() ? EASY_EFFICIENCY : this.efficiency.get(),
                this.area.get(), this.depth.get(),
                this.isEasy.get() ?
                        (this.isMetric.get() ? EASY_RADIUS_METRIC : EASY_RADIUS_IMPERIAL) : this.radius.get(),
                this.isMetric.get() ? CUBCM_PER_CUBM : CUBIN_PER_CUBFT));
        this.cost.set(this.balls.get() * this.price.get());
    }
}
