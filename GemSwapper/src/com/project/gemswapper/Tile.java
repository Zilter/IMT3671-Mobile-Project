package com.project.gemswapper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile{
	int mType;
	Bitmap mSprite;
	Resources res;
	
	private int xPos;
	private int yPos;
	private int spriteHeight;
	private int spriteWidth;
	
	private Rect mRect;
	
	public enum TileType {CIRCLE, TRIANGLE, SQUARE, DIAMOND, STAR };
	
	
	public Tile(Context context)
	{
		res = context.getResources();
		mRect = new Rect();
		xPos = 0;
		yPos = 0;
		spriteHeight = 0;
		spriteWidth = 0;
		mType = 0;
		mSprite = null;
	}
	
	public Tile(Context context, int x, int y, int type)		// 5 types..
	{
		res = context.getResources();
		boolean goOn = true;

		if(type == TileType.CIRCLE.ordinal())
		{
			mSprite = BitmapFactory.decodeResource(res, R.drawable.circle_tile_01);
		}
		else if(type == TileType.TRIANGLE.ordinal())
		{
			mSprite = BitmapFactory.decodeResource(res, R.drawable.triangle_tile_01);
		}
		else if(type == TileType.SQUARE.ordinal())
		{
			mSprite = BitmapFactory.decodeResource(res, R.drawable.square_tile_01);
		}
		else if(type == TileType.DIAMOND.ordinal())
		{
			mSprite = BitmapFactory.decodeResource(res, R.drawable.diamond_tile_01);
		}
		else if(type == TileType.STAR.ordinal())
		{
			mSprite = BitmapFactory.decodeResource(res, R.drawable.star_tile_01);
		}
		else
		{
			System.out.print("Wrong tileType specified: ");
			System.out.print(type);
			System.out.println(" (Expected integer in range: [0-4])");
			
			goOn = false;
		}
		
		if(goOn)
		{
			mType = type;
			spriteHeight = mSprite.getHeight();
			spriteWidth = mSprite.getWidth();
			xPos = x;
			yPos = y;
			
			mRect = new Rect(0, 0, spriteWidth, spriteHeight);
		}
		else		// make sure we don't access a broken object...
		{
			mRect = new Rect();
			xPos = 0;
			yPos = 0;
			spriteHeight = 0;
			spriteWidth = 0;
			mType = 0;
			mSprite = null;
		}
	}
	
	public int getType()
	{
		return mType;
	}
	
	public int getXPos()
	{
		return xPos;
	}
	
	public int getYPos()
	{
		return yPos;
	}
	
	public void setXPos(int x)
	{
		xPos = x - (spriteWidth / 2);
	}
	
	public void setYPos(int y)
	{
		yPos = y - (spriteHeight / 2);
	}
	
	public int getWidth()
	{
		return spriteWidth;
	}
	
	public int getHeight()
	{
		return spriteHeight;
	}
	
	
	public void draw(Canvas canvas)
	{
		Rect dest = new Rect(getXPos(), getYPos(), getXPos() + spriteWidth, getYPos() + spriteHeight);
		
		canvas.drawBitmap(mSprite, mRect, dest, null);
	}
}
