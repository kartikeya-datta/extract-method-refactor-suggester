error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9101.java
text:
```scala
S@@tring msg = "An error occurred while reading from pattern file: "

/*
 * Copyright  2000-2004 The Apache Software Foundation
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

package org.apache.tools.ant.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Named collection of include/exclude tags.
 *
 * <p>Moved out of MatchingTask to make it a standalone object that
 * could be referenced (by scripts for example).
 *
 * @author Arnout J. Kuiper <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a>
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author Stefan Bodewig
 */
public class PatternSet extends DataType implements Cloneable {
    private Vector includeList = new Vector();
    private Vector excludeList = new Vector();
    private Vector includesFileList = new Vector();
    private Vector excludesFileList = new Vector();

    /**
     * inner class to hold a name on list.  "If" and "Unless" attributes
     * may be used to invalidate the entry based on the existence of a
     * property (typically set thru the use of the Available task).
     */
    public class NameEntry {
        private String name;
        private String ifCond;
        private String unlessCond;

        /**
         * Sets the name pattern.
         *
         * @param name The pattern string.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Sets the if attribute. This attribute and the "unless"
         * attribute are used to validate the name, based in the
         * existence of the property.
         *
         * @param cond A property name. If this property is not
         *             present, the name is invalid.
         */
        public void setIf(String cond) {
            ifCond = cond;
        }

        /**
         * Sets the unless attribute. This attribute and the "if"
         * attribute are used to validate the name, based in the
         * existence of the property.
         *
         * @param cond A property name. If this property is
         *             present, the name is invalid.
         */
        public void setUnless(String cond) {
            unlessCond = cond;
        }

        /**
         * @return the name attribute.
         */
        public String getName() {
            return name;
        }

        /**
         * This validates the name - checks the if and unless
         * properties.
         *
         * @param p the current project, used to check the presence or
         *          absence of a property.
         * @return  the name attribute or null if the "if" or "unless"
         *          properties are not/are set.
         */
        public String evalName(Project p) {
            return valid(p) ? name : null;
        }

        private boolean valid(Project p) {
            if (ifCond != null && p.getProperty(ifCond) == null) {
                return false;
            } else if (unlessCond != null && p.getProperty(unlessCond) != null) {
                return false;
            }
            return true;
        }

        /**
         * @return a printable form of this object.
         */
        public String toString() {
            if (name == null) {
                throw new BuildException(
                    "Missing attribute \"name\" for a pattern");
            }
            StringBuffer buf = new StringBuffer(name);
            if ((ifCond != null) || (unlessCond != null)) {
                buf.append(":");
                String connector = "";

                if (ifCond != null) {
                    buf.append("if->");
                    buf.append(ifCond);
                    connector = ";";
                }
                if (unlessCond != null) {
                    buf.append(connector);
                    buf.append("unless->");
                    buf.append(unlessCond);
                }
            }

            return buf.toString();
        }
    }

    /**
     * Creates a new <code>PatternSet</code> instance.
     */
    public PatternSet() {
        super();
    }

    /**
     * Makes this instance in effect a reference to another PatternSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     * @param r the reference to another patternset.
     * @throws BuildException on error.
     */
    public void setRefid(Reference r) throws BuildException {
        if (!includeList.isEmpty() || !excludeList.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * This is a patternset nested element.
     *
     * @param p a configured patternset nested element.
     */
    public void addConfiguredPatternset(PatternSet p) {
        if (isReference()) {
            throw noChildrenAllowed();
        }

        String[] nestedIncludes = p.getIncludePatterns(getProject());
        String[] nestedExcludes = p.getExcludePatterns(getProject());

        if (nestedIncludes != null) {
            for (int i = 0; i < nestedIncludes.length; i++) {
                createInclude().setName(nestedIncludes[i]);
            }
        }

        if (nestedExcludes != null) {
            for (int i = 0; i < nestedExcludes.length; i++) {
                createExclude().setName(nestedExcludes[i]);
            }
        }
    }

    /**
     * add a name entry on the include list
     * @return a nested include element to be configured.
     */
    public NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(includeList);
    }

    /**
     * add a name entry on the include files list
     * @return a nested includesfile element to be configured.
     */
    public NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(includesFileList);
    }

    /**
     * add a name entry on the exclude list
     * @return a nested exclude element to be configured.
     */
    public NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(excludeList);
    }

    /**
     * add a name entry on the exclude files list
     * @return a nested excludesfile element to be configured.
     */
    public NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(excludesFileList);
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
        if (includes != null && includes.length() > 0) {
            StringTokenizer tok = new StringTokenizer(includes, ", ", false);
            while (tok.hasMoreTokens()) {
                createInclude().setName(tok.nextToken());
            }
        }
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
        if (excludes != null && excludes.length() > 0) {
            StringTokenizer tok = new StringTokenizer(excludes, ", ", false);
            while (tok.hasMoreTokens()) {
                createExclude().setName(tok.nextToken());
            }
        }
    }

    /**
     * add a name entry to the given list
     */
    private NameEntry addPatternToList(Vector list) {
        NameEntry result = new NameEntry();
        list.addElement(result);
        return result;
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param includesFile The file to fetch the include patterns from.
     * @throws BuildException on error.
     */
     public void setIncludesfile(File includesFile) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }
         createIncludesFile().setName(includesFile.getAbsolutePath());
     }

    /**
     * Sets the name of the file containing the excludes patterns.
     *
     * @param excludesFile The file to fetch the exclude patterns from.
     * @throws BuildException on error.
     */
     public void setExcludesfile(File excludesFile) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }
         createExcludesFile().setName(excludesFile.getAbsolutePath());
     }

    /**
     *  Reads path matching patterns from a file and adds them to the
     *  includes or excludes list (as appropriate).
     */
    private void readPatterns(File patternfile, Vector patternlist, Project p)
        throws BuildException {

        BufferedReader patternReader = null;
        try {
            // Get a FileReader
            patternReader =
                new BufferedReader(new FileReader(patternfile));

            // Create one NameEntry in the appropriate pattern list for each
            // line in the file.
            String line = patternReader.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    line = p.replaceProperties(line);
                    addPatternToList(patternlist).setName(line);
                }
                line = patternReader.readLine();
            }
        } catch (IOException ioe)  {
            String msg = "An error occured while reading from pattern file: "
                + patternfile;
            throw new BuildException(msg, ioe);
        } finally {
            if (null != patternReader) {
                try {
                    patternReader.close();
                } catch (IOException ioe) {
                    //Ignore exception
                }
            }
        }
    }

    /**
     * Adds the patterns of the other instance to this set.
     * @param other the other PatternSet instance.
     * @param p the current project.
     */
    public void append(PatternSet other, Project p) {
        if (isReference()) {
            throw new BuildException("Cannot append to a reference");
        }

        String[] incl = other.getIncludePatterns(p);
        if (incl != null) {
            for (int i = 0; i < incl.length; i++) {
                createInclude().setName(incl[i]);
            }
        }

        String[] excl = other.getExcludePatterns(p);
        if (excl != null) {
            for (int i = 0; i < excl.length; i++) {
                createExclude().setName(excl[i]);
            }
        }
    }

    /**
     * Returns the filtered include patterns.
     * @param p the current project.
     * @return the filtered included patterns.
     */
    public String[] getIncludePatterns(Project p) {
        if (isReference()) {
            return getRef(p).getIncludePatterns(p);
        } else {
            readFiles(p);
            return makeArray(includeList, p);
        }
    }

    /**
     * Returns the filtered include patterns.
     * @param p the current project.
     * @return the filtered excluded patterns.
     */
    public String[] getExcludePatterns(Project p) {
        if (isReference()) {
            return getRef(p).getExcludePatterns(p);
        } else {
            readFiles(p);
            return makeArray(excludeList, p);
        }
    }

    /**
     * helper for FileSet.
     */
    boolean hasPatterns(Project p) {
        if (isReference()) {
            return getRef(p).hasPatterns(p);
        } else {
            return includesFileList.size() > 0 || excludesFileList.size() > 0
 includeList.size() > 0 || excludeList.size() > 0;
        }
    }

    /**
     * Performs the check for circular references and returns the
     * referenced PatternSet.
     */
    private PatternSet getRef(Project p) {
        if (!isChecked()) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, p);
        }

        Object o = getRefid().getReferencedObject(p);
        if (!(o instanceof PatternSet)) {
            String msg = getRefid().getRefId() + " doesn\'t denote a patternset";
            throw new BuildException(msg);
        } else {
            return (PatternSet) o;
        }
    }

    /**
     * Convert a vector of NameEntry elements into an array of Strings.
     */
    private String[] makeArray(Vector list, Project p) {
        if (list.size() == 0) {
          return null;
        }

        Vector tmpNames = new Vector();
        for (Enumeration e = list.elements(); e.hasMoreElements();) {
            NameEntry ne = (NameEntry) e.nextElement();
            String pattern = ne.evalName(p);
            if (pattern != null && pattern.length() > 0) {
                tmpNames.addElement(pattern);
            }
        }

        String[] result = new String[tmpNames.size()];
        tmpNames.copyInto(result);
        return result;
    }

    /**
     * Read includesfile ot excludesfile if not already done so.
     */
    private void readFiles(Project p) {
        if (includesFileList.size() > 0) {
            Enumeration e = includesFileList.elements();
            while (e.hasMoreElements()) {
                NameEntry ne = (NameEntry) e.nextElement();
                String fileName = ne.evalName(p);
                if (fileName != null) {
                    File inclFile = p.resolveFile(fileName);
                    if (!inclFile.exists()) {
                        throw new BuildException("Includesfile "
                                                 + inclFile.getAbsolutePath()
                                                 + " not found.");
                    }
                    readPatterns(inclFile, includeList, p);
                }
            }
            includesFileList.removeAllElements();
        }

        if (excludesFileList.size() > 0) {
            Enumeration e = excludesFileList.elements();
            while (e.hasMoreElements()) {
                NameEntry ne = (NameEntry) e.nextElement();
                String fileName = ne.evalName(p);
                if (fileName != null) {
                    File exclFile = p.resolveFile(fileName);
                    if (!exclFile.exists()) {
                        throw new BuildException("Excludesfile "
                                                 + exclFile.getAbsolutePath()
                                                 + " not found.");
                    }
                    readPatterns(exclFile, excludeList, p);
                }
            }
            excludesFileList.removeAllElements();
        }
    }

    /**
     * @return a printable form of this object.
     */
    public String toString() {
        return "patternSet{ includes: " + includeList
                + " excludes: " + excludeList + " }";
    }

    /**
     * @since Ant 1.6
     * @return a clone of this patternset.
     */
    public Object clone() {
        if (isReference()) {
            return getRef(getProject()).clone();
        } else {
            try {
                PatternSet ps = (PatternSet) super.clone();
                ps.includeList = (Vector) includeList.clone();
                ps.excludeList = (Vector) excludeList.clone();
                ps.includesFileList = (Vector) includesFileList.clone();
                ps.excludesFileList = (Vector) excludesFileList.clone();
                return ps;
            } catch (CloneNotSupportedException e) {
                throw new BuildException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9101.java