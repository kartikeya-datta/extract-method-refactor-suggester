error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6444.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6444.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6444.java
text:
```scala
private static volatile b@@oolean functionalMode = false;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

package org.apache.jmeter.testelement;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.NewDriver;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class TestPlan extends AbstractTestElement implements Serializable, TestListener {
	private static final Logger log = LoggingManager.getLoggerForClass();

	// does not appear to be needed
//	private final static String THREAD_GROUPS = "TestPlan.thread_groups"; //$NON-NLS-1$

	private final static String FUNCTIONAL_MODE = "TestPlan.functional_mode"; //$NON-NLS-1$

	private final static String USER_DEFINED_VARIABLES = "TestPlan.user_defined_variables"; //$NON-NLS-1$

	private final static String SERIALIZE_THREADGROUPS = "TestPlan.serialize_threadgroups"; //$NON-NLS-1$

	private final static String CLASSPATHS = "TestPlan.user_define_classpath"; //$NON-NLS-1$
    private static final String CLASSPATH_SEPARATOR = ","; //$NON-NLS-1$
    
    private final static String BASEDIR = "basedir";

    private transient List threadGroups = new LinkedList();

    // Does not appear to be needed
//	private transient List configs = new LinkedList();

//    // Does not appear to be needed
//	private static List itemsCanAdd = new LinkedList();

    // Does not appear to be needed
//	private static TestPlan plan;

	// There's only 1 test plan, so can cache the mode here
	private static boolean functionalMode = false;

	static {
		// WARNING! This String value must be identical to the String value
		// returned in org.apache.jmeter.threads.ThreadGroup.getClassLabel()
		// method. If it's not you will not be able to add a Thread Group
		// element to a Test Plan.

        // Does not appear to be needed
//		itemsCanAdd.add(JMeterUtils.getResString("threadgroup")); //$NON-NLS-1$
	}

	public TestPlan() {
		// this("Test Plan");
		// setFunctionalMode(false);
		// setSerialized(false);
	}

	public TestPlan(String name) {
		setName(name);
		// setFunctionalMode(false);
		// setSerialized(false);

        // Does not appear to be needed
//        setProperty(new CollectionProperty(THREAD_GROUPS, threadGroups));
	}

	// create transient item
	private Object readResolve() throws java.io.ObjectStreamException {
		threadGroups = new LinkedList();
        return this;
    }

    public void prepareForPreCompile()
    {
        getVariables().setRunningVersion(true);
    }

	/**
	 * Fetches the functional mode property
	 * 
	 * @return functional mode
	 */
	public boolean isFunctionalMode() {
		return getPropertyAsBoolean(FUNCTIONAL_MODE);
	}

	public void setUserDefinedVariables(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_VARIABLES, vars));
	}

	public JMeterProperty getUserDefinedVariablesAsProperty() {
		return getProperty(USER_DEFINED_VARIABLES);
	}

	public String getBasedir() {
		return getPropertyAsString(BASEDIR);
	}

    // Does not appear to be used yet
	public void setBasedir(String b) {
		setProperty(BASEDIR, b);
	}

	public Arguments getArguments() {
		return getVariables();
	}

	public Map getUserDefinedVariables() {
		Arguments args = getVariables();
		return args.getArgumentsAsMap();
	}

	private Arguments getVariables() {
		Arguments args = (Arguments) getProperty(USER_DEFINED_VARIABLES).getObjectValue();
		if (args == null) {
			args = new Arguments();
			setUserDefinedVariables(args);
		}
		return args;
	}

	public void setFunctionalMode(boolean funcMode) {
		setProperty(new BooleanProperty(FUNCTIONAL_MODE, funcMode));
		functionalMode = funcMode;
	}

	/**
	 * Gets the static copy of the functional mode
	 * 
	 * @return mode
	 */
	public static boolean getFunctionalMode() {
		return functionalMode;
	}

	public void setSerialized(boolean serializeTGs) {
		setProperty(new BooleanProperty(SERIALIZE_THREADGROUPS, serializeTGs));
	}
    
    /**
     * Set the classpath for the test plan
     * @param text
     */
    public void setTestPlanClasspath(String text) {
        setProperty(CLASSPATHS,text);
    }
    
    public void setTestPlanClasspathArray(String[] text) {
        StringBuffer cat = new StringBuffer();
        for (int idx=0; idx < text.length; idx++) {
            if (idx > 0) {
                cat.append(CLASSPATH_SEPARATOR);
            }
            cat.append(text[idx]);
        }
        this.setTestPlanClasspath(cat.toString());
    }
    
    public String[] getTestPlanClasspathArray() {
        return JOrphanUtils.split(this.getTestPlanClasspath(),CLASSPATH_SEPARATOR);
    }
    
    /**
     * Returns the classpath
     * @return classpath
     */
    public String getTestPlanClasspath() {
        return getPropertyAsString(CLASSPATHS);
    }

	/**
	 * Fetch the serialize threadgroups property
	 * 
	 * @return serialized setting
	 */
	public boolean isSerialized() {
		return getPropertyAsBoolean(SERIALIZE_THREADGROUPS);
	}

	public void addParameter(String name, String value) {
		getVariables().addArgument(name, value);
	}

    // Does not appear to be needed
//	public static TestPlan createTestPlan(String name) {
//		if (plan == null) {
//			if (name == null) {
//				plan = new TestPlan();
//			} else {
//				plan = new TestPlan(name);
//			}
//			plan.setProperty(new StringProperty(TestElement.GUI_CLASS, 
//					"org.apache.jmeter.control.gui.TestPlanGui")); //$NON-NLS-1$
//		}
//		return plan;
//	}

	public void addTestElement(TestElement tg) {
		super.addTestElement(tg);
		if (tg instanceof ThreadGroup && !isRunningVersion()) {
			addThreadGroup((ThreadGroup) tg);
		}
	}

//    // Does not appear to be needed
//	public void addJMeterComponent(TestElement child) {
//		if (child instanceof ThreadGroup) {
//			addThreadGroup((ThreadGroup) child);
//		}
//	}

//	/**
//	 * Gets the ThreadGroups attribute of the TestPlan object.
//	 * 
//	 * @return the ThreadGroups value
//	 */
//    // Does not appear to be needed
//	public Collection getThreadGroups() {
//		return threadGroups;
//	}

//	/**
//	 * Adds a feature to the ConfigElement attribute of the TestPlan object.
//	 * 
//	 * @param c
//	 *            the feature to be added to the ConfigElement attribute
//	 */
//    // Does not appear to be needed
//	public void addConfigElement(ConfigElement c) {
//		configs.add(c);
//	}

	/**
	 * Adds a feature to the ThreadGroup attribute of the TestPlan object.
	 * 
	 * @param group
	 *            the feature to be added to the ThreadGroup attribute
	 */
	public void addThreadGroup(ThreadGroup group) {
		threadGroups.add(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded()
	 */
	public void testEnded() {
		try {
			FileServer.getFileServer().closeFiles();
		} catch (IOException e) {
			log.error("Problem closing files at end of test", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
	 */
	public void testEnded(String host) {
		testEnded();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
	 */
	public void testIterationStart(LoopIterationEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted()
	 */
	public void testStarted() {
		if (getBasedir() != null && getBasedir().length() > 0) {
			try {
				FileServer.getFileServer().setBasedir(FileServer.getFileServer().getBaseDir() + getBasedir());
			} catch (IOException e) {
				log.error("Failed to set file server base dir with " + getBasedir(), e);
			}
		}
        // we set the classpath
        String[] paths = this.getTestPlanClasspathArray();
        for (int idx=0; idx < paths.length; idx++) {
            NewDriver.addURL(paths[idx]);
            log.info("add " + paths[idx] + " to classpath");
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
	 */
	public void testStarted(String host) {
		testStarted();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6444.java