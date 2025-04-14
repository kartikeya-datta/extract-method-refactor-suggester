error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3648.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3648.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3648.java
text:
```scala
r@@unner.executeScript("ant_selector");

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
package org.apache.tools.ant.types.optional;

import org.apache.tools.ant.types.selectors.BaseSelector;
import org.apache.tools.ant.util.ScriptRunner;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * Selector that lets you run a script with selection logic inline
 * @since Ant1.7
 */
public class ScriptSelector extends BaseSelector {

    /**
     * Has this object been initialized ?
     */
    private boolean initialized = false;

    /**
     * script runner
     */
    private ScriptRunner runner = new ScriptRunner();

    /**
     * fields updated for every selection
     */
    private File basedir;
    private String filename;
    private File file;

    /**
     * selected flag
     */
    private boolean selected;

    /**
     * Defines the language (required).
     *
     * @param language the scripting language name for the script.
     */
    public void setLanguage(String language) {
        runner.setLanguage(language);
    }

    /**
     * Initialize on demand.
     *
     * @throws org.apache.tools.ant.BuildException
     *          if someting goes wrong
     */
    private void init() throws BuildException {
        if (initialized) {
            return;
        }
        initialized = true;
        runner.bindToComponent(this);
    }


    /**
     * Load the script from an external file ; optional.
     *
     * @param file the file containing the script source.
     */
    public void setSrc(File file) {
        runner.setSrc(file);
    }

    /**
     * The script text.
     *
     * @param text a component of the script text to be added.
     */
    public void addText(String text) {
        runner.addText(text);
    }

    /**
     * Method that each selector will implement to create their selection
     * behaviour. If there is a problem with the setup of a selector, it can
     * throw a BuildException to indicate the problem.
     *
     * @param basedir  A java.io.File object for the base directory
     * @param filename The name of the file to check
     * @param file     A File object for this filename
     *
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        init();
        setSelected(true);
        this.file = file;
        this.basedir = basedir;
        this.filename = filename;
        runner.addBean("basedir", basedir);
        runner.addBean("filename", filename);
        runner.addBean("file", file);
        runner.executeScript("<ANT-Selector>");
        return isSelected();
    }


    /**
     * get the base directory
     * @return the base directory
     */
    public File getBasedir() {
        return basedir;
    }

    /**
     * get the filename of the file
     * @return the filename of the file that is currently been tested
     */
    public String getFilename() {
        return filename;
    }

    /**
     * get the file that is currently to be tested
     * @return the file that is currently been tested
     */
    public File getFile() {
        return file;
    }

    /**
     * get state of selected flag
     * @return the selected flag
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * set the selected state
     * Intended for script use, not as an Ant attribute
     * @param selected the selected state
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3648.java