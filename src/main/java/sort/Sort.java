/*
 * Created on Jan 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sort;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Sort
{
    static public Vector sort (Vector vecIn)
    {
       CusCompare cusCompare = new CusCompare ();
       Collections.sort(vecIn, cusCompare);
       
        return (vecIn);
    }

    static public Vector sortDEAD (Vector vecIn)
    {
       int nEls = vecIn.size ();
       Hashtable hashSort = new Hashtable ();
       Vector vecIndex = new Vector ();
    
       int k=0;
    
       Object indexObj = null;
       ContainsSortable sortObj = null;
    
       for (k=0; k<nEls; k++)
       {
         sortObj = (ContainsSortable) vecIn.elementAt(k);
         indexObj = sortObj.getSortableElement();
         vecIndex.add(indexObj);
         
         hashSort.put(indexObj, sortObj);
       }
    
       Collections.sort(vecIndex);
    
       vecIn.clear();
    
       for (k=0; k<nEls; k++)
       {
           indexObj = vecIndex.elementAt(k);
           
           sortObj = (ContainsSortable) hashSort.get(indexObj);
           
           vecIn.add(sortObj);
       }
    
       return (vecIn);
    }
}
