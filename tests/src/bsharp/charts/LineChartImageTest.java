package bsharp.charts;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;
import bsharp.charts.BarChartImage.Style;
import bsharp.html.HtmlColors;

public class LineChartImageTest extends TestCase {

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
         new ChartData.DataGroup("group3"),
      });

      data.setDataValues( new ChartData.DataValue[] {
         new ChartData.DataValue("0", data.getDataClasses()[0], data.getDataGroups()[0]),
         new ChartData.DataValue("0", data.getDataClasses()[1], data.getDataGroups()[0]),
         new ChartData.DataValue("0", data.getDataClasses()[2], data.getDataGroups()[0]),
         
         new ChartData.DataValue("5", data.getDataClasses()[0], data.getDataGroups()[1]),
         new ChartData.DataValue("5", data.getDataClasses()[1], data.getDataGroups()[1]),
         new ChartData.DataValue("5", data.getDataClasses()[2], data.getDataGroups()[1]),
         
         new ChartData.DataValue("3", data.getDataClasses()[0], data.getDataGroups()[2]),
         new ChartData.DataValue("10", data.getDataClasses()[1], data.getDataGroups()[2]),
         new ChartData.DataValue("20", data.getDataClasses()[2], data.getDataGroups()[2]),
      });

      final DataClassStyle[] classStyles = new DataClassStyle[] {
         new DataClassStyle(HtmlColors.LIGHT_YELLOW, HtmlColors.MED_RED_1, 1 ),
         new DataClassStyle(HtmlColors.BLUE, HtmlColors.BLUE, 1 ),
         new DataClassStyle(HtmlColors.LIGHT_GREEN, Color.green, 1 ),
      };

      final LineChartImage img =
         new LineChartImage( data, "TestLineChart" , 600, 500);

      ChartLine[] lines = (ChartLine[]) img.getLines().toArray(
            new ChartLine[img.getLines().size()] );
      
      for (int i = 0; i < lines.length; i++) {
         lines[i].setColor(classStyles[i].lineColor);
         lines[i].thickness = 10;
      }
      
      img.draw(new File("TestLineChart.png"), "png");

      System.out.println("DONE.");
   }

}
