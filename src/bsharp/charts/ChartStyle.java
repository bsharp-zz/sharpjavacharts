package bsharp.charts;

/**
 * The style attributes for a chart.
 * @author brandon@sharpideas.ca
 *
 */
public class ChartStyle {

   /** The chart height. */
   public final int height;
   /** The chart width. */
   public final int width;
   /** The style attribute for data classes in this chart. */
   public final DataClassStyle[] classStyles;

   /** Create new Style attributes for a chart. */
   public ChartStyle(
      final int chartWidth,
      final int chartHeight,
      final DataClassStyle[] chartClassStyles) {

      width = chartWidth;
      height = chartHeight;
      classStyles = chartClassStyles;
   }
}
