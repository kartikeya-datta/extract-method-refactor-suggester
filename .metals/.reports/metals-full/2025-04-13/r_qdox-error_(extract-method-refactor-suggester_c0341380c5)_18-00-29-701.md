error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3263.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3263.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3263.java
text:
```scala
r@@eturn ((Class) functions.get(functionName)).newInstance();

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.engine.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.functions.Function;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

/**
 * CompoundFunction.
 * 
 * @author mstover
 * @version $Id$
 */
public class CompoundVariable implements Function {
	transient private static Logger log = LoggingManager.getLoggerForClass();

	private String rawParameters;

	static FunctionParser functionParser = new FunctionParser();

	static Map functions = new HashMap();

	private boolean hasFunction, isDynamic;

	private String permanentResults = "";

	LinkedList compiledComponents = new LinkedList();

	static {
		try {
			List classes = ClassFinder.findClassesThatExtend(JMeterUtils.getSearchPaths(),
					new Class[] { Function.class }, true);
			Iterator iter = classes.iterator();
			while (iter.hasNext()) {
				Function tempFunc = (Function) Class.forName((String) iter.next()).newInstance();
				functions.put(tempFunc.getReferenceKey(), tempFunc.getClass());
			}
		} catch (Exception err) {
			log.error("", err);
		}
	}

	public CompoundVariable() {
		super();
		isDynamic = true;
		hasFunction = false;
	}

	public CompoundVariable(String parameters) {
		this();
		try {
			setParameters(parameters);
		} catch (InvalidVariableException e) {
		}
	}

	public String execute() {
		if (isDynamic) {
			JMeterContext context = JMeterContextService.getContext();
			SampleResult previousResult = context.getPreviousResult();
			Sampler currentSampler = context.getCurrentSampler();
			return execute(previousResult, currentSampler);
		} else {
			return permanentResults;
		}
	}

	/**
	 * Allows the retrieval of the original String prior to it being compiled.
	 * 
	 * @return String
	 */
	public String getRawParameters() {
		return rawParameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Function#execute(SampleResult, Sampler)
	 */
	public String execute(SampleResult previousResult, Sampler currentSampler) {
		if (compiledComponents == null || compiledComponents.size() == 0) {
			return "";
		}
		boolean testDynamic = false;
		StringBuffer results = new StringBuffer();
		Iterator iter = compiledComponents.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof Function) {
				testDynamic = true;
				try {
					results.append(((Function) item).execute(previousResult, currentSampler));
				} catch (InvalidVariableException e) {
				}
			} else if (item instanceof SimpleVariable) {
				testDynamic = true;
				results.append(((SimpleVariable) item).toString());
			} else {
				results.append(item);
			}
		}
		if (!testDynamic) {
			isDynamic = false;
			permanentResults = results.toString();
		}
		return results.toString();
	}

	public CompoundVariable getFunction() {
		CompoundVariable func = new CompoundVariable();
		func.compiledComponents = (LinkedList) compiledComponents.clone();
		func.rawParameters = rawParameters;
		return func;
	}

	public List getArgumentDesc() {
		return new LinkedList();
	}

	public void clear() {
		hasFunction = false;
		compiledComponents.clear();
	}

	public void setParameters(String parameters) throws InvalidVariableException {
		this.rawParameters = parameters;
		if (parameters == null || parameters.length() == 0) {
			return;
		}

		compiledComponents = functionParser.compileString(parameters);
		if (compiledComponents.size() > 1 || !(compiledComponents.get(0) instanceof String)) {
			hasFunction = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.functions.Function#setParameters(Collection)
	 */
	public void setParameters(Collection parameters) throws InvalidVariableException {
	}

	static Object getNamedFunction(String functionName) throws InvalidVariableException {
		if (functions.containsKey(functionName)) {
			try {
				return (Function) ((Class) functions.get(functionName)).newInstance();
			} catch (Exception e) {
				log.error("", e);
				throw new InvalidVariableException();
			}
		} else {
			return new SimpleVariable(functionName);
		}
	}

	public boolean hasFunction() {
		return hasFunction;
	}

	/**
	 * @see Function#getReferenceKey()
	 */
	public String getReferenceKey() {
		return "";
	}
	/*
	 * NOT USED
	 * 
	 * private JMeterVariables getVariables() { return
	 * JMeterContextService.getContext().getVariables(); }
	 */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3263.java