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
        // GET FXML PAGE
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/Page.fxml")));

        // SPY ON CONTROLLER
        // SET THE CONTROLLER
        spyController = spy(PageController.class);
        loader.setControllerFactory((_) -> spyController);

        // SET UP THE STAGE
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("All fxml elements should not be null and visible")
    public void allElementsTest(FxRobot robot) {
        // ASSERT THAT NAME LABEL EXISTS
        Label nameLabel = robot.lookup("#nameLabel").query();
        assertNotNull(nameLabel, "nameLabel should not be null");
        assertTrue(nameLabel.isVisible(), "nameLabel should be visible");

        // ASSERT THAT TYPE LABEL EXISTS
        Label typeLabel = robot.lookup("#typeLabel").query();
        assertNotNull(typeLabel, "typeLabel should not be null");
        assertTrue(typeLabel.isVisible(), "typeLabel should be visible");

        // ASSERT THAT SIZE LABEL EXISTS
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        assertNotNull(sizeLabel, "sizeLabel should not be null");
        assertTrue(sizeLabel.isVisible(), "sizeLabel should be visible");

        // ASSERT THAT LOCATION LABEL EXISTS
        Label locationLabel = robot.lookup("#locationLabel").query();
        assertNotNull(locationLabel, "locationLabel should not be null");
        assertTrue(locationLabel.isVisible(), "locationLabel should be visible");

        // ASSERT THAT DATE MODIFIED LABEL EXISTS
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();
        assertNotNull(dateModifiedLabel, "dateModifiedLabel should not be null");
        assertTrue(dateModifiedLabel.isVisible(), "dateModifiedLabel should be visible");

        // ASSERT THAT PIE CHART EXISTS
        PieChart pieChart = robot.lookup("#pieChart").query();
        assertNotNull(pieChart, "pieChart should not be null");
        assertTrue(pieChart.isVisible(), "pieChart should be visible");

        // ASSERT THAT IMAGE VIEW EXISTS
        ImageView imageView = robot.lookup("#imageView").query();
        assertNotNull(imageView, "imageView should not be null");
        assertTrue(imageView.isVisible(), "imageView should be visible");

        // ASSERT THAT TREE VIEW EXISTS
        TreeView<String> treeView = robot.lookup("#treeView").query();
        assertNotNull(treeView, "treeView should not be null");
        assertTrue(treeView.isVisible(), "treeView should be visible");
    }

    @Test
    @DisplayName("ImageNotFound should be shown when image does not exist")
    public void imageNotFoundTest(FxRobot robot) {
        try {
            // GET IMAGE NOT FOUND PATH FIELD
            // SET THE FIELD ACCESSIBLE
            // GET PATH FROM THE FIELD
            Field imageNotFoundPath = spyController.getClass().getDeclaredField("imageNotFoundPath");
            imageNotFoundPath.setAccessible(true);
            String path = (String) imageNotFoundPath.get(spyController);

            // ASSERT THAT IMAGE NOT FOUND PATH EXISTS
            assertNotNull(imageNotFoundPath, "imageNotFoundPath should not be null");
            assertNotNull(path, "imageNotFoundPath value should not be null");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Tree view root should be '<Select Folder>'")
    public void treeViewRootTest(FxRobot robot) {
        // GET TREE VIEW ROOT
        TreeView<String> treeView = robot.lookup("#treeView").query();
        TreeItem<String> root = treeView.getRoot();

        // ASSERT THAT TREE VIEW ROOT IS '<Select Folder>'
        assertEquals("<Select Folder>", root.getValue(), "Tree view root should be '<Select Folder>'");
    }

    @Test
    @DisplayName("Tree view should select file and show details")
    public void treeViewSelectFileTest(FxRobot robot) throws URISyntaxException, TimeoutException {
        // MOCK GET SELECTED FOLDER METHOD
        mockGetSelectedFolderMethod();

        // GET TREE VIEW
        TreeView<String> treeView = robot.lookup("#treeView").query();

        // SELECT A FOLDER TO DISTRIBUTE
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> !treeView.getRoot().getChildren().isEmpty());

        // SELECT A SUB FILE/FOLDER
        Platform.runLater(() -> treeView.getSelectionModel().select(treeView.getRoot().getChildren().getFirst()));
        WaitForAsyncUtils.waitForFxEvents();

        // GET THE SELECTED ITEM NODE
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // GET THE DETAILS
        Label nameLabel = robot.lookup("#nameLabel").query();
        Label typeLabel = robot.lookup("#typeLabel").query();
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        Label locationLabel = robot.lookup("#locationLabel").query();
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();

        // ASSERT THAT THE DETAILS ARE UPDATED
        assertNotNull(selectedItem, "Selected item should not be null");
        assertNotEquals("Name: -", nameLabel.getText(), "Name should be updated");
        assertNotEquals("Type: -", typeLabel.getText(), "Type should be updated");
        assertNotEquals("Size: -", sizeLabel.getText(), "Size should be updated");
        assertNotEquals("Location: -", locationLabel.getText(), "Location should be updated");
        assertNotEquals("Date Modified: -", dateModifiedLabel.getText(), "Date Modified should be updated");

        // VERIFY SELECTED FOLDER
        verifySelectFolder();
    }

    @Test
    @DisplayName("populateTreeView should work correctly")
    public void populateTreeViewTest(FxRobot robot) throws URISyntaxException, TimeoutException {
        // MOCK GET SELECTED FOLDER METHOD
        mockGetSelectedFolderMethod();

        // GET TREE VIEW
        TreeView<String> treeView = robot.lookup("#treeView").query();

        // SELECT A FOLDER TO DISTRIBUTE
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> !treeView.getRoot().getChildren().isEmpty());

        // ASSERT THAT POPULATE TREE VIEW CORRECTLY
        assertNotNull(treeView.getRoot(), "Tree view root should not be null");
        assertEquals("txt", treeView.getRoot().getValue(), "Root should be named 'txt'");
        assertFalse(treeView.getRoot().getChildren().isEmpty(), "Root should have children");

        // VERIFY SELECTED FOLDER
        verifySelectFolder();
    }

    @Test
    @DisplayName("updatePieChart should work correctly")
    public void updatePieChartTest(FxRobot robot) throws URISyntaxException {
        // MOCK GET SELECTED FOLDER METHOD
        mockGetSelectedFolderMethod();

        // GET PIE CHART
        PieChart pieChart = robot.lookup("#pieChart").query();

        // SELECT A FOLDER TO DISTRIBUTE
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // ASSERT THAT UPDATE PIE CHART CORRECTLY
        assertFalse(pieChart.getData().isEmpty(), "Pie chart should have data");
        assertEquals(1, pieChart.getData().size(), "Pie chart should have 1 extensions (*.txt)");

        // VERIFY SELECTED FOLDER
        verifySelectFolder();
    }

    @Test
    @DisplayName("isImage should work correctly")
    public void isImageTest() throws URISyntaxException {
        // GET FILES TO TEST
        File imageNotFound = new File(Objects.requireNonNull(getClass().getResource("/images/imageNotFound.png")).toURI());
        File test1 = new File(Objects.requireNonNull(getClass().getResource("/txt/test1.txt")).toURI());

        // ASSERT THAT IS IMAGE METHOD WORKS CORRECTLY
        assertTrue(spyController.isImage(imageNotFound), "imageNotFound.png should be an image");
        assertFalse(spyController.isImage(test1), "test1.txt should not be an image");
    }

    @Test
    @DisplayName("getFileExtension should work correctly")
    public void getFileExtensionTest() throws URISyntaxException {
        // GET FILES TO TEST
        File imageNotFound = new File(Objects.requireNonNull(getClass().getResource("/images/imageNotFound.png")).toURI());
        File test1 = new File(Objects.requireNonNull(getClass().getResource("/txt/test1.txt")).toURI());

        // ASSERT THAT GET FILE EXTENSION METHOD WORKS CORRECTLY
        assertEquals("*.png", spyController.getFileExtension(imageNotFound), "imageNotFound.png should have extension 'png'");
        assertEquals("*.txt", spyController.getFileExtension(test1), "test1.txt should have extension 'txt'");
    }

    @Test
    @DisplayName("getGithub should open a new browser window")
    public void getGithubTest(FxRobot robot) throws NoSuchFieldException, IllegalAccessException, URISyntaxException, IOException {
        // GET MOCKED DESKTOP
        Desktop mockDesktop = mock(Desktop.class);

        // GET DESKTOP FIELD AND SET NEW VALUE TO THE FIELD
        Field desktopField = PageController.class.getDeclaredField("desktop");
        desktopField.setAccessible(true);
        desktopField.set(spyController, mockDesktop);

        // GET GITHUB
        spyController.getGithub();

        // VERIFY THAT GITHUB IS OPENED
        verify(mockDesktop).browse(new URI("https://github.com/Jagaradoz/JavaFX-Projects"));
    }

    @Test
    @DisplayName("clearFolder should clear all data")
    public void clearFolderTest(FxRobot robot) throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
        // MOCK GET SELECTED FOLDER METHOD
        mockGetSelectedFolderMethod();

        // SELECT A FOLDER TO DISTRIBUTE
        Platform.runLater(() -> spyController.selectFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // CLEAR SELECTED FOLDER
        Platform.runLater(() -> spyController.clearFolder());
        WaitForAsyncUtils.waitForFxEvents();

        // ASSERT THAT PIE CHART IS CLEARED
        PieChart pieChart = robot.lookup("#pieChart").query();
        assertTrue(pieChart.getData().isEmpty(), "PieChart should be empty after clearing");

        // ASSERT THAT TREE VIEW IS CLEARED
        TreeView<String> treeView = robot.lookup("#treeView").query();
        assertEquals("<Select Folder>", treeView.getRoot().getValue(), "TreeView root should be reset to '<Select Folder>'");

        // GET THE DETAILS
        Label nameLabel = robot.lookup("#nameLabel").query();
        Label typeLabel = robot.lookup("#typeLabel").query();
        Label sizeLabel = robot.lookup("#sizeLabel").query();
        Label locationLabel = robot.lookup("#locationLabel").query();
        Label dateModifiedLabel = robot.lookup("#dateModifiedLabel").query();

        // ASSERT THAT DETAILS ARE CLEARED
        assertEquals("Name: -", nameLabel.getText(), "Name label should be reset");
        assertEquals("Type: -", typeLabel.getText(), "Type label should be reset");
        assertEquals("Size: -", sizeLabel.getText(), "Size label should be reset");
        assertEquals("Location: -", locationLabel.getText(), "Location label should be reset");
        assertEquals("Date Modified: -", dateModifiedLabel.getText(), "Date Modified label should be reset");

        // ASSERT THAT IMAGES ARE CLEARED
        ImageView imageView = robot.lookup("#imageView").query();
        assertNull(imageView.getImage(), "ImageView should be cleared");

        // VERIFY CLEAR FOLDER
        verify(spyController).clearFolder();

        // GET SELECTED FILES CLASS
        // GET SELECTED EXTENSIONS CLASS
        Field selectedFilesClass = PageController.class.getDeclaredField("selectedFiles");
        Field selectedExtensionsClass = PageController.class.getDeclaredField("selectedExtensions");

        // SET ACCESSIBLE
        selectedFilesClass.setAccessible(true);
        selectedExtensionsClass.setAccessible(true);

        // GET SELECTED FILES AND EXTENSIONS FROM CONTROLLER
        @SuppressWarnings("unchecked") Map<String, File> selectedFiles = (Map<String, File>) selectedFilesClass.get(spyController);
        @SuppressWarnings("unchecked") Map<String, Integer> selectedExtensions = (Map<String, Integer>) selectedExtensionsClass.get(spyController);

        // ASSERT THAT SELECTED FILES AND EXTENSIONS ARE CLEARED
        assertTrue(selectedFiles.isEmpty(), "selectedFiles should be empty");
        assertTrue(selectedExtensions.isEmpty(), "selectedExtensions should be empty");
    }

    public void mockGetSelectedFolderMethod() throws URISyntaxException {
        // GET TXT FOLDER
        URI path = Objects.requireNonNull(getClass().getResource("/txt")).toURI();
        File mockFolder = new File(path);

        // MOCK GET SELECTED FOLDER METHOD
        doReturn(mockFolder).when(spyController).getSelectedFolder();
    }

    public void verifySelectFolder() {
        // VERIFY SELECTED FOLDER
        verify(spyController).selectFolder();

        // FILE CAPTOR
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);

        // TREE ITEM CAPTOR
        @SuppressWarnings("unchecked")
        ArgumentCaptor<TreeItem<String>> treeItemCaptor = ArgumentCaptor.forClass(TreeItem.class);

        // VERIFY POPULATE TREE VIEW
        verify(spyController).populateTreeView(fileCaptor.capture(), treeItemCaptor.capture());

        // GET VALUES FROM CAPTORS
        File capturedFile = fileCaptor.getValue();
        TreeItem<String> capturedTreeItem = treeItemCaptor.getValue();

        // ASSERT THAT SELECTED FOLDER IS PASSED CORRECTLY
        assertNotNull(capturedFile, "Captured File should not be null");
        assertEquals("txt", capturedFile.getName(), "Captured File name should be 'txt'");
        assertNotNull(capturedTreeItem, "Captured TreeItem should not be null");
        assertEquals("txt", capturedTreeItem.getValue(), "Captured TreeItem value should be 'txt'");

        // VERIFY UPDATE PIE CHART
        verify(spyController).updatePieChart();
    }
}