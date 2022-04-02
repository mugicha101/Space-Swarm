package application.chunk;

import application.action.Bullet;
import application.component.Component;
import application.movement.Position;
import javafx.geometry.Rectangle2D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Chunk {
  private static final HashMap<Pair<Integer, Integer>, Chunk> chunks = new HashMap<>();
  private static final int chunkSize = 25;
  public static Pair<Integer, Integer> getChunkKey(double x, double y) {
    return new Pair<>((int)(x/chunkSize), (int)(y/chunkSize));

  }

  public static Chunk getChunk(Pair<Integer, Integer> key) {
    if (!chunks.containsKey(key))
      chunks.put(key, new Chunk());
    return chunks.get(key);
  }
  public static Chunk getChunk(int x, int y) {
    return getChunk(new Pair<>(x, y));
  }

  public static void tick() {
    ArrayList<Pair<Integer, Integer>> removeChunks = new ArrayList<>();
    for (Map.Entry<Pair<Integer, Integer>, Chunk> kvp : chunks.entrySet()) {
      if (kvp.getValue().components.size() + kvp.getValue().bullets.size() == 0)
        removeChunks.add(kvp.getKey());
    }
    for (Pair<Integer, Integer> key : removeChunks)
      chunks.remove(key);
  }

  public static HashSet<Chunk> getChunks(Rectangle2D bounds) {
    HashSet<Chunk> chunks = new HashSet<>();
    Pair<Integer, Integer> min = getChunkKey(bounds.getMinX(), bounds.getMinY());
    Pair<Integer, Integer> max = getChunkKey(bounds.getMaxX(), bounds.getMaxY());
    for (int x = min.getKey(); x <= max.getKey(); x++)
      for (int y = min.getValue(); y <= max.getValue(); y++)
        chunks.add(getChunk(x, y));
      return chunks;
  }

  public static HashSet<Component> getComponents(Position pos, double radius) {
    HashSet<Component> components = new HashSet<>();
    for (Chunk chunk : Chunk.getChunks(new Rectangle2D(pos.x-radius/2, pos.y-radius/2, radius, radius))) {
      for (Component c : chunk.components) {
        if (c.velo.pos.distSqd(pos) < radius * radius)
          components.add(c);
      }
    }
    return components;
  }

  public final HashSet<Bullet> bullets;
  public final HashSet<Component> components;
  public Chunk() {
    bullets = new HashSet<>();
    components = new HashSet<>();
  }
}
