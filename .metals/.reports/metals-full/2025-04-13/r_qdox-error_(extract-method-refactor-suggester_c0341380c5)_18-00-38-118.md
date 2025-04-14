error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15609.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15609.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15609.java
text:
```scala
r@@elPath = Locator.encodeURI(relPath);

/*
 * Copyright  2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.util.FileUtils;

/**
 * Converts a Path into a property suitable as a Manifest classpath.
 *
 * @since Ant 1.7
 *
 * @ant.task category="property"
 */
public class ManifestClassPath
             extends Task {

    /** The property name to hold the classpath value. */
    private String _name;

    /** The directory the classpath will be relative from. */
    private File _dir;

    /** The maximum parent directory level to traverse. */
    private int _maxParentLevels = 2;

    /** The classpath to convert. */
    private Path _path;

    /**
     * Sets a property, which must not already exists, with a space
     * separated list of files and directories relative to the jar
     * file's parent directory.
     */
    public void execute() {
        if (_name == null) {
          throw new BuildException("Missing 'property' attribute!");
        }
        if (_dir == null) {
          throw new BuildException("Missing 'jarfile' attribute!");
        }
        if (getProject().getProperty(_name) != null) {
          throw new BuildException("Property '" + _name + "' already set!");
        }
        if (_path == null) {
            throw new BuildException("Missing nested <classpath>!");
        }

        // Normalize the reference directory (containing the jar)
        final FileUtils fileUtils = FileUtils.getFileUtils();
        _dir = fileUtils.normalize(_dir.getAbsolutePath());

        // Create as many directory prefixes as parent levels to traverse,
        // in addition to the reference directory itself
        File currDir = _dir;
        String[] dirs = new String[_maxParentLevels + 1];
        for (int i = 0; i < _maxParentLevels + 1; ++i) {
            dirs[i] = currDir.getAbsolutePath() + File.separatorChar;
            currDir = currDir.getParentFile();
            if (currDir == null) {
                _maxParentLevels = i + 1;
                break;
            }
        }

        String[] elements = _path.list();
        StringBuffer buffer = new StringBuffer();
        StringBuffer element = new StringBuffer();
        for (int i = 0; i < elements.length; ++i) {
            // Normalize the current file
            File pathEntry = new File(elements[i]);
            pathEntry = fileUtils.normalize(pathEntry.getAbsolutePath());
            String fullPath = pathEntry.getAbsolutePath();

            // Find the longest prefix shared by the current file
            // and the reference directory.
            String relPath = null;
            for (int j = 0; j <= _maxParentLevels; ++j) {
                String dir = dirs[j];
                if (!fullPath.startsWith(dir)) {
                    continue;
                }

                // We have a match! Add as many ../ as parent
                // directory traversed to get the relative path
                element.setLength(0);
                for (int k = 0; k < j; ++k) {
                    element.append("..");
                    element.append(File.separatorChar);
                }
                element.append(fullPath.substring(dir.length()));
                relPath = element.toString();
                break;
            }

            // No match, so bail out!
            if (relPath == null) {
                throw new BuildException("No suitable relative path from " +
                                         _dir + " to " + fullPath);
            }

            // Manifest's ClassPath: attribute always uses forward
            // slashes '/', and is space-separated. Ant will properly
            // format it on 72 columns with proper line continuation
            if (File.separatorChar != '/') {
                relPath = relPath.replace(File.separatorChar, '/');
            }
            if (pathEntry.isDirectory()) {
                relPath = relPath + '/';
            }
            try {
                relPath = Locator.encodeUri(relPath);
            } catch (UnsupportedEncodingException exc) {
                throw new BuildException(exc);
            }
            buffer.append(relPath);
            buffer.append(' ');
        }
        
        // Get rid of trailing space, if any
        if (buffer.length() > 0) {
            buffer.setLength(buffer.length() - 1);
        }

        // Finally assign the property with the manifest classpath
        getProject().setNewProperty(_name, buffer.toString());
    }

    /**
     * Sets the property name to hold the classpath value.
     *
     * @param  name the property name
     */
    public void setProperty(String name) {
        _name = name;
    }

    /**
     * The JAR file to contain the classpath attribute in its manifest.
     * 
     * @param  jarfile the JAR file. Need not exist yet, but its parent
     *         directory must exist on the other hand.
     */
    public void setJarFile(File jarfile) {
        File parent = jarfile.getParentFile();
        if (!parent.isDirectory()) {
            throw new BuildException("Jar's directory not found: " + parent);
        }
        _dir = parent;
    }

    /**
     * Sets the maximum parent directory levels allowed when computing
     * a relative path.
     *
     * @param  levels the max level. Defaults to 2.
     */
    public void setMaxParentLevels(int levels) {
        _maxParentLevels = levels;
    }

    /**
     * Adds the classpath to convert.
     *
     * @param  path the classpath to convert.
     */
    public void addClassPath(Path path) {
        _path = path;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15609.java