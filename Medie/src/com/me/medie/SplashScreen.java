package com.me.medie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SplashScreen implements Screen {
	Texture texBackground;
	Image imBackground;
	TextButton tbtStart;
	Stage stage;
	
	Medie main;
	
	public SplashScreen(Medie game){
		Texture.setEnforcePotImages(false);
		main = game;

		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		texBackground = new Texture(Gdx.files.internal("data/background.png"));
		imBackground = new Image(texBackground);
		imBackground.setWidth(main.SW);
		imBackground.setHeight(main.SH);
		stage.addActor(imBackground);
		
		tbtStart = new TextButton("Start", main.skin);
		tbtStart.setPosition(main.SW / 2 - tbtStart.getWidth() / 2, 0);
		tbtStart.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				main.setScreen(new FacultateScreen(main));
				
				return true;
			}
		});
		
		stage.addActor(tbtStart);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		main.batch.begin();
		stage.draw();
		main.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
