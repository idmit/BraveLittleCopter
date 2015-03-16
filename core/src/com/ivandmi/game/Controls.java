package com.ivandmi.game;

import com.badlogic.gdx.Gdx;

public class Controls implements SwipeDetector.SwipeListener {

	Copter copter;
	MissileManager missileManager;

	Controls(Copter copter, MissileManager missileManager) {
		this.copter = copter;
		this.missileManager = missileManager;
	}

	@Override
	public void onLeft(float delta) {
		// copter.move(-40 * delta * Gdx.graphics.getDeltaTime(), 0);
	}

	@Override
	public void onRight(float delta) {
		if (delta > 20) {
			missileManager.spawn(copter);
		}
	}

	@Override
	public void onUp(float delta) {
		copter.move(0, 40 * delta * Gdx.graphics.getDeltaTime());
	}

	@Override
	public void onDown(float delta) {
		copter.move(0, -40 * delta * Gdx.graphics.getDeltaTime());
	}

	@Override
	public void onTap(float x, float y) {
		if (BLCopter.gameState == BLCopter.GameState.Pause) {
			if (BLCopter.vpWidth - 64 <= x && x <= BLCopter.vpWidth - 3
					&& BLCopter.vpHeight - 64 <= y && y <= BLCopter.vpHeight - 3) {
				BLCopter.setSoundState(!BLCopter.soundState);
			} else {
			BLCopter.setActiveScreen(BLCopter.GameState.Running);
			}
		} else if (BLCopter.gameState == BLCopter.GameState.Controls) {
			BLCopter.setActiveScreen(BLCopter.GameState.Pause);
		}
	}
}
