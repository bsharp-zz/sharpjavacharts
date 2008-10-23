package bsharp.charts;

import java.awt.Color;
import java.util.Vector;


/**
 * A chart line.
 * @author brandon@sharpideas.ca
 *
 */
public class ChartLine {

   /** The charts name or title. */
   public final String name;
   private Color color;
   /** The line thickness. */
   public int thickness = 1;
   private Vector points = new Vector();

   private float maxValue = (float)0.0;
   private float minValue = (float)Float.MAX_VALUE;

   public ChartLine( final ChartData.DataClass dataClass) {
      name = dataClass.name;
   }

   public void addPoint(final float xVal, final float yVal) {

      if (yVal > maxValue) {
         maxValue = yVal;
      }
      if ( yVal < minValue ) {
         minValue = yVal;
      }
      final Point add = new Point( xVal, yVal);
      points.add(add);
   }

   public void normalize() {

      final int normalize = 100;

      maxValue = maxValue - minValue;

      for (int i = 0; i < points.size(); i++) {

         final Point next = (Point) points.elementAt(i);
         next.y = next.dataY - minValue;
         if ( maxValue == 0) {
            next.y = (float)0.0;
         } else {
            next.y = ((next.y / maxValue) * normalize);
         }
      }
   }

   public Point[] getPoints() {
      return (Point[]) points.toArray(new Point[points.size()]);
   }

   public boolean equals(final Object o) {
      return (o instanceof ChartLine) && ((ChartLine) o).name.equals(name);
   }

   /**
    * Get the y value for the given x value.
    * @param x the x value to lookup.
    * @return the y value for the given x value.
    */
   public float getY(final float x) {
      for ( int i = 0; i < points.size(); i++ ) {
         final Point point = (Point)points.elementAt(i);
         if (point.dataX == x ) {
            return point.dataY;
         }
      }
      return 0;
   }
   /**
    * A chart point.
    * @author brandon@sharpideas.ca
    *
    */
   public static class Point {

      public Point( final float xVal, final float yVal) {
         this.dataX = x = xVal;
         this.dataY = y = yVal;
      }

      /** The points original x data value. */
      public final float dataX;
      /** The points original y data value. */
      public final float dataY;

      /** The points x location. */
      public float x;
      /** The points y location. */
      public float y;

   }

   /**
    * Get line color.
    * @return line color.
    */
   public Color getColor() {
      return color;
   }

   /**
    * Set line color.
    * @param color new line color.
    */
   public void setColor(final Color newColor) {
      this.color = newColor;
   }
}
