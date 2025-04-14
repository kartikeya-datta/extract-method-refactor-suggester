error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15724.java
text:
```scala
S@@tring contents = FileUtils.safeReadFully(reader);

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
package org.apache.tools.ant.taskdefs.condition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;

/**
 * &lt;resourcecontains&gt;
 * Is a string contained in a resource (file currently)?
 * @since Ant 1.7.1
 */
public class ResourceContains implements Condition {

    private Project project;
    private String substring;
    private Resource resource;
    private String refid;
    private boolean casesensitive = true;

    /**
     * Set this condition's Project.
     * @param project Project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Get this condition's Project.
     * @return Project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the resource to search
     * @param r the value to use.
     */
    public void setResource(String r) {
        this.resource = new FileResource(new File(r));
    }

    /**
     * Sets the refid to search; should indicate a resource directly
     * or by way of a single-element ResourceCollection.
     * @param refid the value to use.
     */
    public void setRefid(String refid) {
        this.refid = refid;
    }

    private void resolveRefid() {
        try {
            if (getProject() == null) {
                throw new BuildException("Cannot retrieve refid; project unset");
            }
            Object o = getProject().getReference(refid);
            if (!(o instanceof Resource)) {
                if (o instanceof ResourceCollection) {
                    ResourceCollection rc = (ResourceCollection) o;
                    if (rc.size() == 1) {
                        o = rc.iterator().next();
                    }
                } else {
                    throw new BuildException(
                        "Illegal value at '" + refid + "': " + String.valueOf(o));
                }
            }
            this.resource = (Resource) o;
        } finally {
            refid = null;
        }
    }

    /**
     * Sets the substring to look for
     * @param substring the value to use.
     */
    public void setSubstring(String substring) {
        this.substring = substring;
    }

    /**
     * Sets case sensitivity attribute.
     * @param casesensitive the value to use.
     */
    public void setCasesensitive(boolean casesensitive) {
        this.casesensitive = casesensitive;
    }

    private void validate() {
        if (resource != null && refid != null) {
            throw new BuildException("Cannot set both resource and refid");
        }
        if (resource == null && refid != null) {
            resolveRefid();
        }
        if (resource == null || substring == null) {
            throw new BuildException("both resource and substring are required "
                                     + "in <resourcecontains>");
        }
    }

    /**
     * Evaluates the condition.
     * @return true if the substring is contained in the resource
     * @throws BuildException if there is a problem.
     */
    public synchronized boolean eval() throws BuildException {
        validate();

        if (substring.length() == 0) {
            if (getProject() != null) {
                getProject().log("Substring is empty; returning true",
                                 Project.MSG_VERBOSE);
            }
            return true;
        }
        if (resource.getSize() == 0) {
            return false;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String contents = FileUtils.readFully(reader);
            String sub = substring;
            if (!casesensitive) {
                contents = contents.toLowerCase();
                sub = sub.toLowerCase();
            }
            return contents.indexOf(sub) >= 0;
        } catch (IOException e) {
            throw new BuildException("There was a problem accessing resource : " + resource);
        } finally {
            FileUtils.close(reader);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15724.java