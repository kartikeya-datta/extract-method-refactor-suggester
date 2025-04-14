error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4417.java
text:
```scala
I@@ssueTemplateInfoPeer.SCOPE_ID, Scope.MODULE__PK,

package org.tigris.scarab.om;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.torque.util.Criteria;
import org.tigris.scarab.services.cache.ScarabCache;

// Local classes
import org.tigris.scarab.om.Module;

/** 
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class IssueTemplateInfoPeer 
    extends org.tigris.scarab.om.BaseIssueTemplateInfoPeer
{

    private static final String GET_ALL_TEMPLATES = 
        "getAllTemplates";

    /**
     * List of Issue Template objects associated with this module.
     * And issue type.
     */
    public static List getAllTemplates(Module me, IssueType issueType,
                                ScarabUser user, 
                                String sortColumn, String sortPolarity)
        throws Exception
    {
        List templates = null;
        Object obj = ScarabCache.get("IssueTemplateInfoPeer",GET_ALL_TEMPLATES,                                     user, issueType); 
        if ( obj == null ) 
        {        
            Criteria crit = new Criteria()
                .add(IssuePeer.MODULE_ID, me.getModuleId())
                .add(IssuePeer.DELETED, 0)
                .addJoin(TransactionPeer.TRANSACTION_ID, 
                         ActivityPeer.TRANSACTION_ID) 
                .addJoin(IssuePeer.ISSUE_ID, 
                         ActivityPeer.ISSUE_ID) 
                .add(IssuePeer.TYPE_ID, issueType.getTemplateId())
                .addJoin(IssueTemplateInfoPeer.ISSUE_ID,
                         IssuePeer.ISSUE_ID);
            crit.setDistinct();

            Criteria.Criterion cPriv1 = crit.getNewCriterion(
                IssueTemplateInfoPeer.SCOPE_ID, Scope.PERSONAL__PK, 
                Criteria.EQUAL);
            cPriv1.and( crit.getNewCriterion(TransactionPeer.CREATED_BY, 
                user.getUserId(),  Criteria.EQUAL));
            Criteria.Criterion cGlob = crit.getNewCriterion(
                IssueTemplateInfoPeer.SCOPE_ID, Scope.GLOBAL__PK,
                Criteria.EQUAL);
            cGlob.or(cPriv1);
            crit.add(cGlob);

            // Add sort criteria
            if (sortColumn.equals("desc"))
            {
                addSortOrder(crit, IssueTemplateInfoPeer.DESCRIPTION, 
                             sortPolarity);
            }
            else if (sortColumn.equals("avail"))
            {
                crit.addJoin(IssueTemplateInfoPeer.SCOPE_ID,
                             ScopePeer.SCOPE_ID);
                addSortOrder(crit, ScopePeer.SCOPE_NAME, sortPolarity);
            }
            else if (!sortColumn.equals("user"))
            {
                // sort by name
                addSortOrder(crit, IssueTemplateInfoPeer.NAME, sortPolarity);
            }
            templates = IssuePeer.doSelect(crit);
            ScarabCache.put(templates, "IssueTemplateInfoPeer", 
                            GET_ALL_TEMPLATES, user, issueType);
        }
        else 
        {
            templates = (List)obj;
        }
        if (sortColumn.equals("user"))
        {
            templates = sortByCreatingUser(templates, sortPolarity);
        }
        return templates;
    }

    private static Criteria addSortOrder(Criteria crit, 
                       String sortColumn, String sortPolarity)
    {
        if (sortPolarity.equals("desc"))
        {
            crit.addDescendingOrderByColumn(sortColumn);
        }
        else
        {
            crit.addAscendingOrderByColumn(sortColumn);
        }
        return crit;
    }

    private static List sortByCreatingUser(List result,
                                           String sortPolarity)
        throws Exception
    {
        final int polarity = ("asc".equals(sortPolarity)) ? 1 : -1;   
        Comparator c = new Comparator() 
        {
            public int compare(Object o1, Object o2) 
            {
                int i = 0;
                try
                {
                    i = polarity * 
                        ((Issue)o1).getCreatedBy().getFirstName()
                         .compareTo(((Issue)o2).getCreatedBy().getFirstName());
                }
                catch (Exception e)
                {
                    //
                }
                return i;
             }
        };
        Collections.sort(result, c);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4417.java