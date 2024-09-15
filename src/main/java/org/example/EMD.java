package org.example;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class EMD {



    public List<double[]> emd(double[] signal) {
        List<double[]> imfs = new ArrayList<>();
        double[] residual = signal.clone();

        while (true) {
            double[] imf = sift(residual);
            imfs.add(imf);
            residual = subtract(residual, imf);
            if (isResidualStop(residual)) {
                break;
            }
        }

        return imfs;
    }

    public double[] sift(double[] signal) {
        double[] imf = new double[signal.length];
        double[] residual = signal.clone();
        double[] previousImf = new double[signal.length];

        while (true) {
            // Step 1: Identify local maxima and minima
            double[] maxima = findLocalMaxima(residual);
            double[] minima = findLocalMinima(residual);

            // Step 2: Create upper and lower envelopes
            double[] upperEnvelope = createEnvelope(maxima);
            double[] lowerEnvelope = createEnvelope(minima);

            // Step 3: Calculate the mean of the envelopes
            double[] meanEnvelope = mean(upperEnvelope, lowerEnvelope);

            // Step 4: Extract IMF
            for (int i = 0; i < residual.length; i++) {
                imf[i] = residual[i] - meanEnvelope[i];
            }

            // Step 5: Check for stopping criteria
            if (isStoppingCriteriaMet(imf, previousImf)) {
                break;
            }

            // Update residual for the next iteration
            previousImf = imf.clone();
            for (int i = 0; i < residual.length; i++) {
                residual[i] = residual[i] - imf[i];
            }
        }

        return imf;
    }

    private double[] subtract(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    private double[] findLocalMaxima(double[] signal) {
        List<Double> maxima = new ArrayList<>();

        for (int i = 1; i < signal.length - 1; i++) {
            if (signal[i] > signal[i - 1] && signal[i] > signal[i + 1]) {
                maxima.add(signal[i]);
            }
        }

        // Convert List to array
        return convertListToArray(maxima);
    }

    private double[] findLocalMinima(double[] signal) {
        List<Double> minima = new ArrayList<>();

        for (int i = 1; i < signal.length - 1; i++) {
            if (signal[i] < signal[i - 1] && signal[i] < signal[i + 1]) {
                minima.add(signal[i]);
            }
        }

        // Convert List to array
        return convertListToArray(minima);
    }

    private double[] convertListToArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private double[] createEnvelope(double[] extrema) {
        List<Double> validExtrema = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Collect valid extrema and their indices
        for (int i = 0; i < extrema.length; i++) {
            if (!Double.isNaN(extrema[i])) {
                validExtrema.add(extrema[i]);
                indices.add(i);
            }
        }

        // Convert lists to arrays
        double[] x = indices.stream().mapToDouble(i -> i).toArray();
        double[] y = validExtrema.stream().mapToDouble(d -> d).toArray();

        // Create the cubic spline interpolator
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction spline = interpolator.interpolate(x, y);

        // Create the envelope array
        double[] envelope = new double[extrema.length];

        // Fill the envelope using the spline
        for (int i = 0; i < envelope.length; i++) {
            envelope[i] = spline.value(i); // Evaluate the spline at each index
        }

        return envelope;
    }

    private double[] mean(double[] upper, double[] lower) {
        double[] mean = new double[upper.length];
        for (int i = 0; i < upper.length; i++) {
            // Handle NaN values
            if (Double.isNaN(upper[i]) || Double.isNaN(lower[i])) {
                mean[i] = Double.NaN; // Keep NaN if either envelope is NaN
            } else {
                mean[i] = (upper[i] + lower[i]) / 2.0;
            }
        }
        return mean;
    }

    private boolean isStoppingCriteriaMet(double[] imf, double[] previousImf) {
        // Implement stopping criteria (e.g., check if the IMF is flat)
        // Placeholder implementation
        return false; // Replace with actual stopping criteria
    }
    private boolean isResidualStop(double[] residual) {
        // Define stopping criteria for the residual
        return false; // Placeholder
    }
}
