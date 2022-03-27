package application.chunk;

import javafx.geometry.Rectangle2D;

import java.util.HashSet;

public abstract class Chunkable {
  private final HashSet<Chunk> chunks;
  public Chunkable() {
    chunks = new HashSet<>();
  }
  protected final void updateChunks() {
    if (getChunks().equals(chunks))
      return;
    removeFromChunks();
    addToChunks();
  }
  private final void addToChunks() {
    for (Chunk chunk : getChunks()) {
      if (!chunks.contains(chunk)) {
        chunkAdd(chunk);
        chunks.add(chunk);
      }
    }
  }
  protected final void removeFromChunks() {
    for (Chunk chunk : chunks)
      chunkRemove(chunk);
    chunks.clear();
  }
  protected abstract void chunkAdd(Chunk chunk); // add self to a chunk
  protected abstract void chunkRemove(Chunk chunk); // remove self from a chunk
  protected abstract Rectangle2D getBounds(); // get bounding collision AABB
  public final HashSet<Chunk> getChunks() {
    return Chunk.getChunks(getBounds());
  }
}
