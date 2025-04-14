error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1148.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1148.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[56,1]

error in qdox parser
file content:
```java
offset: 2621
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1148.java
text:
```scala
public class Antidote extends JComponent {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights
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
p@@ackage org.apache.tools.ant.gui;
import org.apache.tools.ant.gui.core.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.Constructor;

/**
 * The root class for the Ant GUI. Assembles all the graphical components
 * based on the configuration files.
 * 
 * @version $Revision$ 
 * @author Simeon Fitch 
 */
public class Antidote extends JPanel {
    /** Source of application state data. */
    private AppContext _context = null;

	/** 
	 * Default ctor.
	 * 
	 */
    public Antidote(AppContext context) {
        setLayout(new BorderLayout());

        _context = context;

        // Add the various modules to the editing area.
        JSplitPane splitter = new JSplitPane();
        splitter.add(JSplitPane.LEFT, populateModules("left"));
        splitter.add(JSplitPane.RIGHT, populateModules("right"));
        // This is necessary because, frankly, the JSplitPane widget
        // sucks, and doesn't provide enought (working) control over the
        // initial size of it's subcomponents. setDividerLocation(double)
        // doesn't seem to work until after the widget is visible.
        splitter.setPreferredSize(new Dimension(500, 300));

        // Top bottom splitter. 
        JSplitPane splitter2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitter2.setOneTouchExpandable(true);

        splitter2.add(JSplitPane.TOP, splitter);
        splitter2.add(JSplitPane.BOTTOM, populateModules("bottom"));

        add(BorderLayout.CENTER, splitter2);
        splitter2.resetToPreferredSizes();

        add(BorderLayout.NORTH, populateModules("top"));

        setPreferredSize(new Dimension(640, 480));
    }


	/** 
	 * Instantiate the configured modules.
	 * 
     * @return Component containing the modules(s).
	 */
    private JComponent populateModules(String prefix) {

        String[] classNames = _context.getResources().
            getStringArray(getClass(), prefix + ".modules");

        AntModule[] modules = new AntModule[classNames.length];

        for(int i = 0; i < classNames.length; i++) {
            try {
                Class type = Class.forName(classNames[i]);

                modules[i] = (AntModule) type.newInstance();
                modules[i].contextualize(_context);
            }
            catch(Exception ex) {
                // XXX log me.
                ex.printStackTrace();
            }
        }

        if(modules.length == 1) {
            return modules[0];
        }
        else if(modules.length > 1) {
            JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);

            for(int i = 0; i < modules.length; i++) {
                tabbed.addTab(modules[i].getName(), modules[i]);
            }
            return tabbed;
        }
        else {
            return new JLabel("I shouldn't be here...");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1148.java