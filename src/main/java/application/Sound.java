package application;

import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Sound {
    private static class MediaPlayerComparator implements Comparator<MediaPlayer> {
        public int compare(MediaPlayer a, MediaPlayer b) {
            double diff = a.getVolume() - b.getVolume();
            if (diff == 0)
                return 0;
            else if (diff < 0)
                return -1;
            else
                return 1;
        }
    }
    private static final String basePath = "src/main/resources/sounds/";
    private static final int maxSounds = 15; // max sounds to play
    private static final double minVolume = 0.01; // min volume of a sound
    private static final PriorityQueue<MediaPlayer> pq = new PriorityQueue<>(maxSounds + 1, new MediaPlayerComparator());
    private static HashMap<String, AudioClip> audioMap = new HashMap<>();
    public static void play(String path, double volume, double pitch, double pitchVariance, Position pos) {
        if (volume < minVolume) return;
        if (!audioMap.containsKey(path))
            audioMap.put(path, new AudioClip(Paths.get(basePath + path).toUri().toString()));
        AudioClip audio = audioMap.get(path);
        if (pos != null) volume *= 500 / (500 + Player.getPos().distSqd(pos.x, pos.y) / 50 / 50);
        double balance = pos == null? 0 : (Player.getPos().moveInDir(DirCalc.dirTo(pos), 1).x);
        double rate = pitch * (1 + (Math.random() * 2 - 1) * pitchVariance);
        int priority = (int)(volume * 10000);
        audio.play(volume, 0, rate, balance, priority);
    }

    /*
    public static void playMedia(String path, double volume, double pitch, double pitchVariance, Position pos) {
        if (pos != null) volume *= 100 / (100 + Player.getPos().distSqd(pos.x, pos.y) / 50 / 50);
        // if (pq.size() > 0) System.out.println(pq.poll().getVolume());
        if (volume < minVolume || (pq.size() > 0 && volume < pq.poll().getVolume())) return;
        Media sound = new Media(new File(basePath + path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        if (pos != null) {
            mediaPlayer.setBalance(Player.getPos().moveInDir(DirCalc.dirTo(pos), 1).x);
        }
        mediaPlayer.setVolume(volume);
        mediaPlayer.setRate(pitch * (1 + (Math.random() * 2 - 1) * pitchVariance));
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
            pq.remove(mediaPlayer);
            mediaPlayer.dispose();
        });
        pq.add(mediaPlayer);
        if (pq.size() > maxSounds) {
            MediaPlayer mp = pq.remove();
            System.out.println(mp.getVolume());
            mp.dispose();
        }
    }
     */
}
