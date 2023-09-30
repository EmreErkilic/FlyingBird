package com.emreerkilic.flyingbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlyingBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture enemy1;
	Texture enemy2;
	Texture enemy3;
	float birdX = 0;
	float birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 1F;
	float enemyVelocity = 5;
	Random random;
	Circle birdCircle;
	ShapeRenderer shapeRenderer; //for paint

	int score = 0;
	int scoredEnemy = 0;
	BitmapFont font;
	BitmapFont font2;
	Music rainMusic;
	Music fullMusic;
	Sound deadSound;


	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffset1 = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float [] enemyOffset3 = new float[numberOfEnemies];
	float distance = 0;
	Circle [] enemyCircles1;
	Circle [] enemyCircles2;
	Circle [] enemyCircles3;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background2.png");
		bird = new Texture("bird.png");
		enemy1 = new Texture("enemy.png");
		enemy2 = new Texture("enemy.png");
		enemy3 = new Texture("enemy.png");

		distance = Gdx.graphics.getWidth() / 2;
		random = new Random();

		birdCircle = new Circle();
		enemyCircles1 = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		font = new BitmapFont(Gdx.files.internal("font3.fnt"));
		font.getData().setScale(5);

		font2 = new BitmapFont(Gdx.files.internal("font3.fnt"));
		font2.getData().setScale(5);

		fullMusic = Gdx.audio.newMusic(Gdx.files.internal("fullmusic.mp3"));
		fullMusic.setLooping(true);
		fullMusic.play();

		deadSound = Gdx.audio.newSound(Gdx.files.internal("deadMusic.mp3"));





		birdX = Gdx.graphics.getWidth()/3;
		birdY = Gdx.graphics.getHeight()/2;

		//shapeRenderer = new ShapeRenderer();

		for (int i=0 ; i<numberOfEnemies ; i++) {

			enemyOffset1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - (enemy1.getWidth() / 2) + (i*distance);

			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();

		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());



		if (gameState == 1) { //start game

			fullMusic.play();

			if (enemyX[scoredEnemy] < birdX) {
				score++;

				if (scoredEnemy < numberOfEnemies -1 ) {
					scoredEnemy++;
				}else {
					scoredEnemy = 0;
				}
			}


			for (int i=0; i<numberOfEnemies; i++) {

				if (enemyX[i] < Gdx.graphics.getWidth()/15) {
					enemyX[i] = enemyX[i] +numberOfEnemies * distance;

					enemyOffset1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

				}else {
					enemyVelocity = enemyVelocity + 0.002f;
					enemyX[i] = enemyX[i] - enemyVelocity;
				}


				if (Gdx.graphics.getHeight()/2 + enemyOffset1[i] > Gdx.graphics.getHeight()) {
					enemyOffset1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				}else {
					batch.draw(enemy1,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset1[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
					enemyCircles1[i] = new Circle(enemyX[i] +Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffset1[i] + Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/40);
				}

				if (Gdx.graphics.getHeight()/2 + enemyOffset2[i] > Gdx.graphics.getHeight()) {
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				}else {
					batch.draw(enemy2,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
					enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
				}

				if (Gdx.graphics.getHeight()/2 + enemyOffset3[i] > Gdx.graphics.getHeight()) {
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				}else {
					batch.draw(enemy3,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
					enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
				}

			}



			if (Gdx.input.justTouched()) {
				velocity = -15;
			}

			if (birdY > 0 && birdY < Gdx.graphics.getHeight()) {
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}else {
				if (gameState != 2) {
					deadSound.play(1.0f);
				}
				gameState = 2;
			}
		}

		else if (gameState == 0) {

			font2.draw(batch,String.valueOf("GET READY"),Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/2);

			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}

		else if (gameState == 2) {
			fullMusic.stop();

			font2.draw(batch,String.valueOf("GAME OVER! \n Tap To Play Again"),Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/2);

			if (Gdx.input.justTouched()) {
				gameState = 1;

				birdY = Gdx.graphics.getHeight()/2;
				birdX = Gdx.graphics.getWidth()/3;

				for (int i=0 ; i<numberOfEnemies ; i++) {

					enemyOffset1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - (enemy1.getWidth() / 2) + (i*distance);

					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();

				}
				velocity = 0;
				enemyVelocity = 5;
				score = 0;
				scoredEnemy = 0;
			}
		}


		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/20);

		batch.end();

		birdCircle.set(birdX + (Gdx.graphics.getWidth()/30),birdY + (Gdx.graphics.getHeight()/20),(Gdx.graphics.getWidth()/40));



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);



		for (int i=0; i<numberOfEnemies; i++) {

			//shapeRenderer.circle(enemyX[i] +Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffset1[i] + Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/40);
			//shapeRenderer.circle(enemyX[i] +Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/40);
			//shapeRenderer.circle(enemyX[i] +Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/40);

			if (Intersector.overlaps(birdCircle,enemyCircles1[i]) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i])) {

				if (gameState != 2) {
					deadSound.play(1.0f);
				}

				gameState = 2;

			}



		}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
