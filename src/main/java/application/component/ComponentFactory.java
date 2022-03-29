package application.component;

import application.Core;

import java.util.Random;

public class ComponentFactory {
  private static final Random rand = new Random();
  public static void create(Core core) {
    switch (rand.nextInt(3)) {
      case 0 -> new Turret(core);
      case 1 -> new Sniper(core);
      case 2 -> new Healer(core);
    }
  }
}
