error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10387.java
text:
```scala
S@@tring args = "-noexit " + sourceJar.getPath() + " " + destJar.getPath();

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
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
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
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

import java.io.*;
import java.util.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.taskdefs.Java;

public class WeblogicDeploymentTool extends GenericDeploymentTool {
    protected static final String WL_DD     = "weblogic-ejb-jar.xml";
    protected static final String WL_CMP_DD = "weblogic-cmp-rdbms-jar.xml";

    /** Instance variable that stores the suffix for the weblogic jarfile. */
    private String jarSuffix = "-wl.jar";

    private Path classpath;

    /** Instance variable that determines whether generic ejb jars are kept. */
    private boolean keepgeneric = true;
    
    /**
     * Set the classpath to be used for this compilation.
     */
    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    /**
     * Setter used to store the suffix for the generated weblogic jar file.
     * @param inString the string to use as the suffix.
     */
    public void setSuffix(String inString) {
        this.jarSuffix = inString;
    }

    /**
     * Setter used to store the value of keepgeneric
     * @param inValue a string, either 'true' or 'false'.
     */
    public void setKeepgeneric(String inValue) {
        this.keepgeneric = Boolean.valueOf(inValue).booleanValue();
    }
    
    protected DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler handler = new DescriptorHandler(srcDir);
        handler.registerResourceDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN",
                                    "/weblogic/ejb/deployment/xml/ejb-jar.dtd");
        return handler;                                    
    }

    /**
     * Add any vendor specific files which should be included in the 
     * EJB Jar.
     */
    protected void addVendorFiles(Hashtable ejbFiles, File srcdir, String baseName) {
        // Then the weblogic deployment descriptor
        File weblogicDD = new File(srcdir,
                              baseName + getBasenameTerminator() + WL_DD);

        if (weblogicDD.exists()) {
            ejbFiles.put(META_DIR + WL_DD,
                         weblogicDD);
        }

        // The the weblogic cmp deployment descriptor
        File weblogicCMPDD = new File(srcdir,
                              baseName + getBasenameTerminator() + WL_CMP_DD);

        if (weblogicCMPDD.exists()) {
            ejbFiles.put(META_DIR + WL_CMP_DD,
                         weblogicCMPDD);
        }
    }
    
    /**
     * Get the vendor specific name of the Jar that will be output. The modification date
     * of this jar will be checked against the dependent bean classes.
     */
    File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName + jarSuffix);
    }

    /**
     * Helper method invoked by execute() for each WebLogic jar to be built.
     * Encapsulates the logic of constructing a java task for calling
     * weblogic.ejbc and executing it.
     * @param sourceJar java.io.File representing the source (EJB1.1) jarfile.
     * @param destJar java.io.File representing the destination, WebLogic
     *        jarfile.
     */
    private void buildWeblogicJar(File sourceJar, File destJar) {
        org.apache.tools.ant.taskdefs.Java javaTask = null;
        
        try {
            String args = "-noexit " + sourceJar.getPath().replace('\\', '/') + " " + destJar.getPath().replace('\\', '/');
            
            javaTask = (Java) getTask().getProject().createTask("java");
            javaTask.setClassname("weblogic.ejbc");
            javaTask.setArgs(args);
            if (classpath != null) {
                javaTask.setClasspath(classpath);
                javaTask.setFork(true);
            }
            else {
                javaTask.setFork(false);
            }
            

            getTask().log("Calling weblogic.ejbc for " + sourceJar.toString(),
                          Project.MSG_VERBOSE);

            javaTask.execute();
        }
        catch (Exception e) {
            // Have to catch this because of the semantics of calling main()
            String msg = "Exception while calling ejbc. Details: " + e.toString();
            throw new BuildException(msg, e);
        }
    }

    /**
     * Method used to encapsulate the writing of the JAR file. Iterates over the
     * filenames/java.io.Files in the Hashtable stored on the instance variable
     * ejbFiles.
     */
    protected void writeJar(String baseName, File jarFile, Hashtable files) throws BuildException {
        // need to create a generic jar first.
        File genericJarFile = super.getVendorOutputJarFile(baseName);
        super.writeJar(baseName, genericJarFile, files);
        
        buildWeblogicJar(genericJarFile, jarFile);
        if (!keepgeneric) {
             getTask().log("deleting generic jar " + genericJarFile.toString(),
                           Project.MSG_VERBOSE);
             genericJarFile.delete();
        }
    }

    /**
     * Called to validate that the tool parameters have been configured.
     *
     */
    public void validateConfigured() throws BuildException {
        super.validateConfigured();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10387.java