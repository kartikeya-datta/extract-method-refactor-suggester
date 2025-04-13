error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6394.java
text:
```scala
p@@1.setJavaVersionProperty();

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
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

package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;

/**
 * Call Ant in a sub-project
 *
 *  <pre>
 *    <target name="foo" depends="init">
 *    <ant antfile="build.xml" target="bar" >
 *      <property name="property1" value="aaaaa" />
 *      <property name="foo" value="baz" />
 *     </ant>
 *  </target>
 *
 * <target name="bar" depends="init">
 *    <echo message="prop is ${property1} ${foo}" />
 * </target>
 * </pre>
 *
 *
 * @author costin@dnt.ro
 */
public class Ant extends Task {

    private File dir = null;
    private String antFile = null;
    private String target = null;
    private String output = null;

    Vector properties=new Vector();
    Project p1;

    public void init() {
        p1 = new Project();
        p1.detectJavaVersion();
        p1.addTaskDefinition("property", 
                             (Class)project.getTaskDefinitions().get("property"));
    }

    private void reinit() {
        init();
        for (int i=0; i<properties.size(); i++) {
            Property p = (Property) properties.elementAt(i);
            Property newP = (Property) p1.createTask("property");
            newP.setName(p.getName());
            if (p.getValue() != null) {
                newP.setValue(p.getValue());
            } else if (p.getFile() != null) {
                newP.setFile(p.getFile());
            } else if (p.getResource() != null) {
                newP.setResource(p.getResource());
            }
            properties.setElementAt(newP, i);
        }
    }

    private void initializeProject() {
        Vector listeners = project.getBuildListeners();
        for (int i = 0; i < listeners.size(); i++) {
            p1.addBuildListener((BuildListener)listeners.elementAt(i));
        }

        if (output != null) {
            try {
                PrintStream out = new PrintStream(new FileOutputStream(output));
                DefaultLogger logger = new DefaultLogger();
                logger.setMessageOutputLevel(Project.MSG_INFO);
                logger.setOutputPrintStream(out);
                p1.addBuildListener(logger);
            }
            catch( IOException ex ) {
                log( "Ant: Can't set output to " + output );
            }
        }

        Hashtable taskdefs = project.getTaskDefinitions();
        Enumeration et = taskdefs.keys();
        while (et.hasMoreElements()) {
            String taskName = (String) et.nextElement();
            Class taskClass = (Class) taskdefs.get(taskName);
            p1.addTaskDefinition(taskName, taskClass);
        }

        // set user-define properties
        Hashtable prop1 = project.getProperties();
        Enumeration e = prop1.keys();
        while (e.hasMoreElements()) {
            String arg = (String) e.nextElement();
            String value = (String) prop1.get(arg);
            p1.setProperty(arg, value);
        }
    }

    /**
     * Do the execution.
     */
    public void execute() throws BuildException {
        try {
            if (p1 == null) {
                reinit();
            }
        
            if(dir == null) 
                dir = project.getBaseDir();

            initializeProject();

            p1.setBaseDir(dir);
            p1.setUserProperty("basedir" , dir.getAbsolutePath());
            
            // Override with local-defined properties
            Enumeration e = properties.elements();
            while (e.hasMoreElements()) {
                Property p=(Property) e.nextElement();
                p.init();
            }
            
            if (antFile == null) 
                antFile = "build.xml";

            antFile = (new File(dir, antFile)).getAbsolutePath();

            p1.setUserProperty( "ant.file" , antFile );
            ProjectHelper.configureProject(p1, new File(antFile));
            
            if (target == null) {
                target = p1.getDefaultTarget();
            }

            p1.executeTarget(target);
        } finally {
            // help the gc
            p1 = null;
        }
    }

    public void setDir(File d) {
        this.dir = d;
    }

    public void setAntfile(String s) {
        this.antFile = s;
    }

    public void setTarget(String s) {
        this.target = s;
    }

    public void setOutput(String s) {
        this.output = s;
    }

    public Property createProperty() {
        if (p1 == null) {
            reinit();
        }

	Property p=(Property)p1.createTask("property");
	p.setUserProperty(true);
	properties.addElement( p );
	return p;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6394.java