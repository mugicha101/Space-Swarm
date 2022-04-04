package application.component;

import application.Core;

import java.util.Random;

public class ComponentFactory {
  private static final Random rand = new Random();
  public static void create(Core core) {
    double r = Math.random();
    if (r < 0.4) {
      switch (rand.nextInt(4)) {
        case 0 -> new Turret(core);
        case 1 -> new Sniper(core);
        case 2 -> new Cannon(core);
      }
    } else if (r < 0.5){
      switch (rand.nextInt(2)) {
        case 0 -> new Lazer(core);
        case 1 -> new Siphongun(core);
      }
    } else {
      switch (rand.nextInt(6)) {
        case 0 -> new Overclocker(core);
        case 1 -> new Shielder(core);
        case 2 -> new Beacon(core);
        case 3 -> new Targeter(core);
        case 4 -> new Radar(core);
        case 5 -> new Energizer(core);
        case 6 -> new Healer(core);
        case 7 -> new Reviver(core);
        case 8 -> new Patcher(core);
      }
    }
  }
}
