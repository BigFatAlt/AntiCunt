package me.bigfatalt.anticheat.utils;

import cc.funkemunky.api.utils.Tuple;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.*;

@UtilityClass
public class MathUtil {

    public final double EXPANDER = Math.pow(2, 24);

    public static double magnitude(final double... points) {
        double sum = 0.0;

        for (final double point : points) {
            sum += point * point;
        }

        return Math.sqrt(sum);
    }


    public double getVariance(final Collection<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        final double average;

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }


    public double getStandardDeviation(Collection<? extends Number> values) {
        double average = getAverage(values);

        AtomicDouble variance = new AtomicDouble(0D);

        values.forEach(delay -> variance.getAndAdd(Math.pow(delay.doubleValue() - average, 2D)));

        return Math.sqrt(variance.get() / values.size());
    }

    /**
     * Calculates the kurtosis of {@param values}
     * @param values The number values
     * @return The kurtosis of {@param values}
     */
      public double getKurtosis(Collection<? extends Number> values) {
        double n = values.size();

        if (n < 3)
            return Double.NaN;

        double average = getAverage(values);
        double stDev = getStandardDeviation(values);

        AtomicDouble accum = new AtomicDouble(0D);

        values.forEach(delay -> accum.getAndAdd(Math.pow(delay.doubleValue() - average, 4D)));

        return n * (n + 1) / ((n - 1) * (n - 2) * (n - 3)) *
                (accum.get() / Math.pow(stDev, 4D)) - 3 *
                Math.pow(n - 1, 2D) / ((n - 2) * (n - 3));
    }

    public double getSkewness(final Collection<? extends Number> data) {
        double sum = 0;
        int count = 0;

        final List<Double> numbers = Lists.newArrayList();

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }

        Collections.sort(numbers);

        final double mean =  sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double variance = getVariance(data);

        return 3 * (mean - median) / variance;
    }

    public double getAverage(final Collection<? extends Number> data) {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(0D);
    }


    public static int getMode(final Collection<? extends Number> array) {
        int mode = (int) array.toArray()[0];
        int maxCount = 0;
        for (final Number value : array) {
            int count = 1;
            for (final Number i : array) {
                if (i.equals(value))
                    count++;
                if (count > maxCount) {
                    mode = (int) value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }

    private double getMedian(final List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }

    public boolean isExponentiallySmall(final Number number) {
        return number.doubleValue() < 1 && Double.toString(number.doubleValue()).contains("E");
    }

    public boolean isExponentiallyLarge(final Number number) {
        return number.doubleValue() > 10000 && Double.toString(number.doubleValue()).contains("E");
    }

    public long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }

    public double getGcd(final double a, final double b) {
        if (a < b) {
            return getGcd(b, a);
        }

        if (Math.abs(b) < 0.001) {
            return a;
        } else {
            return getGcd(b, a - Math.floor(a / b) * b);
        }
    }

    public double getCps(final Collection<? extends Number> data) {
        return 20 / getAverage(data);
    }

    public int getDuplicates(final Collection<? extends Number> data) {
        return (int)(data.size() - data.stream().distinct().count());
    }

    public int getDistinct(final Collection<? extends Number> data) {
        return (int)data.stream().distinct().count();
    }

    public static Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        final float rotX = (float)Math.toRadians(yaw);
        final float rotY = (float)Math.toRadians(pitch);
        vector.setY(-Math.sin(rotY));
        final double xz = Math.cos(rotY);
        vector.setX(-xz * Math.sin(rotX));
        vector.setZ(xz * Math.cos(rotX));
        return vector;
    }

    public static float[] getRotations(final Location one, final Location two) {
        final double diffX = two.getX() - one.getX();
        final double diffZ = two.getZ() - one.getZ();
        final double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float clamp(final float val, final float min, final float max) {
        return Math.max(min, Math.min(max, val));
    }
}
