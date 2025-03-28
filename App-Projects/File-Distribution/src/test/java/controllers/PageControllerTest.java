package controllers;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.testfx.api.FxRobot;
import javafx.scene.control.Label;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.scene.chart.PieChart;
import javafx.scene.image.ImageView;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import org.junit.jupiter.api.DisplayName;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ApplicationExtension.class})
@DisplayName("PageControllerTest")
class PageControllerTest {
    private PageController spyController;

    @Start
    public void start(Stage stage) throws Exception {
        // Get fxml page.
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/Page.fxml")));

        // Spy on controller.
        // Set the controller.
        spyController = spy(PageController.class);
        loader.setControllerFactory((_) -> spyController);

        // Set up the stage.
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("All fxml elements should not be null and visible")
    public void allElementsTest(FxRobot robot) {
        // Assert that name label exists.
        Label nameLabel = robot.lookup("#nameLabel").query();
        assertNotNull(nameLabel, "nameLabel should not be null");
        assertTrue(nameLabel.isVisible(), "nameLabel should be visible");

        // Assert that type label exists.
        Label typeLabel = robot.lookup("#typeLabel").query();
        assertNotNull(typeLabel, "typeLabel should not be null");
        assertTrue(typeLabel.isVisible(), "typeLabel should be visible");

        // Assert that size label exists.
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        assertNotNull(sizeLabel, "sizeLabel should not be null");
        assertTrue(sizeLabel.isVisible(), "sizeLabel should be visible");

        // Assert that location label exists.
        Label locationLabel = robot.lookup("#locationLabel").query();
        assertNotNull(locationLabel, "locationLabel should not be null");
        assertTrue(locationLabel.isVisible(), "locationLabel should be visible");

        // Assert that date modified label exists.
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();
        assertNotNull(dateModifiedLabel, "dateModifiedLabel should not be null");
        assertTrue(dateModifiedLabel.isVisible(), "dateModifiedLabel should be visible");

        // Assert that pie chart exists.
        PieChart pieChart = robot.lookup("#pieChart").query();
        assertNotNull(pieChart, "pieChart should not be null");
        assertTrue(pieChart.isVisible(), "pieChart should be visible");

        // Assert that image view exists.
        ImageView imageView = robot.lookup("#imageView").query();
        assertNotNull(imageView, "imageView should not be null");
        assertTrue(imageView.isVisible(), "imageView should be visible");

        // Assert that tree view exists.
        TreeView<String> treeView = robot.lookup("#treeView").query();
        assertNotNull(treeView, "treeView should not be null");
        assertTrue(treeView.isVisible(), "treeView should be visible");
    }

    @Test
    @DisplayName("ImageNotFound should be shown when image does not exist")
    public void imageNotFoundTest(FxRobot robot) {
        try {
            // Get image not found path field.
            // Set the field accessible.
            // Get path from the field.
            Field imageNotFoundPath = spyController.getClass().getDeclaredField("imageNotFoundPath");
            imageNotFoundPath.setAccessible(true);
            String path = (String) imageNotFoundPath.get(spyController);

            // Assert that image not found path exists.
            assertNotNull(imageNotFoundPath, "imageNotFoundPath should not be null");
            assertNotNull(path, "imageNotFoundPath value should not be null");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Tree view root should be '<Select Folder>'")
    public void treeViewRootTest(FxRobot robot) {
        // Get tree view root.
        TreeView<String> treeView = robot.lookup("#treeView").query();
        TreeItem<String> root = treeView.getRoot();

        // Assert that tree view root is '<Select Folder>'.
        assertEquals("<Select Folder>", root.getValue(), "Tree view root should be '<Select Folder>'");
    }

    @Test
    @DisplayName("Tree view should select file and show details")
    public void treeViewSelectFileTest(FxRobot robot) throws URISyntaxException, TimeoutException {
        // Mock get selected folder method.
        mockGetSelectedFolderMethod();

        // Get tree view.
        TreeView<String> treeView = robot.lookup("#treeView").query();

        // Select a folder to distribute.
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> !treeView.getRoot().getChildren().isEmpty());

        // Select a sub file/folder.
        Platform.runLater(() -> treeView.getSelectionModel().select(treeView.getRoot().getChildren().getFirst()));
        WaitForAsyncUtils.waitForFxEvents();

        // Get the selected item node.
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Get the details.
        Label nameLabel = robot.lookup("#nameLabel").query();
        Label typeLabel = robot.lookup("#typeLabel").query();
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        Label locationLabel = robot.lookup("#locationLabel").query();
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();

        // Assert that the details are updated.
        assertNotNull(selectedItem, "Selected item should not be null");
        assertNotEquals("Name: -", nameLabel.getText(), "Name should be updated");
        assertNotEquals("Type: -", typeLabel.getText(), "Type should be updated");
        assertNotEquals("Size: -", sizeLabel.getText(), "Size should be updated");
        assertNotEquals("Location: -", locationLabel.getText(), "Location should be updated");
        assertNotEquals("Date Modified: -", dateModifiedLabel.getText(), "Date Modified should be updated");

        // Verify selected folder.
        verifySelectFolder();
    }

    @Test
    @DisplayName("populateTreeView should work correctly")
    public void populateTreeViewTest(FxRobot robot) throws URISyntaxException, TimeoutException {
        // Mock get selected folder method.
        mockGetSelectedFolderMethod();

        // Get tree view.
        TreeView<String> treeView = robot.lookup("#treeView").query();

        // Select a folder to distribute.
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> !treeView.getRoot().getChildren().isEmpty());

        // Assert that populate tree view correctly.
        assertNotNull(treeView.getRoot(), "Tree view root should not be null");
        assertEquals("txt", treeView.getRoot().getValue(), "Root should be named 'txt'");
        assertFalse(treeView.getRoot().getChildren().isEmpty(), "Root should have children");

        // Verify selected folder.
        verifySelectFolder();
    }

    @Test
    @DisplayName("updatePieChart should work correctly")
    public void updatePieChartTest(FxRobot robot) throws URISyntaxException {
        // Mock get selected folder method.
        mockGetSelectedFolderMethod();

        // Get pie chart.
        PieChart pieChart = robot.lookup("#pieChart").query();

        // Select a folder to distribute.
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // Assert that update pie chart correctly.
        assertFalse(pieChart.getData().isEmpty(), "Pie chart should have data");
        assertEquals(1, pieChart.getData().size(), "Pie chart should have 1 extensions (*.txt)");

        // Verify selected folder.
        verifySelectFolder();
    }

    @Test
    @DisplayName("isImage should work correctly")
    public void isImageTest() throws URISyntaxException {
        // Get files to test.
        File imageNotFound = new File(Objects.requireNonNull(getClass().getResource("/images/imageNotFound.png")).toURI());
        File test1 = new File(Objects.requireNonNull(getClass().getResource("/txt/test1.txt")).toURI());

        // Assert that is image method works correctly.
        assertTrue(spyController.isImage(imageNotFound), "imageNotFound.png should be an image");
        assertFalse(spyController.isImage(test1), "test1.txt should not be an image");
    }

    @Test
    @DisplayName("getFileExtension should work correctly")
    public void getFileExtensionTest() throws URISyntaxException {
        // Get files to test.
        File imageNotFound = new File(Objects.requireNonNull(getClass().getResource("/images/imageNotFound.png")).toURI());
        File test1 = new File(Objects.requireNonNull(getClass().getResource("/txt/test1.txt")).toURI());

        // Assert that get file extension method works correctly.
        assertEquals("*.png", spyController.getFileExtension(imageNotFound), "imageNotFound.png should have extension 'png'");
        assertEquals("*.txt", spyController.getFileExtension(test1), "test1.txt should have extension 'txt'");
    }

    @Test
    @DisplayName("getGithub should open a new browser window")
    public void getGithubTest(FxRobot robot) throws NoSuchFieldException, IllegalAccessException, URISyntaxException, IOException {
        // Get mocked desktop.
        Desktop mockDesktop = mock(Desktop.class);

        // Get desktop field and set new value to the field.
        Field desktopField = PageController.class.getDeclaredField("desktop");
        desktopField.setAccessible(true);
        desktopField.set(spyController, mockDesktop);

        // Get github.
        spyController.getGithub();

        // Verify that github is opened.
        verify(mockDesktop).browse(new URI("https://github.com/Jagaradoz/JavaFX-Projects"));
    }

    @Test
    @DisplayName("clearFolder should clear all data")
    public void clearFolderTest(FxRobot robot) throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
        // Mock get selected folder method.
        mockGetSelectedFolderMethod();

        // Select a folder to distribute.
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // Clear selected folder.
        Platform.runLater(() -> spyController.clearFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // Assert that pie chart is cleared.
        PieChart pieChart = robot.lookup("#pieChart").query();
        assertTrue(pieChart.getData().isEmpty(), "PieChart should be empty after clearing");

        // Assert that tree view is cleared.
        TreeView<String> treeView = robot.lookup("#treeView").query();
        assertEquals("<Select Folder>", treeView.getRoot().getValue(), "TreeView root should be reset to '<Select Folder>'");

        // Get the details.
        Label nameLabel = robot.lookup("#nameLabel").query();
        Label typeLabel = robot.lookup("#typeLabel").query();
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        Label locationLabel = robot.lookup("#locationLabel").query();
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();

        // Assert that details are cleared.
        assertEquals("Name: -", nameLabel.getText(), "Name label should be reset");
        assertEquals("Type: -", typeLabel.getText(), "Type label should be reset");
        assertEquals("Size: -", sizeLabel.getText(), "Size label should be reset");
        assertEquals("Location: -", locationLabel.getText(), "Location label should be reset");
        assertEquals("Date Modified: -", dateModifiedLabel.getText(), "Date Modified label should be reset");

        // Assert that images are cleared.
        ImageView imageView = robot.lookup("#imageView").query();
        assertNull(imageView.getImage(), "ImageView should be cleared");

        // Verify clear folder.
        verify(spyController).clearFolder();

        // Get selected files class.
        // Get selected extensions class.
        Field selectedFilesClass = PageController.class.getDeclaredField("selectedFiles");
        Field selectedExtensionsClass = PageController.class.getDeclaredField("selectedExtensions");

        // Set accessible.
        selectedFilesClass.setAccessible(true);
        selectedExtensionsClass.setAccessible(true);

        // Get selected files and extensions from controller.
        @SuppressWarnings("unchecked") Map<String, File> selectedFiles = (Map<String, File>) selectedFilesClass.get(spyController);
        @SuppressWarnings("unchecked") Map<String, Integer> selectedExtensions = (Map<String, Integer>) selectedExtensionsClass.get(spyController);

        // Assert that selected files and extensions are cleared.
        assertTrue(selectedFiles.isEmpty(), "selectedFiles should be empty");
        assertTrue(selectedExtensions.isEmpty(), "selectedExtensions should be empty");
    }

    public void mockGetSelectedFolderMethod() throws URISyntaxException {
        // Get txt folder.
        URI path = Objects.requireNonNull(getClass().getResource("/txt")).toURI();
        File mockFolder = new File(path);

        // Mock get selected folder method.
        doReturn(mockFolder).when(spyController).getSelectedFolder();
    }

    public void verifySelectFolder() {
        // Verify selected folder.
        verify(spyController).selectFolder();

        // File captor.
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);

        // Tree item captor.
        @SuppressWarnings("unchecked")
        ArgumentCaptor<TreeItem<String>> treeItemCaptor = ArgumentCaptor.forClass(TreeItem.class);

        // Verify populate tree view.
        verify(spyController).populateTreeView(fileCaptor.capture(), treeItemCaptor.capture());

        // Get values from captors.
        File capturedFile = fileCaptor.getValue();
        TreeItem<String> capturedTreeItem = treeItemCaptor.getValue();

        // Assert that selected folder is passed correctly.
        assertNotNull(capturedFile, "Captured File should not be null");
        assertEquals("txt", capturedFile.getName(), "Captured File name should be 'txt'");
        assertNotNull(capturedTreeItem, "Captured TreeItem should not be null");
        assertEquals("txt", capturedTreeItem.getValue(), "Captured TreeItem value should be 'txt'");

        // Verify update pie chart.
        verify(spyController).updatePieChart();
    }
}
