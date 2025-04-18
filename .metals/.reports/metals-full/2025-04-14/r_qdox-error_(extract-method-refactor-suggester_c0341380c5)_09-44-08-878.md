error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6525.java
text:
```scala
i@@f (position == null || position.equalsIgnoreCase(Configuration.getString(key, "South"))) {

// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.swingext.Orientation;
import org.argouml.ui.ProjectBrowser;

public class ConfigLoader {
    private static Logger _Log = Logger.getLogger("org.argouml.util.ConfigLoader"); 

    ////////////////////////////////////////////////////////////////
    // static utility functions

    public static String TabPath = "org.argouml";
    private static Orientation tabPropsOrientation;

    public static Orientation getTabPropsOrientation() {
        return tabPropsOrientation;
    }

    public static void loadTabs(Vector tabs, String panelName, Orientation orientation) {
        String position = null;
        if (
                panelName.equals("north") || panelName.equals("south") || 
                panelName.equals("west") || panelName.equals("east") ||
                panelName.equals("northwest") || panelName.equals("northeast") || 
                panelName.equals("southwest") || panelName.equals("southeast")) {
            position = panelName;
            panelName = "detail";
        }
        InputStream is = null;
	LineNumberReader lnr = null;
	String configFile = System.getProperty("argo.config");
        //
        //    if property specified
        //
        if(configFile != null) {
            //    try to load a file
            try {
                is = new FileInputStream(configFile);
            }
            catch(FileNotFoundException e) {
                is = ConfigLoader.class.getResourceAsStream(configFile);
                if(is != null) {
                    _Log.info("Value of argo.config (" + configFile + ") could not be loaded.\nLoading default configuration.");
                }
            }
        }
        if(is == null) {
            configFile = "/org/argouml/argo.ini";
            is = ConfigLoader.class.getResourceAsStream(configFile);
        }
        if(is != null) {
            lnr = new LineNumberReader(new InputStreamReader(is));

            if (lnr != null) {
                try {
                    String line = lnr.readLine();
                    while (line != null) {
                        Class tabClass =
                            parseConfigLine(line, panelName, lnr.getLineNumber(), configFile);
                        if (tabClass != null) {
                            try {
                                String className = tabClass.getName();
                                String shortClassName = className.substring(className.lastIndexOf('.')+1).toLowerCase();
                                ConfigurationKey key = Configuration.makeKey("layout", shortClassName);
                                if (position == null || position.equals(Configuration.getString(key, "south"))) {
                                    if (className.equals("org.argouml.uml.ui.TabProps")) {
                                        tabPropsOrientation = orientation;
                                    }
                                    Object newTab = tabClass.newInstance();
                                    tabs.addElement(newTab);
                                }
                            }
                            catch (InstantiationException ex) {
                                _Log.error("Could not make instance of " +
                                    tabClass.getName());
                            }
                            catch (IllegalAccessException ex) {
                                _Log.error("Could not make instance of " +
                                    tabClass.getName());
                            }
                        }
                        line = lnr.readLine();                     
                    }
                }
                catch (java.io.IOException io) {
                    _Log.error(io);
                }
            }
            else {
                _Log.error("lnr is null");
            }
        }
	}

	public static Class parseConfigLine(String line, String panelName,
						int lineNum, String configFile) {
		if (line.startsWith("tabpath")) {
			String newPath = stripBeforeColon(line).trim();
			if (newPath.length() > 0) TabPath = newPath;
			return null;
		}
		else if (line.startsWith(panelName)) {
			String tabNames = stripBeforeColon(line).trim();
			java.util.StringTokenizer tabAlternatives = new java.util.StringTokenizer(tabNames, "|");
			Class res = null;
			while (tabAlternatives.hasMoreElements()) {
				String tabSpec = tabAlternatives.nextToken().trim();
				String tabName = tabSpec;  //TODO: arguments
				String tabClassName;
                                
				if ( tabName.indexOf('.') > 0 )
					tabClassName = tabName;
				else
					tabClassName = TabPath + "." + tabName;

				try {
					res = Class.forName(tabClassName);
				}
				catch (ClassNotFoundException cnfe) { }
				catch (Exception e) {
					_Log.error("Unanticipated exception, skipping "+tabName);
					_Log.error(e);
				}
				if (res != null) {
                                        if (ProjectBrowser.getInstance().getSplashScreen() != null) {
					   ProjectBrowser.getInstance().getSplashScreen().getStatusBar().showStatus("Making Project Browser: " + tabName);
					   ProjectBrowser.getInstance().getSplashScreen().getStatusBar().incProgress(2);
                                        }
					return res;
				}
			}
			if (Boolean.getBoolean("dbg")) {
				_Log.warn("\nCould not find any of these classes:\n" +
								   "TabPath=" + TabPath + "\n" +
								   "Config file=" + configFile + "\n" +
								   "Config line #" + lineNum + ":" + line);
			}
		}
		return null;
	}

	public static String stripBeforeColon(String s) {
		int colonPos = s.indexOf(":");
		return s.substring(colonPos  + 1);
	}

} /* end class ConfigLoader */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6525.java