error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7354.java
text:
```scala
m@@akeNotation("UML", "1.3", Argo.lookupIconResource("UmlNotation"));

// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.application.notation;
import org.argouml.application.api.*;
import org.argouml.application.events.*;
import java.util.*;
import javax.swing.*;

/**
 *   This class provides definition and manipulation of notation names.
 *   All notation names will be accessed using the
 *   {@link NotationName} wrapper.
 *
 *   @author Thierry Lach
 *   @since 0.9.4
 */
public class NotationNameImpl implements NotationName {

    String _name = null;
    String _version = null;
    Icon _icon = null;
    
    private static ArrayList _notations = null;

    /** A notation without a version or icon.
     */
    protected NotationNameImpl(String name) {
        this(name, null, null);
    }

    /** A notation without a version and with an icon.
     */
    protected NotationNameImpl(String name, Icon icon) {
        this(name, null, icon);
    }

    /** A notation with a version and no icon.
     */
    protected NotationNameImpl(String name, String version) {
        this(name, version, null);
    }

    /** A notation with a version and an icon.
     */
    protected NotationNameImpl(String name, String version, Icon icon) {
        _name = name;
        _version = version;
        _icon = icon;
    }

    /** Accessor for the language name
     */
    public String getName() {
        return _name;
    }

    /** Accessor for the language version
     */
    public String getVersion() {
        return _version;
    }

    /** Gets a textual title for the notation suitable for use
     *  in a combo box or other such visual location.
     */
    public String getTitle() {
        // needs-more-work:  Currently this does not
	//                   differentiate from the configuration
	//                   value.
        return getNotationNameString(_name, _version);
    }

    /** Returns an icon for the notation, or null if no icon is available.
     */
    public Icon getIcon() {
        return _icon;
    }
 
    public String getConfigurationValue() {
        return getNotationNameString(_name, _version);
    }

    public static String getNotationNameString(String k1, String k2) {
        if (k2 == null) return k1;
        if (k2.equals("")) return k1;
	return k1 + "." + k2;
    }

    private static void fireEvent(int eventType,  NotationName nn) {
     ArgoEventPump.getInstance().fireEvent(new ArgoNotationEvent(eventType, nn));
    }

    /** Create a notation name with or without a version.
     */
    public static NotationName makeNotation(String k1, String k2, Icon icon) {
        if (_notations == null) _notations = new ArrayList();
	NotationName nn = null;
	nn = findNotation(getNotationNameString(k1, k2));
	if (nn == null) {
	    nn = (NotationName)new NotationNameImpl(k1, k2, icon);
	    _notations.add(nn);
	    fireEvent(ArgoEventTypes.NOTATION_ADDED, nn);
	}
        return nn;
    }

    /** Get all of the registered notations.
     */
    public static ArrayList getAvailableNotations() {
        // Cannot be null unless someone really messed up the code
	// because of the static initializer.
        return _notations;
    }

    /** Finds a NotationName matching the configuration string.
     *  Returns null if no match.
     */
    public static NotationName findNotation(String s) {
        ListIterator iterator = _notations.listIterator();
        while (iterator.hasNext()) {
	    try {
                NotationName nn = (NotationName)iterator.next();
		if (s.equals(nn.getConfigurationValue())) {
		    return nn;
		}
	    }
	    catch (Exception e) {
	        Argo.log.error ("Unexpected exception", e);
	    }
	}
	return null;
    }

    public boolean equals(NotationName nn) {
        return this.getConfigurationValue().equals(nn.getConfigurationValue());
    }

    /** Finds a NotationName matching the language with no version.
     *  Returns null if no match.
     */
    public static NotationName getNotation(String k1) {
        return findNotation(getNotationNameString(k1, null));
    }

    /** Finds a NotationName matching the language and version.
     *  Returns null if no match.
     */
    public static NotationName getNotation(String k1, String k2) {
        return findNotation(getNotationNameString(k1, k2));
    }


    public String toString() {
        if (_version == null) return "{NotationNameImpl:" + _name + "}";
        return "{NotationNameImpl:" + _name + " version " + _version + "}";
    }


    /** Pre-populate the notation list with the internal notations.
     */
    static {
        makeNotation("Default", null, null);
        makeNotation("UML", "1.3", null);
        makeNotation("Java", null, Argo.lookupIconResource("JavaNotation"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7354.java