error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9659.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9659.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9659.java
text:
```scala
i@@f (!found) {

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
/*
* Portions of this software are based upon public domain software
* originally written at the National Center for Supercomputing Applications,
* University of Illinois, Urbana-Champaign.
*/

package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/**
 * @ant.task category="scm"
 */
public class P4Resolve extends P4Base {
    private String resolvemode = null;


    private boolean redoall; /* -f */
    private boolean simulationmode;  /* -n */
    private boolean forcetextmode;  /* -t */
    private boolean markersforall; /* -v */
    private static final String AUTOMATIC = "automatic";
    private static final String FORCE = "force";
    private static final String SAFE = "safe";
    private static final String THEIRS = "theirs";
    private static final String YOURS = "yours";
    private static final String[] RESOLVE_MODES = {
        AUTOMATIC,
        FORCE,
        SAFE,
        THEIRS,
        YOURS
    };
   /**
    * returns the resolve mode
    * @return  returns the resolve mode
    */
    public String getResolvemode() {
        return resolvemode;
    }
    /**
     * values for resolvemode
     * <ul>
     * <li> automatic -am</li>
     * <li> force -af </li>
     * <li> safe -as </li>
     * <li> theirs -at </li>
     * <li> yours -ay </li>
     * </ul>
     * @param resolvemode one of automatic, force, safe, theirs, yours
     */
    public void setResolvemode(String resolvemode) {
        boolean found = false;
        for (int counter = 0; counter < RESOLVE_MODES.length; counter++) {
            if (resolvemode.equals(RESOLVE_MODES[counter])) {
                found = true;
                break;
            }
        }
        if (found == false) {
            throw new BuildException("Unacceptable value for resolve mode");
        }
        this.resolvemode = resolvemode;
    }

    /**
     * allows previously resolved files to be resolved again
     * @return flag indicating whether one wants to
     * allow previously resolved files to be resolved again
     */
    public boolean isRedoall() {
        return redoall;
    }

    /**
     * set the redoall flag
     * @param redoall flag indicating whether one want to
     * allow previously resolved files to be resolved again
     */
    public void setRedoall(boolean redoall) {
        this.redoall = redoall;
    }

    /**
     * read the simulation mode flag
     * @return flag indicating whether one wants just to simulate
     * the p4 resolve operation whithout actually doing it
     */
    public boolean isSimulationmode() {
        return simulationmode;
    }

    /**
     * sets a flag
     * @param simulationmode set to true, lists the integrations which would be performed,
     * without actually doing them.
     */
    public void setSimulationmode(boolean simulationmode) {
        this.simulationmode = simulationmode;
    }

    /**
     * If set to true, attempts a textual merge, even for binary files
     * @return flag value
     */
    public boolean isForcetextmode() {
        return forcetextmode;
    }

    /**
     * If set to true, attempts a textual merge, even for binary files
     * @param forcetextmode set the flag value
     */
    public void setForcetextmode(boolean forcetextmode) {
        this.forcetextmode = forcetextmode;
    }

    /**
     * If set to true, puts in markers for all changes, conflicting or not
     * @return  flag markersforall value
     */
    public boolean isMarkersforall() {
        return markersforall;
    }

    /**
      * If set to true, puts in markers for all changes, conflicting or not
     * @param markersforall flag true or false
     */
    public void setMarkersforall(boolean markersforall) {
        this.markersforall = markersforall;
    }

    /**
     *  execute the p4 resolve
     * @throws BuildException if there is a wrong resolve mode specified
     *  or no view specified
     */
    public void execute() throws BuildException {
        if (this.resolvemode.equals(AUTOMATIC)) {
            P4CmdOpts = P4CmdOpts + " -am";
        } else if (this.resolvemode.equals(FORCE)) {
            P4CmdOpts = P4CmdOpts + " -af";
        } else if (this.resolvemode.equals(SAFE)) {
            P4CmdOpts = P4CmdOpts + " -as";
        } else if (this.resolvemode.equals(THEIRS)) {
            P4CmdOpts = P4CmdOpts + " -at";
        } else if (this.resolvemode.equals(YOURS)) {
            P4CmdOpts = P4CmdOpts + " -ay";
        } else {
            throw new BuildException("unsupported or absent resolve mode");
        }
        if (P4View == null) {
            throw new BuildException("please specify a view");
        }
        if (this.isRedoall()) {
            P4CmdOpts = P4CmdOpts + " -f";
        }
        if (this.isSimulationmode()) {
            P4CmdOpts = P4CmdOpts + " -n";
        }
        if (this.isForcetextmode()) {
            P4CmdOpts = P4CmdOpts + " -t";
        }
        if (this.isMarkersforall()) {
            P4CmdOpts = P4CmdOpts + " -v";
        }
        execP4Command("-s resolve " + P4CmdOpts + " " + P4View, new SimpleP4OutputHandler(this));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9659.java