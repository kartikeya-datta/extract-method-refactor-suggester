error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8585.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8585.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8585.java
text:
```scala
&@@& it.getName().trim().equalsIgnoreCase(name.trim()))

package org.tigris.scarab.om;

import java.util.List;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.om.ObjectKey;

// Local classes
import org.tigris.scarab.services.cache.ScarabCache;


/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class IssueTypePeer 
    extends org.tigris.scarab.om.BaseIssueTypePeer
{
    public static Integer ROOT_KEY = new Integer(0);

    private static final String ISSUE_TYPE_PEER = 
        "IssueTypePeer";
    private static final String GET_ALL_ISSUE_TYPES = 
        "getAllIssueTypes";

    private static final String RETRIEVE_BY_PK = 
        "retrieveByPK";

    /** 
     * Retrieve a single object by pk
     * FIXME: is this method implementation (with the caching) still done this way? -jss
     * @param pk
     */
    public static IssueType retrieveByPK(ObjectKey pk)
        throws TorqueException
    {
        IssueType result = null;
        Object obj = ScarabCache.get(ISSUE_TYPE_PEER, RETRIEVE_BY_PK, pk); 
        if (obj == null) 
        {        
            result = BaseIssueTypePeer.retrieveByPK(pk);
            ScarabCache.put(result, ISSUE_TYPE_PEER, RETRIEVE_BY_PK, pk);
        }
        else 
        {
            result = (IssueType)obj;
        }
        return result;
    }

    /**
     *  Gets a List of all of the Issue types in the database,
     *  That are not template types.
     */
    public static List getAllIssueTypes(boolean deleted,
                       String sortColumn, String sortPolarity)
        throws Exception
    {
        List result = null;
        Boolean b = deleted ? Boolean.TRUE : Boolean.FALSE;
        Object obj = ScarabCache.get(ISSUE_TYPE_PEER, GET_ALL_ISSUE_TYPES, b); 
        if (obj == null) 
        {        
            Criteria c = new Criteria();
            c.add(IssueTypePeer.PARENT_ID, 0);
            c.add(IssueTypePeer.ISSUE_TYPE_ID, 0, Criteria.NOT_EQUAL);
            if (deleted)
            {
                c.add(IssueTypePeer.DELETED, 1);
            }
            else
            {
                c.add(IssueTypePeer.DELETED, 0);
            }
            if (sortColumn != null && sortColumn.equals("desc"))
            {
                addSortOrder(c, IssueTypePeer.DESCRIPTION, 
                             sortPolarity);
            }
            else
            {
                // sort on name
                addSortOrder(c, IssueTypePeer.NAME, 
                             sortPolarity);
            }
            result = doSelect(c);
            ScarabCache.put(result, ISSUE_TYPE_PEER, GET_ALL_ISSUE_TYPES, b);
        }
        else 
        {
            result = (List)obj;
        }
        return result;
    }

    public static List getAllIssueTypes(boolean includeDeleted)
        throws Exception
    {
        return getAllIssueTypes(includeDeleted, "name", "asc");
    } 

    public static List getDefaultIssueTypes()
        throws Exception
    {
        Criteria c = new Criteria();
        c.add(IssueTypePeer.PARENT_ID, 0);
        c.add(IssueTypePeer.DELETED, 0);
        c.add(IssueTypePeer.ISDEFAULT, 1);
        c.add(IssueTypePeer.ISSUE_TYPE_ID, 0, Criteria.NOT_EQUAL);
        return IssueTypePeer.doSelect(c);
    }

    private static Criteria addSortOrder(Criteria crit, 
                    String sortColumn, String sortPolarity)
    {
        if (sortPolarity != null && sortPolarity.equals("desc"))
        {
            crit.addDescendingOrderByColumn(sortColumn);
        }
        else
        {
            crit.addAscendingOrderByColumn(sortColumn);
        }
        return crit;
    }

    /**
     * Checks to see if the name already exists an issue type.  if one
     * does unique will be false unless the given id matches the issue type
     * that already has the given name.
     *
     * @param name a <code>String</code> value
     * @param id an <code>ObjectKey</code> value
     * @return a <code>boolean</code> value
     * @exception Exception if an error occurs
     */
    public static boolean isUnique(String name, Integer id)
        throws Exception
    {
        boolean unique = true;
        Criteria crit = new Criteria().add(IssueTypePeer.NAME, name.trim());
        crit.setIgnoreCase(true);
        List types = IssueTypePeer.doSelect(crit);
        if (types.size() > 0) 
        {
            for (int i =0; i<types.size();i++)
            {
                IssueType it = (IssueType)types.get(i);
                if (id != null && !it.getIssueTypeId().equals(id)
                    && it.getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
                {
                    unique = false;
                }
            }
        }
        return unique;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8585.java