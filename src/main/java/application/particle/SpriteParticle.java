package application.particle;

import application.movement.Position;
import application.sprite.Sprite;

public class SpriteParticle extends Particle {
  private final Sprite sprite;
  public SpriteParticle(Sprite sprite, double opacity, Position pos, double dir, double speed, int time) {
    super(pos, dir, speed, time);
    this.sprite = sprite.clone();
    this.sprite.pos = this.pos;
    this.sprite.alpha = opacity;
    this.sprite.enable();
  }

  protected void drawUpdate() {
    sprite.dir = dir;
    sprite.scale = 1 - getProgress();
    sprite.drawUpdate();
  }

  protected void delete() {
    this.sprite.disable();
  }
}
