error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4315.java
text:
```scala
M@@ainInterface.frameModel = new FrameModel();

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.core.main;

import java.io.File;

import org.columba.addressbook.main.AddressbookMain;

import org.columba.core.backgroundtask.BackgroundTaskManager;
import org.columba.core.command.DefaultProcessor;
import org.columba.core.config.Config;
import org.columba.core.gui.ClipboardManager;
import org.columba.core.gui.focus.FocusManager;
import org.columba.core.gui.frame.FrameModel;
import org.columba.core.gui.themes.ThemeSwitcher;
import org.columba.core.gui.util.FontProperties;
import org.columba.core.gui.util.StartUpFrame;
import org.columba.core.help.HelpManager;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.plugin.ActionPluginHandler;
import org.columba.core.plugin.ConfigPluginHandler;
import org.columba.core.plugin.ExternalToolsPluginHandler;
import org.columba.core.plugin.FramePluginHandler;
import org.columba.core.plugin.InterpreterHandler;
import org.columba.core.plugin.MenuPluginHandler;
import org.columba.core.plugin.PluginManager;
import org.columba.core.plugin.ThemePluginHandler;

import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.config.accountwizard.AccountWizardLauncher;
import org.columba.mail.main.MailMain;

public class Main {
    private Main() {}
    
    public static void main(String[] args) {
        ColumbaCmdLineParser cmdLineParser = new ColumbaCmdLineParser();
        try {
            cmdLineParser.parseCmdLine(args);
        } catch (IllegalArgumentException e) {
            ColumbaCmdLineParser.printUsage();
            System.exit(2);
        }

        // initialize configuration backend
        String path = cmdLineParser.getPathOption();
        MainInterface.config = new Config(path == null ? null : new File(path));

        // the configPath settings are made in the commandlineParser @see ColumbaCmdLineParser
        ColumbaClient.loadInVMInstance(args);

        StartUpFrame frame = new StartUpFrame();
        frame.setVisible(true);

        AddressbookMain addressbook = new AddressbookMain();
        addressbook.initConfiguration();

        MailMain mail = new MailMain();
        mail.initConfiguration();

        MainInterface.config.init();

        MainInterface.clipboardManager = new ClipboardManager();
        MainInterface.focusManager = new FocusManager();

        MainInterface.processor = new DefaultProcessor();
        MainInterface.processor.start();

        MainInterface.pluginManager = new PluginManager();

        MainInterface.pluginManager.registerHandler(new InterpreterHandler());

        MainInterface.pluginManager.registerHandler(new ExternalToolsPluginHandler());

        MainInterface.pluginManager.registerHandler(new ActionPluginHandler());

        MainInterface.pluginManager.registerHandler(new MenuPluginHandler(
                "org.columba.core.menu"));
        MainInterface.pluginManager.registerHandler(new ConfigPluginHandler());

        MainInterface.pluginManager.registerHandler(new FramePluginHandler());

        MainInterface.pluginManager.registerHandler(new ThemePluginHandler());

        MainInterface.backgroundTaskManager = new BackgroundTaskManager();

        addressbook.initPlugins();
        mail.initPlugins();

        MainInterface.pluginManager.initPlugins();

        ThemeSwitcher.setTheme();

        // init font configuration
        new FontProperties();

        // set application wide font
        FontProperties.setFont();

        // initialze JavaHelp manager
        new HelpManager();

        //MainInterface.frameModelManager = new FrameModelManager();
        addressbook.initGui();

        mail.initGui();

        new FrameModel();

        frame.setVisible(false);

        if (MailConfig.getAccountList().count() == 0) {
            try {
                new AccountWizardLauncher().launchWizard();
            } catch (Exception ex) {
                ColumbaLogger.log.severe(ex.getMessage());
            }
        }

        new CmdLineArgumentHandler(cmdLineParser);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4315.java