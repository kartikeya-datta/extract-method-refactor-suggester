error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16728.java
text:
```scala
i@@f (x.isSilent()) { // no message, just return

/* *******************************************************************
 * Copyright (c) 2001-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.aspectj.bridge.*;
import org.aspectj.util.LangUtil;


/**
 * EXPERIMENTAL - This adapts the AspectJ compiler as if it were Javac.
 * It just passes the arguments through to the compiler.
 * Limitations:
 * <ol>
 * <li>Users still must specify all source files to compile.</li>
 * <li>No support for non-Javac options</li>
 * <li>Javac srcdir are treated as sourceroots unless -DajcAdapterSkipRoots=...</li>
 * </ol>
 * 
 * @author Wes Isberg <a href="mailto:isberg@aspectj.org">isberg@aspectj.org</a>
 * @since AspectJ 1.1, Ant 1.3
 */
public class Ajc11CompilerAdapter extends DefaultCompilerAdapter {
    /*
     * Not sure how best to implement our functionality -- not documented.
     * Desired functionality:
     * - ability to run in-line or forked 
     *   - switch based only on fork="[true|false]" 
     *     (so classpath required to fork must be calculated)
     *   - able to set memory available when forking
     * - handles .lst files on command-line by prefixing with @
     *   - and/or handles compiler option -XargPrefix=...
     * - this means all AspectJ-specific options (injars/outjars...)
     *   are in the .lst file?
     * 
     * Implementation options:
     * - straight CompilerAdapter
     * - override DefaultCompilerAdapter methods for template processes
     * - create a new FacadeTaskHelper, supply to DefaultCompilerAdaper
     */
    public static final String SKIP_ROOTS_NAME = "ajcAdapterSkipRoots";

    // ------------------------------ setup
    /**
     * Adds the command line arguments specifc to the current implementation.
     */
    protected void addCurrentCompilerArgs(Commandline cmd) {
        cmd.addArguments(getJavac().getCurrentCompilerArgs());
    }

    // ------------------------------ run
    /**
     * Run the compilation.
     *
     * @exception BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        Commandline cmd = setupModernJavacCommand();

        try {
            String[] args = cmd.getArguments();
            int result = runCompiler(args);
            return (0 == result);
        } catch (BuildException e) {
            throw e;
        } catch (AbortException x) {
            if (AbortException.ABORT.equals(x)) { // no message, just return
                return false;
            } else {
                Throwable t = x.getThrown();
                if (null == t) {
                    t = x;
                }
                throw new BuildException("Thrown: ", t, location);
            }
        } catch (Throwable x) {
            throw new BuildException("Thrown: ", x, location);
        }
    }
    
    // run? handle forking?
    private int runCompiler(String[] args) {
        int result = -1;
        IMessageHolder sink = new MessageHandler();
        ICommand compiler = ReflectionFactory.makeCommand(ReflectionFactory.ECLIPSE, sink);
        if ((null == compiler) || sink.hasAnyMessage(IMessage.FAIL, true)) {
            throwBuildException("loading compiler", sink);
        } else {
            args = filterArgs(args);
            if (!compiler.runCommand(args, sink)) {
                System.err.println("runCompiler args: " + Arrays.asList(args));
                throwBuildException("running compiler", sink);
            } else {
                result = 0;
            }
        }
        return  result;
    }

	/**
	 * Method throwBuildException.
	 * @param string
	 * @param sink
	 */
	private void throwBuildException(String string, IMessageHolder sink) { // XXX nicer
        if ((null != sink) && (0 < sink.numMessages(null, true))) {
            MessageUtil.print(System.err, sink, null);
        }
        throw new BuildException(string + ": " + sink, location);
	}
    
    /** Convert javac argument list to a form acceptable by ajc */
    protected String[] filterArgs(String[] args) {
        LinkedList argList = new LinkedList();
        argList.addAll(LangUtil.arrayAsList(args));
        ArrayList roots = new ArrayList();
        for (ListIterator iter = argList.listIterator(); iter.hasNext();) {
			String arg = (String) iter.next();
            if ("-sourcepath".equals(arg)) { // similar to -sourceroots?
                iter.remove();
                roots.add(iter.next()); // need next after remove?
                iter.remove();
            } else if ("-0".equals(arg)) { // unsupported
                System.err.println("warning: ignoring -0 argument");
                iter.remove();
            } else if ("-bootclasspath".equals(arg)) { // ajc fakes
            } else if ("-extdirs".equals(arg)) { // ajc fakes
            } else if ("-target".equals(arg)) { // -1.4 or -1.3
                iter.remove();
                String vers = (String) iter.next();
                if ("1.3".equals(vers)) {
                    iter.set("-1.3");
                } else if ("1.4".equals(vers)) {
                    iter.set("-1.4");
                } else { // huh?
                    String s = "expecting 1.3 or 1.4 at " + vers + " in " + argList;
                    throwBuildException(s, null);                    
                }
            }
		}
        
        if (0 < roots.size()) {
            String skipRoots = null;
            try {
                skipRoots = System.getProperty(SKIP_ROOTS_NAME);
            } catch (Throwable t) {
                // ignore SecurityException, etc.
            }
            
            if (null == skipRoots) {
                StringBuffer sb = new StringBuffer();
                boolean first = true;
                for (Iterator iter = roots.iterator(); iter.hasNext();) {
                    if (!first) {
                        sb.append(File.pathSeparator);
                    } else {
                        first = false;
                    }
                    sb.append((String) iter.next());
                }
                argList.add(0, "-sourceroots");
                argList.add(1, sb.toString());
            }
        }

        return (String[]) argList.toArray(new String[0]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16728.java