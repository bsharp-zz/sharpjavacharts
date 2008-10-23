package bsharp.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import bsharp.html.HtmlColors;
import bsharp.html.BoxOverToolTip;
import bsharp.html.Html;
import bsharp.html.HtmlImageMapItem;

/**
 * A Line chart.
 * @author brandon@sharpideas.ca
 *
 */
public class LineChartImage {

 //toDo
   /*

chart data /iamge
chart image map

draw lines (bounds)
draw xLabels
draw ylabels
X adjust near edges

make constant in drawable chart

mids.

more floats instead of ints

speartate appending to trend and graphing
bsharp.strings.StringVector
    */

   private ChartData.DataGroup[] xAxisValues;
   private static final String FONT_STYLE = "Verdana";
   private static final Font FONT = new Font(FONT_STYLE, Font.PLAIN, 12);

   private final int fWidth;
   private final int fHeight;
   private Vector imageMapItems = new Vector();
   private final Vector drawnLineSegments = new Vector();
   private final Vector lines;
   private final ChartData data;

   /**
    * Create a new chart.
    * @param chartTitle the title.
    */
   public LineChartImage(
      final ChartData newData, final String chartTitle, final int width, final int height ) {

      this.data = newData;
      fWidth = width;
      fHeight = height;
      xAxisValues = newData.getDataGroups();
      lines = createLines();
   }

   private Vector createLines() {
      final Vector result = new Vector();

      for ( int i = 0; i < data.getDataClasses().length; i++ ) {

         final ChartData.DataClass currClass = data.getDataClasses()[i];

         final ChartLine next = new ChartLine(currClass);
         result.addElement( next );
         createPoints(currClass, next);
      }

      return result;
   }

   private void createPoints(final ChartData.DataClass currClass, final ChartLine next) {
      for ( int j = 0; j < data.getDataValues().length; j++ ) {

         final ChartData.DataValue nextValue = data.getDataValues()[j];
         final ChartData.DataGroup group = nextValue.dataGroup;
         final int x = data.getDataGroupsVector().indexOf(group);

         if ( nextValue.dataClass == currClass) {
            next.addPoint(x, Float.valueOf(nextValue.value));
         }
      }
   }



   /**
    * Get the line object of this chart.
    * @return the lines.
    */
   public Vector getLines() {
      return lines;
   }

   /**
    * Draw the chart to an image file.
    * @param outputfile the file to write to.
    * @param format the image format (jpeg, gif, etc..).
    */
   public void draw(final File outputfile, final String format ) {

      // Create buffered image that does not support transparency
      final BufferedImage bimage =
         new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_INT_RGB);

      // Create a graphics context on the buffered image
      final Graphics2D g2d = bimage.createGraphics();

      // Anti-alias!
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);

      // Backgound color.
      g2d.setColor(Color.white);
      g2d.fillRect(0, 0, fWidth, fHeight);

      // Outline entire graph
//      g2d.setColor(Color.black);
//      g2d.draw(new Rectangle(1, 1, width - 1, height - 1));

      final int textHeight = 15;
      final int titleHeight = textHeight;
      final int xLabelsHeight = textHeight;
      final int yLabelsWidth = 25;
      final int legendWidth = 50;
      final int dataAreaHeight = fHeight - (xLabelsHeight + titleHeight);
      final int dataAreaWidth = fWidth - (yLabelsWidth + legendWidth);

      // Draw Data
      final Rectangle dataLinesBounds = new Rectangle(
         yLabelsWidth,
         titleHeight,
         dataAreaWidth,
         dataAreaHeight );
      drawData(g2d, dataLinesBounds);


      // Draw x-axis labels
      final Rectangle xLabelsBounds = new Rectangle(
         yLabelsWidth,
         fHeight - xLabelsHeight,
         dataAreaWidth,
         xLabelsHeight );
      drawXAxisLabels( g2d, xLabelsBounds);

      // Draw y axis labels
      final Rectangle yLabelsBounds = new Rectangle(
         0,
         titleHeight,
         yLabelsWidth,
         dataAreaHeight );
      drawYLabels( g2d, yLabelsBounds);

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
    * Draw the chart y labels.
    * @param outputfile the file to write to.
    * @param format the image format (jpeg, gif, etc..).
    */
   private void drawYLabels(final Graphics2D g2d, final Rectangle bounds ) {

      g2d.rotate(-Math.PI / 2.0);
      g2d.setColor(HtmlColors.DARK_GRAY_1);

      final Font values = new Font(FONT_STYLE, Font.PLAIN, 10);

      String yLabel = "Highest Ever";
      TextLayout textLayout = new TextLayout(yLabel, values, g2d.getFontRenderContext());
      Rectangle2D r = textLayout.getBounds();
      int y = (0 - bounds.y) - (int)r.getWidth();
      textLayout.draw(g2d, y, (bounds.width / 2));

      yLabel = "Lowest Ever";
      textLayout = new TextLayout(yLabel, values, g2d.getFontRenderContext());
      r = textLayout.getBounds();
      y = (0 - bounds.y) - (bounds.height);
      textLayout.draw(g2d, y, (bounds.width / 2));

      yLabel = "Metrics Values";
      textLayout = new TextLayout(yLabel, FONT, g2d.getFontRenderContext());
      r = textLayout.getBounds();
      y = (0 - bounds.y) - (bounds.height / 2);
      textLayout.draw(g2d, y - (float)(r.getWidth() / 2), (bounds.width / 2));

   }

   private void drawXAxisLabels(final Graphics2D g2d, final Rectangle bounds) {

      g2d.setFont(FONT);
      g2d.setColor(Color.black);
      final int xSpacing = bounds.width / xAxisValues.length;

      for (int i = 0; i < xAxisValues.length; i++ ) {

         if ( isHighlighted( i ) ) {

            final String xLabel = xAxisValues[i].name;
            final TextLayout textLayout = new TextLayout(xLabel, FONT, g2d.getFontRenderContext());
            final Rectangle2D r = textLayout.getBounds();

            final int x = bounds.x + (xSpacing * i);
            drawDot(g2d, x, bounds.y );

            //x = adjustNearSides(x, labelLen, bounds.x, bounds.x + width);
            g2d.drawString(xLabel, x - (int)(r.getWidth() / 2), (bounds.y + bounds.height) - 2);
         }
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

      // Draw data sections
      drawDataSections(g2d, bounds);

      // Draw data lines
      for (int i = 0; i < lines.size(); i++ ) {
         final ChartLine next = (ChartLine)lines.elementAt(i);
         drawPoints( g2d, bounds, next, (float)0);
      }

   }

   private void drawDataSections(
      final Graphics2D g2d, final Rectangle bounds ) {

      final int yOrig = bounds.y + bounds.height;
      final int xSpacing = bounds.width / xAxisValues.length;

      // Draw time lines
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND ) );
      g2d.setColor( HtmlColors.LIGHT_GRAY );
      for (int i = 0; i < xAxisValues.length; i++ ) {
         final int x = bounds.x + (xSpacing * i);

         if ( i != 0 ) {
            g2d.drawLine(x, yOrig - 1, x, bounds.y + 1);
         }

         final int half = xSpacing / 2;

         // Create time image map areas
         final String timeName = xAxisValues[i].name;
         Rectangle rect;
         if ( i == 0 ) {
            rect = new Rectangle(x, bounds.y, half, bounds.height - 1 );
         } else {
            rect = new Rectangle(x - half, bounds.y, xSpacing, bounds.height - 1 );
         }
         final HtmlImageMapItem timeArea = new HtmlImageMapItem( rect );

         // Create tool tip for html image map area.
         final StringBuffer tooltipText = new StringBuffer();
         for ( int l = 0; l < lines.size(); l++ ) {
            // TODO don't add line value if line dones't have y value on this x.
            final ChartLine aLine = (ChartLine)lines.elementAt(l);
            tooltipText.append(aLine.name).append(": ").append(String.valueOf(aLine.getY(i)));
            tooltipText.append(Html.TITLE_LINE_BREAK);
         }

         final BoxOverToolTip tooltip = new BoxOverToolTip(timeName, tooltipText.toString());
         timeArea.setToolTip(tooltip.toString());
         imageMapItems.add(timeArea);
      }
   }

   private void drawPoints(
      final Graphics2D g2d, final Rectangle bounds,
      final ChartLine line, final float adj) {

      final ChartLine.Point[] points = line.getPoints();

      final float xOrig = (float)bounds.x;
      final float yOrig = (float)bounds.y + (float)bounds.height;
      final float xSpacing = (float)bounds.width / (float)xAxisValues.length;
      final float ySpacing = (float)bounds.height / (float)100;

      for (int j = 0; j < points.length - 1; j++ ) {

         final ChartLine.Point point1 = points[j];
         final ChartLine.Point point2 = points[j + 1];

         // y is negative since the axis are reversed.
         final float x1 = xOrig + (point1.x * xSpacing) + adj;
         final float y1 = yOrig - (point1.y * ySpacing) + adj;
         final float x2 = xOrig + (point2.x * xSpacing) + adj;
         final float y2 = yOrig - (point2.y * ySpacing) + adj;

         // Check and account for overlapping line segments
         final LineSegment segment = new LineSegment((int)x1, (int)y1, (int)x2, (int)y2);
         final int thickness = adjustThickness( segment, line.thickness);
         drawnLineSegments.addElement(segment);

         g2d.setStroke(
            new BasicStroke(thickness, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND ) );
         g2d.setColor( line.getColor() );
         g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

//         highlight(g2d, point1, j, x1, y1, bounds.y);
//         highlight(g2d, point2, j + 1, x2, y2, bounds.y);

         // Last point
//         if (j + 1 == points.length - 1 ) {
//            g2d.drawString("'value'", x2, y2);
//         }

      }
   }


   /**
    * Can draw up to three lines on top of each other.
    * @param segment
    * @param thickness
    * @return
    */
   private int adjustThickness( final LineSegment segment, final int thickness ) {

      final int matches = numberOfMatches( segment );

      if ( matches == 1) {
         return thickness / 2;
      } else if ( matches > 1) {
         return 1;

      }

      return thickness;
   }
   private int numberOfMatches(final LineSegment search ) {
      int count = 0;
      for ( int i = 0; i < drawnLineSegments.size(); i++ ) {
         final LineSegment next = (LineSegment)drawnLineSegments.elementAt(i);
         if ( next.equals(search)) {
            count++;
         }
      }
      return count;
   }
   /**
    * a line segment. to check for overlapping lines.
    * @author brandon@sharpideas.ca
    *
    */
   private class LineSegment {
      final int x1;
      final int x2;
      final int y1;
      final int y2;

      public LineSegment(final int newx1, final int newy1, final int newx2, final int newy2 ) {
         x1 = newx1;
         y1 = newy1;
         x2 = newx2;
         y2 = newy2;
      }

      public boolean equals( final Object o ) {
         if ( o instanceof LineSegment) {
            final LineSegment other = (LineSegment)o;
            final boolean match =
               other.x1 == x1 && other.y1 == y1 && other.x2 == x2 && other.y2 == y2;

            return match;
         }
         return false;
      }

      public String toString() {
         final StringBuffer sb = new StringBuffer("Line Segment: ");
         final String space = " ";
         sb.append(x1).append(space);
         sb.append(y1).append(space);
         sb.append(x2).append(space);
         sb.append(y2).append(space);
         return sb.toString();


      }
   }

//   private int[] mids( final int size, final int parts ) {
//
//      final int[] mids = new int[parts];
//
//      if ( parts % 2 == 0 ) {
//
//         // even
//
//      } else {
//
//         //eg size = 15, parts  =5
//         //
//         // odd
//
//         final int len = parts / 2; //eg 5 -> 2
//
//         final int[] midsHalf1 = mids( len, parts);
//         System.arraycopy(midsHalf1, 0, mids, 0, len);
//
//         mids[size / 2] = size / 2;
//
//         final int[] midsHalf2 = mids( len, parts);
//         System.arraycopy(midsHalf2, 0, mids, size / 2, len);
//
//      }
//
//      return mids;
//   }

   private boolean isHighlighted( final int count ) {


      // we always display the first and last label, make sure it is highlighted
      if ( count == 0 || count == xAxisValues.length - 1) {
         return true;
      }


      final int mid = xAxisValues.length / 2;
      final int mid01 = mid / 2;
      final int mid10 = mid + mid / 2;

      // Middle
      if ( count == mid ||  count == mid01 || count == mid10 ) {
         return true;
      }

      return false;

   }

   private void highlight(
      final Graphics2D g2d, final ChartLine.Point point, final int count, final int x, final int y,
      final int bottomOfDataBounds) {

      final boolean highlight = isHighlighted( count );

      if ( highlight ) {
         drawDot( g2d, x, y);
      }
   }
   private void drawDot( final Graphics2D g2d, final int x1, final int y1) {
      final int dotDiameter = 4;
      final int radius = dotDiameter / 2;

      g2d.fill( new Ellipse2D.Double(x1 - radius, y1 - radius, dotDiameter, dotDiameter) );
   }



//   private int adjustNearSides(
//         final int loc, final int length, final int left, final int right) {
//
//      if ( loc + length > right) {
//         System.out.println("adjusting right");
//         // Too far to the right.
//         // as far right as possible
//         return right - length;
//
//      } else if ( loc < left ) {
//
//         System.out.println("adjusting left loc" + loc + " left " + left);
//         // Too far to the left,
//         // as far left as possible.
//         return left;
//
//      } else {
//         // No Adjustment needed.
//         return loc;
//
//      }
//   }

   /**
    * Get the html image map items that are a part of this chart.
    * @return
    */
   public HtmlImageMapItem[] getMapItems() {
      return (HtmlImageMapItem[])imageMapItems.toArray( new HtmlImageMapItem[imageMapItems.size()]);
   }
}
