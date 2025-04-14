error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13830.java
text:
```scala
S@@tringBuilder buf = new StringBuilder(

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
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;

/**
 * Selector that filters files based on a regular expression.
 *
 * @since Ant 1.6
 */
public class ContainsRegexpSelector extends BaseExtendSelector
        implements ResourceSelector {

    private String userProvidedExpression = null;
    private RegularExpression myRegExp = null;
    private Regexp myExpression = null;
    private boolean caseSensitive = true;
    private boolean multiLine = false;
    private boolean singleLine = false;
    /** Key to used for parameterized custom selector */
    public static final String EXPRESSION_KEY = "expression";
    /** Parameter name for the casesensitive attribute. */
    private static final String CS_KEY = "casesensitive";
    /** Parameter name for the multiline attribute. */
    private static final String ML_KEY = "multiline";
    /** Parameter name for the singleline attribute. */
    private static final String SL_KEY = "singleline";

    /**
     * Creates a new <code>ContainsRegexpSelector</code> instance.
     */
    public ContainsRegexpSelector() {
    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(
                "{containsregexpselector expression: ");
        buf.append(userProvidedExpression);
        buf.append("}");
        return buf.toString();
    }

    /**
     * The regular expression used to search the file.
     *
     * @param theexpression this must match a line in the file to be selected.
     */
    public void setExpression(String theexpression) {
        this.userProvidedExpression = theexpression;
    }

    /**
     * Whether to ignore case or not.
     * @param b if false, ignore case.
     * @since Ant 1.8.2
     */
    public void setCaseSensitive(boolean b) {
        caseSensitive = b;
    }

    /**
     * Whether to match should be multiline.
     * @param b the value to set.
     * @since Ant 1.8.2
     */
    public void setMultiLine(boolean b) {
        multiLine = b;
    }

    /**
     * Whether to treat input as singleline ('.' matches newline).
     * Corresponds to java.util.regex.Pattern.DOTALL.
     * @param b the value to set.
     * @since Ant 1.8.2
     */
    public void setSingleLine(boolean b) {
        singleLine = b;
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
                if (EXPRESSION_KEY.equalsIgnoreCase(paramname)) {
                    setExpression(parameters[i].getValue());
                } else if (CS_KEY.equalsIgnoreCase(paramname)) {
                    setCaseSensitive(Project
                                     .toBoolean(parameters[i].getValue()));
                } else if (ML_KEY.equalsIgnoreCase(paramname)) {
                    setMultiLine(Project.toBoolean(parameters[i].getValue()));
                } else if (SL_KEY.equalsIgnoreCase(paramname)) {
                    setSingleLine(Project.toBoolean(parameters[i].getValue()));
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks that an expression was specified.
     *
     */
    public void verifySettings() {
        if (userProvidedExpression == null) {
            setError("The expression attribute is required");
        }
    }

    /**
     * Tests a regular expression against each line of text in the file.
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
     * Tests a regular expression against each line of text in a Resource.
     *
     * @param r the Resource to check.
     * @return whether the Resource is selected or not
     */
    public boolean isSelected(Resource r) {
        String teststr = null;
        BufferedReader in = null;

        // throw BuildException on error

        validate();

        if (r.isDirectory()) {
            return true;
        }

        if (myRegExp == null) {
            myRegExp = new RegularExpression();
            myRegExp.setPattern(userProvidedExpression);
            myExpression = myRegExp.getRegexp(getProject());
        }

        try {
            in = new BufferedReader(new InputStreamReader(r.getInputStream()));
        } catch (Exception e) {
            throw new BuildException("Could not get InputStream from "
                    + r.toLongString(), e);
        }
        try {
            teststr = in.readLine();

            while (teststr != null) {

                if (myExpression.matches(teststr,
                                         RegexpUtil.asOptions(caseSensitive,
                                                              multiLine,
                                                              singleLine))) {
                    return true;
                }
                teststr = in.readLine();
            }

            return false;
        } catch (IOException ioe) {
            throw new BuildException("Could not read " + r.toLongString());
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                throw new BuildException("Could not close "
                                         + r.toLongString());
            }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13830.java