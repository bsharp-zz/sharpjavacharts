package bsharp.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A bar chart Image.
 *
 * @author brandon@sharpideas.ca
 */
public class BarChartImage {

   /** The array index of the x value. */
   private static final int X = 0;
   /** The array index of the y value. */
   private static final int Y = 1;

   private final ChartData data;
   private final Style style;

   /**
    * Create a new bar chart image object.
    *
    * @param chartData the chart data.
    * @param chartStyle the chart style attributes.
    */
   public BarChartImage( final ChartData chartData, final Style chartStyle) {
      data = chartData;
      style = chartStyle;
   }

   /**
    * All the style attributes for a bar chart.
    *
    * @author brandon@sharpideas.ca
    */
   public static class Style extends ChartStyle {

      /**
       * Displays.
       *  ---+++
       * Instead of:
       *  ---
       *  +++
       */
      public final boolean stackGroups;

      /** The chart bar width. */
      public final float barWidth;

      /** If true draw bar horizontally. */
      public boolean horizontalBars;

      /** Create new Style attributes for a bar chart. */
      public Style(
         final int chartWidth,
         final int chartHeight,
         final DataClassStyle[] chartClassStyles,
         final float chartbarWidth,
         final boolean stackGroupsFlag) {

         super(chartWidth, chartHeight, chartClassStyles );
         barWidth = chartbarWidth;
         stackGroups = stackGroupsFlag;
      }
   }


   /**
    * Draw the graph image to the given file.
    *
    * @param outputfile the file to write the image to.
    * @param format thie image formate (eg png, jpg etc...)
    */
   public void draw(final File outputfile, final String format) {

      // Create buffered image that does not support transparency
      final BufferedImage bimage =
         new BufferedImage(style.width, style.height, BufferedImage.TYPE_INT_RGB);

      // Create a graphics context on the buffered image
      final Graphics2D g2d = bimage.createGraphics();

      // Anti-alias!
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);

      // Backgound color.
      g2d.setColor(Color.white);
      g2d.fillRect(0, 0, style.width, style.height);

      // Outline entire graph
//      g2d.setColor(Color.black);
//      g2d.draw(new Rectangle(1, 1, width - 1, height - 1));

      final int textHeight = 15;
      final int titleHeight = textHeight;
      final int xLabelsHeight = textHeight;
      final int yLabelsWidth = 25;
      final int legendWidth = 50;
      final int dataAreaHeight = style.height - (xLabelsHeight + titleHeight);
      final int dataAreaWidth = style.width - (yLabelsWidth + legendWidth);

      // Draw Data
      final Rectangle dataLinesBounds = new Rectangle(
         yLabelsWidth,
         titleHeight,
         dataAreaWidth,
         dataAreaHeight );
      drawData(g2d, dataLinesBounds);

//
//      // Draw x-axis labels
//      final Rectangle xLabelsBounds = new Rectangle(
//         yLabelsWidth,
//         fHeight - xLabelsHeight,
//         dataAreaWidth,
//         xLabelsHeight );
//      drawXAxisLabels( g2d, xLabelsBounds);
//
//      // Draw y axis labels
//      final Rectangle yLabelsBounds = new Rectangle(
//         0,
//         titleHeight,
//         yLabelsWidth,
//         dataAreaHeight );
//      drawYLabels( g2d, yLabelsBounds);

      g2d.dispose();

      // Save to file
      try {
         ImageIO.write(
            bimage,
            format,
            outputfile );
      } catch (final IOException e) {
         e.printStackTrace();
      }

   }

   /**
    * Draw the chart lines.
    * @param outputfile the file to write to.
    * @param format the image format (jpeg, gif, etc..).
    */
   private void drawData(final Graphics2D g2d, final Rectangle bounds ) {

      // Outline area
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND ) );
      g2d.setColor( Color.gray );
      g2d.draw(bounds);

      final int[] pos = new int[] {bounds.x,  bounds.y};

      // Draw data bars
      for (int i = 0; i < data.getDataGroups().length; i++ ) {

         drawBarGroup(g2d, data.getDataGroups()[i], pos, bounds );

         if ( style.stackGroups ) {
            pos[Y] = pos[Y] + (int)style.barWidth;
            pos[X] = bounds.x;
         }
      }
   }

   private void drawBarGroup(
      final Graphics2D g2d, final ChartData.DataGroup currGroup,
      final int[] pos, final Rectangle bounds ) {

      for ( int j = 0; j < data.getDataValues().length; j++ ) {
         final ChartData.DataValue next = data.getDataValues()[j];

         if ( next.dataGroup == currGroup) {

            drawBar( g2d, pos, next);

            // Increment x,y Position
            final float value = Float.valueOf(next.value).floatValue();
            if ( !style.stackGroups) {
               pos[Y] = pos[Y] + (int)style.barWidth;
            } else {
               pos[X] = pos[X] + (int)value;
            }

         }
      }
   }

   private void drawBar(final Graphics2D g2d,
      final int[] pos, final ChartData.DataValue dataValue ) {


      final float value = Float.valueOf(dataValue.value).floatValue();
      final Rectangle bar = new Rectangle(pos[X], pos[Y], (int)value, (int)style.barWidth);
      final DataClassStyle classStyle = getBarStyle( dataValue);

      g2d.setColor(classStyle.fillColor);
      g2d.fill(bar);

      // Optional outline
      if ( classStyle.lineWidth > 0 ) {
         g2d.setColor(classStyle.lineColor);
         g2d.draw(bar);
      }
   }

   private DataClassStyle getBarStyle(final ChartData.DataValue next) {
      DataClassStyle classStyle = null;
      for ( int k = 0; k < data.getDataClasses().length; k++ ) {
         if ( next.dataClass == data.getDataClasses()[k] ) {
            classStyle = style.classStyles[k];
         }
      }
      return classStyle;
   }
}
