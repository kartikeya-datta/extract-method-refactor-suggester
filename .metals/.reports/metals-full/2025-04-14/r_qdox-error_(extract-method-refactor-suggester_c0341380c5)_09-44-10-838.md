error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4404.java
text:
```scala
F@@ile configDir = env.getServerConfigurationDir();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server.mgmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.Element;
import org.jboss.as.model.ServerModel;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.staxmapper.XMLContentWriter;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.jboss.staxmapper.XMLMapper;

/**
 * Default implementation of {@link ServerConfigurationPersister}.
 *
 * @author Brian Stansberry
 */
public class ServerConfigurationPersisterImpl implements ServerConfigurationPersister, Service<ServerConfigurationPersister> {

    private static Logger logger = Logger.getLogger("org.jboss.as.server");

    private final InjectedValue<ServerEnvironment> serverEnvironment = new InjectedValue<ServerEnvironment>();
    private final ServerModel configuration;
    private File configFile;

    public static void addService(ServerModel configuration, BatchBuilder batchBuilder) {
        ServerConfigurationPersisterImpl service = new ServerConfigurationPersisterImpl(configuration);
        batchBuilder.addService(SERVICE_NAME, service)
                    .addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, service.serverEnvironment);
    }

    public ServerConfigurationPersisterImpl(ServerModel configuration) {
        if (configuration == null)
            throw new IllegalArgumentException("configuration is null");
        this.configuration = configuration;
    }

    @Override
    public void configurationModified() {
        if (configFile != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            OutputStreamWriter writer = null;
            try {
                backupConfigFile();
                configFile.createNewFile();
                fos = new FileOutputStream(configFile);
                bos = new BufferedOutputStream(fos);
                writer = new OutputStreamWriter(bos);
                final XMLMapper mapper = XMLMapper.Factory.create();
                mapper.deparseDocument(new RootElementWriter(), XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
            }
            catch (Exception e) {
                logger.errorf(e, "Failed persisting configuration file %s" , configFile.getAbsolutePath());
            }
            finally {
                 if (writer != null) {
                     try {
                        writer.close();
                    } catch (IOException e) {
                        logger.warnf(e, "Failed closing writer to configuration file %s" , configFile.getAbsolutePath());
                    }
                 }
                 if (fos != null) {
                     try {
                        fos.close();
                    } catch (IOException e) {
                        logger.warnf(e, "Failed closing output stream to configuration file %s" , configFile.getAbsolutePath());
                    }
                 }
            }

        }

    }

    private void backupConfigFile() throws IOException {
        File backup = new File(configFile.getParent(), configFile.getName() + ".last-known-good");
        copyFile(configFile, backup);
    }

    private void copyFile(File file, File backup) throws IOException {

        if (backup.exists())
            backup.delete();

        if (!file.renameTo(backup)) {
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            FileInputStream fis = null;
            try {
                fos = new FileOutputStream(backup);
                bos = new BufferedOutputStream(fos);
                fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                byte[] bytes = new byte[8192];
                int read;
                while ((read = bis.read(bytes)) > -1) {
                    bos.write(bytes, 0, read);
                }
            }
            finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                 } catch (Exception ignored) {}
                 try {
                     if (fos != null) {
                         fos.close();
                     }
                  } catch (Exception ignored) {}
                  try {
                      if (fis != null) {
                          fis.close();
                      }
                   } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public void start(StartContext context) throws StartException {

        ServerEnvironment env;
        try {
            env = serverEnvironment.getValue();
        }
        catch (IllegalStateException ise) {
            throw new StartException(ServerEnvironment.class.getSimpleName() + " was not injected");
        }

        if (env.isStandalone()) {
            File configDir = serverEnvironment.getValue().getServerConfigurationDir();
            if (configDir.exists()) {
                if (!configDir.isDirectory()) {
                    throw new StartException(configDir.getAbsolutePath() + " is not a directory");
                }
                else if (!configDir.canWrite()) {
                    throw new StartException(configDir.getAbsolutePath() + " is not a writable");
                }
            }
            else if (!configDir.mkdirs()) {
                throw new StartException("Cannot create directory " + configDir.getAbsolutePath());
            }

            configFile = new File(configDir, "standalone.xml");
            if (configFile.exists()) {
                if (configFile.isDirectory()) {
                    throw new StartException(configFile.getAbsolutePath() + " is a directory");
                }
                else if (!configDir.canWrite()) {
                    throw new StartException(configFile.getAbsolutePath() + " is not a writable");
                }

            }
        }
    }

    @Override
    public void stop(StopContext context) {
        configFile = null;
    }

    @Override
    public ServerConfigurationPersister getValue() throws IllegalStateException {
        return this;
    }

    private class RootElementWriter implements XMLContentWriter {

        @Override
        public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
            streamWriter.writeStartDocument();
            streamWriter.writeStartElement(Element.SERVER.getLocalName());
            configuration.writeContent(streamWriter);
            streamWriter.writeEndDocument();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4404.java