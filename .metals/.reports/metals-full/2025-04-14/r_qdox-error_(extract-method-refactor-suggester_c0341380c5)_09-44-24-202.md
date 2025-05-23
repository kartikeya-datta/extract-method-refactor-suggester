error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8351.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8351.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8351.java
text:
```scala
S@@tringBuffer buf = new StringBuffer(name != null ? name : "");

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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
 * 4. The names "Ant" and "Apache Software
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

        public void setName(String name) {
            this.name = name;
        }

        public void setIf(String cond) {
            ifCond = cond;
        }

        public void setUnless(String cond) {
            unlessCond = cond;
        }

        public String getName() {
            return name;
        }

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

        public String toString() {
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

    public PatternSet() {
        super();
    }

    /**
     * Makes this instance in effect a reference to another PatternSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     */
    public void setRefid(Reference r) throws BuildException {
        if (!includeList.isEmpty() || !excludeList.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

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
     */
    public NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(includeList);
    }

    /**
     * add a name entry on the include files list
     */
    public NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(includesFileList);
    }

    /**
     * add a name entry on the exclude list
     */
    public NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return addPatternToList(excludeList);
    }

    /**
     * add a name entry on the exclude files list
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

    public String toString() {
        return "patternSet{ includes: " + includeList
                + " excludes: " + excludeList + " }";
    }

    /**
     * @since Ant 1.6
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8351.java