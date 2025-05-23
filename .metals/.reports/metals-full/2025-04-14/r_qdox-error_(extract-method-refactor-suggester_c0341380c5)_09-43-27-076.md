error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9622.java
text:
```scala
U@@serPreferencesAdapter preferencesAdapter = new UserPreferencesStore(true);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.tools.ajbrowser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.BuildConfigManager;
import org.aspectj.ajde.BuildListener;
import org.aspectj.ajde.TaskListManager;
import org.aspectj.ajde.ui.InvalidResourceException;
import org.aspectj.ajde.ui.UserPreferencesAdapter;
import org.aspectj.ajde.ui.internal.UserPreferencesStore;
import org.aspectj.ajde.ui.swing.AjdeUIManager;
import org.aspectj.ajde.ui.swing.BasicEditor;
import org.aspectj.ajde.ui.swing.IconRegistry;
import org.aspectj.ajde.ui.swing.MultiStructureViewPanel;

/**
 * IDE manager for standalone AJDE application.
 *
 * @author  Mik Kersten
 */
public class BrowserManager {
	
	private static final BrowserManager INSTANCE = new BrowserManager();
	private BrowserProperties browserProjectProperties;
	
	public static BrowserManager getDefault() {
		return INSTANCE;
	}
    	
	private List configFiles = new ArrayList();
	
	public static final String TITLE = "AspectJ Browser";
    
    private static TopFrame topFrame = null;
    
	public void init(String[] configFilesArgs, boolean visible) {
		try {
			UserPreferencesAdapter preferencesAdapter = new UserPreferencesStore();
			browserProjectProperties = new BrowserProperties(preferencesAdapter);
			TaskListManager taskListManager = new CompilerMessagesPanel();
			BasicEditor ajdeEditor = new BasicEditor();
			BrowserUIAdapter browserUIAdapter = new BrowserUIAdapter();
			topFrame = new TopFrame(); 
			configFiles = getConfigFilesList(configFilesArgs);	

			AjdeUIManager.getDefault().init(
				ajdeEditor,
				taskListManager,
				browserProjectProperties,
				preferencesAdapter,
				browserUIAdapter,
				new IconRegistry(),
				topFrame,
				true);	
			
			Ajde.getDefault().getBuildManager().addListener(BUILD_MESSAGES_LISTENER);
			
			MultiStructureViewPanel multiViewPanel = new MultiStructureViewPanel(
				AjdeUIManager.getDefault().getViewManager().getBrowserPanel(),
				AjdeUIManager.getDefault().getFileStructurePanel()
			);
			
			topFrame.init(
				multiViewPanel,
				(CompilerMessagesPanel)taskListManager,
				Ajde.getDefault().getEditorManager().getEditorPanel()
			);
				
			if (visible) topFrame.setVisible(true);
			  
			if (configFiles.size() == 0) {
				Ajde.getDefault().getErrorHandler().handleWarning(
					"No build configuration selected. "
						+ "Select a \".lst\" build configuration file in order to compile and navigate structure.");
			} else {
				//UiManager.getDefault().getViewManager().updateConfigsList();
			}
		
			AjdeUIManager.getDefault().getOptionsFrame().addOptionsPanel(new BrowserOptionsPanel());
		
			//String lastOpenFilePath = browserProjectProperties.getLastOpenSourceFilePath();
			//Ajde.getDefault().getEditorManager().showSourceLine(lastOpenFilePath, 1, false);	
			//Ajde.getDefault().getStructureViewManager().fireNavigationAction(lastOpenFilePath, 6);
			//Ajde.getDefault().enableLogging(System.out); 
		
			if (configFilesArgs.length > 0 && configFilesArgs[0] != null) {
				Ajde.getDefault().getConfigurationManager().setActiveConfigFile(configFilesArgs[0]);	
			}
		} catch (Throwable t) {
			t.printStackTrace();
			Ajde.getDefault().getErrorHandler().handleError(
				"AJDE failed to initialize.",
				t);
		}
	}

    public void resetEditorFrame() {
        topFrame.resetSourceEditorPanel();
    }

	public void resetEditor() {
        BrowserManager.getDefault().getRootFrame().setSize(BrowserManager.getDefault().getRootFrame().getWidth()+1, BrowserManager.getDefault().getRootFrame().getHeight()+1);
        BrowserManager.getDefault().getRootFrame().doLayout();
        BrowserManager.getDefault().getRootFrame().repaint();
    }

    public void setStatusInformation(String text) {
        topFrame.statusText_label.setText(text);
    }

    public void setEditorStatusText(String text) {
        topFrame.setTitle(BrowserManager.TITLE + " - " + text);
    }

    public void saveAll() {
        Ajde.getDefault().getEditorManager().saveContents();
    }

    public void showMessages() {
        topFrame.showMessagesPanel();
    }

    public void hideMessages() {
        topFrame.hideMessagesPanel();
    }

    public JFrame getRootFrame() {
        return topFrame;
    }

	public void openFile(String filePath) {
		try {
			if (filePath.endsWith(".lst")) {
				AjdeUIManager.getDefault().getBuildConfigEditor().openFile(filePath);
				topFrame.setEditorPanel(AjdeUIManager.getDefault().getBuildConfigEditor());
			} else if (filePath.endsWith(".java") || filePath.endsWith(".aj")){
				Ajde.getDefault().getEditorManager().showSourceLine(filePath, 0, false);		
			} else {
				Ajde.getDefault().getErrorHandler().handleError("File: " + filePath 
					+ " could not be opened because the extension was not recoginzed.");	
			}
		} catch (IOException ioe) {
			Ajde.getDefault().getErrorHandler().handleError("Could not open file: " + filePath, ioe);
		} catch (InvalidResourceException ire) {
			Ajde.getDefault().getErrorHandler().handleError("Invalid file: " + filePath, ire);
		} 
		
		browserProjectProperties.setLastOpenSourceFilePath(filePath);
	}

	private List getConfigFilesList(String[] configFiles) {
		List configs = new ArrayList();
		for (int i = 0; i < configFiles.length; i++) {
            if (configFiles[i].endsWith(BuildConfigManager.CONFIG_FILE_SUFFIX)) {
                configs.add(configFiles[i]);
            }
        }
        return configs;
	}

//    private static class Runner {
//  
//        public static void invoke(String className) {
//            try {
//                if (className == null || className.length() == 0) {
//                    Ajde.getDefault().getErrorHandler().handleWarning("No main class specified, please select a class to run.");
//
//                } else {
//                    Class[] argTypes = { String[].class };
//                    java.lang.reflect.Method method = Class.forName(className).getDeclaredMethod("main", argTypes);
//                    Object[] args = { new String[0] };
//                    method.invoke(null, args);
//                }
//            } catch(ClassNotFoundException cnfe) {
//                Ajde.getDefault().getErrorHandler().handleWarning("Main class not found: " + className +
//                "\nMake sure that you have \".\" on your classpath.");
//            } catch(NoSuchMethodException nsme) {
//                Ajde.getDefault().getErrorHandler().handleWarning("Class: " + className + " does not declare public static void main(String[])");
//            } catch(java.lang.reflect.InvocationTargetException ite) {
//                Ajde.getDefault().getErrorHandler().handleWarning("Could not execute: " + className);
//            } catch(IllegalAccessException iae) {
//                Ajde.getDefault().getErrorHandler().handleWarning("Class: " + className + " does not declare public main method");
//            }
//        }
//    }
    
	private final BuildListener BUILD_MESSAGES_LISTENER = new BuildListener() {
		
		public void compileStarted(String buildConfigFile) { }
		
        public void compileFinished(String buildConfigFile, int buildTime, boolean succeeded, boolean warnings) {
            int timeInSeconds = buildTime/1000;
            if (succeeded && !warnings) {
                hideMessages();
            } else {
                showMessages();
            }
        }
        
        public void compileAborted(String buildConfigFile, String message) { }
    };
    
	public List getConfigFiles() {
		return configFiles;
	}

	public BrowserProperties getBrowserProjectProperties() {
		return browserProjectProperties;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9622.java