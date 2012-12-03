package com.project.gemswapper;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {

	private final int GRIDSIZE = 8;
	
	Paint paint;
	Resources res;
	Bitmap grid;
	Context mContext;
	Tile tiles[][];
	Random randomGen;
	
	public GameView(Context context) {
		super(context);
		paint = new Paint();
		tiles = new Tile[8][8];
		
		mContext = context;
		res = this.getResources();
		grid = BitmapFactory.decodeResource(res,  R.drawable.grid_01);
		randomGen = new Random();
		
		for(int i = 0; i < GRIDSIZE; ++i)
		{
			for(int j = 0; j < GRIDSIZE; ++j)
			{
				tiles[i][j] = new Tile(context, i * 100, j * 100, randomGen.nextInt(5));
			}
		}
		
	}

	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Fill screen white
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawPaint(paint);
		
		canvas.drawBitmap(grid, 0, 0, paint);
		
		for(int i = 0; i < GRIDSIZE; ++i)
		{
			for(int j = 0; j < GRIDSIZE; ++j)
			{
				tiles[i][j].draw(canvas);
			}
		}
		
	}
}
