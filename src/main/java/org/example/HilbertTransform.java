package org.example;

import org.apache.commons.math3.complex.Complex;

public class HilbertTransform {
    public double[] hilbertTransform(double[] signal) {
        int n = signal.length;
        double[] transformed = new double[n];

        for (int i = 0; i < n; i++) {
            Complex sum = Complex.ZERO;
            for (int j = 0; j < n; j++) {
                double angle = Math.PI * (i - j) / n;
                sum = sum.add(new Complex(signal[j] * Math.cos(angle), signal[j] * Math.sin(angle)));
            }
            transformed[i] = sum.getReal();
        }

        return transformed;
    }

    public double[] calculateInstantaneousAmplitude(double[] signal, double[] hilbertTransformed) {
        double[] amplitude = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            amplitude[i] = Math.sqrt(signal[i] * signal[i] + hilbertTransformed[i] * hilbertTransformed[i]);
        }
        return amplitude;
    }

    public double[] calculateInstantaneousFrequency(double[] hilbertTransformed) {
        double[] frequency = new double[hilbertTransformed.length];
        double[] phase = new double[hilbertTransformed.length];

        // Calculate phase from the Hilbert Transform
        for (int i = 0; i < hilbertTransformed.length; i++) {
            phase[i] = Math.atan2(hilbertTransformed[i], hilbertTransformed[i]); // TODO Replace with actual analytic signal
        }
        return phase;
    }
}
