error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2366.java
text:
```scala
c@@lasspath = new Path(getProject());

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
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

package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.BuildException;

/**
 * Compiles EJB stubs and skeletons for the iPlanet Application Server.
 * The EJBs to be processed are specified by the EJB 1.1 standard XML
 * descriptor, and additional attributes are obtained from the iPlanet Application
 * Server-specific XML descriptor.  Since the XML descriptors can include
 * multiple EJBs, this is a convenient way of specifying many EJBs in a single
 * Ant task.  The following attributes are allowed:
 *   <ul>
 *     <li><i>ejbdescriptor</i> -- Standard EJB 1.1 XML descriptor (typically
 *                                 titled "ejb-jar.xml").  This attribute is
 *                                 required.
 *     <li><i>iasdescriptor</i> -- EJB XML descriptor for iPlanet Application
 *                                 Server (typically titled "ias-ejb-jar.xml).
 *                                 This attribute is required.
 *     <li><i>dest</i> -- The is the base directory where the RMI stubs and
 *                        skeletons are written.  In addition, the class files
 *                        for each bean (home interface, remote interface, and
 *                        EJB implementation) must be found in this directory.
 *                        This attribute is required.
 *     <li><i>classpath</i> -- The classpath used when generating EJB stubs and
 *                             skeletons.  This is an optional attribute (if
 *                             omitted, the classpath specified when Ant was
 *                             started will be used).  Nested "classpath"
 *                             elements may also be used.
 *     <li><i>keepgenerated</i> -- Indicates whether or not the Java source
 *                                 files which are generated by ejbc will be
 *                                 saved or automatically deleted.  If "yes",
 *                                 the source files will be retained.  This is
 *                                 an optional attribute (if omitted, it
 *                                 defaults to "no").
 *     <li><i>debug</i> -- Indicates whether or not the ejbc utility should
 *                         log additional debugging statements to the standard
 *                         output.  If "yes", the additional debugging statements
 *                         will be generated (if omitted, it defaults to "no").
 *     <li><i>iashome</i> -- May be used to specify the "home" directory for
 *                           this iPlanet Application Server installation.  This
 *                           is used to find the ejbc utility if it isn't
 *                           included in the user's system path.  This is an
 *                           optional attribute (if specified, it should refer
 *                           to the <code>[install-location]/iplanet/ias6/ias
 *                           </code> directory).  If omitted, the ejbc utility
 *                           must be on the user's system path.
 *   </ul>
 * <p>
 * For each EJB specified, this task will locate the three classes that comprise
 * the EJB.  If these class files cannot be located in the <code>dest</code>
 * directory, the task will fail.  The task will also attempt to locate the EJB
 * stubs and skeletons in this directory.  If found, the timestamps on the
 * stubs and skeletons will be checked to ensure they are up to date.  Only if
 * these files cannot be found or if they are out of date will ejbc be called
 * to generate new stubs and skeletons.
 *
 * @see    IPlanetEjbc
 * @author Greg Nelson <a href="mailto:greg@netscape.com">greg@netscape.com</a>
 *
 * @ant.task name="iplanet-ejbc" category="ejb"
 */
public class IPlanetEjbcTask extends Task {

    /* Attributes set by the Ant build file */
    private File    ejbdescriptor;
    private File    iasdescriptor;
    private File    dest;
    private Path    classpath;
    private boolean keepgenerated = false;
    private boolean debug         = false;
    private File    iashome;

    /**
     * Sets the location of the standard XML EJB descriptor.  Typically, this
     * file is named "ejb-jar.xml".
     *
     * @param ejbdescriptor The name and location of the EJB descriptor.
     */
    public void setEjbdescriptor(File ejbdescriptor) {
        this.ejbdescriptor = ejbdescriptor;
    }

    /**
     * Sets the location of the iAS-specific XML EJB descriptor.  Typically,
     * this file is named "ias-ejb-jar.xml".
     *
     * @param iasdescriptor The name and location of the iAS-specific EJB
     *                      descriptor.
     */
    public void setIasdescriptor (File iasdescriptor) {
        this.iasdescriptor = iasdescriptor;
    }

    /**
     * Sets the destination directory where the EJB source classes must exist
     * and where the stubs and skeletons will be written.  The destination
     * directory must exist before this task is executed.
     *
     * @param dest The directory where the compiled classes will be written.
     */
    public void setDest(File dest) {
        this.dest = dest;
    }

    /**
     * Sets the classpath to be used when compiling the EJB stubs and skeletons.
     *
     * @param classpath The classpath to be used.
     */
    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    /**
     * Adds to the classpath used when compiling the EJB stubs and skeletons.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }
        return classpath.createPath();
    }

    /**
     * If true, the Java source files which are generated by ejbc will be saved .
     *
     * @param keepgenerated A boolean indicating if the Java source files for
     *                      the stubs and skeletons should be retained.
     */
    public void setKeepgenerated(boolean keepgenerated) {
        this.keepgenerated = keepgenerated;
    }

    /**
     * If true, debugging output will be generated when ejbc is
     * executed.
     *
     * @param debug A boolean indicating if debugging output should be generated
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * May be used to specify the "home" directory for this iAS installation.
     * The directory specified should typically be
     * <code>[install-location]/iplanet/ias6/ias</code>.
     *
     * @param iashome The home directory for the user's iAS installation.
     */
    public void setIashome(File iashome) {
        this.iashome = iashome;
    }

    /**
     * Does the work.
     */
    public void execute() throws BuildException {
        checkConfiguration();

        executeEjbc(getParser());
    }

    /**
     * Verifies that the user selections are valid.
     *
     * @throws BuildException If the user selections are invalid.
     */
    private void checkConfiguration() throws BuildException {

        if (ejbdescriptor == null) {
            String msg = "The standard EJB descriptor must be specified using "
                            + "the \"ejbdescriptor\" attribute.";
            throw new BuildException(msg, location);
        }
        if ((!ejbdescriptor.exists()) || (!ejbdescriptor.isFile())) {
            String msg = "The standard EJB descriptor (" + ejbdescriptor
                            + ") was not found or isn't a file.";
            throw new BuildException(msg, location);
        }

        if (iasdescriptor == null) {
            String msg = "The iAS-speific XML descriptor must be specified using"
                            + " the \"iasdescriptor\" attribute.";
            throw new BuildException(msg, location);
        }
        if ((!iasdescriptor.exists()) || (!iasdescriptor.isFile())) {
            String msg = "The iAS-specific XML descriptor (" + iasdescriptor
                            + ") was not found or isn't a file.";
            throw new BuildException(msg, location);
        }

        if (dest == null) {
            String msg = "The destination directory must be specified using "
                            + "the \"dest\" attribute.";
            throw new BuildException(msg, location);
        }
        if ((!dest.exists()) || (!dest.isDirectory())) {
            String msg = "The destination directory (" + dest + ") was not "
                            + "found or isn't a directory.";
            throw new BuildException(msg, location);
        }

        if ((iashome != null) && (!iashome.isDirectory())) {
            String msg = "If \"iashome\" is specified, it must be a valid "
                            + "directory (it was set to " + iashome + ").";
            throw new BuildException(msg, getLocation());
        }
    }

    /**
     * Returns a SAXParser that may be used to process the XML descriptors.
     *
     * @return Parser which may be used to process the EJB descriptors.
     * @throws BuildException If the parser cannot be created or configured.
     */
    private SAXParser getParser() throws BuildException {

        SAXParser saxParser = null;
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(true);
            saxParser = saxParserFactory.newSAXParser();
        } catch (SAXException e) {
            String msg = "Unable to create a SAXParser: " + e.getMessage();
            throw new BuildException(msg, e, location);
        } catch (ParserConfigurationException e) {
            String msg = "Unable to create a SAXParser: " + e.getMessage();
            throw new BuildException(msg, e, location);
        }

        return saxParser;
    }

    /**
     * Executes the EJBc utility using the SAXParser provided.
     *
     * @param saxParser SAXParser that may be used to process the EJB
     *                  descriptors
     * @throws BuildException If there is an error reading or parsing the XML
     *                        descriptors
     */
    private void executeEjbc(SAXParser saxParser) throws BuildException {
        IPlanetEjbc ejbc = new IPlanetEjbc(ejbdescriptor,
                                            iasdescriptor,
                                            dest,
                                            getClasspath().toString(),
                                            saxParser);
        ejbc.setRetainSource(keepgenerated);
        ejbc.setDebugOutput(debug);
        if (iashome != null) {
            ejbc.setIasHomeDir(iashome);
        }

        try {
            ejbc.execute();
        } catch (IOException e) {
            String msg = "An IOException occurred while trying to read the XML "
                            + "descriptor file: " + e.getMessage();
            throw new BuildException(msg, e, location);
        } catch (SAXException e) {
            String msg = "A SAXException occurred while trying to read the XML "
                            + "descriptor file: " + e.getMessage();
            throw new BuildException(msg, e, location);
        } catch (IPlanetEjbc.EjbcException e) {
            String msg = "An exception occurred while trying to run the ejbc "
                            + "utility: " + e.getMessage();
            throw new BuildException(msg, e, location);
        }
    }

    /**
     * Returns the CLASSPATH to be used when calling EJBc.  If no user CLASSPATH
     * is specified, the System classpath is returned instead.
     *
     * @return Path The classpath to be used for EJBc.
     */
    private Path getClasspath() {
        if (classpath == null) {
            classpath = Path.systemClasspath;
        }

        return classpath;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2366.java