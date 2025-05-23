error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10278.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10278.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10278.java
text:
```scala
l@@ist.add(e.nextElement());

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

import org.apache.openjpa.lib.conf.Configurable;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.InternalException;

/**
 * Default {@link CacheMarshaller} implementation that writes data
 * to a specified file and reads data from a specified file or URL.
 *
 * @since 1.1.0
 */
public class CacheMarshallerImpl
    implements CacheMarshaller, Configurable {

    private static final Localizer _loc =
        Localizer.forPackage(CacheMarshallerImpl.class);

    private String _id;
    private ValidationPolicy _validationPolicy;
    private OpenJPAConfiguration _conf;
    private Log _log;
    private File _outputFile;
    private URL _inputURL;

    // temporary storage for resource location specification
    private String _inputResourceLocation;
    
    private boolean _consumeErrors = true;

    public Object load() {
        if (_inputURL == null) {
            _log.trace(_loc.get("cache-marshaller-no-inputs", getId()));
            return null;
        }

        Object o = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(
                _inputURL.openStream()));

            o = in.readObject();
            o = _validationPolicy.getValidData(o);

            if (o != null && o.getClass().isArray()) {
                Object[] array = (Object[]) o;
                for (int i = 0; i < array.length; i++)
                    configure(array[i]);
            } else {
                configure(o);
            }

            if (_log.isTraceEnabled())
                _log.trace(_loc.get("cache-marshaller-loaded",
                    o == null ? null : o.getClass().getName(), _inputURL));
        } catch (Exception e) {
            if (_consumeErrors) {
                if (_log.isWarnEnabled())
                    _log.warn(_loc.get("cache-marshaller-load-exception-ignore",
                        _inputURL), e);
            } else {
                throw new InternalException(
                    _loc.get("cache-marshaller-load-exception-fatal",
                        _inputURL),
                    e);
            }
        } finally {
            if (in != null)
                try { in.close(); } catch (IOException e) { }
        }

        return o;
    }

    private void configure(Object o) {
        if (o instanceof Configurable) {
            ((Configurable) o).setConfiguration(_conf);
            ((Configurable) o).startConfiguration();
            ((Configurable) o).endConfiguration();
        }
    }

    public void store(Object o) {
        if (_outputFile == null) {
            _log.trace(_loc.get("cache-marshaller-no-output-file", getId()));
            return;
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(_outputFile);
            ObjectOutputStream oos =
                new ObjectOutputStream(new BufferedOutputStream(out));
            Object toStore = _validationPolicy.getCacheableData(o);
            oos.writeObject(toStore);
            oos.flush();
            out.flush();
            if (_log.isTraceEnabled())
                _log.trace(_loc.get("cache-marshaller-stored",
                    o.getClass().getName(), _outputFile));
        } catch (Exception e) {
            if (_consumeErrors) {
                if (_log.isWarnEnabled())
                    _log.warn(_loc.get("cache-marshaller-store-exception",
                        o.getClass().getName(), _outputFile), e);
            } else {
                throw new InternalException(
                    _loc.get("cache-marshaller-store-exception",
                        o.getClass().getName(), _outputFile),
                    e);
            }
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException ioe) { }
            }
        }
    }

    public void setOutputFile(File file) {
        _outputFile = file;
    }

    public File getOutputFile() {
        return _outputFile;
    }

    public void setInputURL(URL url) {
        _inputURL = url;
    }

    public void setInputResource(String resource) {
        _inputResourceLocation = resource;
    }

    public void setConsumeSerializationErrors(boolean consume) {
        _consumeErrors = consume;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public void setValidationPolicy(String policy)
        throws InstantiationException, IllegalAccessException {
        String name = Configurations.getClassName(policy);
        String props = Configurations.getProperties(policy);
        _validationPolicy = (ValidationPolicy)
            Configurations.newInstance(name, _conf, props, null);
    }

    public ValidationPolicy getValidationPolicy() {
        return _validationPolicy;
    }

    public void setConfiguration(Configuration conf) {
        _conf = (OpenJPAConfiguration) conf;
        _log = conf.getConfigurationLog();
    }

    public void startConfiguration() {
    }

    public void endConfiguration() {
        if (_inputResourceLocation != null && _inputURL != null)
            throw new IllegalStateException(
                _loc.get("cache-marshaller-input-url-and-resource-specified")
                    .getMessage());
        if (_inputResourceLocation != null)
            setInputUrlFromResourceLocation();

        if (_validationPolicy == null)
            throw new IllegalStateException(
                _loc.get("cache-marshaller-null-validation-policy",
                    getClass().getName()).getMessage());
        if (_id == null)
            throw new IllegalStateException(
                _loc.get("cache-marshaller-null-id",
                    getClass().getName()).getMessage());
    }

    private void setInputUrlFromResourceLocation() {
        try {
            ClassLoader cl = _conf.getClassResolverInstance()
                .getClassLoader(getClass(), null);
            List list = new ArrayList();
            for (Enumeration e = cl.getResources(_inputResourceLocation);
                e.hasMoreElements(); )
                list.add(e);

            if (list.size() > 1) {
                if (_consumeErrors) {
                    if (_log.isWarnEnabled())
                        _log.warn(_loc.get(
                            "cache-marshaller-multiple-resources-warn",
                            getId(), _inputResourceLocation, list)
                            .getMessage());
                } else {
                    throw new IllegalStateException(
                        _loc.get("cache-marshaller-multiple-resources",
                            getId(), _inputResourceLocation, list)
                            .getMessage());
                }
            }

            if (!list.isEmpty())
                _inputURL = (URL) list.get(0);
        } catch (IOException ioe) {
            IllegalStateException ise = new IllegalStateException(
                _loc.get("cache-marshaller-bad-url", getId(),
                    _inputResourceLocation).getMessage());
            ise.initCause(ioe);
            throw ise;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10278.java