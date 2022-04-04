package application.action;

import application.chunk.Chunk;
import application.component.Component;
import application.component.Siphongun;
import application.movement.DirCalc;
import application.movement.Position;
import application.particle.CircleParticle;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class RangeEffect extends Effect {
    private final double startDuration;
    protected final Component sourceComponent;
    private final Circle circle;
    private Position pos;
    private Color color;
    private static Component[] getAffected(Position pos, double range, Component sourceComponent) {
        ArrayList<Component> affectedList = new ArrayList<>();
        for (Component component : sourceComponent.getParent().components) {
            if (component == sourceComponent)
                continue;
            if (component instanceof Siphongun || component.velo.pos.distSqd(pos) <= range * range)
                affectedList.add(component);
        }
        Component[] affected = new Component[affectedList.size()];
        int i = 0;
        for (Component c : affectedList)
            affected[i++] = c;
        return affected;
    }
    public RangeEffect(Component sourceComponent, double range, double duration, Color color) {
        super(getAffected(sourceComponent.velo.pos, range, sourceComponent), duration);
        ArrayList<Component> filtered = new ArrayList<>();
        for (Component component : affected) {
            if (canBeAffected(component))
                filtered.add(component);
        }
        Component[] newAffected = new Component[filtered.size()];
        for (int i = 0; i < filtered.size(); i++)
            newAffected[i] = filtered.get(i);
        affected = newAffected;
        this.startDuration = duration;
        this.sourceComponent = sourceComponent;
        this.color = color;
        pos = sourceComponent.velo.pos;
        circle = new Circle(0, 0, range);
        circle.setFill(new RadialGradient(0, 0, 0, 0, range, false, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(0.75, color.interpolate(Color.TRANSPARENT, 0.5)), new Stop(1, Color.TRANSPARENT)));
        circle.setTranslateX(pos.x);
        circle.setTranslateY(pos.y);
        group.getChildren().add(circle);
    }

    @Override
    protected final void baseAction(double timeMulti) {
        circle.setTranslateX(pos.x);
        circle.setTranslateY(pos.y);
        circle.setOpacity(Math.min(1 - (startDuration - duration) * 2, 1) * 0.5);
    }

    @Override
    protected final void action(int index, Component component, double timeMulti) {
        effectTick(index, component, timeMulti);
        double dir = Math.random() * 360;
        if (Math.random() < 0.05) new CircleParticle(5, color, 0.75, component.velo.pos.clone().moveInDir(dir, Math.random() * component.getRadius()), 0, 0, (int)(15 + Math.random() * 15));
    }

    protected boolean canBeAffected(Component component) {return true;}

    protected abstract void effectTick(int index, Component component, double timeMulti);
}
