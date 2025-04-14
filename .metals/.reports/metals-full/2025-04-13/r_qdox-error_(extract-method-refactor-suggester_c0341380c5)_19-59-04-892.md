error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17210.java
text:
```scala
r@@eturn executor.executeUpdate(query, params);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.slice.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.kernel.JDBCStoreQuery;
import org.apache.openjpa.kernel.ExpressionStoreQuery;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.OrderingMergedResultObjectProvider;
import org.apache.openjpa.kernel.QueryContext;
import org.apache.openjpa.kernel.StoreManager;
import org.apache.openjpa.kernel.StoreQuery;
import org.apache.openjpa.kernel.exps.ExpressionParser;
import org.apache.openjpa.lib.rop.MergedResultObjectProvider;
import org.apache.openjpa.lib.rop.RangeResultObjectProvider;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.StoreException;

/**
 * A query for distributed databases.
 * 
 * @author Pinaki Poddar 
 *
 */
@SuppressWarnings("serial")
class DistributedStoreQuery extends JDBCStoreQuery {
	private List<StoreQuery> _queries = new ArrayList<StoreQuery>();
	private ExpressionParser _parser;
	
	public DistributedStoreQuery(JDBCStore store, ExpressionParser parser) {
		super(store, parser);
		_parser = parser;
		
	}
	
	void add(StoreQuery q) {
		_queries.add(q);
	}
	
	public DistributedStoreManager getDistributedStore() {
		return (DistributedStoreManager)getStore();
	}
	
    public Executor newDataStoreExecutor(ClassMetaData meta, boolean subs) {
    	ParallelExecutor ex = new ParallelExecutor(this, meta, subs, _parser, 
    			ctx.getCompilation());
        for (StoreQuery q : _queries) {
            ex.addExecutor(q.newDataStoreExecutor(meta, subs));
        }
        return ex;
    }
    
    public void setContext(QueryContext ctx) {
    	super.setContext(ctx);
    	for (StoreQuery q : _queries) 
    		q.setContext(ctx); 
    }
    
    public ExecutorService getExecutorServiceInstance() {
        DistributedJDBCConfiguration conf = 
            ((DistributedJDBCConfiguration)getStore().getConfiguration());
        return conf.getExecutorServiceInstance();
    }
    
	/**
	 * Executes queries on multiple databases.
	 * 
	 * @author Pinaki Poddar 
	 *
	 */
	public static class ParallelExecutor extends 
		ExpressionStoreQuery.DataStoreExecutor {
		private List<Executor> executors = new ArrayList<Executor>();
		private DistributedStoreQuery owner = null;
		private ExecutorService threadPool = null;
		
		public void addExecutor(Executor ex) {
			executors.add(ex);
		}
		
        public ParallelExecutor(DistributedStoreQuery dsq, ClassMetaData meta, 
        		boolean subclasses, ExpressionParser parser, Object parsed) {
        	super(dsq, meta, subclasses, parser, parsed);
        	owner = dsq;
        	threadPool = dsq.getExecutorServiceInstance();
        }
        
        /**
         * Each child query must be executed with slice context and not the 
         * given query context.
         */
        public ResultObjectProvider executeQuery(StoreQuery q,
                final Object[] params, final Range range) {
        	final List<Future<ResultObjectProvider>> futures = 
        		new ArrayList<Future<ResultObjectProvider>>();
        	List<SliceStoreManager> targets = findTargets();
        	for (int i = 0; i < owner._queries.size(); i++) {
        		StoreQuery query = owner._queries.get(i);
        		StoreManager sm  = owner.getDistributedStore().getSlice(i);
        		if (!targets.contains(sm))
        			continue;
        		// if replicated, then execute only on single slice
        		if (i > 0 && containsReplicated(query.getContext()))
        			continue;
        		QueryExecutor call = new QueryExecutor();
        		call.executor = executors.get(i);
        		call.query    = query;
        		call.params   = params;
        		call.range    = range;
        		futures.add(threadPool.submit(call)); 
        	}
        	int i = 0;
        	ResultObjectProvider[] tmp = new ResultObjectProvider[futures.size()];
        	for (Future<ResultObjectProvider> future:futures) {
        		try {
					tmp[i++] = future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new StoreException(e.getCause());
				}
        	}
        	boolean[] ascending = getAscending(q);
        	boolean isAscending = ascending.length > 0;
        	boolean isAggregate = q.getContext().isAggregate();
        	boolean hasRange    = q.getContext().getEndRange() != Long.MAX_VALUE;
        	ResultObjectProvider result = null;
        	if (isAggregate) {
        	    result = new UniqueResultObjectProvider(tmp, q, 
        	            getQueryExpressions());
        	} else if (isAscending) {
        	    result = new OrderingMergedResultObjectProvider(tmp, ascending, 
                  (Executor[])executors.toArray(new Executor[executors.size()]),
                  q, params);
        	} else {
        	    result = new MergedResultObjectProvider(tmp);
        	}
        	if (hasRange)
        	    result = new RangeResultObjectProvider(result, 
        	            q.getContext().getStartRange(), 
        	            q.getContext().getEndRange());
        	return result;
        }
        
        /**
		 * Scans metadata to find out if a replicated class is the candidate.
        **/
        boolean containsReplicated(QueryContext query) {
        	Class candidate = query.getCandidateType();
        	if (candidate != null) {
        		ClassMetaData meta = query.getStoreContext().getConfiguration()
        			.getMetaDataRepositoryInstance()
        			.getMetaData(candidate, null, true);
        		if (meta != null && meta.isReplicated())
        			return true;
        	}
        	ClassMetaData[] metas = query.getAccessPathMetaDatas();
        	if (metas == null || metas.length < 1)
        		return false;
        	for (ClassMetaData type : metas)
        		if (type.isReplicated())
        			return true;
        	return false;
        }
        
        public Number executeDelete(StoreQuery q, Object[] params) {
        	Iterator<StoreQuery> qs = owner._queries.iterator();
        	final List<Future<Number>> futures = new ArrayList<Future<Number>>();
        	for (Executor ex:executors) {
        		DeleteExecutor call = new DeleteExecutor();
        		call.executor = ex;
        		call.query    = qs.next();
        		call.params   = params;
        		futures.add(threadPool.submit(call)); 
        	}
        	int N = 0;
        	for (Future<Number> future:futures) {
        		try {
            		Number n = future.get();
            		if (n != null) 
            			N += n.intValue();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new StoreException(e.getCause());
				}
        	}
        	return new Integer(N);
        }
        
        public Number executeUpdate(StoreQuery q, Object[] params) {
        	Iterator<StoreQuery> qs = owner._queries.iterator();
        	final List<Future<Number>> futures = new ArrayList<Future<Number>>();
        	for (Executor ex:executors) {
        		UpdateExecutor call = new UpdateExecutor();
        		call.executor = ex;
        		call.query    = qs.next();
        		call.params   = params;
        		futures.add(threadPool.submit(call)); 
        	}
        	int N = 0;
        	for (Future<Number> future:futures) {
        		try {
            		Number n = future.get();
            		if (n != null) 
            			N += n.intValue();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new StoreException(e.getCause());
				}
        	}
        	return new Integer(N);
        }
        
        List<SliceStoreManager> findTargets() {
        	FetchConfiguration fetch = owner.getContext().getFetchConfiguration();
        	return owner.getDistributedStore().getTargets(fetch);
        }

	}
	
	static  class QueryExecutor implements Callable<ResultObjectProvider> {
		StoreQuery query;
		Executor executor;
		Object[] params;
		Range range;
		public ResultObjectProvider call() throws Exception {
			return executor.executeQuery(query, params, range);
		}
	}
	
	static  class DeleteExecutor implements Callable<Number> {
		StoreQuery query;
		Executor executor;
		Object[] params;
		public Number call() throws Exception {
			return executor.executeDelete(query, params);
		}
	}
	
	static  class UpdateExecutor implements Callable<Number> {
		StoreQuery query;
		Executor executor;
		Object[] params;
		public Number call() throws Exception {
			return executor.executeDelete(query, params);
		}
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17210.java