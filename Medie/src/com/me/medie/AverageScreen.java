package com.me.medie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class AverageScreen implements Screen {
	public Stage stage;
	TextButton tbtCalc;
	Button btDark;
	TextField tbA, tbB, tbMedie;
	boolean isdark = true;
	Medie main;
	
	public AverageScreen(Medie main){
		this.main = main;
		Texture.setEnforcePotImages(false);
		
		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		tbtCalc = new TextButton("Calculeaza", main.skin);
		tbtCalc.setPosition(main.SW / 2 - tbtCalc.getWidth(), 0);
		stage.addActor(tbtCalc);
		
		tbA = new TextField("", main.skin);
		tbB = new TextField("", main.skin);
		tbMedie = new TextField("", main.skin);
		stage.addActor(tbA);
		stage.addActor(tbB);
		stage.addActor(tbMedie);

		tbtCalc.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				try
				{
					tbMedie.setText(getavg(tbA.getText(), tbB.getText()));
				}
				catch(NumberFormatException e1){
					tbMedie.setText("Introdu valori!");
				}
				
				return true;
			}
		});

		tbA.setPosition(10, 210);
		tbB.setPosition(10, 160);
		tbMedie.setPosition(10, 100);
		
		main.skin.add("Dark", new Texture(Gdx.files.internal("data/night.png")));
		Drawable drNight = main.skin.getDrawable("Dark");
		btDark = new Button(new ButtonStyle(drNight, drNight, drNight));
		float ratio = btDark.getWidth() / btDark.getHeight();
		btDark.setHeight(tbtCalc.getHeight() / 2);
		btDark.setWidth(btDark.getHeight() / ratio);
		btDark.setPosition((float) (main.SW - btDark.getWidth()), tbtCalc.getY());
		btDark.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				if(isdark)
					isdark = false;
				else
					isdark = true;
				
				return true;
			}
		}
		);
		stage.addActor(btDark);
	}
	
	@Override
	public void render(float delta) {
		if(isdark)
			Gdx.gl.glClearColor(0, 0, 0, 1);
		else
			Gdx.gl.glClearColor(1, 1, 1, 1);
		
		main.batch.begin();
		stage.draw();
		main.batch.end();
	}

	public String getavg(String a, String b){
		float aa = Float.parseFloat(a);
		float bb = Float.parseFloat(b);
		float avg = (aa + bb) / 2;
		return Float.toString(avg);
	}
	
	@Override
	public void resize(int width, int height) {
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
	}

}
