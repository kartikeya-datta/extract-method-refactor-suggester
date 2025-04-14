error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7581.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7581.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7581.java
text:
```scala
(@@PluggableMenu.KEY_TOOLS.equals(o[1]))) {

// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.tools.ui;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.reveng.*;

import org.tigris.gef.base.*;

import ru.novosoft.uml.foundation.core.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import silk.*;

/* class ActionExecSilk */
public class ActionExecSilk extends UMLAction
implements PluggableMenu {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionExecSilk SINGLETON = new ActionExecSilk(); 

    private static JMenuItem _menuItem = null;

    public static final String separator = "/"; //System.getProperty("file.separator");

    private final static String SILK_CLASS = "silk.Scheme";

    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionExecSilk() {
        super("Exec SILK script...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent event) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	
	// if (!(target instanceof MClassifier)) return;

        try {
            String directory = Globals.getLastDirectory();
            JFileChooser chooser = new JFileChooser(directory);

            if (chooser == null) chooser = new JFileChooser();

            //chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Exec SILK script");
            //      FileFilter filter = FileFilters.SilkFilter;
            //chooser.addChoosableFileFilter(filter);
            //chooser.setFileFilter(filter);
	    
            int retval = chooser.showOpenDialog(pb);

            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
		    silk.Scheme.load(theFile.getCanonicalPath());

		    Symbol init = Symbol.intern("main");      
		    if (init != null) {
			silk.Scheme.eval(new Pair(init, new Pair(this, Pair.EMPTY)));
		    }
                }
            }
        } catch (Exception exception) {
            Argo.log.error("Exception in ActionExecSilk", exception);
        }
    }



    public boolean initializeModule() {
        boolean initialized = false;
	try {
            Class c = Class.forName(SILK_CLASS);
	    // Make sure we can instantiate it also.
	    Object o = c.newInstance();
            Argo.log.info ("+----------------------------+");
            Argo.log.info ("| JScheme scripting enabled! |");
            Argo.log.info ("+----------------------------+");
	    initialized = true;
	}
	catch (Exception e) {
	    Argo.log.error("JScheme does not appear to be in the classpath.");
	    Argo.log.error("Unable to initialize JScheme scripting plugin.");
	}
        return initialized;
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] { a, b };
    }

    public void setModuleEnabled(boolean enabled) { }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "ActionExecSilk"; }
    public String getModuleDescription() { return "JScheme Scripting"; }
    public String getModuleAuthor() { return "Andreas Rueckert"; }
    public String getModuleVersion() { return "0.9.4"; }
    public String getModuleKey() { return "module.tools.scripting.jscheme"; }

    public boolean inContext(Object[] o) {
        if (o.length < 2) return false;
	// Allow ourselves on the "Tools" menu.
	if ((o[0] instanceof JMenuItem) &&
	        ("Tools".equals(o[1]))) {
	    return true;
	}
        return false;
    }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        return getMenuItem(buildContext(mi, s));
    }

    public JMenuItem getMenuItem(Object[]  context) {

        if (! inContext(context)) {
	    return null;
	}

        if (_menuItem == null) {
            _menuItem = new JMenuItem(Argo.localize(Argo.MENU_BUNDLE,
	                                            "Exec Silk Script..."));
	    _menuItem.addActionListener(this);
	}
        return _menuItem;
    }

}
/* end class ActionExecSilk */   













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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7581.java