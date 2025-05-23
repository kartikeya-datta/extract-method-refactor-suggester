error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2665.java
text:
```scala
i@@f (!getClass().isAssignableFrom(o.getClass())) {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
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

package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;

import java.io.File;
import java.util.Stack;
import java.util.Vector;

/**
 * Moved out of MatchingTask to make it a standalone object that could
 * be referenced (by scripts for example).
 *
 * @author Arnout J. Kuiper <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a> 
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 */
public class FileSet extends DataType implements Cloneable {
    
    private PatternSet defaultPatterns = new PatternSet();
    private Vector additionalPatterns = new Vector();

    private File dir;
    private boolean useDefaultExcludes = true;
    private boolean isCaseSensitive = true;

    private String dataTypeName = "fileset";

    public FileSet() {
        super();
    }

    protected FileSet(FileSet fileset) {
        this.dir = fileset.dir;
        this.defaultPatterns = fileset.defaultPatterns;
        this.additionalPatterns = fileset.additionalPatterns;
        this.useDefaultExcludes = fileset.useDefaultExcludes;
        this.isCaseSensitive = fileset.isCaseSensitive;
        setProject(getProject());
    }

    /**
     * Makes this instance in effect a reference to another PatternSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p> 
     */
    public void setRefid(Reference r) throws BuildException {
        if (dir != null || defaultPatterns.hasPatterns()) {
            throw tooManyAttributes();
        }
        if (!additionalPatterns.isEmpty()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    public void setDir(File dir) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }

        this.dir = dir;
    }

    public File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        return dir;
    }

    public PatternSet createPatternSet() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        PatternSet patterns = new PatternSet();
        additionalPatterns.addElement(patterns);
        return patterns;
    }

    /**
     * add a name entry on the include list
     */
    public PatternSet.NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createInclude();
    }
    
    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createIncludesFile();
    }
    
    /**
     * add a name entry on the exclude list
     */
    public PatternSet.NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createExclude();
    }

    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createExcludesFile();
    }
    
    /**
     * Appends <code>includes</code> to the current list of include patterns. 
     * Patterns may be separated by a comma or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        defaultPatterns.setIncludes(includes);
    }

    /**
     * Appends <code>excludes</code> to the current list of exclude patterns. 
     * Patterns may be separated by a comma or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        defaultPatterns.setExcludes(excludes);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param incl The file to fetch the include patterns from.  
     */
     public void setIncludesfile(File incl) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }

         defaultPatterns.setIncludesfile(incl);
     }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param excl The file to fetch the exclude patterns from.  
     */
     public void setExcludesfile(File excl) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }

         defaultPatterns.setExcludesfile(excl);
     }

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        this.useDefaultExcludes = useDefaultExcludes;
    }

    /**
     * Sets case sensitivity of the file system
     *
     * @param isCaseSensitive "true"|"on"|"yes" if file system is case
     *                           sensitive, "false"|"off"|"no" when not.
     */
    public void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    /**
     * sets the name used for this datatype instance.
     */
    protected final void setDataTypeName(String name) {
        dataTypeName = name;
    }

    /**
     * Returns the directory scanner needed to access the files to process.
     */
    public DirectoryScanner getDirectoryScanner(Project p) {
        if (isReference()) {
            return getRef(p).getDirectoryScanner(p);
        }

        if (dir == null) {
            throw new BuildException("No directory specified for "
                                     + dataTypeName + ".");
        }

        if (!dir.exists()) {
            throw new BuildException(dir.getAbsolutePath()+" not found.");
        }
        if (!dir.isDirectory()) {
            throw new BuildException(dir.getAbsolutePath()+" is not a directory.");
        }

        DirectoryScanner ds = new DirectoryScanner();
        setupDirectoryScanner(ds, p);
        ds.scan();
        return ds;
    }
    
    public void setupDirectoryScanner(FileScanner ds, Project p) {
        if (ds == null) {
            throw new IllegalArgumentException("ds cannot be null");
        }
        
        ds.setBasedir(dir);

        final int count = additionalPatterns.size();
        for (int i = 0; i < count; i++) {
            Object o = additionalPatterns.elementAt(i);
            defaultPatterns.append((PatternSet) o, p);
        }

        p.log(dataTypeName + ": Setup scanner in dir " + dir + 
            " with " + defaultPatterns, Project.MSG_DEBUG );
        
        ds.setIncludes(defaultPatterns.getIncludePatterns(p));
        ds.setExcludes(defaultPatterns.getExcludePatterns(p));
        if (useDefaultExcludes) {
            ds.addDefaultExcludes();
        }
        ds.setCaseSensitive(isCaseSensitive);
    }

    /**
     * Performs the check for circular references and returns the
     * referenced FileSet.  
     */
    protected FileSet getRef(Project p) {
        if (!checked) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, p);
        }
        
        Object o = ref.getReferencedObject(p);
        if (!o.getClass().equals(getClass())) {
            String msg = ref.getRefId()+" doesn\'t denote a " + dataTypeName;
            throw new BuildException(msg);
        } else {
            return (FileSet) o;
        }
    }

    /**
     * Return a FileSet that has the same basedir and same patternsets
     * as this one.
     */
    public Object clone() {
        if (isReference()) {
            return new FileSet(getRef(getProject()));
        } else {
            return new FileSet(this);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2665.java