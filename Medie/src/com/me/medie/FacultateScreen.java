package com.me.medie;

import java.util.Arrays;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FacultateScreen implements Screen {
	Medie main;
	
	Table table;
	Stage stage;
	SelectBox sbFacultati, sbSectii;
	int nrFacultati;
	String[] strFacultati;
	String[][] strSectii;
	
	public FacultateScreen(final Medie main){
		this.main = main;
		
		stage = new Stage();
		
		table = new Table();
		table.setSize(main.SW, main.SH);
		table.top();
		
		Gdx.input.setInputProcessor(stage);
		stage.clear();
		
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android)
		  dirHandle = Gdx.files.internal("data/studii/");
		else
		  dirHandle = Gdx.files.internal("bin/data/studii/");

		FileHandle[] fhFacultati = dirHandle.list();
		
		nrFacultati = fhFacultati.length;
		
		strFacultati = new String[nrFacultati];

		for(int i = 0;i < nrFacultati;i++){
			String path = fhFacultati[i].path();
			if(fhFacultati[i].isDirectory())
				strFacultati[i] = path.substring(path.lastIndexOf('/') + 1);
		}

		sbFacultati = new SelectBox(strFacultati, main.skin);
		
		strSectii = new String[strFacultati.length][];
		
		for(int i = 0;i < nrFacultati;i++){
			if (Gdx.app.getType() == ApplicationType.Android)
				  dirHandle = Gdx.files.internal("data/studii/" + strFacultati[i] + "/");
			else
				  dirHandle = Gdx.files.internal("bin/data/studii/" + strFacultati[i] + "/");
			
			FileHandle[] fhSectii = dirHandle.list();
			
			strSectii[i] = new String[fhSectii.length];
			
			for(int j = 0;j < fhSectii.length;j++){
				String path = fhSectii[j].path();
				path = path.substring(path.lastIndexOf('/') + 1);
				path = path.substring(0, path.length() - 4);
				strSectii[i][j] = path;
			}
		}

		sbSectii = new SelectBox(strSectii, main.skin);
		
		updateSectii();
		
		ChangeListener changelistener = new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateSectii();
			}
		};
		
		sbFacultati.addListener(changelistener);
		
		table.add(new Label("Alege facultatea:", main.skin));
		table.row();
		table.add(sbFacultati);
		table.row();
		table.add(new Label("Alege sectia:", main.skin));
		table.row();
		table.add(sbSectii);
		table.row();
		
		table.add(new Label("An:", main.skin)).row();
		
		final SelectBox sbAn = new SelectBox(new String[]{"1","2","3","4"}, main.skin);
		table.add(sbAn);
		table.row();
		
		TextButton tbContinue = new TextButton("Continue", main.skin);
		table.add().height(tbContinue.getHeight());
		table.row();
		table.add(tbContinue);
		
		tbContinue.addListener(new ClickListener(){
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				main.setScreen(new AverageScreen(main, sbSectii.getSelection(), Integer.parseInt(sbAn.getSelection())));
				
				return true;
			}
		});
		
		stage.addActor(table);
	}
	
	void updateSectii(){
		int selectedFac = sbFacultati.getSelectionIndex();
		
		String[] toenter = new String[strSectii[selectedFac].length];
		for(int i = 0;i < strSectii[selectedFac].length;i++){
			toenter[i] = strSectii[selectedFac][i];
		}
		sbSectii.setItems(toenter);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		
		main.batch.begin();
		stage.draw();
		Table.drawDebug(stage);
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
