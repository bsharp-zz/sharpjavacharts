package bsharp.charts;

import java.awt.Color;

/**
 * The Style attributes for a class of data in a chart.
 * @author brandon@sharpideas.ca
 *
 */
public class DataClassStyle {

   /** The line width to draw. */
   public final float lineWidth;
   /** The fill Color to draw. */
   public final Color fillColor;
   /** The line Color to draw. */
   public final Color lineColor;

   public DataClassStyle(
      final Color color, final Color outline, final float borderWidth ) {

      fillColor = color;
      lineColor = outline;
      lineWidth = borderWidth;
   }

}
