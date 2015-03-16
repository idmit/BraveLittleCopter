package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class RunningScreen implements Screen {
	private Ribbon background;
	private BuildingManager buildingManager;
	private Copter copter;
	private MissileManager missileManager;
	private PlaneMissileManager planeMissileManager;
	private BoxManager boxManager;
	static float distance;
	private static float time = 0;

	Plane plane;

	public RunningScreen() {
		background = new Ribbon(BLCopter.ribbon);
		copter = new Copter("copterpack.atlas", "missilebangpack.atlas", 0.02f);
		missileManager = new MissileManager("missilepack.atlas",
				"missilebangpack.atlas", 0.1f);
		buildingManager = new BuildingManager("slim.png", 1.5f);
		plane = new Plane("plane", copter);
		plane.alive = false;
		planeMissileManager = new PlaneMissileManager("missilepack.atlas",
				"missilebangpack.atlas", 0.1f);
		boxManager = new BoxManager("box.png");
		distance = 0;

		Gdx.input.setInputProcessor(new SwipeDetector(new Controls(copter,
				missileManager)));
	}

	@Override
	public void render(float delta) {
		BLCopter.shapeRenderer.setProjectionMatrix(BLCopter.camera.combined);
		BLCopter.shapeRenderer.begin(ShapeType.Filled);
		BLCopter.shapeRenderer.setColor(0.627f, 0.627f, 0.694f, 1);
		BLCopter.shapeRenderer.rect(0, BLCopter.vpHeight / 2, BLCopter.vpWidth,
				BLCopter.vpHeight / 2);
		BLCopter.shapeRenderer.setColor(0.317f, 0.337f, 0.419f, 1);
		BLCopter.shapeRenderer.rect(0, 0, BLCopter.vpWidth,
				BLCopter.vpHeight / 2);
		BLCopter.shapeRenderer.end();
		BLCopter.batch.setProjectionMatrix(BLCopter.camera.combined);

		BLCopter.batch.begin();
		background.render(BLCopter.batch);
		if (!plane.alive) {
			buildingManager.spawn(delta);
		}
		buildingManager.render(BLCopter.batch, delta);
		buildingManager.hasCollision(copter);
		if (copter.isDead()) {
			copter.kill();
			time = 0;
			BLCopter.batch.end();
			return;
		}
		copter.checkPos();
		copter.render(BLCopter.batch, delta);
		boxManager.spawn();
		boxManager.render(BLCopter.batch, delta);
		boxManager.collide(copter);
		missileManager.render(BLCopter.batch, delta);
		missileManager.collide(buildingManager, plane);
		if (plane.alive) {
			plane.render(BLCopter.batch, delta);
			if (!plane.crash && plane.missiles > 0) {
				planeMissileManager.spawn(plane, copter);
			}
			if (plane.missiles == 0) {
				plane.crash = true;
			}
			time = 0;
			BLCopter.font.draw(BLCopter.batch, "" + plane.hitsLeft(),
					plane.xy.x, plane.xy.y);
		}
		planeMissileManager.render(BLCopter.batch, delta, copter);
		planeMissileManager.collide(copter);

		float width1 = BLCopter.font.getBounds(BLCopter.bundle.format(
				"coveredDistance", 10000)).width;
		float width2 = BLCopter.font.getBounds(BLCopter.bundle.format(
				"livesLeft", copter.lives)).width;

		BLCopter.font.draw(BLCopter.batch, BLCopter.bundle.format(
				"coveredDistance", (int) (distance / 10)), BLCopter.MWidth,
				2 * BLCopter.MHeight);
		BLCopter.font.draw(BLCopter.batch,
				BLCopter.bundle.format("livesLeft", copter.lives), 4
						* BLCopter.MWidth + width1, 2 * BLCopter.MHeight);
		BLCopter.font.draw(BLCopter.batch,
				BLCopter.bundle.format("missilesLeft", copter.missiles), 6
						* BLCopter.MWidth + width1 + width2,
				2 * BLCopter.MHeight);
		BLCopter.batch.end();

		distance += (Copter.velocity * delta) * 2;
		time += delta;
		if (time > BLCopter.bossPeriod) {
			plane.reset("plane");
		}
	}

	@Override
	public void resize(int width, int height) {
		// BraveLittleCopter.vpWidth = Gdx.graphics.getWidth();
		// BraveLittleCopter.vpHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void show() {
		copter.startSound();
	}

	@Override
	public void hide() {
		copter.stopSound();
	}

	@Override
	public void pause() {
		BLCopter.setActiveScreen(BLCopter.GameState.Pause);
		BLCopter.stopAmbientMusic();
	}

	@Override
	public void resume() {
		copter.stopSound();
	}

	@Override
	public void dispose() {
		boxManager.dispose();
		buildingManager.dispose();
		missileManager.dispose();
		planeMissileManager.dispose();
		copter.dispose();
	}
}
