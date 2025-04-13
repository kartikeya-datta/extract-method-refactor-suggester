error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6990.java
text:
```scala
i@@f (r.isDirectory() || contains.length() == 0) {

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.FileUtils;

/**
 * Selector that filters files/resources based on whether they contain a
 * particular string.
 *
 * @since 1.5
 */
public class ContainsSelector extends BaseExtendSelector implements ResourceSelector {

    private String contains = null;
    private boolean casesensitive = true;
    private boolean ignorewhitespace = false;
    /** Key to used for parameterized custom selector */
    public static final String EXPRESSION_KEY = "expression";
    /** Used for parameterized custom selector */
    public static final String CONTAINS_KEY = "text";
    /** Used for parameterized custom selector */
    public static final String CASE_KEY = "casesensitive";
    /** Used for parameterized custom selector */
    public static final String WHITESPACE_KEY = "ignorewhitespace";


    /**
     * Creates a new <code>ContainsSelector</code> instance.
     *
     */
    public ContainsSelector() {
    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{containsselector text: ");
        buf.append('"').append(contains).append('"');
        buf.append(" casesensitive: ");
        buf.append(casesensitive ? "true" : "false");
        buf.append(" ignorewhitespace: ");
        buf.append(ignorewhitespace ? "true" : "false");
        buf.append("}");
        return buf.toString();
    }

    /**
     * The string to search for within a file.
     *
     * @param contains the string that a file must contain to be selected.
     */
    public void setText(String contains) {
        this.contains = contains;
    }

    /**
     * Whether to ignore case in the string being searched.
     *
     * @param casesensitive whether to pay attention to case sensitivity
     */
    public void setCasesensitive(boolean casesensitive) {
        this.casesensitive = casesensitive;
    }

    /**
     * Whether to ignore whitespace in the string being searched.
     *
     * @param ignorewhitespace whether to ignore any whitespace
     *        (spaces, tabs, etc.) in the searchstring
     */
    public void setIgnorewhitespace(boolean ignorewhitespace) {
        this.ignorewhitespace = ignorewhitespace;
    }

    /**
     * When using this as a custom selector, this method will be called.
     * It translates each parameter into the appropriate setXXX() call.
     *
     * @param parameters the complete set of parameters for this selector
     */
    public void setParameters(Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                String paramname = parameters[i].getName();
                if (CONTAINS_KEY.equalsIgnoreCase(paramname)) {
                    setText(parameters[i].getValue());
                } else if (CASE_KEY.equalsIgnoreCase(paramname)) {
                    setCasesensitive(Project.toBoolean(
                            parameters[i].getValue()));
                } else if (WHITESPACE_KEY.equalsIgnoreCase(paramname)) {
                    setIgnorewhitespace(Project.toBoolean(
                            parameters[i].getValue()));
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks to make sure all settings are kosher. In this case, it
     * means that the pattern attribute has been set.
     *
     */
    public void verifySettings() {
        if (contains == null) {
            setError("The text attribute is required");
        }
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object the selector can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        return isSelected(new FileResource(file));
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a Resource.
     *
     * @param r the Resource to check.
     * @return whether the Resource is selected.
     */
    public boolean isSelected(Resource r) {

        // throw BuildException on error
        validate();

        if (r.isDirectory()) {
            return true;
        }

        String userstr = contains;
        if (!casesensitive) {
            userstr = contains.toLowerCase();
        }
        if (ignorewhitespace) {
            userstr = SelectorUtils.removeWhitespace(userstr);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(r.getInputStream()));
        } catch (Exception e) {
            throw new BuildException("Could not get InputStream from "
                    + r.toLongString(), e);
        }
        try {
            String teststr = in.readLine();
            while (teststr != null) {
                if (!casesensitive) {
                    teststr = teststr.toLowerCase();
                }
                if (ignorewhitespace) {
                    teststr = SelectorUtils.removeWhitespace(teststr);
                }
                if (teststr.indexOf(userstr) > -1) {
                    return true;
                }
                teststr = in.readLine();
            }
            return false;
        } catch (IOException ioe) {
            throw new BuildException("Could not read " + r.toLongString());
        } finally {
            FileUtils.close(in);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6990.java