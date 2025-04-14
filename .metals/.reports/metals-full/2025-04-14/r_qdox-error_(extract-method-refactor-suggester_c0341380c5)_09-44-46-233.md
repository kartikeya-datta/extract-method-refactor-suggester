error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2317.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2317.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2317.java
text:
```scala
transient private T@@hreadLocal perTheadNumber = new ThreadLocal() {

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.modifiers;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * Provides a counter per-thread/user or globally
 * The long value can be 
 */
public class CounterConfig extends AbstractTestElement implements Serializable, LoopIterationListener, NoThreadClone {

	public final static String START = "CounterConfig.start"; // $NON-NLS-1$

	public final static String END = "CounterConfig.end"; // $NON-NLS-1$

	public final static String INCREMENT = "CounterConfig.incr"; // $NON-NLS-1$

	private final static String FORMAT = "CounterConfig.format"; // $NON-NLS-1$

    public final static String PER_USER = "CounterConfig.per_user"; // $NON-NLS-1$

	public final static String VAR_NAME = "CounterConfig.name"; // $NON-NLS-1$

	// This class is not cloned per thread, so this is shared
	private long globalCounter = Long.MIN_VALUE;
    
    // Used for per-thread/user numbers
    private ThreadLocal perTheadNumber = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new Long(getStart());
        }
    };

	/**
	 * @see LoopIterationListener#iterationStart(LoopIterationEvent)
	 */
	public synchronized void iterationStart(LoopIterationEvent event) {
		// Cannot use getThreadContext() as not cloned per thread
		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		long start = getStart(), end = getEnd(), increment = getIncrement();
		if (!isPerUser()) {
			if (globalCounter == Long.MIN_VALUE || globalCounter > end) {
				globalCounter = start;
			}
			variables.put(getVarName(), formatNumber(globalCounter));
			globalCounter += increment;
		} else {
            long current = ((Long) perTheadNumber.get()).longValue();
            variables.put(getVarName(), formatNumber(current));
            current += increment;
            if (current > end) {
                current = start;
            }
            perTheadNumber.set(new Long(current));
		}
	}

    // Use format to create number; if it fails, use the default
    private String formatNumber(long value){
        String format = getFormat();
        if (format != null && format.length() > 0) {
            try {
                DecimalFormat myFormatter = new DecimalFormat(format);
                return myFormatter.format(value);
            } catch (NumberFormatException ignored) {
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Long.toString(value);
    }
    
	public void setStart(long start) {
		setProperty(new LongProperty(START, start));
	}

	public void setStart(String start) {
		setProperty(START, start);
	}

	public long getStart() {
		return getPropertyAsLong(START);
	}

	public void setEnd(long end) {
		setProperty(new LongProperty(END, end));
	}

	public void setEnd(String end) {
		setProperty(END, end);
	}

	public long getEnd() {
		return getPropertyAsLong(END);
	}

	public void setIncrement(long inc) {
		setProperty(new LongProperty(INCREMENT, inc));
	}

	public void setIncrement(String incr) {
		setProperty(INCREMENT, incr);
	}

	public long getIncrement() {
		return getPropertyAsLong(INCREMENT);
	}

	public void setIsPerUser(boolean isPer) {
		setProperty(new BooleanProperty(PER_USER, isPer));
	}

	public boolean isPerUser() {
		return getPropertyAsBoolean(PER_USER);
	}

	public void setVarName(String name) {
		setProperty(VAR_NAME, name);
	}

	public String getVarName() {
		return getPropertyAsString(VAR_NAME);
	}

    public void setFormat(String format) {
        setProperty(FORMAT, format);
    }

    public String getFormat() {
        return getPropertyAsString(FORMAT);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2317.java