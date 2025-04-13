error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13374.java
text:
```scala
private b@@oolean _overwrite = false;

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.backend.syslib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtend.backend.util.ErrorHandler;
import org.eclipse.xtend.backend.util.NullWriter;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public final class FileOutlet implements Outlet {
    private File _baseDir;
    private String _fileEncoding = System.getProperty ("file.encoding");
    private boolean _overwrite = false;;
    private boolean _append = false;

    private final List<InMemoryPostprocessor> _inMemoryPp = new ArrayList<InMemoryPostprocessor> ();
    private final List<UriBasedPostprocessor> _uriBasedPp = new ArrayList<UriBasedPostprocessor> ();
    
    public void register (InMemoryPostprocessor pp) {
        _inMemoryPp.add (pp);
    }
    
    public void register (UriBasedPostprocessor pp) {
        _uriBasedPp.add (pp);
    }
    
    public File getBaseDir () {
        return _baseDir;
    }

    public void setBaseDir (File baseDir) {
        _baseDir = baseDir;
    }

    public String getFileEncoding () {
        return _fileEncoding;
    }

    public void setFileEncoding (String fileEncoding) {
        _fileEncoding = fileEncoding;
    }

    public boolean isOverwrite () {
        return _overwrite;
    }

    public void setOverwrite (boolean overwrite) {
        _overwrite = overwrite;
    }

    public void setAppend (boolean append) {
        _append = append;
    }
    
    public boolean isAppend () {
        return _append;
    }
    
    public Writer createWriter (String filename) {
        return createWriter (filename, _append);
    }
        
    public Writer createWriter (String filename, boolean append) {
        try {
            final File target = createTargetFile (filename);
            if (target.exists() && !_overwrite)
                return new NullWriter ();
            
            final File f = new File (_baseDir, filename);
            final File parentDir = f.getParentFile();
            
            if (parentDir.isFile())
                throw new IllegalStateException ("'" + parentDir + "' exists but is no directory.");
            if (! parentDir.exists ())
                parentDir.mkdirs();
                
            final FileOutputStream fos = new FileOutputStream (f, append);
            final BufferedOutputStream bos = new BufferedOutputStream (fos);
            
            if (_fileEncoding == null)
                return new OutputStreamWriter (bos);
            else
                return new OutputStreamWriter (bos, _fileEncoding);
        } catch (IOException exc) {
            ErrorHandler.handle (exc);
            return null; // just for the compiler - this code is never executed
        }
    }

    private File createTargetFile (String filename) {
        return new File (_baseDir, filename);
    }
    
    public String createUri (String filename) {
        return createTargetFile (filename).getPath().replace ("\\", "/");
    }

    public List<InMemoryPostprocessor> getInMemoryPostprocessors () {
        return _inMemoryPp;
    }

    public List<UriBasedPostprocessor> getUriBasedPostprocessors () {
        return _uriBasedPp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13374.java