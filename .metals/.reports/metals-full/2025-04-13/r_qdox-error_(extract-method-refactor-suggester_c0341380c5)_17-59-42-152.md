error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12938.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12938.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12938.java
text:
```scala
private final S@@tring[] tokenizedPath;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types.selectors;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.SymbolicLinkUtils;

/**
 * Container for a path that has been split into its components.
 * @since 1.8.0
 */
public class TokenizedPath {

    /**
     * Instance that holds no tokens at all.
     */
    public static final TokenizedPath EMPTY_PATH =
        new TokenizedPath("", new String[0]);

    /** Helper. */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    /** Helper. */
    private static final SymbolicLinkUtils SYMLINK_UTILS =
        SymbolicLinkUtils.getSymbolicLinkUtils();
    /** iterations for case-sensitive scanning. */
    private static final boolean[] CS_SCAN_ONLY = new boolean[] {true};
    /** iterations for non-case-sensitive scanning. */
    private static final boolean[] CS_THEN_NON_CS = new boolean[] {true, false};

    private final String path;
    private final String tokenizedPath[];

    /**
    * Initialize the TokenizedPath by parsing it. 
    * @param path The path to tokenize. Must not be
    *                <code>null</code>.
    */
    public TokenizedPath(String path) {
        this(path, SelectorUtils.tokenizePathAsArray(path));
    }
    
    /**
     * Creates a new path as a child of another path.
     *
     * @param parent the parent path
     * @param child the child, must not contain the file separator
     */
    public TokenizedPath(TokenizedPath parent, String child) {
        if (parent.path.length() > 0
            && parent.path.charAt(parent.path.length() - 1)
               != File.separatorChar) {
            path = parent.path + File.separatorChar + child;
        } else {
            path = parent.path + child;
        }
        tokenizedPath = new String[parent.tokenizedPath.length + 1];
        System.arraycopy(parent.tokenizedPath, 0, tokenizedPath, 0,
                         parent.tokenizedPath.length);
        tokenizedPath[parent.tokenizedPath.length] = child;
    }

    /* package */ TokenizedPath(String path, String[] tokens) {
        this.path = path;
        this.tokenizedPath = tokens;
    }

    /**
     * @return The original path String
     */
    public String toString() {
        return path;
    }
    
    /**
     * The depth (or length) of a path.
     */
    public int depth() {
        return tokenizedPath.length;
    }

    /* package */ String[] getTokens() {
        return tokenizedPath;
    }

    /**
     * From <code>base</code> traverse the filesystem in order to find
     * a file that matches the given name.
     *
     * @param base base File (dir).
     * @param cs whether to scan case-sensitively.
     * @return File object that points to the file in question or null.
     */
    public File findFile(File base, final boolean cs) {
        String[] tokens = tokenizedPath;
        if (FileUtils.isAbsolutePath(path)) {
            if (base == null) {
                String[] s = FILE_UTILS.dissect(path);
                base = new File(s[0]);
                tokens = SelectorUtils.tokenizePathAsArray(s[1]);
            } else {
                File f = FILE_UTILS.normalize(path);
                String s = FILE_UTILS.removeLeadingPath(base, f);
                if (s.equals(f.getAbsolutePath())) {
                    //removing base from path yields no change; path
                    //not child of base
                    return null;
                }
                tokens = SelectorUtils.tokenizePathAsArray(s);
            }
        }
        return findFile(base, tokens, cs);
    }

    /**
     * Do we have to traverse a symlink when trying to reach path from
     * basedir?
     * @param base base File (dir).
     */
    public boolean isSymlink(File base) {
        for (int i = 0; i < tokenizedPath.length; i++) {
            try {
                if ((base != null
                     && SYMLINK_UTILS.isSymbolicLink(base, tokenizedPath[i]))

                    (base == null
                     && SYMLINK_UTILS.isSymbolicLink(tokenizedPath[i]))
                    ) {
                    return true;
                }
                base = new File(base, tokenizedPath[i]);
            } catch (java.io.IOException ioe) {
                String msg = "IOException caught while checking "
                    + "for links, couldn't get canonical path!";
                // will be caught and redirected to Ant's logging system
                System.err.println(msg);
            }
        }
        return false;
    }

    /**
     * true if the original paths are equal.
     */
    public boolean equals(Object o) {
        return o instanceof TokenizedPath
            && path.equals(((TokenizedPath) o).path);
    }

    public int hashCode() {
        return path.hashCode();
    }

    /**
     * From <code>base</code> traverse the filesystem in order to find
     * a file that matches the given stack of names.
     *
     * @param base base File (dir) - must not be null.
     * @param pathElements array of path elements (dirs...file).
     * @param cs whether to scan case-sensitively.
     * @return File object that points to the file in question or null.
     */
    private static File findFile(File base, final String[] pathElements,
                                 final boolean cs) {
        for (int current = 0; current < pathElements.length; current++) {
            if (!base.isDirectory()) {
                return null;
            }
            String[] files = base.list();
            if (files == null) {
                throw new BuildException("IO error scanning directory "
                                         + base.getAbsolutePath());
            }
            boolean found = false;
            boolean[] matchCase = cs ? CS_SCAN_ONLY : CS_THEN_NON_CS;
            for (int i = 0; !found && i < matchCase.length; i++) {
                for (int j = 0; !found && j < files.length; j++) {
                    if (matchCase[i]
                        ? files[j].equals(pathElements[current])
                        : files[j].equalsIgnoreCase(pathElements[current])) {
                        base = new File(base, files[j]);
                        found = true;
                    }
                }
            }
            if (!found) {
                return null;
            }
        }
        return pathElements.length == 0 && !base.isDirectory() ? null : base;
    }

    /**
     * Creates a TokenizedPattern from the same tokens that make up
     * this path.
     */
    public TokenizedPattern toPattern() {
        return new TokenizedPattern(path, tokenizedPath); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12938.java