/*
 * Created on Jan 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sort;

import java.util.Comparator;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CusCompare implements Comparator
{
           
    public int compare(Object obj1, Object obj2)
    {
       Comparable comp1 = 
           ((ContainsSortable) obj1).getSortableElement();
       
       Comparable comp2 = 
           ((ContainsSortable) obj2).getSortableElement();
       
       return (comp1.compareTo(comp2));
    }

}
