package org.example;

import java.util.List;

public class HHT {
    public static void main(String[] args) {
        double[] signal = { /* Your input signal data */ };

        EMD emd = new EMD();
        List<double[]> imfs = emd.emd(signal);

        HilbertTransform hilbert = new HilbertTransform();
        for (double[] imf : imfs) {
            double[] hilbertTransformed = hilbert.hilbertTransform(imf);
            // Process the Hilbert transformed data (e.g., calculate instantaneous frequency)
        }
    }
}