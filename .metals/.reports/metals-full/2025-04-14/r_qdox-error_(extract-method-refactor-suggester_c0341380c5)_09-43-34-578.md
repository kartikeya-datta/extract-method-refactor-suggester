error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2485.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2485.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2485.java
text:
```scala
r@@esourceDelimiter = new AddModuleArgument("--resource-delimiter");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.cli.handlers.module;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.handlers.CommandHandlerWithHelp;
import org.jboss.as.cli.handlers.DefaultFilenameTabCompleter;
import org.jboss.as.cli.handlers.FilenameTabCompleter;
import org.jboss.as.cli.handlers.WindowsFilenameTabCompleter;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.DefaultCompleter;
import org.jboss.as.cli.impl.DefaultCompleter.CandidatesProvider;
import org.jboss.as.cli.impl.FileSystemPathArgument;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 *
 * @author Alexey Loubyansky
 */
public class ASModuleHandler extends CommandHandlerWithHelp {

    private class AddModuleArgument extends ArgumentWithValue {
        private AddModuleArgument(String fullName) {
            super(ASModuleHandler.this, fullName);
        }

        private AddModuleArgument(String fullName, CommandLineCompleter completer) {
            super(ASModuleHandler.this, completer, fullName);
        }

        @Override
        public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
            final String actionValue = action.getValue(ctx.getParsedCommandLine());
            return ACTION_ADD.equals(actionValue) && name.isPresent(ctx.getParsedCommandLine()) && super.canAppearNext(ctx);
        }
    }

    private static final String JBOSS_HOME = "JBOSS_HOME";

    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final String MODULE_SEPARATOR = ",";

    private static final String ACTION_ADD = "add";
    private static final String ACTION_REMOVE = "remove";

    private final ArgumentWithValue action = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){
        @Override
        public Collection<String> getAllCandidates(CommandContext ctx) {
            return Arrays.asList(new String[]{ACTION_ADD, ACTION_REMOVE});
        }}), 0, "--action");

    private final ArgumentWithValue name;
    private final ArgumentWithValue mainClass;
    private final ArgumentWithValue resources;
    private final ArgumentWithValue dependencies;
    private final ArgumentWithValue props;
    private final ArgumentWithValue moduleArg;
    private final ArgumentWithValue slot;
    private final ArgumentWithValue resourceDelimiter;

    private File modulesDir;

    public ASModuleHandler(CommandContext ctx) {
        super("module", false);

        final FilenameTabCompleter pathCompleter = Util.isWindows() ? new WindowsFilenameTabCompleter(ctx) : new DefaultFilenameTabCompleter(ctx);
        final CommandLineCompleter moduleNameCompleter = new CommandLineCompleter() {
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
                String path = buffer.replace('.', File.separatorChar);
                String modulesPath;
                try {
                    modulesPath = getModulesDir().getAbsolutePath() + File.separatorChar;
                } catch (CommandLineException e) {
                    return -1;
                }
                int result = pathCompleter.complete(ctx, modulesPath + path, cursor, candidates);
                if(result < 0) {
                    return result;
                }
                for(int i = 0; i < candidates.size(); ++i) {
                    candidates.set(i, candidates.get(i).replace(File.separatorChar, '.'));
                }
                return result - modulesPath.length();
            }
        };

        name = new ArgumentWithValue(this, moduleNameCompleter, "--name");
        name.addRequiredPreceding(action);

        mainClass = new AddModuleArgument("--main-class");

        resources = new AddModuleArgument("--resources", new CommandLineCompleter(){
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
                final int lastSeparator = buffer.lastIndexOf(PATH_SEPARATOR);
                if(lastSeparator >= 0) {
                    return lastSeparator + 1 + pathCompleter.complete(ctx, buffer.substring(lastSeparator + 1), cursor, candidates);
                }
                return pathCompleter.complete(ctx, buffer, cursor, candidates);
            }}) {
            @Override
            public String getValue(ParsedCommandLine args) {
                String value = super.getValue(args);
                if(value != null) {
                    if(value.length() >= 0 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1, value.length() - 1);
                    }
                    value = pathCompleter.translatePath(value);
                }
                return value;
            }
        };

        resourceDelimiter = new AddModuleArgument("--resourceDelimiter");

        dependencies = new AddModuleArgument("--dependencies", new CommandLineCompleter(){
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
                final int lastSeparator = buffer.lastIndexOf(MODULE_SEPARATOR);
                if(lastSeparator >= 0) {
                    return lastSeparator + 1 + moduleNameCompleter.complete(ctx, buffer.substring(lastSeparator + 1), cursor, candidates);
                }
                return moduleNameCompleter.complete(ctx, buffer, cursor, candidates);
            }});
        props = new AddModuleArgument("--properties");

        moduleArg = new FileSystemPathArgument(this, pathCompleter, "--module-xml") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                final String actionValue = action.getValue(ctx.getParsedCommandLine());
                return ACTION_ADD.equals(actionValue) && name.isPresent(ctx.getParsedCommandLine()) && super.canAppearNext(ctx);
            }
        };

        slot = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider() {
            @Override
            public Collection<String> getAllCandidates(CommandContext ctx) {
                final String moduleName = name.getValue(ctx.getParsedCommandLine());
                if(moduleName == null) {
                    return java.util.Collections.emptyList();
                }

                final File moduleDir;
                try {
                    moduleDir = new File(getModulesDir(), moduleName.replace('.', File.separatorChar));
                } catch (CommandLineException e) {
                    return java.util.Collections.emptyList();
                }
                if(!moduleDir.exists()) {
                    return java.util.Collections.emptyList();
                }
                return Arrays.asList(moduleDir.list());
            }})
        , "--slot");

        moduleArg.addCantAppearAfter(mainClass);
        moduleArg.addCantAppearAfter(dependencies);
        moduleArg.addCantAppearAfter(props);

        mainClass.addCantAppearAfter(moduleArg);
        dependencies.addCantAppearAfter(moduleArg);
        props.addCantAppearAfter(moduleArg);
    }

    @Override
    public boolean isAvailable(CommandContext ctx) {
        return !ctx.isDomainMode();
    }

    @Override
    protected void doHandle(CommandContext ctx) throws CommandLineException {

        final ParsedCommandLine parsedCmd = ctx.getParsedCommandLine();
        final String actionValue = action.getValue(parsedCmd);
        if(actionValue == null) {
            throw new CommandFormatException("Action argument is missing: " + ACTION_ADD + " or " + ACTION_REMOVE);
        }

        if(ACTION_ADD.equals(actionValue)) {
            addModule(ctx, parsedCmd);
        } else if(ACTION_REMOVE.equals(actionValue)) {
            removeModule(parsedCmd);
        } else {
            throw new CommandFormatException("Unexpected action '" + actionValue + "', expected values: " + ACTION_ADD + ", " + ACTION_REMOVE);
        }
    }

    protected void addModule(CommandContext ctx, final ParsedCommandLine parsedCmd) throws CommandLineException {

        final String moduleName = name.getValue(parsedCmd, true);

        // resources required only if we are generating module.xml
        final String resourcePaths = resources.getValue(parsedCmd, !moduleArg.isPresent(parsedCmd));

        String pathDelimiter = PATH_SEPARATOR;
        if (resourceDelimiter.isPresent(parsedCmd)) {
            pathDelimiter = resourceDelimiter.getValue(parsedCmd);
        }

        final String[] resourceArr = (resourcePaths == null) ? new String[0] : resourcePaths.split(pathDelimiter);
        File[] resourceFiles = new File[resourceArr.length];
        for(int i = 0; i < resourceArr.length; ++i) {
            final File f = new File(resourceArr[i]);
            if(!f.exists()) {
                throw new CommandLineException("Failed to locate " + f.getAbsolutePath());
            }
            resourceFiles[i] = f;
        }

        final File moduleDir = getModulePath(getModulesDir(), moduleName, slot.getValue(parsedCmd));
        if(moduleDir.exists()) {
            throw new CommandLineException("Module " + moduleName + " already exists at " + moduleDir.getAbsolutePath());
        }

        if(!moduleDir.mkdirs()) {
            throw new CommandLineException("Failed to create directory " + moduleDir.getAbsolutePath());
        }

        final ModuleConfigImpl config;
        final String moduleXml = moduleArg.getValue(parsedCmd);
        if(moduleXml != null) {
            config = null;
            final File source = new File(moduleXml);
            if(!source.exists()) {
                throw new CommandLineException("Failed to locate the file on the filesystem: " + source.getAbsolutePath());
            }
            copy(source, new File(moduleDir, "module.xml"));
        } else {
            config = new ModuleConfigImpl(moduleName);
        }

        for(File f : resourceFiles) {
            copy(f, new File(moduleDir, f.getName()));
            if(config != null) {
                config.addResource(new ResourceRoot(f.getName()));
            }
        }

        if(config != null) {
            final String dependenciesStr = dependencies.getValue(parsedCmd);
            if(dependenciesStr != null) {
                final String[] depsArr = dependenciesStr.split(",+");
                for(String dep : depsArr) {
                    // TODO validate dependencies
                    config.addDependency(new ModuleDependency(dep));
                }
            }

            final String propsStr = props.getValue(parsedCmd);
            if(propsStr != null) {
                final String[] pairs = propsStr.split(",");
                for (String pair : pairs) {
                    int equals = pair.indexOf('=');
                    if (equals == -1) {
                        throw new CommandFormatException("Property '" + pair + "' in '" + propsStr + "' is missing the equals sign.");
                    }
                    final String propName = pair.substring(0, equals);
                    if (propName.isEmpty()) {
                        throw new CommandFormatException("Property name is missing for '" + pair + "' in '" + propsStr + "'");
                    }
                    config.setProperty(propName, pair.substring(equals + 1));
                }
            }

            final String slotVal = slot.getValue(parsedCmd);
            if (slotVal != null) {
                config.setSlot(slotVal);
            }

            final String mainCls = mainClass.getValue(parsedCmd);
            if(mainCls != null) {
                config.setMainClass(mainCls);
            }

            FileWriter moduleWriter = null;
            final File moduleFile = new File(moduleDir, "module.xml");
            try {
                moduleWriter = new FileWriter(moduleFile);
                XMLExtendedStreamWriter xmlWriter = create(XMLOutputFactory.newInstance().createXMLStreamWriter(moduleWriter));
                config.writeContent(xmlWriter, null);
                xmlWriter.flush();
            } catch (IOException e) {
                throw new CommandLineException("Failed to create file " + moduleFile.getAbsolutePath(), e);
            } catch (XMLStreamException e) {
                throw new CommandLineException("Failed to write to " + moduleFile.getAbsolutePath(), e);
            } finally {
                if(moduleWriter != null) {
                    try {
                        moduleWriter.close();
                    } catch (IOException e) {}
                }
            }
        }
    }

    private void removeModule(ParsedCommandLine parsedCmd) throws CommandLineException {

        final String moduleName = name.getValue(parsedCmd, true);
        final File modulesDir = getModulesDir();
        File modulePath = getModulePath(modulesDir, moduleName, slot.getValue(parsedCmd));
        if(!modulePath.exists()) {
            throw new CommandLineException("Failed to locate module " + moduleName + " at " + modulePath.getAbsolutePath());
        }

        // delete the whole slot directory
        deleteRecursively(modulePath);

        modulePath = modulePath.getParentFile();
        while(!modulesDir.equals(modulePath)) {
            if(modulePath.list().length > 0) {
                break;
            }
            if(!modulePath.delete()) {
                throw new CommandLineException("Failed to delete " + modulePath.getAbsolutePath());
            }
            modulePath = modulePath.getParentFile();
        }
    }

    protected void deleteRecursively(final File file) throws CommandLineException {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteRecursively(f);
                }
            }
        }
        if (!file.delete()) {
            throw new CommandLineException("Failed to delete " + file.getAbsolutePath());
        }
    }

    protected File getModulePath(File modulesDir, final String moduleName, String slot) throws CommandLineException {
        return new File(modulesDir, moduleName.replace('.', File.separatorChar) + File.separatorChar + (slot == null ? "main" : slot));
    }

    protected File getModulesDir() throws CommandLineException {
        if(modulesDir != null) {
            return modulesDir;
        }
        final String modulesDirStr = WildFlySecurityManager.getEnvPropertyPrivileged(JBOSS_HOME, null);
        if(modulesDirStr == null) {
            throw new CommandLineException(JBOSS_HOME + " environment variable is not set.");
        }
        modulesDir = new File(modulesDirStr, "modules");
        if(!modulesDir.exists()) {
            throw new CommandLineException("Failed to locate the modules dir on the filesystem: " + modulesDir.getAbsolutePath());
        }
        return modulesDir;
    }

    public static XMLExtendedStreamWriter create(XMLStreamWriter writer) throws CommandLineException {
        try {
            // Use reflection to access package protected class FormattingXMLStreamWriter
            // TODO: at some point the staxmapper API could be enhanced to make this unnecessary
            Class<?> clazz = Class.forName("org.jboss.staxmapper.FormattingXMLStreamWriter");
            Constructor<?> ctr = clazz.getConstructor( XMLStreamWriter.class );
            ctr.setAccessible(true);
            return (XMLExtendedStreamWriter)ctr.newInstance(new Object[]{writer});
        } catch (Exception e) {
            throw new CommandLineException("Failed to create xml stream writer.", e);
        }
    }

    public static void copy(final File source, final File target) throws CommandLineException {
        final byte[] buff = new byte[8192];
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        int read;
        try {
            in = new BufferedInputStream(new FileInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(target));
            while ((read = in.read(buff)) != -1) {
                out.write(buff, 0, read);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            throw new CommandLineException("Failed to locate the file on the filesystem copying " +
                source.getAbsolutePath() + " to " + target.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new CommandLineException("Failed to copy " + source.getAbsolutePath() + " to " + target.getAbsolutePath(), e);
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch(IOException e) {}
            try {
                if(in != null) {
                    in.close();
                }
            } catch(IOException e) {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2485.java