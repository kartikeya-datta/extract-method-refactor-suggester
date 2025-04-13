error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3620.java
text:
```scala
C@@lose the conglomerate controller.

/*

   Derby - Class org.apache.derby.impl.store.access.btree.index.B2IController

   Copyright 1997, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.store.access.btree.index;

import java.io.IOException;
import java.util.Properties;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.store.access.conglomerate.Conglomerate;
import org.apache.derby.iapi.store.access.conglomerate.TransactionManager;
import org.apache.derby.iapi.store.access.ConglomerateController;
import org.apache.derby.iapi.store.access.DynamicCompiledOpenConglomInfo;
import org.apache.derby.iapi.store.access.Qualifier;
import org.apache.derby.iapi.types.RowLocation;
import org.apache.derby.iapi.store.access.ScanController;
import org.apache.derby.iapi.store.access.TransactionController;
import org.apache.derby.iapi.store.access.ConglomerateController;
import org.apache.derby.iapi.store.raw.ContainerHandle;
import org.apache.derby.iapi.store.raw.LockingPolicy;
import org.apache.derby.iapi.store.raw.Transaction;

import org.apache.derby.iapi.types.DataValueDescriptor;

import org.apache.derby.impl.store.access.btree.BTreeController;
import org.apache.derby.impl.store.access.btree.BTreeLockingPolicy;

import org.apache.derby.impl.store.access.conglomerate.ConglomerateUtil;

import org.apache.derby.iapi.services.io.FormatableBitSet;

/**

  A B2I controller object is the concrete class which corresponds to an open
  b-tree secondary index.

**/

public class B2IController extends BTreeController
{

	/*
	** Fields of B2IController.
	*/
    private ConglomerateController  base_cc_for_locking;

	/*
	** Methods of B2IController.
	*/

	B2IController()
	{
		// Perform the generic b-tree construction.
		super();
	}

	void init(
    TransactionManager              xact_manager,
    Transaction                     rawtran, 
    int                             open_mode,
    int                             lock_level,
    LockingPolicy                   locking_policy,
    boolean                         get_locks,
    B2I                             conglomerate,
    B2IUndo                         undo,
    B2IStaticCompiledInfo           static_info,
    DynamicCompiledOpenConglomInfo  dynamic_info)
		throws StandardException
	{
        // open and lock the base table.

        int base_open_mode = 
            open_mode | TransactionController.OPENMODE_FOR_LOCK_ONLY;

        // open the base conglomerate - just to get the lock.  Since btree
        // controllers only support update operations we just hard code 
        // the TransactionController.ISOLATION_REPEATABLE_READ, which is only
        // used for geting the IX intent lock on the table.
        if (static_info != null)
        {
            base_cc_for_locking  = 
                xact_manager.openCompiledConglomerate(
                    false,
                    base_open_mode, lock_level,
                    TransactionController.ISOLATION_REPEATABLE_READ,
                    static_info.base_table_static_info, 
                    /* TODO - maintain a dynamic info for this */
                    ((Conglomerate) static_info.getConglom()).
                        getDynamicCompiledConglomInfo(
                            conglomerate.baseConglomerateId));
        }
        else
        {
            base_cc_for_locking  = 
                xact_manager.openConglomerate(
                    conglomerate.baseConglomerateId,
                    false,
                    base_open_mode, lock_level,
                    TransactionController.ISOLATION_REPEATABLE_READ);
        }
        
        BTreeLockingPolicy b2i_locking_policy;
        if (lock_level == TransactionController.MODE_TABLE)
        {
            b2i_locking_policy = 
                new B2ITableLocking3(
                    rawtran, lock_level, locking_policy, 
                    base_cc_for_locking, this);
        }
        else if (lock_level == TransactionController.MODE_RECORD)
        {
            b2i_locking_policy = 
                new B2IRowLocking3(
                    rawtran, lock_level, locking_policy, 
                    base_cc_for_locking, this);
        }
        else
        {
            if (SanityManager.DEBUG)
            {
                SanityManager.THROWASSERT("Bad lock level: " + lock_level);
            }
            b2i_locking_policy = null;
        }

		// Do generic b-tree initialization.
		super.init(
            xact_manager, 
            (ContainerHandle) null, 
            rawtran, 
            open_mode,
            lock_level,
            b2i_locking_policy,
            conglomerate, 
            undo,
            static_info,
            dynamic_info);

        if (SanityManager.DEBUG)
            SanityManager.ASSERT(conglomerate != null);
	}

	/*
	** Methods of ConglomerateController.
	*/

    /**
    Close the conglomerate controller
	<p>
	Any changes to this method will probably have to be reflected in close as 
    well.
	<p>
	Currently delegates to OpenBTree.  If the btree controller ends up not 
    having any state of its own, we can remove this method (the VM will 
    dispatch to OpenBTree), gaining some small efficiency.  For now, this 
    method remains for clarity.  

	@see ConglomerateController#close
    **/
    public void close()
        throws StandardException
	{
		super.close();

        if (base_cc_for_locking != null)
        {
            base_cc_for_locking.close();
            base_cc_for_locking = null;
        }
	}


	/**
    Insert a row into the conglomerate.
	@see ConglomerateController#insert

    @exception StandardException Standard exception policy.
    **/
    public int insert(DataValueDescriptor[] row)
		throws StandardException
	{
        if (SanityManager.DEBUG)
        {
            if (this.container != null)
            {
                SanityManager.ASSERT(this.getConglomerate() instanceof B2I);

                RowLocation rowloc = (RowLocation)
                    row[((B2I)(this.getConglomerate())).rowLocationColumn];

                SanityManager.ASSERT(
                    !rowloc.isNull(), "RowLocation value is null");
            }
        }

        return(super.insert(row));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3620.java