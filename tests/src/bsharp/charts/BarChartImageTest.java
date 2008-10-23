package bsharp.charts;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;
import bsharp.charts.BarChartImage.Style;
import bsharp.html.HtmlColors;

public class BarChartImageTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testBarChartImage() {
      final ChartData data = new ChartData();

      data.setDataClasses( new ChartData.DataClass[] {
         new ChartData.DataClass("metric1"),
         new ChartData.DataClass("metric2"),
         new ChartData.DataClass("metric3"),
      });

      data.setDataGroups( new ChartData.DataGroup[] {
         new ChartData.DataGroup("group1"),
         new ChartData.DataGroup("group2"),
      });

      data.setDataValues( new ChartData.DataValue[] {
         new ChartData.DataValue("50", data.getDataClasses()[0], data.getDataGroups()[0]),
         new ChartData.DataValue("40", data.getDataClasses()[1], data.getDataGroups()[0]),
         new ChartData.DataValue("1", data.getDataClasses()[2], data.getDataGroups()[0]),
         new ChartData.DataValue("20", data.getDataClasses()[0], data.getDataGroups()[1]),
         new ChartData.DataValue("80", data.getDataClasses()[1], data.getDataGroups()[1]),
         new ChartData.DataValue("15", data.getDataClasses()[2], data.getDataGroups()[1]),
      });

      final DataClassStyle[] classStyles = new DataClassStyle[] {
         new DataClassStyle(HtmlColors.LIGHT_YELLOW, HtmlColors.MED_RED_1, 1 ),
         new DataClassStyle(HtmlColors.ORANGE_YELLOW, HtmlColors.LIGHT_RED, 1 ),
         new DataClassStyle(HtmlColors.LIGHT_GREEN, Color.green, 1 ),
      };

      final BarChartImage img =
         new BarChartImage( data, new Style(600, 500, classStyles, 20, true));

      img.draw(new File("TestBarChart.png"), "png");

      System.out.println("DONE.");
   }

   public void testDraw() {
      fail("Not yet implemented");
   }

}
