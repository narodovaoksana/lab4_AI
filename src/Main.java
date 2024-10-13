import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main {
    private static Map<String, int[]> trainingData = new HashMap<>();
    private static String[] letters = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public static void main(String[] args) {
        DrawingPanel drawingPanel = new DrawingPanel(20, 20);
        Neuron neuron = new Neuron(400);

        JFrame frame = new JFrame("Letter Recognition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton trainButton = new JButton("Train");
        JButton classifyButton = new JButton("Classify");
        JButton clearButton = new JButton("Clear");
        JButton testButton = new JButton("Test All");

        JComboBox<String> letterSelector = new JComboBox<>(letters);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(new JLabel("Select Letter:"));
        buttonPanel.add(letterSelector);
        buttonPanel.add(trainButton);
        buttonPanel.add(classifyButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(testButton);

        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.setSize(500, 450);
        frame.setVisible(true);

        trainButton.addActionListener(e -> {
            String selectedLetter = (String) letterSelector.getSelectedItem();
            int[] desiredOutput = getDesiredOutput(selectedLetter);
            int[] input = drawingPanel.getPixels();

            int epochCount = 0;
            boolean allCorrect = false;

            while (!allCorrect) {
                allCorrect = true;
                epochCount++;

                for (int i = 0; i < input.length; i++) {
                    double[] output = neuron.classify(input);
                    if (!Arrays.equals(getBinaryOutput(output), desiredOutput)) {
                        neuron.train(input, desiredOutput);
                        allCorrect = false;
                    }
                }
            }

            trainingData.put(selectedLetter, input);
            System.out.println("Neuron trained for letter: " + selectedLetter);
            System.out.println("Training completed in " + epochCount + " epochs.");
        });

        classifyButton.addActionListener(e -> {
            int[] input = drawingPanel.getPixels();
            double[] output = neuron.classify(input);

            System.out.println("Similarity with each trained letter:");
            for (int i = 0; i < letters.length; i++) {
                double similarity = output[i] * 100;
                System.out.printf("Letter %s: %.2f%% similarity\n", letters[i], similarity);
            }

            // Виведення літери з найбільшою схожістю
            double maxSimilarity = 0;
            String recognizedLetter = "Unknown";
            for (int i = 0; i < letters.length; i++) {
                if (output[i] > maxSimilarity) {
                    maxSimilarity = output[i];
                    recognizedLetter = letters[i];
                }
            }

            JOptionPane.showMessageDialog(frame, "Recognized letter: " + recognizedLetter +
                    "\nSimilarity: " + (maxSimilarity * 100) + "%");
        });

        clearButton.addActionListener(e -> drawingPanel.clear());

        testButton.addActionListener(e -> {
            if (trainingData.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No data for testing.");
                return;
            }

            for (Map.Entry<String, int[]> entry : trainingData.entrySet()) {
                String letter = entry.getKey();
                int[] input = entry.getValue();
                double[] output = neuron.classify(input);

                double maxSimilarity = 0;
                String recognizedLetter = "Unknown";
                for (int i = 0; i < letters.length; i++) {
                    double similarity = output[i];
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                        recognizedLetter = letters[i];
                    }
                }

                System.out.println("Test: " + letter + " -> Recognized: " + recognizedLetter +
                        " (Similarity: " + (maxSimilarity * 100) + "%)");
            }
        });
    }

    private static int[] getDesiredOutput(String letter) {
        int[] output = new int[26];
        Arrays.fill(output, 0);
        output[Arrays.asList(letters).indexOf(letter)] = 1;
        return output;
    }

    private static int[] getBinaryOutput(double[] output) {
        int[] binaryOutput = new int[output.length];
        for (int i = 0; i < output.length; i++) {
            binaryOutput[i] = output[i] >= 0.5 ? 1 : 0;
        }
        return binaryOutput;
    }
}


