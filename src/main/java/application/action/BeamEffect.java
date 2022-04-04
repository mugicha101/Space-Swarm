package application.action;

import application.component.Component;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public abstract class BeamEffect extends Effect {
    protected final Component sourceComponent;
    private final Line[] lines;
    private final double width;
    public BeamEffect(Component sourceComponent, Component[] affected, double duration, Color color, double width) {
        super(affected, duration);
        this.sourceComponent = sourceComponent;
        this.width = width;
        lines = new Line[affected.length];
        for (int i = 0; i < lines.length; i++) {
            Line l = new Line(sourceComponent.velo.pos.x, sourceComponent.velo.pos.y, affected[i].velo.pos.x, affected[i].velo.pos.y);
            l.setOpacity(0.5);
            l.setStroke(color);
            group.getChildren().add(l);
            lines[i] = l;
        }
    }

    @Override
    protected final void action(int index, Component component, double timeMulti) {
        Position origin = sourceComponent.velo.pos;
        Position target = component.velo.pos;
        Line l = lines[index];
        l.setStartX(origin.x);
        l.setStartY(origin.y);
        l.setEndX(target.x);
        l.setEndY(target.y);
        l.setStrokeWidth(width * Math.min(duration * 10, 1));
        effectTick(index, component, timeMulti);
    }

    protected abstract void effectTick(int index, Component component, double timeMulti);
}
