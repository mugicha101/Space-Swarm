package application;

import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

public class Sound {
    private static final String basePath = "src/main/resources/sounds/";
    private static final double minVolume = 0.01; // min volume of a sound
    private static final double sfxVolume = 0.5;
    private static final double musicVolume = 1;
    private static final HashMap<String, AudioClip> audioMap = new HashMap<>();
    public static void play(String path, double volume, double pitch, double pitchVariance, Position pos) {
        if (pos != null) volume *= 500 / (500 + Player.getPos().distSqd(pos.x, pos.y) / 50 / 50);
        if (volume < minVolume) return;
        if (volume > 1) volume = 1;
        volume *= sfxVolume;
        if (!audioMap.containsKey(path)) {
            audioMap.put(path, new AudioClip(Paths.get(basePath + path).toUri().toString()));
        }
        AudioClip audio = audioMap.get(path);
        double balance = pos == null? 0 : (Player.getPos().moveInDir(DirCalc.dirTo(pos), 1).x);
        double rate = pitch * (1 + (Math.random() * 2 - 1) * pitchVariance);
        int priority = (int)(volume * 10000);
        audio.play(volume, 0, rate, balance, priority);
    }

    public static void initMusic() {
        Media music = new Media(new File(basePath + "music/space-swarm.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setVolume(musicVolume);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        Game.rootGroup.getChildren().add(new MediaView(mediaPlayer));
        mediaPlayer.play();
    }
}
