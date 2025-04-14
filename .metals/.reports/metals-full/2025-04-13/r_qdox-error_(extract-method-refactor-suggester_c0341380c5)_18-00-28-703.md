error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5861.java
text:
```scala
c@@omponentService.loadLib(url.toString(), importAll, false);

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.ant.antlib.system;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.ant.common.antlib.AbstractTask;
import org.apache.ant.common.antlib.AntContext;
import org.apache.ant.common.service.ComponentService;
import org.apache.ant.common.util.ExecutionException;
import org.apache.ant.init.InitUtils;

/**
 * Load an AntLibrary and optionally import all its components
 *
 * @author Conor MacNeill
 * @created 29 January 2002
 */
public class LoadLib extends AbstractTask {
    /** Flag which indicates if all components should be imported */
    private boolean importAll;

    /**
     * This is the location, either file or URL of the library or libraries
     * to be loaded
     */
    private URL url;

    /**
     * Sets the URL of the library to be loaded
     *
     * @param url the URL from which the library is to be loaded
     * @exception ExecutionException if the URL cannot be set
     */
    public void setURL(URL url) throws ExecutionException {
        checkNullURL();
        this.url = url;
    }

    /**
     * Set the file from which the library should be loaded.
     *
     * @param file the file from which the library should be loaded
     * @exception ExecutionException if the file attribute cannot be set
     */
    public void setFile(File file) throws ExecutionException {
        checkNullURL();
        try {
            this.url = InitUtils.getFileURL(file);
        } catch (MalformedURLException e) {
            throw new ExecutionException(e);
        }
    }

    /**
     * Set the dir in which to search for AntLibraries.
     *
     * @param dir the dir from which all Ant Libraries found will be loaded.
     * @exception ExecutionException if the dir attribute cannot be set
     */
    public void setDir(File dir) throws ExecutionException {
        checkNullURL();
        try {
            this.url = InitUtils.getFileURL(dir);
        } catch (MalformedURLException e) {
            throw new ExecutionException(e);
        }
    }

    /**
     * Indicate whether all components from the library should be imported
     *
     * @param importAll true if all components in the library should be
     *      imported.
     */
    public void setImportAll(boolean importAll) {
        this.importAll = importAll;
    }

    /**
     * Validate this task is configured correctly
     *
     * @exception ExecutionException if the task is not configured correctly
     */
    public void validateComponent() throws ExecutionException {
        if (url == null) {
            throw new ExecutionException("A location from which to load "
                 + "libraries must be provided");
        }
    }


    /**
     * Load the library or libraries and optiinally import their components
     *
     * @exception ExecutionException if the library or libraries cannot be
     *      loaded.
     */
    public void execute() throws ExecutionException {
        AntContext context = getAntContext();
        ComponentService componentService = (ComponentService)
            context.getCoreService(ComponentService.class);
        componentService.loadLib(url.toString(), importAll);
    }

    /**
     * Check if any of the location specifying attributes have already been
     * set.
     *
     * @exception ExecutionException if the search URL has already been set
     */
    private void checkNullURL() throws ExecutionException {
        if (url != null) {
            throw new ExecutionException("Location of library has already been "
                 + "set. Please use only one of file, dir or url attributes");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5861.java