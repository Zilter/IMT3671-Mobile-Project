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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

	private final int GRIDSIZE = 8;
	private final int NUM_PATTERNS = 32;
	
	Paint paint;
	Resources res;
	Bitmap grid;
	Context mContext;
	Tile tiles[][];
	Random randomGen;
	
	float mScale;
	int mScore;
	
	int gridOffset;
	int tileSize;
	
	float mViewWidth;
	float mViewHeight;
	
	int startX;
	int startY;
	int endX;
	int endY;
	
	int patterns[][];
	
	boolean dragStarted;
	
	SoundPool sounds;
	int sFailure;
	int sSuccess;
	
	public GameView(Context context) {
		super(context);
		paint = new Paint();
		tiles = new Tile[8][8];
		patterns = new int[NUM_PATTERNS][];
		
		fillPatterns();
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
		
		mScore = 0;
		
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		sFailure = sounds.load(mContext, R.raw.failure, 1);
		sSuccess = sounds.load(mContext, R.raw.success, 1);
		
		
		fillGrid();
	}
	
	// Patterns are defined as [Above, Above, Left, Left, CENTER, Right, Right, Down, Down, SUMNEEDED, SCORE] with 1's and 0's 
	// corresponding to the pattern we want to match. e.g [1, 1, 0, 0, 1, 0, 0, 0, 0, 3, 100] for 3 in a row starting from the center node
	// and going up, giving a score of 100. 
	
	// Patterns should be added in descending order of SUM needed. 
	private void fillPatterns()
	{
		final int FIVE_T_SCORE = 1000;
		final int FOUR_T_SCORE = 800;
		final int FIVE_ROW_SCORE = 700;
		final int THREE_T_SCORE  = 600;
		final int CORNER_SCORE = 500;
		final int FOUR_ROW_SCORE = 400;
		final int THREE_ROW_SCORE = 300;
		
		// Five-T shapes.
		patterns[0] = new int[] {1, 1, 1, 1, 1, 1, 1, 0, 0, 7, FIVE_T_SCORE}; // Left, right, up
		patterns[1] = new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1, 7, FIVE_T_SCORE}; // left, right, down
		patterns[2] = new int[] {1, 1, 1, 1, 1, 0, 0, 1, 1, 7, FIVE_T_SCORE}; // up, down, left
		patterns[3] = new int[] {1, 1, 0, 0, 1, 1, 1, 1, 1, 7, FIVE_T_SCORE}; // up, down, right.
		
		// Four-T Shapes
		patterns[4]  = new int[] {1, 1, 0, 1, 1, 1, 1, 0, 0, 6, FOUR_T_SCORE}; // up, 1 left, 2 right. 
		patterns[5]  = new int[] {1, 1, 1, 1, 1, 1, 0, 0, 0, 6, FOUR_T_SCORE}; // up, 2 left, 1 right. 
		patterns[6]  = new int[] {0, 0, 0, 1, 1, 1, 1, 1, 1, 6, FOUR_T_SCORE}; // down, 1 left, 2 right.
		patterns[7]  = new int[] {0, 0, 1, 1, 1, 1, 0, 1, 1, 6, FOUR_T_SCORE}; // down, 2 left, 1 right.
		patterns[8]  = new int[] {0, 1, 1, 1, 1, 0, 0, 1, 1, 6, FOUR_T_SCORE}; // 1 up, 2 left, 2 down.
		patterns[9]  = new int[] {1, 1, 1, 1, 1, 0, 0, 1, 0, 6, FOUR_T_SCORE}; // 2 up, 2 left, 1 down. 
		patterns[10] = new int[] {0, 1, 0, 0, 1, 1, 1, 1, 1, 6, FOUR_T_SCORE}; // 1 up, 2 right, 2 down. 
		patterns[11] = new int[] {1, 1, 0, 0, 1, 1, 1, 1, 0, 6, FOUR_T_SCORE}; // 2 up, 2 right, 1 down. 
		
		// Five in a row.
		patterns[12] = new int[] {1, 1, 0, 0, 1, 0, 0, 1, 1, 5, FIVE_ROW_SCORE}; // up and down
		patterns[13] = new int[] {0, 0, 1, 1, 1, 1, 1, 0, 0, 5, FIVE_ROW_SCORE}; // left and right
		
		// Three-T shapes
		patterns[14] = new int[] {0, 0, 0, 1, 1, 1, 0, 1, 1, 5, THREE_T_SCORE}; // 1 left, 1 right, 2 down
		patterns[15] = new int[] {0, 1, 0, 0, 1, 1, 1, 1, 0, 5, THREE_T_SCORE}; // 1 up, 1 down, 2 right.
		patterns[16] = new int[] {0, 1, 1, 1, 1, 0, 0, 1, 0, 5, THREE_T_SCORE}; // 1 up, 1 down, 2 left. 
		patterns[17] = new int[] {1, 1, 0, 1, 1, 1, 0, 0, 0, 5, THREE_T_SCORE}; // 1 left, 1 right, 2 up. 
		
		// Corner shapes
		patterns[18] = new int[] {0, 0, 1, 1, 1, 0, 0, 1, 1, 5, CORNER_SCORE}; // left, down
		patterns[19] = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 5, CORNER_SCORE}; // right, down
		patterns[20] = new int[] {1, 1, 1, 1, 1, 0, 0, 0, 0, 5, CORNER_SCORE}; // left, up
		patterns[21] = new int[] {1, 1, 0, 0, 1, 1, 1, 0, 0, 5, CORNER_SCORE}; // right, up
		
		// Four in a row
		patterns[22] = new int[] {0, 0, 1, 1, 1, 1, 0, 0, 0, 4, FOUR_ROW_SCORE}; // 2 left, 1 right
		patterns[23] = new int[] {0, 0, 0, 1, 1, 1, 1, 0, 0, 4, FOUR_ROW_SCORE}; // 1 left, 2 right
		patterns[24] = new int[] {1, 1, 0, 0, 1, 0, 0, 1, 0, 4, FOUR_ROW_SCORE}; // 2 up, 1 down
		patterns[25] = new int[] {0, 1, 0, 0, 1, 0, 0, 1, 1, 4, FOUR_ROW_SCORE}; // 1 up, 2 down
				
		// Three in a row
		patterns[26] = new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0, 3, THREE_ROW_SCORE}; // 1 left, 1 right
		patterns[27] = new int[] {0, 0, 0, 0, 1, 1, 1, 0, 0, 3, THREE_ROW_SCORE}; // 2 right
		patterns[28] = new int[] {0, 0, 1, 1, 1, 0, 0, 0, 0, 3, THREE_ROW_SCORE}; // 2 left
		patterns[29] = new int[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 3, THREE_ROW_SCORE}; // 1 up, 1 down
		patterns[30] = new int[] {0, 0, 0, 0, 1, 0, 0, 1, 1, 3, THREE_ROW_SCORE}; // 2 down
		patterns[31] = new int[] {1, 1, 0, 0, 1, 0, 0, 0, 0, 3, THREE_ROW_SCORE}; // 2 up
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
			
			
			
			Tile temp;
			boolean failure = false;
			
			if(startX != endX || startY != endY)
			{	
				if(endX > startX)	// drag right.
				{
					temp = tiles[startY][startX + 1];
					tiles[startY][startX + 1] = tiles[startY][startX];
					tiles[startY][startX] = temp;
					
					if(checkMatch(startY, startX + 1, tiles[startY][startX + 1].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
						
					if(checkMatch(startY, startX, tiles[startY][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					if(!failure)
					{
						playSound(sSuccess);
					}
					else
					{
						temp = tiles[startY][startX + 1];
						tiles[startY][startX + 1] = tiles[startY][startX];		// Reverse swap if failure.. 
						tiles[startY][startX] = temp;
						playSound(sFailure);
					}
				}
				else if(endX < startX) // drag left
				{
					temp = tiles[startY][startX - 1];
					tiles[startY][startX - 1] = tiles[startY][startX];
					tiles[startY][startX] = temp;
					
					if(checkMatch(startY, startX - 1, tiles[startY][startX - 1].getType()))
					{
						failure = false;
					}
					else 
					{
						failure = true;
					}
						
					if(checkMatch(startY, startX, tiles[startY][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					if(!failure)
					{
						playSound(sSuccess);
					}
					else
					{
						temp = tiles[startY][startX - 1];
						tiles[startY][startX - 1] = tiles[startY][startX];		// Reverse swap if failure.. 
						tiles[startY][startX] = temp;
						playSound(sFailure);
					}
				}
				else if(endY > startY) // drag down.
				{
					temp = new Tile(tiles[startY + 1][startX ]);
					tiles[startY + 1][startX] = new Tile(tiles[startY][startX]);
					tiles[startY][startX] = temp;
					
					if(checkMatch(startY + 1, startX, tiles[startY + 1][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					
					if(checkMatch(startY, startX, tiles[startY][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					if(!failure)
					{
						playSound(sSuccess);
					}
					else
					{
						temp = new Tile(tiles[startY + 1][startX]);
						tiles[startY + 1][startX] = new Tile(tiles[startY][startX]);		// Reverse swap if failure.. 
						tiles[startY][startX] = temp;
						playSound(sFailure);
					}
				}
				else if(endY < startY) // drag up
				{
					temp = tiles[startY - 1][startX];
					tiles[startY - 1][startX] = tiles[startY][startX];
					tiles[startY][startX] = temp;
					
					if(checkMatch(startY - 1, startX, tiles[startY - 1][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					if(checkMatch(startY, startX, tiles[startY][startX].getType()))
					{
						failure = false;
					}
					else
					{
						failure = true;
					}
					
					if(!failure)
					{
						playSound(sSuccess);
					}
					else
					{
						temp = tiles[startY - 1][startX];
						tiles[startY - 1][startX] = tiles[startY][startX];		// Reverse swap if failure.. 
						tiles[startY][startX] = temp;
						playSound(sFailure);
					}
				}
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
		
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		
		canvas.drawText("Score: " + String.valueOf(mScore), 25, 25, paint);
		
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
	
	private boolean checkMatch(int y, int x, int type)
	{
		int typeMatch[] = {0, 0, 0, 0, 1, 0, 0, 0, 0}; // The center, is always a match.
		
//		System.out.print("(X, y, type): ");
//		System.out.print(x);
//		System.out.print(", ");
//		System.out.print(y);
//		System.out.print(", ");
//		System.out.println(type);
		
		if(x > 0 && tiles[y][x - 1].getType() == type)
		{
			typeMatch[3] = 1;
			
			if(x > 1 && tiles[y][x - 2].getType() == type)
			{
				typeMatch[2] = 1;
			}
		}
		
		if(x < GRIDSIZE - 1 && tiles[y][x + 1].getType() == type)
		{
			typeMatch[5] = 1;
			
			if(x < GRIDSIZE - 2 && tiles[y][x + 2].getType() == type)
			{
				typeMatch[6] = 1;
			}
		}
		
		if(y > 0 && tiles[y - 1][x].getType() == type)
		{
			typeMatch[1] = 1;
			
			if(y > 1 && tiles[y - 2][x].getType() == type)
			{
				typeMatch[0] = 1;
			}
		}
		
		if(y < GRIDSIZE - 1 && tiles[y + 1][x].getType() == type)
		{
			typeMatch[7] = 1;
			
			if(y < GRIDSIZE - 2 && tiles[y + 2][x].getType() == type)
			{
				typeMatch[8] = 1;
			}
		}
		
		int sum = 0;
		
//		System.out.print("TypeMatch: ");
//		System.out.print(typeMatch[0]);
//		System.out.print(", ");
//		System.out.print(typeMatch[1]);
//		System.out.print(", ");
//		System.out.print(typeMatch[2]);
//		System.out.print(", ");
//		System.out.print(typeMatch[3]);
//		System.out.print(", ");
//		System.out.print(typeMatch[4]);
//		System.out.print(", ");
//		System.out.print(typeMatch[5]);
//		System.out.print(", ");
//		System.out.print(typeMatch[6]);
//		System.out.print(", ");
//		System.out.print(typeMatch[7]);
//		System.out.print(", ");
//		System.out.println(typeMatch[8]);

		
		for(int i = 0; i < NUM_PATTERNS; ++i)
		{
			sum = typeMatch[0] * patterns[i][0] +
				  typeMatch[1] * patterns[i][1] +
				  typeMatch[2] * patterns[i][2] +
				  typeMatch[3] * patterns[i][3] +
				  typeMatch[4] * patterns[i][4] +
				  typeMatch[5] * patterns[i][5] +
				  typeMatch[6] * patterns[i][6] +
				  typeMatch[7] * patterns[i][7] + 
				  typeMatch[8] * patterns[i][8];
			
//			System.out.print("Pattern ");
//			System.out.print(i);
//			System.out.print(": ");
//			System.out.print(patterns[i][0]);
//			System.out.print(", ");
//			System.out.print(patterns[i][1]);
//			System.out.print(", ");
//			System.out.print(patterns[i][2]);
//			System.out.print(", ");
//			System.out.print(patterns[i][3]);
//			System.out.print(", ");
//			System.out.print(patterns[i][4]);
//			System.out.print(", ");
//			System.out.print(patterns[i][5]);
//			System.out.print(", ");
//			System.out.print(patterns[i][6]);
//			System.out.print(", ");
//			System.out.print(patterns[i][7]);
//			System.out.print(", ");
//			System.out.println(patterns[i][8]);
			
			
			if(sum >= patterns[i][9])
			{
				clearOut(i, x, y);
				return true;
			}
		}
		
		return false;
	}
	
	private void clearOut(int pattern, int x, int y)
	{
		
	}
	
	private void playSound(int type)
	{
		sounds.play(type, 1.0f, 1.0f, 0, 0, 1.0f);
	}
}
