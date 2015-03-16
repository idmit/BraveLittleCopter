package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ReadyScreen implements Screen {
	private int vHeightHalf;
	private int rWidth;
	private int rdHeight;
	static Preferences prefs = Gdx.app.getPreferences("Preferences");
	static private int topDistance;

	public ReadyScreen() {
		topDistance = prefs.getInteger("topDistance", 0);
		rWidth = BLCopter.ribbon.getWidth();
		rdHeight = BLCopter.vpWidth * BLCopter.ribbon.getHeight() / rWidth;
		vHeightHalf = rdHeight / 2;
		BLCopter.replayAmbientMusic();
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

		BLCopter.batch.draw(BLCopter.soundState ? BLCopter.soundIconOn
				: BLCopter.soundIconOff, BLCopter.vpWidth - 67, 3, 64, 64);

		float width = BLCopter.headFont.getBounds(BLCopter.bundle
				.get("gameName")).width;
		BLCopter.headFont.draw(BLCopter.batch, BLCopter.bundle.get("gameName"),
				BLCopter.vpWidth / 2 - width / 2, 5 * BLCopter.vpHeight / 6);
		BLCopter.font.draw(BLCopter.batch,
				BLCopter.bundle.format("topDistance", topDistance),
				BLCopter.MWidth, 2 * BLCopter.MHeight);
		BLCopter.batch.end();
	}

	static public void updateTopDistance(int newTop) {
		if (newTop > topDistance) {
			topDistance = newTop;
			prefs.putInteger("topDistance", newTop);
		}
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
		BLCopter.stopAmbientMusic();
	}

	@Override
	public void resume() {
		BLCopter.replayAmbientMusic();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		prefs.flush();
	}

}