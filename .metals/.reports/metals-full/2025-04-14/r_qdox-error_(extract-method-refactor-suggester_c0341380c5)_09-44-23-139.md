error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7980.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7980.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7980.java
text:
```scala
Q@@ueryPeer.SCOPE_ID, Scope.MODULE__PK,

package org.tigris.scarab.om;

/* ================================================================
 * Copyright (c) 2000-2002 CollabNet.  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement: "This product includes
 * software developed by Collab.Net <http://www.Collab.Net/>."
 * Alternately, this acknowlegement may appear in the software itself, if
 * and wherever such third-party acknowlegements normally appear.
 * 
 * 4. The hosted project names must not be used to endorse or promote
 * products derived from this software without prior written
 * permission. For written permission, please contact info@collab.net.
 * 
 * 5. Products derived from this software may not use the "Tigris" or 
 * "Scarab" names nor may "Tigris" or "Scarab" appear in their names without 
 * prior written permission of Collab.Net.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL COLLAB.NET OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * 
 * This software consists of voluntary contributions made by many
 * individuals on behalf of Collab.Net.
 */ 

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
public class QueryPeer 
    extends org.tigris.scarab.om.BaseQueryPeer
{

    public static final String GET_ALL_QUERIES = 
        "getAllQueries";
    public static final String GET_PRIVATE_QUERIES = 
        "getPrivateQueries";
    public static final String GET_GLOBAL_QUERIES = 
        "getGlobalQueries";

    /**
     * List of private queries associated with this module.
     * Created by this user.
     */
    public static List getQueries(Module me, IssueType issueType,
                                  ScarabUser user, String sortColumn,   
                                  String sortPolarity, String type)
        throws Exception
    {
        List queries = null;
        String cacheKey = "all";
        if (type.equals("private"))
        {
            cacheKey = GET_PRIVATE_QUERIES;
        }
        else if (type.equals("global"))
        {
            cacheKey = GET_GLOBAL_QUERIES;  
        }
      
        Object obj = ScarabCache.get("QueryPeer", GET_ALL_QUERIES, 
                                     user, issueType); 

        if (me == null || issueType == null)
        {
            queries = new ArrayList();
        }
        else if ( obj == null ) 
        {
            Criteria crit = new Criteria()
                .add(QueryPeer.MODULE_ID, me.getModuleId())
                .add(QueryPeer.ISSUE_TYPE_ID, issueType.getIssueTypeId())
                .add(QueryPeer.DELETED, 0);

            Criteria.Criterion cGlob = crit.getNewCriterion(
                QueryPeer.SCOPE_ID, Scope.GLOBAL__PK, 
                Criteria.EQUAL);
            Criteria.Criterion cPriv1 = crit.getNewCriterion(
                QueryPeer.USER_ID, user.getUserId(), Criteria.EQUAL);
            Criteria.Criterion cPriv2 = crit.getNewCriterion(
                QueryPeer.SCOPE_ID, Scope.PERSONAL__PK, 
                Criteria.EQUAL);
            cPriv1.and(cPriv2);
            if (type.equals("private"))
            {
                crit.add(cPriv1);
            }
            else if (type.equals("global"))
            {
                crit.add(cGlob);
            }
            else
            {
                // All queries
                cGlob.or(cPriv1);
                crit.add(cGlob);
            }
            crit.setDistinct();

            // Add sort criteria
            if (sortColumn.equals("desc"))
            {
                addSortOrder(crit, QueryPeer.DESCRIPTION, 
                             sortPolarity);
            }
            else if (sortColumn.equals("avail"))
            {
                crit.addJoin(QueryPeer.SCOPE_ID,
                             ScopePeer.SCOPE_ID);
                addSortOrder(crit, ScopePeer.SCOPE_NAME, sortPolarity);
            }
            else if (sortColumn.equals("user"))
            {
                addSortOrder(crit, QueryPeer.USER_ID, sortPolarity);
            }
            else
            {
                // sort by name
                addSortOrder(crit, QueryPeer.NAME, sortPolarity);
            }
            queries = QueryPeer.doSelect(crit);
            ScarabCache.put(queries, "QueryPeer", 
                            cacheKey, user, issueType);
        }
        else 
        {
            queries = (List)obj;
        }
        return queries;
    }

    public static List getQueries(Module me, IssueType issueType,
                                     ScarabUser user)
        throws Exception
    {
        return getQueries(me, issueType, user, "avail", "asc", "all");
    }

    public static List getQueries(Module me, IssueType issueType,
                                     ScarabUser user, String sortColumn,   
                                     String sortPolarity)
        throws Exception
    {
        return getQueries(me, issueType, user, sortColumn, 
                          sortPolarity, "all");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7980.java