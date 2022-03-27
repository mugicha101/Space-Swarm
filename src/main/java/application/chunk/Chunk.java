package application.chunk;

import application.action.Bullet;
import application.component.Component;
import javafx.geometry.Rectangle2D;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;

public class Chunk {
  private static HashMap<Pair<Integer, Integer>, Chunk> chunks = new HashMap<>();
  private static final int chunkSize = 100;
  public static Chunk getChunk(double x, double y) {
    Pair<Integer, Integer> key = new Pair<>((int)(x/chunkSize), (int)(y/chunkSize));
    if (!chunks.containsKey(key))
      chunks.put(key, new Chunk());
    return chunks.get(key);
  }
  public static HashSet<Chunk> getChunks(Rectangle2D bounds) {
    HashSet<Chunk> chunks = new HashSet<>();
    chunks.add(Chunk.getChunk(bounds.getMinX(), bounds.getMinY()));
    chunks.add(Chunk.getChunk(bounds.getMinX(), bounds.getMaxY()));
    chunks.add(Chunk.getChunk(bounds.getMaxX(), bounds.getMinY()));
    chunks.add(Chunk.getChunk(bounds.getMaxX(), bounds.getMaxY()));
    return chunks;
  }

  public final HashSet<Bullet> bullets;
  public final HashSet<Component> components;
  public Chunk() {
    bullets = new HashSet<>();
    components = new HashSet<>();
  }
}
