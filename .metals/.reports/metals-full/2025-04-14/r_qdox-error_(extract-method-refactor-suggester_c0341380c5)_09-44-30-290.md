error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13820.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13820.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13820.java
text:
```scala
public E@@numeration<FileSelector> selectorElements() {

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

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.DifferentSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SelectorContainer;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.TypeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

/**
 * This is an abstract task that should be used by all those tasks that
 * require to include or exclude files based on pattern matching.
 *
 * @since Ant 1.1
 */

public abstract class MatchingTask extends Task implements SelectorContainer {

    // CheckStyle:VisibilityModifier OFF - bc
    protected FileSet fileset = new FileSet();
    // CheckStyle:VisibilityModifier ON

    /** {@inheritDoc}. */
    public void setProject(Project project) {
        super.setProject(project);
        fileset.setProject(project);
    }

    /**
     * add a name entry on the include list
     * @return a NameEntry object to be configured
     */
    public PatternSet.NameEntry createInclude() {
        return fileset.createInclude();
    }

    /**
     * add a name entry on the include files list
     * @return an NameEntry object to be configured
     */
    public PatternSet.NameEntry createIncludesFile() {
        return fileset.createIncludesFile();
    }

    /**
     * add a name entry on the exclude list
     * @return an NameEntry object to be configured
     */
    public PatternSet.NameEntry createExclude() {
        return fileset.createExclude();
    }

    /**
     * add a name entry on the include files list
     * @return an NameEntry object to be configured
     */
    public PatternSet.NameEntry createExcludesFile() {
        return fileset.createExcludesFile();
    }

    /**
     * add a set of patterns
     * @return PatternSet object to be configured
     */
    public PatternSet createPatternSet() {
        return fileset.createPatternSet();
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        fileset.setIncludes(includes);
    }

    // CheckStyle:MethodNameCheck OFF - bc
    /**
     * Set this to be the items in the base directory that you want to be
     * included. You can also specify "*" for the items (ie: items="*")
     * and it will include all the items in the base directory.
     *
     * @param itemString the string containing the files to include.
     */
    public void XsetItems(String itemString) {
        log("The items attribute is deprecated. "
            + "Please use the includes attribute.", Project.MSG_WARN);
        if (itemString == null || itemString.equals("*")
 itemString.equals(".")) {
            createInclude().setName("**");
        } else {
            StringTokenizer tok = new StringTokenizer(itemString, ", ");
            while (tok.hasMoreTokens()) {
                String pattern = tok.nextToken().trim();
                if (pattern.length() > 0) {
                    createInclude().setName(pattern + "/**");
                }
            }
        }
    }

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        fileset.setExcludes(excludes);
    }

    /**
     * List of filenames and directory names to not include. They should be
     * either , or " " (space) separated. The ignored files will be logged.
     *
     * @param ignoreString the string containing the files to ignore.
     */
    public void XsetIgnore(String ignoreString) {
        log("The ignore attribute is deprecated."
            + "Please use the excludes attribute.", Project.MSG_WARN);
        if (ignoreString != null && ignoreString.length() > 0) {
            StringTokenizer tok = new StringTokenizer(ignoreString, ", ",
                                                      false);
            while (tok.hasMoreTokens()) {
                createExclude().setName("**/" + tok.nextToken().trim() + "/**");
            }
        }
    }

    // CheckStyle:VisibilityModifier ON

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        fileset.setDefaultexcludes(useDefaultExcludes);
    }

    /**
     * Returns the directory scanner needed to access the files to process.
     * @param baseDir the base directory to use with the fileset
     * @return a directory scanner
     */
    protected DirectoryScanner getDirectoryScanner(File baseDir) {
        fileset.setDir(baseDir);
        return fileset.getDirectoryScanner(getProject());
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param includesfile A string containing the filename to fetch
     * the include patterns from.
     */
    public void setIncludesfile(File includesfile) {
        fileset.setIncludesfile(includesfile);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param excludesfile A string containing the filename to fetch
     * the include patterns from.
     */
    public void setExcludesfile(File excludesfile) {
        fileset.setExcludesfile(excludesfile);
    }

    /**
     * Sets case sensitivity of the file system
     *
     * @param isCaseSensitive "true"|"on"|"yes" if file system is case
     *                           sensitive, "false"|"off"|"no" when not.
     */
    public void setCaseSensitive(boolean isCaseSensitive) {
        fileset.setCaseSensitive(isCaseSensitive);
    }

    /**
     * Sets whether or not symbolic links should be followed.
     *
     * @param followSymlinks whether or not symbolic links should be followed
     */
    public void setFollowSymlinks(boolean followSymlinks) {
        fileset.setFollowSymlinks(followSymlinks);
    }

    /**
     * Indicates whether there are any selectors here.
     *
     * @return whether any selectors are in this container
     */
    public boolean hasSelectors() {
        return fileset.hasSelectors();
    }

    /**
     * Gives the count of the number of selectors in this container
     *
     * @return the number of selectors in this container
     */
    public int selectorCount() {
        return fileset.selectorCount();
    }

    /**
     * Returns the set of selectors as an array.
     * @param p the current project
     * @return an array of selectors in this container
     */
    public FileSelector[] getSelectors(Project p) {
        return fileset.getSelectors(p);
    }

    /**
     * Returns an enumerator for accessing the set of selectors.
     *
     * @return an enumerator that goes through each of the selectors
     */
    public Enumeration selectorElements() {
        return fileset.selectorElements();
    }

    /**
     * Add a new selector into this container.
     *
     * @param selector the new selector to add
     */
    public void appendSelector(FileSelector selector) {
        fileset.appendSelector(selector);
    }

    /* Methods below all add specific selectors */

    /**
     * add a "Select" selector entry on the selector list
     * @param selector the selector to add
     */
    public void addSelector(SelectSelector selector) {
        fileset.addSelector(selector);
    }

    /**
     * add an "And" selector entry on the selector list
     * @param selector the selector to add
     */
    public void addAnd(AndSelector selector) {
        fileset.addAnd(selector);
    }

    /**
     * add an "Or" selector entry on the selector list
     * @param selector the selector to add
     */
    public void addOr(OrSelector selector) {
        fileset.addOr(selector);
    }

    /**
     * add a "Not" selector entry on the selector list
     * @param selector the selector to add
     */
    public void addNot(NotSelector selector) {
        fileset.addNot(selector);
    }

    /**
     * add a "None" selector entry on the selector list
     * @param selector the selector to add
     */
    public void addNone(NoneSelector selector) {
        fileset.addNone(selector);
    }

    /**
     * add a majority selector entry on the selector list
     * @param selector the selector to add
     */
    public void addMajority(MajoritySelector selector) {
        fileset.addMajority(selector);
    }

    /**
     * add a selector date entry on the selector list
     * @param selector the selector to add
     */
    public void addDate(DateSelector selector) {
        fileset.addDate(selector);
    }

    /**
     * add a selector size entry on the selector list
     * @param selector the selector to add
     */
    public void addSize(SizeSelector selector) {
        fileset.addSize(selector);
    }

    /**
     * add a selector filename entry on the selector list
     * @param selector the selector to add
     */
    public void addFilename(FilenameSelector selector) {
        fileset.addFilename(selector);
    }

    /**
     * add an extended selector entry on the selector list
     * @param selector the selector to add
     */
    public void addCustom(ExtendSelector selector) {
        fileset.addCustom(selector);
    }

    /**
     * add a contains selector entry on the selector list
     * @param selector the selector to add
     */
    public void addContains(ContainsSelector selector) {
        fileset.addContains(selector);
    }

    /**
     * add a present selector entry on the selector list
     * @param selector the selector to add
     */
    public void addPresent(PresentSelector selector) {
        fileset.addPresent(selector);
    }

    /**
     * add a depth selector entry on the selector list
     * @param selector the selector to add
     */
    public void addDepth(DepthSelector selector) {
        fileset.addDepth(selector);
    }

    /**
     * add a depends selector entry on the selector list
     * @param selector the selector to add
     */
    public void addDepend(DependSelector selector) {
        fileset.addDepend(selector);
    }

    /**
     * add a regular expression selector entry on the selector list
     * @param selector the selector to add
     */
    public void addContainsRegexp(ContainsRegexpSelector selector) {
        fileset.addContainsRegexp(selector);
    }

    /**
     * add a type selector entry on the type list
     * @param selector the selector to add
     * @since ant 1.6
     */
    public void addDifferent(DifferentSelector selector) {
        fileset.addDifferent(selector);
    }

    /**
     * add a type selector entry on the type list
     * @param selector the selector to add
     * @since ant 1.6
     */
    public void addType(TypeSelector selector) {
        fileset.addType(selector);
    }

    /**
     * add the modified selector
     * @param selector the selector to add
     * @since ant 1.6
     */
    public void addModified(ModifiedSelector selector) {
        fileset.addModified(selector);
    }

    /**
     * add an arbitrary selector
     * @param selector the selector to add
     * @since Ant 1.6
     */
    public void add(FileSelector selector) {
        fileset.add(selector);
    }

    /**
     * Accessor for the implicit fileset.
     * @return the implicit fileset
     * @since Ant 1.5.2
     */
    protected final FileSet getImplicitFileSet() {
        return fileset;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13820.java