package com.ivandmi.game;

import com.badlogic.gdx.input.GestureDetector;

public class SwipeDetector extends GestureDetector {
	public interface SwipeListener {
		void onLeft(float delta);

		void onRight(float delta);

		void onUp(float delta);

		void onDown(float delta);

		void onTap(float x, float y);
	}

	public SwipeDetector(SwipeListener directionListener) {
		super(new SwipeAdapter(directionListener));
	}

	private static class SwipeAdapter extends GestureAdapter {
		SwipeListener swipeListener;

		public SwipeAdapter(SwipeListener swipeListener) {
			this.swipeListener = swipeListener;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			swipeListener.onTap(x, y);
			return super.tap(x, y, count, button);
		}
		
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (deltaX > 0) {
					swipeListener.onRight(deltaX);
				} else {
					swipeListener.onLeft(-deltaX);
				}
			} else {
				if (deltaY > 0) {
					swipeListener.onDown(deltaY);
				} else {
					swipeListener.onUp(-deltaY);
				}
			}

			return super.pan(x, y, deltaX, deltaY);
		}
	}
}