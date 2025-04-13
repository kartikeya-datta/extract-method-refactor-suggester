error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1094
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12469.java
text:
```scala
public final class CustomHandlerService implements HandlerService {

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

p@@ackage org.jboss.as.logging;

import org.jboss.dmr.Property;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

import static org.jboss.as.logging.LogHandlerPropertiesConfigurator.setProperties;
import static org.jboss.as.logging.LoggingMessages.MESSAGES;

/**
 * Service for custom handlers.
 * <p/>
 * Date: 03.08.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public final class CustomHandlerService implements Service<Handler> {
    private final String className;
    private final String moduleName;
    private final List<Property> properties;

    private AbstractFormatterSpec formatterSpec;
    private Level level;
    private String encoding;
    private Handler value;

    /**
     * Creates a new custom handler service.
     *
     * @param className  the handler class name.
     * @param moduleName the module name the handler class is dependent on.
     */
    public CustomHandlerService(final String className, final String moduleName) {
        this.className = className;
        this.moduleName = moduleName;
        properties = new ArrayList<Property>();
    }

    @Override
    public synchronized void start(final StartContext context) throws StartException {
        final Handler handler;
        final ModuleLoader moduleLoader = Module.forClass(CustomHandlerService.class).getModuleLoader();
        final ModuleIdentifier id = ModuleIdentifier.create(moduleName);
        try {
            final Class<?> handlerClass = Class.forName(className, false, moduleLoader.loadModule(id).getClassLoader());
            if (Handler.class.isAssignableFrom(handlerClass)) {
                handler = (Handler) handlerClass.newInstance();
            } else {
                throw MESSAGES.invalidType(className, Handler.class);
            }
        } catch (ClassNotFoundException e) {
            throw MESSAGES.classNotFound(e, className);
        } catch (ModuleLoadException e) {
            throw MESSAGES.cannotLoadModule(e, moduleName);
        } catch (InstantiationException e) {
            throw MESSAGES.cannotInstantiateClass(e, className);
        } catch (IllegalAccessException e) {
            throw MESSAGES.cannotAccessClass(e, className);
        }
        formatterSpec.apply(handler);
        if (level != null) handler.setLevel(level);
        try {
            handler.setEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new StartException(e);
        }
        // Set the properties
        setProperties(handler, properties);
        value = handler;
    }

    @Override
    public synchronized void stop(final StopContext context) {
        final Handler handler = value;
        handler.close();
        properties.clear();
        value = null;
    }

    public synchronized void addProperty(final Property property) {
        properties.add(property);
        final Handler handler = value;
        if (handler != null) {
            setProperties(handler, this.properties);
        }
    }


    public synchronized void addProperties(final Collection<Property> properties) {
        this.properties.addAll(properties);
        final Handler handler = value;
        if (handler != null) {
            setProperties(handler, this.properties);
        }
    }

    @Override
    public synchronized Handler getValue() throws IllegalStateException {
        return value;
    }

    public synchronized Level getLevel() {
        return level;
    }

    public synchronized void setLevel(final Level level) {
        this.level = level;
        final Handler handler = value;
        if (handler != null) handler.setLevel(level);
    }


    public synchronized AbstractFormatterSpec getFormatterSpec() {
        return formatterSpec;
    }

    public synchronized void setFormatterSpec(final AbstractFormatterSpec formatterSpec) {
        this.formatterSpec = formatterSpec;
        final Handler handler = value;
        if (handler != null) formatterSpec.apply(handler);
    }

    public synchronized String getEncoding() {
        return encoding;
    }

    public synchronized void setEncoding(final String encoding) throws UnsupportedEncodingException {
        final Handler handler = value;
        if (handler != null) handler.setEncoding(encoding);
        this.encoding = encoding;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12469.java