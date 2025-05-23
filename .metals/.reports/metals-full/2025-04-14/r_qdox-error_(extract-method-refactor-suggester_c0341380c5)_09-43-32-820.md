error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16980.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16980.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16980.java
text:
```scala
l@@oader = AntClassLoader.newAntClassLoader(getProject().getCoreLoader(),

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

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.AntClassLoader;

import java.net.URL;

/**
 * Find a class or resource on the supplied classpath, or the
 * system classpath if none is supplied. The named property is set if
 * the item can be found. For example
 * <pre>
 * &lt;whichresource resource="/log4j.properties"
 *   property="log4j.url" &gt;
 * </pre>
 * @since Ant 1.6
 * @ant.attribute.group name="oneof" description="Exactly one of these two"
 */
public class WhichResource extends Task {
    /**
     * our classpath
     */
    private Path classpath;

    /**
     * class to look for
     */
    private String classname;

    /**
     * resource to look for
     */
    private String resource;

    /**
     * property to set
     */
    private String property;


    /**
     * Set the classpath to be used for this compilation.
     * @param cp the classpath to be used.
     */
    public void setClasspath(Path cp) {
        if (classpath == null) {
            classpath = cp;
        } else {
            classpath.append(cp);
        }
    }

    /**
     * Adds a path to the classpath.
     * @return a classpath to be configured.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath.createPath();
    }

    /**
     * Set the classpath to use by reference.
     *
     * @param r a reference to an existing classpath.
     * @since Ant 1.7.1
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    /**
     * validate
     */
    private void validate() {
        int setcount = 0;
        if (classname != null) {
            setcount++;
        }
        if (resource != null) {
            setcount++;
        }


        if (setcount == 0) {
            throw new BuildException(
                    "One of classname or resource must be specified");
        }
        if (setcount > 1) {
            throw new BuildException(
                    "Only one of classname or resource can be specified");
        }
        if (property == null) {
            throw new BuildException("No property defined");
        }
    }

    /**
     * execute it
     * @throws BuildException on error
     */
    public void execute() throws BuildException {
        validate();
        if (classpath != null) {
            getProject().log("using user supplied classpath: " + classpath,
                    Project.MSG_DEBUG);
            classpath = classpath.concatSystemClasspath("ignore");
        } else {
            classpath = new Path(getProject());
            classpath = classpath.concatSystemClasspath("only");
            getProject().log("using system classpath: " + classpath, Project.MSG_DEBUG);
        }
        AntClassLoader loader;
        loader = new AntClassLoader(getProject().getCoreLoader(),
                    getProject(),
                    classpath, false);
        String loc = null;
        if (classname != null) {
            //convert a class name into a resource
            resource = classname.replace('.', '/') + ".class";
        }

        if (resource == null) {
            throw new BuildException("One of class or resource is required");
        }

        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }

        log("Searching for " + resource, Project.MSG_VERBOSE);
        URL url;
        url = loader.getResource(resource);
        if (url != null) {
            //set the property
            loc = url.toExternalForm();
            getProject().setNewProperty(property, loc);
        }
    }

    /**
     * name the resource to look for
     * @param resource the name of the resource to look for.
     * @ant.attribute group="oneof"
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * name the class to look for
     * @param classname the name of the class to look for.
     * @ant.attribute group="oneof"
     */
    public void setClass(String classname) {
        this.classname = classname;
    }

    /**
     * the property to fill with the URL of the resource or class
     * @param property the property to be set.
     * @ant.attribute group="required"
     */
    public void setProperty(String property) {
        this.property = property;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16980.java