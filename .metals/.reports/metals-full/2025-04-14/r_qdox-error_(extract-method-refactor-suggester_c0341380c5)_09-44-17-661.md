error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2300.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2300.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2300.java
text:
```scala
S@@hutdownManager.getInstance().register(new Runnable() {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.config;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.columba.core.io.DiskIO;
import org.columba.core.shutdown.ShutdownManager;
import org.columba.core.util.OSInfo;
import org.columba.core.xml.XmlElement;
import org.columba.core.xml.XmlIO;

/**
 * Main entrypoint for configuration management.
 * <p>
 * Stores a list of all xml files in a hashtable. Hashtable key is the name of 
 * the xml file. Value is {@link XmlIO} object.
 * <p>
 * Mail and Addressbook components are just wrappers, encapsulating this 
 * class. Using these wrapper classes, you don't need to specify the module
 * name (for example: mail, or addressbook) manually.
 * <p>
 * Note that all configuration file have default templates in the /res directory
 * in package org.columba.core.config. These default configuration files are
 * copied into the users's configuration directory the first time Columba is
 * started.
 * <p>
 * Config creates the top-level directory for Columba's configuration in
 * ".columba", which usually resides in the user's home directory or on
 * older Windows versions in Columba's program folder.
 * <p>
 * Saving and loading of all configuration files is handled here, too.
 * <p>
 * @see org.columba.mail.config.MailConfig
 * @see org.columba.addressbook.config.AddressbookConfig
 *  
 * @author fdietz
 */
public class Config {

    private static final String CORE_STR = "core";

    private static final Logger LOG = Logger
            .getLogger("org.columba.core.config");
    
    protected Map pluginList = new Hashtable();

    protected File path;

    protected File optionsFile;

    protected File toolsFile;
    
    protected File viewsFile;
    
    private static Config instance;

    /**
     * Creates a new configuration from the given directory.
     */
    public Config(File path) {
        if (path == null) {
            path = getDefaultConfigPath();
        }

        this.path = path;
        path.mkdir();
        optionsFile = new File(path, "options.xml");
        toolsFile = new File(path, "external_tools.xml");
        viewsFile = new File(path, "views.xml");
        
        registerPlugin(CORE_STR, optionsFile.getName(), new OptionsXmlConfig(
                optionsFile));

        registerPlugin(CORE_STR, toolsFile.getName(), new DefaultXmlConfig(
                toolsFile));
        registerPlugin(CORE_STR, viewsFile.getName(), new DefaultXmlConfig(
        		viewsFile));
        
        // register at shutdown-manager
        // -> this will save all configuration data, when closing Columba
        ShutdownManager.getShutdownManager().register(new Runnable() {

            public void run() {
                try {
                    save();
                } catch (Exception e) {
                    LOG.severe(e.getMessage());
                }
            }
        });
        
        instance = this;
    }

    public static Config getInstance() {
    	if( instance == null) {
    		throw new RuntimeException("Must call Constructor first!");
    	}
    	
    	return instance;
    }
    
    /**
     * Returns the directory the configuration is located in.
     */
    public File getConfigDirectory() {
        return path;
    }

    /**
     * Method registerPlugin.
     * 
     * @param moduleName
     * @param id
     * @param configPlugin
     */
    public void registerPlugin(String moduleName, String id,
            DefaultXmlConfig configPlugin) {
        File directory;

        if (moduleName.equals(CORE_STR)) {
            directory = getConfigDirectory();
        } else {
            directory = new File(getConfigDirectory(), moduleName);
        }

        File destination = new File(directory, id);

        if (!destination.exists()) {
            String hstr = "org/columba/" + moduleName + "/config/" + id;

            try {
                DiskIO.copyResource(hstr, destination);
            } catch (IOException e) {
            }
        }

        if (!pluginList.containsKey(moduleName)) {
            Map map = new Hashtable();
            pluginList.put(moduleName, map);
        }

        addPlugin(moduleName, id, configPlugin);
        
        // load config-file from disk
        configPlugin.load();
    }

    /**
     * Method getPlugin.
     * 
     * @param moduleName
     * @param id
     * @return DefaultXmlConfig
     */
    public DefaultXmlConfig getPlugin(String moduleName, String id) {
        if (pluginList.containsKey(moduleName)) {
            Map map = (Map) pluginList.get(moduleName);

            if (map.containsKey(id)) {
                DefaultXmlConfig plugin = (DefaultXmlConfig) map.get(id);

                return plugin;
            }
        }

        return null;
    }

    /**
     * Method addPlugin.
     * 
     * @param moduleName
     * @param id
     * @param configPlugin
     */
    public void addPlugin(String moduleName, String id,
            DefaultXmlConfig configPlugin) {
        Map map = (Map) pluginList.get(moduleName);

        if (map != null) {
            map.put(id, configPlugin);
        }
    }

    /**
     * Method getPluginList.
     * 
     * @return List
     */
    public List getPluginList() {
        List list = new LinkedList();

        for (Iterator keys = pluginList.keySet().iterator(); keys.hasNext();) {
            String key = (String) keys.next();
            Map map = (Map) pluginList.get(key);

            if (map != null) {
                for (Iterator keys2 = map.keySet().iterator(); keys2.hasNext();) {
                    String key2 = (String) keys2.next();
                    DefaultXmlConfig plugin = (DefaultXmlConfig) map.get(key2);

                    list.add(plugin);
                }
            }
        }

        return list;
    }

    /**
     * Method save.
     */
    public void save() throws Exception {
        List list = getPluginList();

        for (Iterator it = list.iterator(); it.hasNext();) {
            DefaultXmlConfig plugin = (DefaultXmlConfig) it.next();

            if (plugin == null) {
                continue;
            }

            plugin.save();
        }
    }

    /**
     * Loads all plugins and template plugins.
     */
    protected void load() {
        List list = getPluginList();

        for (Iterator it = list.iterator(); it.hasNext();) {
            DefaultXmlConfig plugin = (DefaultXmlConfig) it.next();

            if (plugin == null) {
                continue;
            }

            plugin.load();
        }
    }

    public XmlElement get(String name) {
        DefaultXmlConfig xml = getPlugin(CORE_STR, name + ".xml");

        return xml.getRoot();
    }

    /**
     * Method getOptionsMainInterface.config.
     * 
     * @return OptionsXmlConfig
     */
    public OptionsXmlConfig getOptionsConfig() {
        return (OptionsXmlConfig) getPlugin(CORE_STR, optionsFile.getName());
    }

    /**
     * Returns the default configuration path. This value depends on the
     * underlying operating system. This method must never return null.
     */
    public static File getDefaultConfigPath() {
        if (OSInfo.isWindowsPlatform()) {
            return new File("config");
        } else {
            return new File(System.getProperty("user.home"), ".columba");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2300.java