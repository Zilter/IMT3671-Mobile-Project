package com.project.gemswapper;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
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
	
	int gridOffset;
	int tileSize;
	
	float mViewWidth;
	float mViewHeight;
	
	int startX;
	int startY;
	int endX;
	int endY;
	
	boolean dragStarted;
	
	SoundPool sounds;
	int sFailure;
	int sSuccess;
	
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
		mViewWidth = metrics.widthPixels;
		mViewHeight = metrics.heightPixels;
		mScale = mViewWidth / 720;
		
		gridOffset = (int)(mViewHeight - mViewWidth);
		tileSize = (int) (mViewWidth / 8);
		
		System.out.println(mViewWidth);
		dragStarted = false;
		
		startX = -1;
		startY = -1;
		endX = -1;
		endY = -1;
		
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		sFailure = sounds.load(mContext, R.raw.failure, 1);
		sSuccess = sounds.load(mContext, R.raw.success, 1);
		
		
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
				tiles[y][x] = new Tile(mContext, x * 90 + (int)tileScale , y * 90 + (int)(tileScale + gridOffset), typeTemp);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		
		
		
		if(action == MotionEvent.ACTION_DOWN)
		{
//			int x = (int) event.getX();
//			int y = (int) event.getY();
//			
//			if(y > gridOffset)
//			{
//				startX = x / tileSize;
//				startY = (y - gridOffset) / tileSize;
//				
//				System.out.print(startY);
//				System.out.print(", ");
//				System.out.println(startX);
//			}
		}
		else if(action == MotionEvent.ACTION_MOVE)
		{
			if(!dragStarted)
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				
				if(y > gridOffset)
				{
					startX = x / tileSize;
					startY = (y - gridOffset) / tileSize;
					
//					System.out.print(startY);
//					System.out.print(", ");
//					System.out.println(startX);
					
					dragStarted = true;
				}
			} 
			else
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				
				if(y > gridOffset)
				{
					endX = x / tileSize;
					endY = (y - gridOffset) / tileSize;
					
//					System.out.print("End: ");
//					System.out.print(endY);
//					System.out.print(", ");
//					System.out.println(endX);
				}
			}
		}
		else if(action == MotionEvent.ACTION_UP)
		{
			dragStarted = false;
			
			System.out.print(startX);
			System.out.print(", ");
			System.out.print(startY);
			System.out.print(", ");
			System.out.print(endX);
			System.out.print(", ");
			System.out.println(endY);
			
			if(startX != endX || startY != endY)
			{	
				if(endX > startX)	// drag right.
				{
					if(checkMatch(startY, startX + 1, tiles[startY][startX + 1].getType()))
					{
						playSound(sSuccess);
					}
					else
					{
						playSound(sFailure);
					}
				}
				else if(endX < startX) // drag left
				{
					if(checkMatch(startY, startX - 1, tiles[startY][startX - 1].getType()))
					{
						playSound(sSuccess);
					}
					else
					{
						playSound(sFailure);
					}
				}
				else if(endY > startY) // drag down.
				{
					if(checkMatch(startY + 1, startX, tiles[startY + 1][startX].getType()))
					{
						playSound(sSuccess);
					}
					else
					{
						playSound(sFailure);	
					}
				}
				else if(endY < startY) // drag up
				{
					if(checkMatch(startY - 1, startX, tiles[startY - 1][startX].getType()))
					{
						playSound(sSuccess);
					}
					else
					{
						playSound(sFailure);
					}
				}
				
				//switch, clear and reward points. 
			}
			
			this.invalidate();		// Force redraw. 
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
		
		canvas.drawBitmap(grid, 0, mViewHeight - mViewWidth, paint);
		
		for(int i = 0; i < GRIDSIZE; ++i)
		{
			for(int j = 0; j < GRIDSIZE; ++j)
			{
				tiles[i][j].draw(canvas);
			}
		}
	}
	
	private boolean checkMatch(int x, int y, int type)
	{
		boolean right, rightRight, left, leftLeft, up, upUp, down, downDown;
		right = rightRight = left = leftLeft = up = upUp = down = downDown = false;
		
		if(x < GRIDSIZE - 1 && tiles[y][x + 1].getType() == type)	// Check right
		{
			right = true;
			
			if(x < GRIDSIZE - 2 && tiles[y][x + 2].getType() == type)	// Check 2 to the right
			{
				rightRight = true;
			}
		}
		
		if(x > 0 && tiles[y][x - 1].getType() == type) // Check left
		{
			left = true;
			
			if(x > 1 && tiles[y][x - 2].getType() == type) // Check 2 to the left
			{
				leftLeft = true;
			}
		}
		
		if(y < GRIDSIZE - 1 && tiles[y + 1][x].getType() == type) // Check below. 
		{
			down = true;
			
			if(y < GRIDSIZE - 2 && tiles[y + 2][x].getType() == type) // Check 2 below. 
			{
				downDown = true;
			}
		}
		
		if(y > 0 && tiles[y - 1][x].getType() == type)
		{
			up = true;
			
			if(y > 1 && tiles[y - 2][x].getType() == type)
			{
				upUp = true;
			}
		}
		
		if(right || rightRight || left || leftLeft || down || downDown || up || upUp)
		{
			return findShape(right, rightRight, left, leftLeft, down, downDown, up, upUp);
		}
		else
		{
			return false;
		}
	}
	
	private boolean findShape(boolean right, boolean rightRight, boolean left,
			boolean leftLeft, boolean down, boolean downDown, boolean up,
			boolean upUp) 
	{
		return false;
	}

	private void playSound(int type)
	{
		sounds.play(type, 1.0f, 1.0f, 0, 0, 1.0f);
	}
}
