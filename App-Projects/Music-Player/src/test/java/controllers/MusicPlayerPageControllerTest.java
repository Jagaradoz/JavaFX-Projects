package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testfx.api.FxRobot;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({ApplicationExtension.class})
@DisplayName("Initialize MusicPlayerPageControllerTest")
class MusicPlayerPageControllerTest {
    private MusicPlayerPageController spyController;

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/MusicPlayerPage.fxml")));

        spyController = spy(MusicPlayerPageController.class);
        loader.setControllerFactory((_) -> spyController);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("All fxml elements should not be null and visible")
    public void allElementsShouldNotBeNullAndVisible(FxRobot robot) {
        Pane rootPane = robot.lookup("#rootPane").query();
        assertNotNull(rootPane, "RootPane should not be null");
        assertTrue(rootPane.isVisible(), "RootPane should be visible");

        ProgressBar progressBar = robot.lookup("#progressBar").query();
        assertNotNull(progressBar, "ProgressBar should not be null");
        assertTrue(progressBar.isVisible(), "ProgressBar should be visible");

        Slider volumeSlider = robot.lookup("#volumeSlider").query();
        assertNotNull(volumeSlider, "VolumeSlider should not be null");
        assertTrue(volumeSlider.isVisible(), "VolumeSlider should be visible");

        ListView<String> listView = robot.lookup("#listView").query();
        assertNotNull(listView, "ListView should not be null");
        assertTrue(listView.isVisible(), "ListView should be visible");

        Button nextButton = robot.lookup("#nextButton").query();
        assertNotNull(nextButton, "NextButton should not be null");
        assertTrue(nextButton.isVisible(), "NextButton should be visible");

        Button playButton = robot.lookup("#playButton").query();
        assertNotNull(playButton, "PlayButton should not be null");
        assertTrue(playButton.isVisible(), "PlayButton should be visible");

        Button prevButton = robot.lookup("#prevButton").query();
        assertNotNull(prevButton, "PrevButton should not be null");
        assertTrue(prevButton.isVisible(), "PrevButton should be visible");

        Button loadSongsButton = robot.lookup("#loadSongsButton").query();
        assertNotNull(loadSongsButton, "LoadSongsButton should not be null");
        assertTrue(loadSongsButton.isVisible(), "LoadSongsButton should be visible");

        Label songTitleLabel = robot.lookup("#songTitleLabel").query();
        assertNotNull(songTitleLabel, "SongTitleLabel should not be null");
        assertTrue(songTitleLabel.isVisible(), "SongTitleLabel should be visible");

        Label currentTimeLabel = robot.lookup("#currentTimeLabel").query();
        assertNotNull(currentTimeLabel, "CurrentTimeLabel should not be null");
        assertTrue(currentTimeLabel.isVisible(), "CurrentTimeLabel should be visible");

        Label totalTimeLabel = robot.lookup("#totalTimeLabel").query();
        assertNotNull(totalTimeLabel, "TotalTimeLabel should not be null");
        assertTrue(totalTimeLabel.isVisible(), "TotalTimeLabel should be visible");
    }

    @Test
    @DisplayName("listView should select songs properly")
    public void listViewShouldSelectSongsProperly(FxRobot robot) {
        List<String> mockSongPaths = new ArrayList<>();
        ObservableList<String> mockSongNames = FXCollections.observableArrayList();

        generateMockSongs(mockSongPaths, mockSongNames);

        ListView<String> listView = robot.lookup("#listView").query();
        listView.setItems(mockSongNames);

        doReturn(mockSongNames).when(spyController).getSongNames();
        doReturn(mockSongPaths).when(spyController).getSongPaths();

        Platform.runLater(() -> spyController.setSong(0));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("testSong1", spyController.getSongTitleLabel().getText());
        assertEquals("▶", spyController.getPlayButton().getText());
        assertEquals(0, spyController.getProgressBar().getProgress());

        Platform.runLater(() -> spyController.setSong(1));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("testSong2", spyController.getSongTitleLabel().getText());
        assertEquals("▶", spyController.getPlayButton().getText());
        assertEquals(0, spyController.getProgressBar().getProgress());

        verify(spyController, times(2)).setSong(anyInt());
    }

    @Test
    @DisplayName("volumeSlider should work properly")
    public void volumeSliderShouldWorkProperly(FxRobot robot) {
        Slider volumeSlider = robot.lookup("#volumeSlider").query();

        assertEquals(100.0, volumeSlider.getValue());

        volumeSlider.setValue(50.0);

        assertEquals(50.0, volumeSlider.getValue());

        verify(spyController).setVolume(anyDouble());
    }

    @Test
    @DisplayName("playButton should work properly")
    public void playButtonShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        Button playButton = robot.lookup("#playButton").query();
        Platform.runLater(playButton::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("⏹", spyController.getPlayButton().getText());
    }

    @Test
    @DisplayName("prevButton should work properly")
    public void prevButtonShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        spyController.setSong(1);
        WaitForAsyncUtils.waitForFxEvents();

        Button prevButton = robot.lookup("#prevButton").query();
        Platform.runLater(prevButton::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0, spyController.getCurrentIndex());
    }

    @Test
    @DisplayName("nextButton should work properly")
    public void nextButtonShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        spyController.setSong(1);
        WaitForAsyncUtils.waitForFxEvents();

        Button prevButton = robot.lookup("#nextButton").query();
        Platform.runLater(prevButton::fire);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(2, spyController.getCurrentIndex());
    }

    @Test
    @DisplayName("loadSongsButton should work properly")
    public void loadSongsButtonShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        File audios = new File(Objects.requireNonNull(getClass().getResource("/audios")).toURI());
        List<File> mockSelectedFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(audios.listFiles())));

        assertTrue(mockSelectedFiles.containsAll(spyController.getSelectedFiles()));

        verify(spyController).loadSongs();
        verify(spyController, times(8)).getSongPaths();
        verify(spyController, times(7)).getSongNames();
        verify(spyController, times(2)).getSelectedFiles();
    }

    @Test
    @DisplayName("progressBar should work properly")
    public void progressBarShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        ProgressBar progressBar = robot.lookup("#progressBar").query();

        Platform.runLater(() -> progressBar.setProgress(0.5));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0.5, progressBar.getProgress(), 0.01);

        Platform.runLater(() -> progressBar.setProgress(0.3));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0.3, progressBar.getProgress(), 0.01);
    }

    @Test
    @DisplayName("updateTimeLabels should work properly")
    public void updateTimeLabelsShouldWorkProperly(FxRobot robot) throws URISyntaxException {
        loadSongsForTest(robot);

        Label currentTimeLabel = robot.lookup("#currentTimeLabel").query();
        Label totalTimeLabel = robot.lookup("#totalTimeLabel").query();

        Platform.runLater(() -> {
            spyController.setCurrentTime(60);
            spyController.setTotalTime(180);
            spyController.updateTimeLabels();
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("01:00", currentTimeLabel.getText());
        assertEquals("03:00", totalTimeLabel.getText());
    }

    public void loadSongsForTest(FxRobot robot) throws URISyntaxException {
        File audios = new File(Objects.requireNonNull(getClass().getResource("/audios")).toURI());

        List<String> mockSongPaths = new ArrayList<>();
        ObservableList<String> mockSongNames = FXCollections.observableArrayList();

        List<File> mockSelectedFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(audios.listFiles())));

        doReturn(mockSongNames).when(spyController).getSongNames();
        doReturn(mockSongPaths).when(spyController).getSongPaths();
        doReturn(mockSelectedFiles).when(spyController).getSelectedFiles();

        Button loadSongsButton = robot.lookup("#loadSongsButton").query();

        Platform.runLater(loadSongsButton::fire);
        WaitForAsyncUtils.waitForFxEvents();
    }

    public void generateMockSongs(List<String> songPaths, ObservableList<String> songNames) {
        try {
            File audios = new File(Objects.requireNonNull(getClass().getResource("/audios")).toURI());

            songPaths.clear();
            songNames.clear();

            for (File song : Objects.requireNonNull(audios.listFiles())) {
                songPaths.add(song.getAbsolutePath());
                songNames.add(song.getName());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}