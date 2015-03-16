package com.ivandmi.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ControlsScreen implements Screen {
	private int vHeightHalf;
	private int rWidth;
	private int rdHeight;

	public ControlsScreen() {
		rWidth = BLCopter.ribbon.getWidth();
		rdHeight = BLCopter.vpWidth * BLCopter.ribbon.getHeight() / rWidth;
		vHeightHalf = rdHeight / 2;
	}

	@Override
	public void render(float delta) {
		BLCopter.camera.update();
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
		BLCopter.batch.draw(BLCopter.ribbon, 0, BLCopter.vpHeight / 2
				- vHeightHalf, BLCopter.vpWidth, rdHeight);
		BLCopter.font.draw(BLCopter.batch, BLCopter.bundle.get("controlsMove"),
				BLCopter.MWidth, BLCopter.vpHeight - (BLCopter.MHeight + 5));
		BLCopter.font.draw(BLCopter.batch, BLCopter.bundle.get("controlsFire"),
				BLCopter.MWidth, BLCopter.vpHeight
						- (2 * BLCopter.MHeight + 10));
		BLCopter.font.draw(BLCopter.batch, BLCopter.bundle.get("avoidEnemies"),
				BLCopter.MWidth, BLCopter.vpHeight
						- (3 * BLCopter.MHeight + 15));
		BLCopter.font.draw(BLCopter.batch, BLCopter.bundle.get("gameMission"),
				BLCopter.MWidth, BLCopter.vpHeight
						- (4 * BLCopter.MHeight + 20));
		BLCopter.batch.end();
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
	}

}