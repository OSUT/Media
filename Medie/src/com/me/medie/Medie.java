package com.me.medie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Medie extends Game {
	public OrthographicCamera camera;
	public SpriteBatch batch;
	Skin skHoloLight, skHoloDark, skDefault, skin;
	int SW, SH;
	
	@Override
	public void create() {		
		Texture.setEnforcePotImages(false);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		SW = Gdx.graphics.getWidth();
		SH = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(SW, SH);
		camera.translate(SW/2, SH/2);
		camera.update();
		batch = new SpriteBatch();
		
		skHoloDark = new Skin(Gdx.files.internal("data/HoloSkin/Holo-dark-hdpi.json"));
		skHoloLight = new Skin(Gdx.files.internal("data/HoloSkin/Holo-light-hdpi.json"));
		skDefault = new Skin(Gdx.files.internal("data/default-skin/uiskin.json"));
		skin = skHoloDark;
		
		this.setScreen(new FacultateScreen(this));
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		skHoloDark.dispose();
		skHoloLight.dispose();
	}
	
	@Override
	public void render() {		
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		SW = width;
		SH = height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
