package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.Application;
import com.game.screens.PlayScreen;;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 728;
        config.title = ("Polarity");
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
		new LwjglApplication(new Application(), config);
	}
}
