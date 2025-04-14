error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7004.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7004.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7004.java
text:
```scala
public S@@tring getModuleVersion() { return ArgoVersion.getVersion(); }

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

package org.argouml.ui;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.*;
import org.argouml.application.helpers.*;
import org.argouml.kernel.*;
import org.argouml.uml.ui.UMLAction;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;
import org.tigris.gef.util.*;

/** Action object for handling Argo settings
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class SettingsTabPreferences extends SettingsTabHelper
implements SettingsTabPanel {

    JCheckBox _splash = null;
    JCheckBox _preload = null;
    JCheckBox _edem = null;
    JCheckBox _profile = null;
    JCheckBox _reloadRecent = null;

    public SettingsTabPreferences() {
        super();
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout()); 

	GridBagConstraints checkConstraints = new GridBagConstraints();
	checkConstraints.anchor = GridBagConstraints.WEST;
	checkConstraints.gridy = 0;
	checkConstraints.gridx = 0;
	checkConstraints.gridwidth = 1;
	checkConstraints.gridheight = 1;
	checkConstraints.insets = new Insets(0, 30, 0, 4);

	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.EAST;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(2, 10, 2, 4);

	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.WEST;
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.gridy = 0;
	fieldConstraints.gridx = 1;
	fieldConstraints.gridwidth = 3;
	fieldConstraints.gridheight = 1;
	fieldConstraints.weightx = 1.0;
	fieldConstraints.insets = new Insets(0, 4, 0, 20);

	checkConstraints.gridy = 0;
	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;
        _splash = createCheckBox("label.splash");
	top.add(_splash, checkConstraints);
	top.add(new JLabel(""), labelConstraints);
	top.add(new JLabel(""), fieldConstraints);

	checkConstraints.gridy = 1;
        _preload = createCheckBox("label.preload");
 	top.add(_preload, checkConstraints);

	checkConstraints.gridy = 2;
        _edem = createCheckBox("label.edem");
 	top.add(_edem, checkConstraints);

	checkConstraints.gridy = 3;
        _profile = createCheckBox("label.profile");
 	top.add(_profile, checkConstraints);

	checkConstraints.gridy = 4;
        _reloadRecent = createCheckBox("label.reload-recent");
 	top.add(_reloadRecent, checkConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _splash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH, true));
        _preload.setSelected(Configuration.getBoolean(Argo.KEY_PRELOAD, true));
        _edem.setSelected(Configuration.getBoolean(Argo.KEY_EDEM, true));
        _profile.setSelected(Configuration.getBoolean(Argo.KEY_PROFILE, false));
        _reloadRecent.setSelected(Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT, false));
    }

    public void handleSettingsTabSave() {
        Configuration.setBoolean(Argo.KEY_SPLASH, _splash.isSelected());
        Configuration.setBoolean(Argo.KEY_PRELOAD, _preload.isSelected());
        Configuration.setBoolean(Argo.KEY_EDEM, _edem.isSelected());
        Configuration.setBoolean(Argo.KEY_PROFILE, _profile.isSelected());
        Configuration.setBoolean(Argo.KEY_RELOAD_RECENT_PROJECT, _reloadRecent.isSelected());
    }

    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    public String getModuleName() { return "SettingsTabPreferences"; }
    public String getModuleDescription() { return "Settings Tab for Preferences"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.VERSION; }
    public String getModuleKey() { return "module.settings.preferences"; }

    public String getTabKey() { return "tab.preferences"; }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7004.java