error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6247.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6247.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6247.java
text:
```scala
a@@ddAttributeValue(aval);

package org.tigris.scarab.om;

// JDK classes
import java.util.*;
import java.sql.Connection;

// Turbine classes
import org.apache.turbine.om.*;
import org.apache.turbine.om.peer.BasePeer;
import org.apache.turbine.util.db.Criteria;
import org.apache.turbine.util.ObjectUtils;
import org.apache.turbine.services.resources.TurbineResources;
import org.apache.turbine.util.StringUtils;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.ParameterParser;
import org.apache.turbine.util.Log;
import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.db.pool.DBConnection;
import org.apache.turbine.util.db.map.DatabaseMap;

import org.tigris.scarab.util.ScarabConstants;

/** 
  * The skeleton for this class was autogenerated by Torque on:
  *
  * [Wed Feb 28 16:36:26 PST 2001]
  *
  * You should add additional methods to this class to meet the
  * application requirements.  This class will only be generated as
  * long as it does not already exist in the output directory.

  */
public class Issue 
    extends BaseIssue
    implements Persistent
{

    public String getUniqueId() throws Exception
    {
        return getIdPrefix() + getIdCount();
    }

    /**
     * AttributeValues that are relevant to the issue's current module.
     * Empty AttributeValues that are relevant for the module, but have 
     * not been set for the issue are included.
     */
    public HashMap getModuleAttributeValuesMap() throws Exception
    {
        Criteria crit = new Criteria(2)
            .add(RModuleAttributePeer.DELETED, false);
        
        Attribute[] attributes = null;
        HashMap siaValuesMap = null;
        // this exception is getting lost 
        try{
        attributes = getModule().getAttributes(crit);
        siaValuesMap = getAttributeValuesMap();
        }catch (Exception e){e.printStackTrace();}

        HashMap map = new HashMap( (int)(1.25*attributes.length + 1) );

        for ( int i=0; i<attributes.length; i++ ) 
        {
            String key = attributes[i].getName().toUpperCase();

            if ( siaValuesMap.containsKey(key) ) 
            {
                map.put( key, siaValuesMap.get(key) );
            }
            else 
            {
                AttributeValue aval = AttributeValue
                    .getNewInstance(attributes[i], this);
                addAttributeValues(aval);
                map.put( key, aval );
            }
        }
        
        return map;
    }


    /**
     * AttributeValues in the order that is preferred for this module
     */
    public AttributeValue[] getOrderedModuleAttributeValues() throws Exception
    {
        
        Map values = getModuleAttributeValuesMap();

        Criteria crit = new Criteria(3)
            .add(RModuleAttributePeer.DELETED, false)
            .addOrderByColumn(RModuleAttributePeer.PREFERRED_ORDER);
        Attribute[] attributes = getModule().getAttributes(crit);

        return orderAttributeValues(values, attributes);
    }

    /**
     * AttributeValues that are set for this Issue in the order
     * that is preferred for this module
     */
    public AttributeValue[] getOrderedAttributeValues() throws Exception
    {        
        Map values = getAttributeValuesMap();

        Criteria crit = new Criteria(3)
            .add(RModuleAttributePeer.DELETED, false)
            .addOrderByColumn(RModuleAttributePeer.PREFERRED_ORDER);
        Attribute[] attributes = getModule().getAttributes(crit);

        return orderAttributeValues(values, attributes);
    }


    /**
     * Extract the AttributeValues from the Map according to the 
     * order in the Attribute[]
     */
    private AttributeValue[] orderAttributeValues(Map values, 
                                                  Attribute[] attributes) 
        throws Exception
    {
        AttributeValue[] orderedValues = new AttributeValue[values.size()];
        try{

        int i=0;
        for ( int j=0; j<attributes.length; j++ ) 
        {
            AttributeValue av = (AttributeValue) values
                .remove( attributes[j].getName().toUpperCase() );
            if ( av != null ) 
            {
                orderedValues[i++] = av;                
            }
        }
        Iterator iter = values.values().iterator();
        while ( iter.hasNext() ) 
        {
            orderedValues[i++] = (AttributeValue)iter.next();
        }

        }catch (Exception e){e.printStackTrace();}

        for ( int j=0; j<orderedValues.length; j++ ) 
        {
            
        }
        return orderedValues;
    }



    /**
     * AttributeValues that are set for this Issue
     */
    public HashMap getAttributeValuesMap() throws Exception
    {
        Criteria crit = new Criteria(2)
            .add(AttributeValuePeer.DELETED, false);        
        List siaValues = getAttributeValues(crit);
        HashMap map = new HashMap( (int)(1.25*siaValues.size() + 1) );
        for ( int i=0; i<siaValues.size(); i++ ) 
        {
            AttributeValue att = (AttributeValue) siaValues.get(i);
            String name = att.getAttribute().getName();
            map.put(name.toUpperCase(), att);
        }

        return map;
    }

    /**
     * AttributeValues that are set for this issue and
     * Empty AttributeValues that are relevant for the module, but have 
     * not been set for the issue are included.
     */
    public HashMap getAllAttributeValuesMap() throws Exception
    {
        Map moduleAtts = getModuleAttributeValuesMap();
        Map issueAtts = getAttributeValuesMap();
        HashMap allValuesMap = new HashMap( (int)(1.25*(moduleAtts.size() + 
                                            issueAtts.size())+1) );

        allValuesMap.putAll(moduleAtts);
        allValuesMap.putAll(issueAtts);
        return allValuesMap;
    }


    public boolean containsMinimumAttributeValues()
        throws Exception
    {
        Criteria crit = new Criteria(3)
            .add(RModuleAttributePeer.DELETED, false)        
            .add(RModuleAttributePeer.REQUIRED, true);        
        Attribute[] attributes = getModule().getAttributes(crit);
        //        Vector moduleAttributes = 
        //    getModule().getRModuleAttributes(crit);
        
        boolean result = true;
        Iterator i = getModuleAttributeValuesMap()
            .values().iterator();
        while (i.hasNext()) 
        {
            AttributeValue aval = (AttributeValue)i.next();
            if ( aval.getOptionId() == null && aval.getValue() == null ) 
            {
                for ( int j=attributes.length-1; j>=0; j-- ) 
                {
                    if ( aval.getAttribute().getPrimaryKey().equals(
                         attributes[j].getPrimaryKey() )) 
                    {
                        result = false;
                        break;
                    }                    
                }

                break;
            }
        }

        return result;
    }       

    public void save()
        throws Exception
    {
        // remove unset AttributeValues before saving
        Criteria crit = new Criteria(2)
            .add(AttributeValuePeer.DELETED, false);        
        List attValues = getAttributeValues(crit);
        for ( int i=0; i<attValues.size(); i++ ) 
        {
            AttributeValue attVal = (AttributeValue) attValues.get(i);
            if ( attVal.getOptionId() == null && attVal.getValue() == null ) 
            {
                attValues.remove(i);
            }
        }

        // set the issue id
        if ( isNew() ) 
        {
            String prefix = getModule().getCode();

            /* thinking of keeping this in separate column
            String instanceCode = TurbineResources
                .getString(ScarabConstants.INSTANCE_NAME);
            if ( instanceCode != null && instanceCode.length() > 0 ) 
            {
                prefix = instanceCode + "-" + prefix;
            }
            */

            DatabaseMap dbMap = IssuePeer.getTableMap().getDatabaseMap();
            DBConnection dbCon = null;
            int numId = -1; 
            try
            {
                dbCon = TurbineDB.getConnection( dbMap.getName() );
                Connection con = dbCon.getConnection();
                numId = dbMap.getIDBroker().getIdAsInt(con, prefix);
            }
            finally
            {
                TurbineDB.releaseConnection(dbCon);
            }

            setIdPrefix(prefix);
            setIdCount(numId);
            
        }
        

        super.save();
    }       

    
    /**
     * Performs a search over an issue's attribute values.
     *
     * @param keywords a <code>String[]</code> value
     * @param useAnd, an AND search if true, otherwise OR
     * @return a <code>List</code> value
     */
    public static List searchKeywords(String[] keywords, boolean useAnd)
        throws Exception
    {
        Criteria c = new Criteria(0);
        return IssuePeer.doSelect(c);
    }

}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6247.java