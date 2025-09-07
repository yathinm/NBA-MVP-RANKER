import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class MVPViewerEnhanced extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> teamFilter;
    private JComboBox<String> sortCombo;
    private JCheckBox ascendingCheck;
    private JLabel statsLabel;
    private List<PlayerData> allPlayers;
    private DecimalFormat df = new DecimalFormat("#.##");
    
    public MVPViewerEnhanced() {
        initializeUI();
        loadData();
        setupEventHandlers();
    }
    
    private void initializeUI() {
        setTitle("NBA MVP Analyzer - Advanced Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Use default look and feel
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);
        
        // Create main content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 35));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🏀 NBA MVP Analyzer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Advanced Player Statistics Dashboard");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(titlePanel, BorderLayout.WEST);
        
        // Add refresh button
        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.setBackground(new Color(0, 120, 215));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        refreshBtn.addActionListener(e -> loadData());
        
        panel.add(refreshBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new CompoundBorder(
            new EmptyBorder(20, 15, 20, 15),
            new TitledBorder("Filters & Controls")
        ));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Search field
        JLabel searchLabel = new JLabel("Search Player:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(searchLabel);
        panel.add(Box.createVerticalStrut(5));
        
        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(searchField);
        panel.add(Box.createVerticalStrut(15));
        
        // Team filter
        JLabel teamLabel = new JLabel("Filter by Team:");
        teamLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(teamLabel);
        panel.add(Box.createVerticalStrut(5));
        
        teamFilter = new JComboBox<>();
        teamFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(teamFilter);
        panel.add(Box.createVerticalStrut(15));
        
        // Sort options
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(sortLabel);
        panel.add(Box.createVerticalStrut(5));
        
        sortCombo = new JComboBox<>(new String[]{
            "MVP Score", "Points", "Assists", "Rebounds", "Steals", "Blocks", "Player Name"
        });
        sortCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(sortCombo);
        panel.add(Box.createVerticalStrut(10));
        
        ascendingCheck = new JCheckBox("Ascending Order");
        panel.add(ascendingCheck);
        panel.add(Box.createVerticalStrut(20));
        
        // Action buttons
        JButton applyBtn = new JButton("Apply Filters");
        applyBtn.setBackground(new Color(0, 120, 215));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setFocusPainted(false);
        applyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        applyBtn.addActionListener(e -> applyFilters());
        panel.add(applyBtn);
        panel.add(Box.createVerticalStrut(10));
        
        JButton resetBtn = new JButton("Reset Filters");
        resetBtn.setBackground(new Color(108, 117, 125));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        resetBtn.addActionListener(e -> resetFilters());
        panel.add(resetBtn);
        panel.add(Box.createVerticalStrut(20));
        
        // Export buttons
        JLabel exportLabel = new JLabel("Export Data:");
        exportLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(exportLabel);
        panel.add(Box.createVerticalStrut(5));
        
        JButton exportCsvBtn = new JButton("Export to CSV");
        exportCsvBtn.setBackground(new Color(40, 167, 69));
        exportCsvBtn.setForeground(Color.WHITE);
        exportCsvBtn.setFocusPainted(false);
        exportCsvBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        exportCsvBtn.addActionListener(e -> exportToCSV());
        panel.add(exportCsvBtn);
        panel.add(Box.createVerticalStrut(10));
        
        JButton exportJsonBtn = new JButton("Export to JSON");
        exportJsonBtn.setBackground(new Color(255, 193, 7));
        exportJsonBtn.setForeground(Color.BLACK);
        exportJsonBtn.setFocusPainted(false);
        exportJsonBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        exportJsonBtn.addActionListener(e -> exportToJSON());
        panel.add(exportJsonBtn);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create table with custom renderer
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        
        // Custom cell renderer for numbers
        table.setDefaultRenderer(Double.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value instanceof Double) {
                    setText(df.format((Double) value));
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                
                if (isSelected) {
                    setBackground(new Color(173, 216, 230));
                } else if (row % 2 == 0) {
                    setBackground(new Color(248, 249, 250));
                } else {
                    setBackground(Color.WHITE);
                }
                
                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        statsLabel = new JLabel("Ready");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel versionLabel = new JLabel("NBA MVP Analyzer v2.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(100, 100, 100));
        
        panel.add(statsLabel, BorderLayout.WEST);
        panel.add(versionLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void loadData() {
        allPlayers = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("mvp_candidates.csv"));
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (firstLine) {
                    firstLine = false;
                } else if (data.length >= 7) {
                    try {
                        PlayerData player = new PlayerData(
                            data[0], // Player
                            data[1], // Team
                            Double.parseDouble(data[2]), // PTS
                            Double.parseDouble(data[3]), // AST
                            Double.parseDouble(data[4]), // TRB
                            Double.parseDouble(data[5]), // STL
                            Double.parseDouble(data[6]), // BLK
                            Double.parseDouble(data[7])  // MVP_Score
                        );
                        allPlayers.add(player);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
            
            reader.close();
            
            // Update UI
            updateTable();
            updateTeamFilter();
            updateStats();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        
        if (allPlayers.isEmpty()) return;
        
        // Set headers
        String[] headers = {"Rank", "Player", "Team", "Points", "Assists", "Rebounds", "Steals", "Blocks", "MVP Score"};
        model.setColumnIdentifiers(headers);
        
        // Add data
        for (int i = 0; i < allPlayers.size(); i++) {
            PlayerData player = allPlayers.get(i);
            Object[] row = {
                i + 1,
                player.name,
                player.team,
                player.points,
                player.assists,
                player.rebounds,
                player.steals,
                player.blocks,
                player.mvpScore
            };
            model.addRow(row);
        }
    }
    
    private void updateTeamFilter() {
        teamFilter.removeAllItems();
        teamFilter.addItem("All Teams");
        
        Set<String> teams = new TreeSet<>();
        for (PlayerData player : allPlayers) {
            teams.add(player.team);
        }
        
        for (String team : teams) {
            teamFilter.addItem(team);
        }
    }
    
    private void updateStats() {
        if (allPlayers.isEmpty()) {
            statsLabel.setText("No data loaded");
            return;
        }
        
        int totalPlayers = allPlayers.size();
        double avgScore = allPlayers.stream().mapToDouble(p -> p.mvpScore).average().orElse(0);
        PlayerData topPlayer = allPlayers.get(0);
        
        statsLabel.setText(String.format("Loaded %d players | Avg MVP Score: %.1f | Top: %s (%.1f)", 
            totalPlayers, avgScore, topPlayer.name, topPlayer.mvpScore));
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
        });
        
        // Team filter
        teamFilter.addActionListener(e -> applyFilters());
        
        // Sort options
        sortCombo.addActionListener(e -> applyFilters());
        ascendingCheck.addActionListener(e -> applyFilters());
    }
    
    private void applyFilters() {
        if (allPlayers == null) return;
        
        List<PlayerData> filtered = new ArrayList<>(allPlayers);
        
        // Apply search filter
        String searchText = searchField.getText().toLowerCase();
        if (!searchText.isEmpty()) {
            filtered.removeIf(player -> !player.name.toLowerCase().contains(searchText));
        }
        
        // Apply team filter
        String selectedTeam = (String) teamFilter.getSelectedItem();
        if (selectedTeam != null && !selectedTeam.equals("All Teams")) {
            filtered.removeIf(player -> !player.team.equals(selectedTeam));
        }
        
        // Apply sorting
        String sortBy = (String) sortCombo.getSelectedItem();
        boolean ascending = ascendingCheck.isSelected();
        
        switch (sortBy) {
            case "MVP Score":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.mvpScore, b.mvpScore) : 
                    Double.compare(b.mvpScore, a.mvpScore));
                break;
            case "Points":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.points, b.points) : 
                    Double.compare(b.points, a.points));
                break;
            case "Assists":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.assists, b.assists) : 
                    Double.compare(b.assists, a.assists));
                break;
            case "Rebounds":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.rebounds, b.rebounds) : 
                    Double.compare(b.rebounds, a.rebounds));
                break;
            case "Steals":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.steals, b.steals) : 
                    Double.compare(b.steals, a.steals));
                break;
            case "Blocks":
                filtered.sort((a, b) -> ascending ? 
                    Double.compare(a.blocks, b.blocks) : 
                    Double.compare(b.blocks, a.blocks));
                break;
            case "Player Name":
                filtered.sort((a, b) -> ascending ? 
                    a.name.compareTo(b.name) : 
                    b.name.compareTo(a.name));
                break;
        }
        
        // Update table with filtered data
        model.setRowCount(0);
        for (int i = 0; i < filtered.size(); i++) {
            PlayerData player = filtered.get(i);
            Object[] row = {
                i + 1,
                player.name,
                player.team,
                player.points,
                player.assists,
                player.rebounds,
                player.steals,
                player.blocks,
                player.mvpScore
            };
            model.addRow(row);
        }
    }
    
    private void resetFilters() {
        searchField.setText("");
        teamFilter.setSelectedIndex(0);
        sortCombo.setSelectedIndex(0);
        ascendingCheck.setSelected(false);
        applyFilters();
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("mvp_analysis_export.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                // Write headers
                writer.println("Rank,Player,Team,Points,Assists,Rebounds,Steals,Blocks,MVP_Score");
                
                // Write data
                for (int i = 0; i < model.getRowCount(); i++) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if (j > 0) line.append(",");
                        Object value = model.getValueAt(i, j);
                        if (value instanceof Double) {
                            line.append(df.format((Double) value));
                        } else {
                            line.append(value.toString());
                        }
                    }
                    writer.println(line.toString());
                }
                
                JOptionPane.showMessageDialog(this, "Data exported successfully!", 
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToJSON() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("mvp_analysis_export.json"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("{");
                writer.println("  \"mvp_analysis\": {");
                writer.println("    \"export_date\": \"" + new Date() + "\",");
                writer.println("    \"total_players\": " + model.getRowCount() + ",");
                writer.println("    \"players\": [");
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    writer.println("      {");
                    writer.println("        \"rank\": " + model.getValueAt(i, 0) + ",");
                    writer.println("        \"player\": \"" + model.getValueAt(i, 1) + "\",");
                    writer.println("        \"team\": \"" + model.getValueAt(i, 2) + "\",");
                    writer.println("        \"points\": " + df.format((Double) model.getValueAt(i, 3)) + ",");
                    writer.println("        \"assists\": " + df.format((Double) model.getValueAt(i, 4)) + ",");
                    writer.println("        \"rebounds\": " + df.format((Double) model.getValueAt(i, 5)) + ",");
                    writer.println("        \"steals\": " + df.format((Double) model.getValueAt(i, 6)) + ",");
                    writer.println("        \"blocks\": " + df.format((Double) model.getValueAt(i, 7)) + ",");
                    writer.println("        \"mvp_score\": " + df.format((Double) model.getValueAt(i, 8)));
                    writer.print("      }");
                    if (i < model.getRowCount() - 1) writer.print(",");
                    writer.println();
                }
                
                writer.println("    ]");
                writer.println("  }");
                writer.println("}");
                
                JOptionPane.showMessageDialog(this, "JSON data exported successfully!", 
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting JSON: " + e.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MVPViewerEnhanced().setVisible(true);
        });
    }
    
    // Inner class to hold player data
    private static class PlayerData {
        String name, team;
        double points, assists, rebounds, steals, blocks, mvpScore;
        
        PlayerData(String name, String team, double points, double assists, 
                  double rebounds, double steals, double blocks, double mvpScore) {
            this.name = name;
            this.team = team;
            this.points = points;
            this.assists = assists;
            this.rebounds = rebounds;
            this.steals = steals;
            this.blocks = blocks;
            this.mvpScore = mvpScore;
        }
    }
}
