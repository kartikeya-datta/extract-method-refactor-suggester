error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4095.java
text:
```scala
S@@tring className = ev.getActionCommand().substring(ActionNames.LAF_PREFIX.length()).replace('/', '.');

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.JMeterMenuBar;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Implements the Look and Feel menu item.
 */
public class LookAndFeelCommand implements Command {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String JMETER_LAF = "jmeter.laf"; // $NON-NLS-1$

    private static final Set<String> commands = new HashSet<String>();

    static {
        UIManager.LookAndFeelInfo[] lfs = JMeterMenuBar.getAllLAFs();
        for (int i = 0; i < lfs.length; i++) {
            commands.add(ActionNames.LAF_PREFIX + lfs[i].getClassName());
        }

        try {
            String jMeterLaf = getJMeterLaf();
            UIManager.setLookAndFeel(jMeterLaf);
            if (log.isInfoEnabled()) {
                ArrayList<String> names=new ArrayList<String>();
                for(UIManager.LookAndFeelInfo laf : lfs) {
                    if (laf.getClassName().equals(jMeterLaf)) {
                        names.add(laf.getName());
                    }
                }
                if (names.size() > 0) {
                    log.info("Using look and feel: "+jMeterLaf+ " " +names.toString());
                } else {
                    log.info("Using look and feel: "+jMeterLaf);
                }
            }
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
    }

    /**
     * Get LookAndFeel classname from the following properties:
     * <ul>
     * <li>jmeter.laf.&lt;os.name> - lowercased; spaces replaced by '_'</li>
     * <li>jmeter.laf.&lt;os.family> - lowercased.</li>
     * <li>jmeter.laf</li>
     * <li>UIManager.getCrossPlatformLookAndFeelClassName()</li>
     * </ul>
     * @return LAF classname
     */
    private static String getJMeterLaf(){
        String osName = System.getProperty("os.name") // $NON-NLS-1$
                        .toLowerCase(Locale.ENGLISH);
        String laf;
        // Spaces are not allowed in property names read from files
        laf = JMeterUtils.getProperty(JMETER_LAF+"."+osName.replace(' ', '_'));
        if (laf != null) {
            return checkLafName(laf);
        }
        String[] osFamily = osName.split("\\s"); // e.g. windows xp => windows
        laf = JMeterUtils.getProperty(JMETER_LAF+"."+osFamily[0]);
        if (laf != null) {
            return checkLafName(laf);
        }
        laf = JMeterUtils.getProperty(JMETER_LAF);
        if (laf != null) {
            return checkLafName(laf);
        }
        return UIManager.getCrossPlatformLookAndFeelClassName();
    }

    // Check if LAF is a built-in one
    private static String checkLafName(String laf){
        if (JMeterMenuBar.SYSTEM_LAF.equalsIgnoreCase(laf)){
            return UIManager.getSystemLookAndFeelClassName();
        }
        if (JMeterMenuBar.CROSS_PLATFORM_LAF.equalsIgnoreCase(laf)){
            return UIManager.getCrossPlatformLookAndFeelClassName();
        }
        return laf;
    }

    public LookAndFeelCommand() {
    }

    public void doAction(ActionEvent ev) {
        try {
            String className = ev.getActionCommand().substring(4).replace('/', '.');
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(GuiPackage.getInstance().getMainFrame());
        } catch (javax.swing.UnsupportedLookAndFeelException e) {
            JMeterUtils.reportErrorToUser("Look and Feel unavailable:" + e.toString());
        } catch (InstantiationException e) {
            JMeterUtils.reportErrorToUser("Look and Feel unavailable:" + e.toString());
        } catch (ClassNotFoundException e) {
            JMeterUtils.reportErrorToUser("Look and Feel unavailable:" + e.toString());
        } catch (IllegalAccessException e) {
            JMeterUtils.reportErrorToUser("Look and Feel unavailable:" + e.toString());
        }
    }

    public Set<String> getActionNames() {
        return commands;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4095.java