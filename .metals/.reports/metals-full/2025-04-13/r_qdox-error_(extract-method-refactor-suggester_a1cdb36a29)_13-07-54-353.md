error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4319.java
text:
```scala
U@@ndertowServletLogger.ROOT_LOGGER.failedToPersistSessionAttribute(sessionAttribute.getKey(), sessionAttribute.getValue(), sessionEntry.getKey());

package org.wildfly.extension.undertow;

import io.undertow.servlet.UndertowServletLogger;
import io.undertow.servlet.api.SessionPersistenceManager;
import org.jboss.marshalling.ByteBufferInput;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.ModularClassResolver;
import org.jboss.marshalling.OutputStreamByteOutput;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.marshalling.river.RiverMarshallerFactory;
import org.jboss.modules.ModuleLoader;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistent session manager
 *
 * @author Stuart Douglas
 */
public abstract class AbstractPersistentSessionManager implements SessionPersistenceManager, Service<SessionPersistenceManager> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("undertow", "persistent-session-manager");

    private MarshallerFactory factory;
    private MarshallingConfiguration configuration;

    private final InjectedValue<ModuleLoader> moduleLoaderInjectedValue = new InjectedValue<>();

    @Override
    public void persistSessions(String deploymentName, Map<String, PersistentSession> sessionData) {
        try {
            final Marshaller marshaller = createMarshaller();
            try {
                final Map<String, SessionEntry> serializedData = new HashMap<String, SessionEntry>();
                for (Map.Entry<String, PersistentSession> sessionEntry : sessionData.entrySet()) {
                    Map<String, byte[]> data = new HashMap<String, byte[]>();
                    for (Map.Entry<String, Object> sessionAttribute : sessionEntry.getValue().getSessionData().entrySet()) {
                        try {
                            final ByteArrayOutputStream out = new ByteArrayOutputStream();
                            marshaller.start(new OutputStreamByteOutput(out));
                            marshaller.writeObject(sessionAttribute.getValue());
                            marshaller.finish();
                            data.put(sessionAttribute.getKey(), out.toByteArray());
                        } catch (Exception e) {
                            UndertowLogger.ROOT_LOGGER.failedToPersistSessionAttribute(sessionAttribute.getKey(), sessionAttribute.getValue(), sessionEntry.getKey(), e);
                        }
                    }
                    serializedData.put(sessionEntry.getKey(), new SessionEntry(sessionEntry.getValue().getExpiration(), data));
                }
                persistSerializedSessions(deploymentName, serializedData);
            } finally {
                marshaller.close();
            }
        } catch (Exception e) {
            UndertowServletLogger.ROOT_LOGGER.failedToPersistSessions(e);
        }

    }

    protected abstract void persistSerializedSessions(String deploymentName, Map<String, SessionEntry> serializedData) throws IOException;

    protected abstract Map<String, SessionEntry> loadSerializedSessions(final String deploymentName) throws IOException;

    @Override
    public Map<String, PersistentSession> loadSessionAttributes(String deploymentName, final ClassLoader classLoader) {
        try {
            Unmarshaller unmarshaller = createUnmarshaller();
            try {
                long time = System.currentTimeMillis();
                Map<String, SessionEntry> data = loadSerializedSessions(deploymentName);
                if (data != null) {
                    Map<String, PersistentSession> ret = new HashMap<String, PersistentSession>();
                    for (Map.Entry<String, SessionEntry> sessionEntry : data.entrySet()) {
                        if (sessionEntry.getValue().expiry.getTime() > time) {
                            Map<String, Object> session = new HashMap<String, Object>();
                            for (Map.Entry<String, byte[]> sessionAttribute : sessionEntry.getValue().data.entrySet()) {
                                unmarshaller.start(new ByteBufferInput(ByteBuffer.wrap(sessionAttribute.getValue())));
                                session.put(sessionAttribute.getKey(), unmarshaller.readObject());
                                unmarshaller.finish();
                            }
                            ret.put(sessionEntry.getKey(), new PersistentSession(sessionEntry.getValue().expiry, session));
                        }
                    }
                    return ret;
                }
            } finally {
                unmarshaller.close();
            }
        } catch (Exception e) {
            UndertowServletLogger.ROOT_LOGGER.failedtoLoadPersistentSessions(e);
        }
        return null;
    }

    protected Marshaller createMarshaller() throws IOException {
        return factory.createMarshaller(configuration);
    }

    protected Unmarshaller createUnmarshaller() throws IOException {
        return factory.createUnmarshaller(configuration);
    }

    @Override
    public void clear(String deploymentName) {
    }

    @Override
    public synchronized void start(StartContext startContext) throws StartException {
        final RiverMarshallerFactory factory = new RiverMarshallerFactory();
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setClassResolver(ModularClassResolver.getInstance(moduleLoaderInjectedValue.getValue()));
        this.configuration = configuration;
        this.factory = factory;
    }

    @Override
    public synchronized void stop(StopContext stopContext) {
    }

    @Override
    public synchronized SessionPersistenceManager getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public InjectedValue<ModuleLoader> getModuleLoaderInjectedValue() {
        return moduleLoaderInjectedValue;
    }

    protected static final class SessionEntry {
        private final Date expiry;
        private final Map<String, byte[]> data;

        private SessionEntry(Date expiry, Map<String, byte[]> data) {
            this.expiry = expiry;
            this.data = data;
        }

        public Date getExpiry() {
            return expiry;
        }

        public Map<String, byte[]> getData() {
            return data;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4319.java