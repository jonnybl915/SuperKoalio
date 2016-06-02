package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	TextureRegion stand;
	TextureRegion jump;

	float x, y, xv, yv; //xv = x velocity...
	static final float MAX_JUMP_VELOCITY = 2000;
	static final float MAX_VELOCITY = 200;
	static final float DECELERATOR = 0.99f;
	static final int WIDTH = 18;
	static final int HEIGHT = 28;
	static final int GRAVITY = -50;
	boolean canJump, faceRight = true;
	Animation walk;
	float time;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture sheet = new Texture("koalio.png");
		TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT);
		stand = tiles[0][0];
		jump = tiles[0][1];
		walk = new Animation(0.2f, tiles[0][2], tiles[0][3], tiles[0][4]);

	}

	@Override
	public void render () {
		move();

		time += Gdx.graphics.getDeltaTime();

		TextureRegion img;
		if(y > 0){
			img = jump;
		}
		else if(xv!=0){
			img = walk.getKeyFrame(time, true);
		}
		else{
			img = stand;
		}
		Gdx.gl.glClearColor(0.8f, 0.7f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (faceRight){
			batch.draw(img, x, y, WIDTH * 3, HEIGHT * 3);
		}
		else{
			batch.draw(img, x +WIDTH * 3, y, WIDTH * -3, HEIGHT * 3);
		}
		batch.end();
	}

	public void move(){
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump){
			yv = MAX_JUMP_VELOCITY;
			canJump = false;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			yv = -MAX_VELOCITY;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			xv = MAX_VELOCITY;
			faceRight = true;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			xv = -MAX_VELOCITY;
			faceRight = false;
		}

		yv += GRAVITY;

		float delta = Gdx.graphics.getDeltaTime(); //amount of seconds which have passed since the last frame
		y+= yv * delta;
		x+= xv * delta;

		yv = decelerate(yv);
		xv = decelerate(xv);

		if(y<0){
			y=0;
			canJump = true;
		}
		if(x>948){ //*** not working ***
			x=948;
		}
		if(x<0){
			x=0;
		}
	}
	public float decelerate(float velocity){
		velocity *= DECELERATOR;
		if (Math.abs(velocity) <1) {
			velocity = 0;
		}
		return velocity;
	}
}
