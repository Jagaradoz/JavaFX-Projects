package controllers;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MusicPlayerPageController implements Initializable {
    @FXML
    private Pane rootPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button nextButton, playButton, prevButton, loadSongsButton;
    @FXML
    private Label songTitleLabel, currentTimeLabel, totalTimeLabel;

    private final List<String> songPaths = new ArrayList<>();
    private final ObservableList<String> songNames = FXCollections.observableArrayList();

    private MediaPlayer musicPlayer;
    private int currentIndex;

    private double currentTime;
    private double totalTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Implements list view.
        listView.setItems(getSongNames());
        listView.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> setSong(newValue.intValue()));

        // Implements volume slider.
        volumeSlider.setValue(100);
        volumeSlider.valueProperty().addListener((_, _, newValue) -> setVolume(newValue));

        // Implements play/pause/stop buttons.
        playButton.setOnAction(this::playSong);
        prevButton.setOnAction(this::prevSong);
        nextButton.setOnAction(this::nextSong);

        // Implements load songs button.
        loadSongsButton.setOnAction((_) -> loadSongs());

        // Implements progress bar.
        progressBar.setOnMouseClicked(this::setProgressBarSeekHandler);
    }

    public void updateTimeLabels() {
        // Formats time (00:00).
        String currentTimeString = formatTime(currentTime);
        String totalTimeString = formatTime(totalTime);

        // Sets text.
        currentTimeLabel.setText(currentTimeString);
        totalTimeLabel.setText(totalTimeString);
    }

    public void setSong(int index) {
        // Checks valid index.
        // Disposes music player.
        if (index < 0 || index >= getSongPaths().size()) return;
        if (musicPlayer != null) musicPlayer.dispose();

        // Gets song path from index.
        // Loads media and media player.
        String path = getSongPaths().get(index);
        URI uri = new File(path).toURI();
        Media media = new Media(uri.toString());
        musicPlayer = new MediaPlayer(media);
        currentIndex = index;

        // Adds change listener (tracking time) to music player.
        musicPlayer.currentTimeProperty().addListener((_, _, newValue) -> {
            currentTime = newValue.toSeconds();
            updateTimeLabels();
            progressBar.setProgress(currentTime / totalTime);
        });

        // Waits for music player's readiness.
        musicPlayer.setOnReady(() -> {
            totalTime = media.getDuration().toSeconds();
            updateTimeLabels();
            progressBar.setProgress(0);
            musicPlayer.setVolume(volumeSlider.getValue() / 100);
            playButton.setText("▶");
            songTitleLabel.setText(getSongNames().get(currentIndex).split("\\.")[0]);
        });
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public void setVolume(Number newValue) {
        if (musicPlayer != null) {
            musicPlayer.setVolume(newValue.doubleValue() / 100);
        }
    }

    public void loadSongs() {
        // Gets selected files from file chooser.
        List<File> selectedFiles = getSelectedFiles();

        if (selectedFiles == null) return;

        // Clears current songs and listviews.
        getSongPaths().clear();
        getSongNames().clear();

        // Adds files to listviews and sets current song.
        for (File file : selectedFiles) {
            getSongPaths().add(file.getAbsolutePath());
            getSongNames().add(file.getName());
        }

        // Sets default current song.
        if (!getSongPaths().isEmpty()) {
            setSong(0);
            listView.getSelectionModel().select(0);
        }
    }

    public void playSong(ActionEvent event) {
        if (musicPlayer == null) return;

        // Plays or pauses music player.
        if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            musicPlayer.pause();
            playButton.setText("▶");
        } else {
            musicPlayer.play();
            playButton.setText("⏹");
        }
    }

    public void nextSong(ActionEvent event) {
        if (musicPlayer == null) return;

        // Plays next song.
        currentIndex = (currentIndex + 1) % getSongNames().size();
        listView.getSelectionModel().select(currentIndex);
        setSong(currentIndex);
    }

    public void prevSong(ActionEvent event) {
        if (musicPlayer == null) return;

        // Plays previous song.
        currentIndex = (currentIndex - 1 + getSongNames().size()) % getSongNames().size();
        listView.getSelectionModel().select(currentIndex);
        setSong(currentIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public String formatTime(double seconds) {
        // Formats minutes and remaining seconds.
        int minutes = (int) (seconds / 60);
        int remainingSeconds = (int) (seconds % 60);

        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public List<File> getSelectedFiles() {
        // Creates file chooser instance and sets filters (*.mp3).
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP3");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        // Opens file chooser dialog.
        return fileChooser.showOpenMultipleDialog(rootPane.getScene().getWindow());
    }

    public List<String> getSongPaths() {
        return songPaths;
    }

    public ObservableList<String> getSongNames() {
        return songNames;
    }

    public Label getSongTitleLabel() {
        return songTitleLabel;
    }

    @FXML
    public void setProgressBarSeekHandler(MouseEvent event) {
        // Calculates new time based on mouse position.
        if (musicPlayer != null && totalTime > 0) {
            double mouseX = event.getX();
            double width = progressBar.getWidth();
            double newTime = (mouseX / width) * totalTime;
            musicPlayer.seek(Duration.seconds(newTime));
        }
    }
}
