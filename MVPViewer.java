import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MVPViewer {
    public static void main(String[] args) {
        // Create the main window
        JFrame frame = new JFrame("Top MVP Candidates");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Table and model to hold data
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        try {
            // Read the CSV file
            BufferedReader reader = new BufferedReader(new FileReader("mvp_candidates.csv"));
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (firstLine) {
                    // Add table headers
                    model.setColumnIdentifiers(data);
                    firstLine = false;
                } else {
                    // Add each row of data
                    model.addRow(data);
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading file.");
        }

        // Add table to scroll pane and show the window
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}
