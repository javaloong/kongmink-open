package org.javaloong.kongmink.open.common.model.analytics;

public class StatsAnalytics implements Analytics {

    private Float count;
    private Float min;
    private Float max;
    private Float avg;
    private Float sum;
    private Float rps;
    private Float rpm;
    private Float rph;

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public Float getAvg() {
        return avg;
    }

    public void setAvg(Float avg) {
        this.avg = avg;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Float getRps() {
        return rps;
    }

    public void setRps(Float rps) {
        this.rps = rps;
    }

    public Float getRpm() {
        return rpm;
    }

    public void setRpm(Float rpm) {
        this.rpm = rpm;
    }

    public Float getRph() {
        return rph;
    }

    public void setRph(Float rph) {
        this.rph = rph;
    }
}
