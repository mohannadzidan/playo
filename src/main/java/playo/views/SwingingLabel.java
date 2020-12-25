package playo.views;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

public class SwingingLabel extends Label {

    public SwingingLabel() {
        convertLabel(this, 60, false);
    }

    public static void convertLabel(Label label, double pixelsPerSeconds, boolean resetOnExit) {
        // set clipping area for the track info container
        var parent = (Pane) Objects.requireNonNull(label.getParent());
        Rectangle rect = new Rectangle();
        parent.setClip(rect);
        parent.layoutBoundsProperty().addListener((a, oldValue, newValue) -> {
            rect.setWidth(newValue.getWidth());
            rect.setHeight(newValue.getHeight());
        });
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(label);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.setInterpolator(Interpolator.LINEAR);
        label.widthProperty().addListener((o, a, b) -> {
            double clippedAreaLength = Math.min(parent.getWidth() - b.doubleValue(), 0);
            transition.setDuration(Duration.seconds(Math.max(-clippedAreaLength / pixelsPerSeconds, 0.1)));
            transition.setFromX(0);
            transition.setToX(clippedAreaLength);
            if (transition.getStatus() != Animation.Status.STOPPED) transition.playFromStart();
        });
        label.setOnMouseEntered((a) -> transition.play());
        if (resetOnExit) {
            label.setOnMouseExited((a) -> {
                label.setTranslateX(0);
                transition.stop();
            });
        }

    }


}
