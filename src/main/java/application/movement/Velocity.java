package application.movement;

public class Velocity {
  public Position pos;
  public double x;
  public double y;
  public double resistance;
  public Velocity(Position pos, double x, double y, double resistance) {
    this.pos = pos;
    this.x = x;
    this.y = y;
    this.resistance = resistance;
  }
  public Velocity(Position pos, double resistance) {
    this(pos, 0, 0, resistance);
  }
  public Velocity() {
    this(new Position(), 0, 0, 0);
  }
  public void tick() {
    pos.x += x;
    pos.y += y;
    x -= x * resistance;
    y -= y * resistance;
  }
  public void add(double x, double y) {
    this.x += x;
    this.y += y;
  }
  public void add(Position pos) {
    add(pos.x, pos.y);
  }
  public Velocity clone() {
    return new Velocity(pos.clone(), x, y, resistance);
  }
}
