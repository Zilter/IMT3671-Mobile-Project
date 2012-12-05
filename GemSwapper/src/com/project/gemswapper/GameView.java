package com.project.gemswapper;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

	private final int GRIDSIZE = 8;
	
	Paint paint;
	Resources res;
	Bitmap grid;
	Context mContext;
	Tile tiles[][];
	Random randomGen;
	
	float mScale;
	
	float mViewWidth;
	float mViewHeight;
	
	public GameView(Context context) {
		super(context);
		paint = new Paint();
		tiles = new Tile[8][8];
		
		mContext = context;
		res = this.getResources();
		grid = BitmapFactory.decodeResource(res,  R.drawable.grid_01);
		randomGen = new Random();
		
		//get width and height of screen for scaling
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		float mViewWidth = metrics.widthPixels;
		float mViewHeight = metrics.heightPixels;
		mScale = mViewWidth / 720;
		
		System.out.println(mViewWidth);
		
		fillGrid();
	}
	
	private void fillGrid()
	{
		int typeTemp;
		boolean goodType = true;
		
		for(int y = 0; y < GRIDSIZE; ++y)
		{
			for(int x = 0; x < GRIDSIZE; ++x)
			{
				do
				{
					goodType = true;
					typeTemp = randomGen.nextInt(5);
					
					if(x >= 2)
					{
						if(tiles[y][x - 1].getType() == typeTemp && tiles[y][x - 2].getType() == typeTemp)
						{
							goodType = false;
						}
					}
					
					if(y >= 2)
					{
						if(tiles[y - 1][x].getType() == typeTemp && tiles[y - 2][x].getType() == typeTemp)
						{
							goodType = false;
						}
					}
					
				}while(!goodType);
				float tileScale = ( 90 - ( mScale * 90 ) ) / 2;
				tiles[y][x] = new Tile(mContext, x * 90 + (int)tileScale , y * 90 + (int)tileScale, typeTemp);
			}
		}
	}
	
	private boolean checkMatch(int x, int y, int type)
	{
		if(x >= 2)	// Check to the left for 3 in a row. 
		{
			if(tiles[x - 1][y].getType() == type && tiles[x - 2][y].getType() == type)
			{
				return true;
			}
		}
		
		if(x <= GRIDSIZE - 3) // Check to the right for 3 in a row.
		{
			if(tiles[x + 1][y].getType() == type && tiles[x + 2][y].getType() == type)
			{
				return true;
			}
		}
		
		if(y >= 2) // Check above.
		{
			if(tiles[x][y - 1].getType() == type && tiles[x][y - 2].getType() == type)
			{
				return true;
			}
		}
		
		if(y <= GRIDSIZE - 3)	// Check below.
		{
			if(tiles[x][y + 1].getType() == type && tiles[x][y + 2].getType() == type)
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		
		int startX;
		int startY;
		int endX;
		int endY;
		
		
		if(action == MotionEvent.ACTION_DOWN)
		{
			Tile temp;
			int x = (int) event.getX();
			int y = (int) event.getY();
			
			for(int i = 0; i < GRIDSIZE; ++i)
			{
				for(int j = 0; j < GRIDSIZE; j++)
				{
					temp = tiles[i][j];
					if(x >= temp.getXPos() && x < (temp.getXPos() + temp.getWidth()) &&
					   y >= temp.getYPos() && y < (temp.getYPos() + temp.getHeight()))
					{
						startX = x;
						startY = y;
						
						System.out.print("I touched tile: ");
						System.out.print(i);
						System.out.print(", ");
						System.out.println(j);
					}
				}
			}
		}
		else if(action == MotionEvent.ACTION_MOVE)
		{
			System.out.print(event.getX());
			System.out.print(", ");
			System.out.println(event.getY());
			
			for(int i = 0; i < GRIDSIZE; ++i)
			{
				for(int j = 0; j < GRIDSIZE; ++j)
				{
					
				}
			}
			
			this.invalidate();		// Force redraw. 
		}
		else if(action == MotionEvent.ACTION_UP)
		{
			// ...
		}
		
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Fill screen white
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawPaint(paint);
		
		canvas.scale(mScale, mScale);	
		
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
