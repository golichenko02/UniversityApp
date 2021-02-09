package org.example.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private TranslateTransition translateTransition;

    public Shake(Node node) {
        this.translateTransition = new TranslateTransition(Duration.millis(70),node);
        translateTransition.setFromX(0f);
        translateTransition.setByX(5f);
        translateTransition.setCycleCount(3);
        translateTransition.setAutoReverse(true);
    }

    public void play(){
        translateTransition.playFromStart();
    }

    public static void shake(Node password, Node login){
        Shake shakeLog = new Shake(login);
        Shake shakePass = new Shake(password);
        shakeLog.play();
        shakePass.play();
    }

    public static void shake(Node node){
        Shake shakeLog = new Shake(node);
        shakeLog.play();
    }
}
