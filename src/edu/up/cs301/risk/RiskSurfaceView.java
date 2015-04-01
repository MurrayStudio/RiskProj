package edu.up.cs301.risk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * drawing class
 * 
 * @author Steven R. Vegdahl
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will Bryant
 * @version March 2015
 */
public class RiskSurfaceView extends SurfaceView {

	//define vars for draw
	private Bitmap troopBlack;
	private Bitmap troopWhite;

	/**
	 * Constructor for the MySurfaceView class. This version is the one that is
	 * typically called directly by Java code.
	 * 
	 * @param context
	 *            a reference to the activity this animation is run under
	 */
	public RiskSurfaceView(Context context) {
		super(context);
		init(); // perform common initialization
	}// ctor

	/**
	 * An alternate constructor for use when a subclass is directly specified in
	 * the layout.
	 * 
	 * @param context
	 *            a reference to the activity this animation is run under
	 * @param attrs
	 *            set of attributes passed from system
	 */
	public RiskSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(); // perform common initialization
	}// ctor

	/**
	 * helper-method: performs initialization common to both constructors
	 */
	private void init() {

	}

	/**
	 * callback method when draw is needed: note that this method performs
	 * memory allocations (with 'new'), which is not a recommended practice
	 * during drawing operations
	 * 
	 * @param g
	 *            the canvas to draw on
	 */
	@Override
	public void onDraw(Canvas g) {

/*		//define bitmaps for troops
		troopBlack = BitmapFactory.decodeResource(getResources(), R.drawable.soldier2);
		troopWhite = BitmapFactory.decodeResource(getResources(), R.drawable.soldier2white);

		//draw some examples of troops in countries 
		g.drawBitmap(troopBlack, 5, 5, null);
		g.drawBitmap(troopWhite, 5, 550, null);
		g.drawBitmap(troopWhite, 350, 250, null);
		g.drawBitmap(troopBlack, 500, 5, null);
		g.drawBitmap(troopWhite, 250, 400, null);
		g.drawBitmap(troopBlack, 500, 400, null);*/
	}

	/**
	 * tells whether the path contains a point
	 * 
	 * @param x
	 *            the x-coordinate of the point
	 * @param y
	 *            the y-coordinate of the point
	 * @param p
	 *            the path
	 * @return true iff the path contains the point
	 */
	public static boolean pathContainsPoint(float x, float y, Path p) {

		// translate a copy of the Path
		Path copy = new Path(p);
		copy.offset(-x, -y);

		// create a 1x1 bitmap that is the single pixel at (0,0)
		Bitmap lookup = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

		// set the pixel to black
		lookup.setPixel(0, 0, Color.BLACK);

		// draw a white version of the path on the bitmap
		Canvas canvas = new Canvas(lookup);
		Paint white = new Paint();
		white.setColor(Color.WHITE);
		canvas.drawPath(copy, white);

		// return true if the pixel is white, meaning that the Path has
		// "painted" it
		int color = lookup.getPixel(0, 0);
		return color == Color.WHITE;
	}

}
