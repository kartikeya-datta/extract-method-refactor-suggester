error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2240.java
text:
```scala
public v@@oid setCurrentTargets(Map currentTargets) {

/*
 * Copyright  2003-2005 The Apache Software Foundation
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
package org.apache.tools.ant.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.Locator;
import org.xml.sax.Attributes;


import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.RuntimeConfigurable;


/**
 * Context information for the ant processing.
 *
 */
public class AntXMLContext {
    /** The project to configure. */
    private Project project;

    /** The configuration file to parse. */
    private File buildFile;

    /** Vector with all the targets, in the order they are
     * defined. Project maintains a Hashtable, which is not ordered.
     * This will allow description to know the original order.
     */
    private Vector targetVector = new Vector();

    /**
     * Parent directory of the build file. Used for resolving entities
     * and setting the project's base directory.
     */
    private File buildFileParent;

    /** Name of the current project */
    private String currentProjectName;

    /**
     * Locator for the configuration file parser.
     * Used for giving locations of errors etc.
     */
    private Locator locator;

     /**
      * Target that all other targets will depend upon implicitly.
      *
      * <p>This holds all tasks and data type definitions that have
      * been placed outside of targets.</p>
      */
    private Target implicitTarget = new Target();

    /** Current target ( no need for a stack as the processing model
        allows only one level of target ) */
    private Target currentTarget = null;

    /** The stack of RuntimeConfigurable2 wrapping the
        objects.
    */
    private Vector wStack = new Vector();

    /**
     * Indicates whether the project tag attributes are to be ignored
     * when processing a particular build file.
     */
    private boolean ignoreProjectTag = false;

    /** Keeps track of prefix -> uri mapping during parsing */
    private Map prefixMapping = new HashMap();


    /** Keeps track of targets in files */
    private Map currentTargets = null;

    /**
     * constructor
     * @param project the project to which this antxml context belongs to
     */
    public AntXMLContext(Project project) {
        this.project = project;
        implicitTarget.setProject(project);
        implicitTarget.setName("");
        targetVector.addElement(implicitTarget);
    }

    /**
     * sets the build file to which the XML context belongs
     * @param buildFile  ant build file
     */
    public void setBuildFile(File buildFile) {
        this.buildFile = buildFile;
        this.buildFileParent = new File(buildFile.getParent());
        implicitTarget.setLocation(new Location(buildFile.getAbsolutePath()));
    }

    /**
     * find out the build file
     * @return  the build file to which the xml context belongs
     */
    public File getBuildFile() {
        return buildFile;
    }

    /**
     * find out the parent build file of this build file
     * @return the parent build file of this build file
     */
    public File getBuildFileParent() {
        return buildFileParent;
    }

    /**
     * find out the project to which this antxml context belongs
     * @return project
     */
    public Project getProject() {
        return project;
    }

    /**
     * find out the current project name
     * @return current project name
     */
    public String getCurrentProjectName() {
        return currentProjectName;
    }

    /**
     * set the name of the current project
     * @param name name of the current project
     */
    public void setCurrentProjectName(String name) {
        this.currentProjectName = name;
    }

    /**
     * get the current runtime configurable wrapper
     * can return null
     * @return runtime configurable wrapper
     */
    public RuntimeConfigurable currentWrapper() {
        if (wStack.size() < 1) {
            return null;
        }
        return (RuntimeConfigurable) wStack.elementAt(wStack.size() - 1);
    }

    /**
     * get the runtime configurable wrapper of the parent project
     * can return null
     * @return runtime configurable wrapper  of the parent project
     */
    public RuntimeConfigurable parentWrapper() {
        if (wStack.size() < 2) {
            return null;
        }
        return (RuntimeConfigurable) wStack.elementAt(wStack.size() - 2);
    }

    /**
     * add a runtime configurable wrapper to the internal stack
     * @param wrapper runtime configurable wrapper
     */
    public void pushWrapper(RuntimeConfigurable wrapper) {
        wStack.addElement(wrapper);
    }

    /**
     * remove a runtime configurable wrapper from the stack
     */
    public void popWrapper() {
        if (wStack.size() > 0) {
            wStack.removeElementAt(wStack.size() - 1);
        }
    }

    /**
     * access the stack of wrappers
     * @return the stack of wrappers
     */
    public Vector getWrapperStack() {
        return wStack;
    }

    /**
     * add a new target
     * @param target target to add
     */
    public void addTarget(Target target) {
        targetVector.addElement(target);
        currentTarget = target;
    }

    /**
     * get the current target
     * @return current target
     */
    public Target getCurrentTarget() {
        return currentTarget;
    }

    /**
     * get the implicit target
     * @return implicit target
     */
    public Target getImplicitTarget() {
        return implicitTarget;
    }

    /**
     * sets the current target
     * @param target current target
     */
    public void setCurrentTarget(Target target) {
        this.currentTarget = target;
    }

    /**
     * sets the implicit target
     * @param target the implicit target
     */
    public void setImplicitTarget(Target target) {
        this.implicitTarget = target;
    }

    /**
     * access the vector of targets
     * @return vector of targets
     */
    public Vector getTargets() {
        return targetVector;
    }

    /**
     * Scans an attribute list for the <code>id</code> attribute and
     * stores a reference to the target object in the project if an
     * id is found.
     * <p>
     * This method was moved out of the configure method to allow
     * it to be executed at parse time.
     * @param element the current element
     * @param attr attributes of the current element
     */
    public void configureId(Object element, Attributes attr) {
        String id = attr.getValue("id");
        if (id != null) {
            project.addReference(id, element);
        }
    }

    /**
     * access the locator
     * @return locator
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * sets the locator
     * @param locator locator
     */
    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * tells whether the project tag is being ignored
     * @return whether the project tag is being ignored
     */
    public boolean isIgnoringProjectTag() {
        return ignoreProjectTag;
    }

    /**
     *  sets the flag to ignore the project tag
     * @param flag to ignore the project tag
     */
    public void setIgnoreProjectTag(boolean flag) {
        this.ignoreProjectTag = flag;
    }

    /**
     * Called during parsing, stores the prefix to uri mapping.
     *
     * @param prefix a namespace prefix
     * @param uri    a namespace uri
     */
    public void startPrefixMapping(String prefix, String uri) {
        List list = (List) prefixMapping.get(prefix);
        if (list == null) {
            list = new ArrayList();
            prefixMapping.put(prefix, list);
        }
        list.add(uri);
    }

    /**
     * End of prefix to uri mapping.
     *
     * @param prefix the namespace prefix
     */
    public void endPrefixMapping(String prefix) {
        List list = (List) prefixMapping.get(prefix);
        if (list == null || list.size() == 0) {
            return; // Should not happen
        }
        list.remove(list.size() - 1);
    }

    /**
     * prefix to namespace uri mapping
     *
     * @param prefix the prefix to map
     * @return the uri for this prefix, null if not present
     */
    public String getPrefixMapping(String prefix) {
        List list = (List) prefixMapping.get(prefix);
        if (list == null || list.size() == 0) {
            return null;
        }
        return (String) list.get(list.size() - 1);
    }

    /**
     * Get the targets in the current source file.
     * @return the current targets.
     */
    public Map getCurrentTargets() {
        return currentTargets;
    }

    /**
     * Set the map of the targets in the current source file.
     * @param currentTargets a map of targets.
     */
    void setCurrentTargets(Map currentTargets) {
        this.currentTargets = currentTargets;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2240.java