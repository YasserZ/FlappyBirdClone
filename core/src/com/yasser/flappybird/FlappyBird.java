package com.yasser.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture []birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
	float gap = 400;
	Texture topTube;
	Texture bottomTube;
	float maxTubeOffset;
	Random random;
	float tubeVelocity;
	int numberOfTubes = 4;
	float []tubeX = new float[numberOfTubes];
	float disctanceBetweenTubes;
	float []tubeOffset = new float[numberOfTubes];
	Circle circle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	ShapeRenderer shapeRenderer;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	Texture gameover;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap/2 - 100;
		random = new Random();
		tubeVelocity = 4;
		disctanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		shapeRenderer = new ShapeRenderer();
		circle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		for(int i = 0; i < numberOfTubes; i++){
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() +  i * disctanceBetweenTubes;
			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
 		}
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameover = new Texture("gameover.png");



	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {
			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;
				scoringTube ++;
				scoringTube %= numberOfTubes;
				Gdx.app.log("Score is: " , String.valueOf(score));
			}
			if(Gdx.input.justTouched()){
				velocity = -20;
			}
			for(int i = 0; i < numberOfTubes; i++){
				if(tubeX[i] < - topTube.getWidth()){
					tubeX[i] += numberOfTubes * disctanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}
				else {
					tubeX[i] -= tubeVelocity;

				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			}


			if(birdY >= 0){
				velocity += gravity;
				birdY -= velocity;
			}
			else{
				gameState = 2;
			}



		}
		else if (gameState == 0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			}

		}
		else{
			batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight());
			if(Gdx.input.justTouched()){
				gameState = 1;
				birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
				for(int i = 0; i < numberOfTubes; i++){
					tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() +  i * disctanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
					topTubeRectangles[i] = new Rectangle();
					bottomTubeRectangles[i] = new Rectangle();
				}
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
		circle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth()/2);
//		shapeRenderer.circle(circle.x, circle.y, circle.radius);
		for(int i = 0; i < numberOfTubes; i++) {
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			if(Intersector.overlaps(circle, topTubeRectangles[i]) || Intersector.overlaps(circle, bottomTubeRectangles[i])){
				gameState = 2;
			}
		}
		shapeRenderer.end();
	}
}
