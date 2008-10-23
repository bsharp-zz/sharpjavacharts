package bsharp.charts;

import java.util.Vector;

/**
 * Holds all the data in a chart.
 * @author brandon@sharpideas.ca
 *
 */
public class ChartData {

//   /** The data groups in this data set. */
//   private DataGroup[] dataGroups;
//   /** The data classes in this data set. */
//   private DataClass[] dataClasses;
//   /** The data values in this data set. */
//   private DataValue[] dataValues;

   private Vector dataGroupsV = new Vector();
   private Vector dataClassesV = new Vector();
   private Vector dataValuesV = new Vector();

   public DataGroup[] getDataGroups() {
      return (DataGroup[])dataGroupsV.toArray(new DataGroup[dataGroupsV.size()]);
   }
   public DataClass[] getDataClasses() {
      return (DataClass[])dataClassesV.toArray(new DataClass[dataClassesV.size()]);
   }
   public DataValue[] getDataValues() {
      return (DataValue[])dataValuesV.toArray(new DataValue[dataValuesV.size()]);
   }

   public void setDataGroups(final DataGroup[] newValues) {
      dataGroupsV = toVector( newValues);
   }
   public void setDataClasses(final DataClass[] newValues) {
      dataClassesV = toVector( newValues);
   }
   public void setDataValues(final DataValue[] newValues) {
      dataValuesV = toVector( newValues);
   }

   private Vector toVector( Object[] array ) {
      final Vector result = new Vector();
      for ( int i = 0; i < array.length; i++ ) {
         result.addElement( array[i]);
      }
      return result;
   }
   
   
   public Vector getDataClassesVector() {
      return dataClassesV;
   }
   public Vector getDataGroupsVector() {
      return dataGroupsV;
   }

   public DataGroup addDataGroup(final String name) {
      final DataGroup newGroup = new DataGroup(name);
      dataGroupsV.addElement( newGroup );
//      dataGroups = (DataGroup[])dataGroupsV.toArray(new DataGroup[dataGroupsV.size()]);

      return newGroup;
   }
   public void addDataValue(final String value, final DataClass dataClassRef, final DataGroup dataGroupRef) {

      dataValuesV.addElement( new DataValue(value, dataClassRef, dataGroupRef) );
//      dataValues = (DataValue[])dataValuesV.toArray(new DataValue[dataValuesV.size()]);
   }
   /**
    * A grouping of data classes.
    * @author brandon@sharpideas.ca
    *
    */
   public static class DataGroup {
      /** The name of the group. */
      public final String name;
      public DataGroup( final String groupName ) {
         name = groupName;
      }
   }

   /**
    * A class of data.
    * @author brandon@sharpideas.ca
    *
    */
   public static class DataClass {

      /** The name of the class. */
      public final String name;
      public DataClass( final String className ) {
         name = className;
      }
      public boolean equals( final Object o ) {
         if ( o instanceof DataClass ) {
            return ((DataClass)o).name.equals(name);
         }
         return false;
      }


   }

   /**
    * A data value entry.
    * @author brandon@sharpideas.ca
    *
    */
   public static class DataValue {

      /** The class this data belongs to. */
      public final DataClass dataClass;
      /** The group this data belongs to. */
      public final DataGroup dataGroup;
      /** The value of this data entry. */
      public final String value;

      /**
       * Create a new data value.
       * @param dataValue The value of this data entry.
       * @param dataClass The class this data belongs to.
       * @param dataGroup The group this data belongs to.
       */
      public DataValue(
         final String dataValue, final DataClass dataClassRef, final DataGroup dataGroupRef ) {

         this.dataClass = dataClassRef;
         this.dataGroup = dataGroupRef;
         value = dataValue;
      }

   }
}
