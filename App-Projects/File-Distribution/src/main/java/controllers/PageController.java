package controllers;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ResourceBundle;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PageController implements Initializable {
    @FXML
    public PieChart pieChart;
    @FXML
    public ImageView imageView;
    @FXML
    public TreeView<String> treeView;
    @FXML
    private Label nameLabel, typeLabel, sizeLabel, locationLabel, dateModifiedLabel;

    private final Desktop desktop = Desktop.getDesktop();

    private final String imageNotFoundPath = Objects.requireNonNull(getClass().getResource("/images/imageNotFound.png")).toExternalForm();

    private final Map<String, File> selectedFiles = new HashMap<>();
    private final Map<String, Integer> selectedExtensions = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set tree view root.
        TreeItem<String> root = new TreeItem<>("<Select Folder>");
        treeView.setRoot(root);

        // Set tree view selection listener.
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue.getValue().equals("<Select Folder>")) return;

            String fileName = newValue.getValue();
            File file = selectedFiles.get(fileName);

            if (file == null) return;

            // Get file info.
            String type = file.isDirectory() ? "Directory" : "File";
            String dateModified = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(file.lastModified()));
            String fileLocation = file.getParent();
            String size = file.isDirectory() ? "-" : String.format("%.2f KB", file.length() / 1024.0);

            // Get and update label info.
            nameLabel.setText("Name: " + fileName);
            typeLabel.setText("Type: " + type);
            dateModifiedLabel.setText("Date Modified: " + dateModified);
            locationLabel.setText("Location: " + fileLocation);
            sizeLabel.setText("Size: " + size);

            // Set image preview.
            if (isImage(file)) {
                try {
                    Image image = new Image(new FileInputStream(file));
                    imageView.setImage(image);
                } catch (IOException e) {
                    imageView.setImage(new Image(imageNotFoundPath));
                }
            } else {
                imageView.setImage(new Image(imageNotFoundPath));
            }
        });
    }

    public void updatePieChart() {
        // Create pie chart data.
        // Get total extensions.
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalFiles = selectedExtensions.values().stream().mapToInt(Integer::intValue).sum();

        // Loop through selected extensions.
        for (Map.Entry<String, Integer> entry : selectedExtensions.entrySet()) {
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalFiles;

            String extension = entry.getKey();
            String label = String.format("%s: %.1f%% (%d)", extension, percentage, count);

            // Add data to pie chart.
            pieChartData.add(new PieChart.Data(label, count));
        }

        pieChart.setData(pieChartData);

        pieChart.getData().forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / totalFiles * 100));
            Tooltip tooltip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), tooltip);
        });
    }

    public void populateTreeView(File directory, TreeItem<String> parent) {
        File[] files = directory.listFiles();
        if (files != null) {
            // Loop through files.
            for (File file : files) {
                TreeItem<String> item = new TreeItem<>(file.getName());
                parent.getChildren().add(item);

                // Add to selected files.
                selectedFiles.put(file.getName(), file);

                // If it is a directory, populate tree view again.
                if (file.isDirectory()) {
                    populateTreeView(file, item);
                } else {
                    // Check file extension (e.g., *.jpg, *.java, or no extension).
                    String extension = getFileExtension(file);

                    // Add to selected extensions.
                    selectedExtensions.put(extension, selectedExtensions.getOrDefault(extension, 0) + 1);
                }
            }
        }
    }

    public boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp");
    }

    public String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return "No Extension";
        }
        return "*." + name.substring(lastIndexOf + 1);
    }

    public File getSelectedFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        return directoryChooser.showDialog(null);
    }

    @FXML
    public void getGithub() {
        try {
            desktop.browse(new URI("https://github.com/Jagaradoz/JavaFX-Projects"));
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void clearFolder() {
        pieChart.getData().clear();

        treeView.setRoot(new TreeItem<>("<Select Folder>"));

        selectedFiles.clear();
        selectedExtensions.clear();

        nameLabel.setText("Name: -");
        typeLabel.setText("Type: -");
        sizeLabel.setText("Size: -");
        locationLabel.setText("Location: -");
        dateModifiedLabel.setText("Date Modified: -");

        imageView.setImage(null);
    }

    @FXML
    public void selectFolder() {
        // Get selected directory.
        File selectedFolder = getSelectedFolder();

        // Check if the selected folder exists and is a directory.
        if (selectedFolder != null && selectedFolder.exists() && selectedFolder.isDirectory()) {
            TreeItem<String> root = new TreeItem<>(selectedFolder.getName());
            treeView.setRoot(root);
            selectedExtensions.clear();
            selectedFiles.clear();

            populateTreeView(selectedFolder, root);
            updatePieChart();
        }
    }
}
