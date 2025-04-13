error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10456.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10456.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10456.java
text:
```scala
r@@eturn "../runtime/bin" + File.pathSeparator + "../aspectj5rt/bin" + File.pathSeparator +

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.ajdt.internal.compiler.batch;

import org.aspectj.ajdt.ajc.*;
import org.aspectj.ajdt.ajc.AjdtCommand;
import org.aspectj.bridge.ICommand;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.IMessageHolder;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.weaver.bcel.LazyClassGen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public abstract class CommandTestCase extends TestCase {

	/**
	 * Constructor for CommandTestCase.
	 * @param name
	 */
	public CommandTestCase(String name) {
		super(name);
	}
	
	public static final int[] NO_ERRORS = new int[0];
	public static final int[] TOP_ERROR = new int[0];


	public static void checkCompile(String source, int[] expectedErrors) {
		checkCompile(source, new String[0], expectedErrors);
	}
	
	public static void checkCompile(String source, String[] extraArgs, int[] expectedErrors) {
		List args = new ArrayList();
		args.add("-verbose");
		
		args.add("-d");
		args.add("out");
		
		args.add("-classpath");		

		args.add(getRuntimeClasspath() + File.pathSeparator + 
			"../lib/junit/junit.jar");
		
		args.add("-g");  //XXX need this to get sourcefile and line numbers, shouldn't
		
		for (int i = 0; i < extraArgs.length; i++) {
			args.add(extraArgs[i]);
		}
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/" + source);
		
		runCompiler(args, expectedErrors);
	}
	
	public void checkMultipleCompile(String source) throws InterruptedException {
		List args = new ArrayList();
		args.add("-verbose");
		
		args.add("-d");
		args.add("out");
		
		args.add("-classpath");
		args.add(getRuntimeClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/" + source);
		
		ICommand compiler = runCompiler(args, NO_ERRORS);
		Thread.sleep(100); 
		
		rerunCompiler(compiler);
	}		

	public void rerunCompiler(ICommand command) {
		MessageHandler myHandler = new MessageHandler();
//		List recompiledFiles = new ArrayList();
	    if (!command.repeatCommand(myHandler)) {
            assertTrue("recompile failed", false);
        }
        assertEquals(0, myHandler.numMessages(IMessage.ERROR, true));
	}
	
	
	
	public static ICommand runCompiler(List args, int[] expectedErrors) {
		ICommand command = new AjdtCommand();
		MessageHandler myHandler = new MessageHandler();
		myHandler.setInterceptor(org.aspectj.tools.ajc.Main.MessagePrinter.TERSE);
	    boolean result = command.runCommand((String[])args.toArray(new String[args.size()]), myHandler);
        System.out.println("result: " + result);
//		System.out.println("errors: " + Arrays.asList(myHandler.getErrors()));
//		System.out.println("warnings: " + Arrays.asList(myHandler.getWarnings()));
		
        int nErrors = myHandler.numMessages(IMessage.ERROR, IMessageHolder.EQUAL);
        if (expectedErrors == NO_ERRORS) {
            if (0 != nErrors) {
                String s = ""+Arrays.asList(myHandler.getErrors());
                assertTrue("unexpected errors: " + s, false);
            }
		} else if (expectedErrors == TOP_ERROR) { // ?? what is this?
            assertTrue("expected error", nErrors > 0);
		} else {
			List errors = new ArrayList(Arrays.asList(myHandler.getErrors()));
			for (int i=0, len=expectedErrors.length; i < len; i++) {
				int line = expectedErrors[i];
				boolean found = false;
				for (Iterator iter = errors.iterator(); iter.hasNext(); ) {
					IMessage m = (IMessage)iter.next();
					if (m.getSourceLocation() != null && m.getSourceLocation().getLine() == line) {
						found = true;
						iter.remove();
					}
				}
				assertTrue("didn't find error on line " + line, found);
			}
			if (errors.size() > 0) {
				assertTrue("didn't expect errors: " + errors, false);
			}
		}
		return command;
	}
	
	public static void printGenerated(String path, String name) throws IOException {		
		String fullpath = AjdtAjcTests.TESTDATA_PATH + "/" + path;
		LazyClassGen.disassemble(fullpath, name, System.out);
	}
	
	
    
    /** incremental test case adapter to JUnit */
    public class IncCase extends IncrementalCase {
        protected void fail(IMessageHandler handler, String mssg) {
			assertTrue(mssg, false);
		}
		protected void message(
			IMessage.Kind kind,
			String mssg,
			IMessageHandler handler) {
            if ((kind == IMessage.FAIL)  || (kind == IMessage.ABORT)) {
                assertTrue(mssg, false);
            } else {
                System.err.println("IncCase " + kind + ": " + mssg); // XXX
            }
			super.message(kind, mssg, handler);
		}

    }

	/** get the location of the org.aspectj.lang & runtime classes */
	protected static String getRuntimeClasspath() {
		return "../runtime/bin" + File.pathSeparator + 
			   System.getProperty("aspectjrt.path");
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10456.java