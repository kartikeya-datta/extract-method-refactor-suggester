error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3851.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3851.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3851.java
text:
```scala
p@@attern = Pattern.compile("\\Q" + placeholderPrefix + "\\E(.+?)\\Q" + placeholderSuffix + "\\E");

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.ComponentDefinitionRegistryProcessor;
import org.apache.aries.blueprint.mutable.MutableBeanArgument;
import org.apache.aries.blueprint.mutable.MutableBeanMetadata;
import org.apache.aries.blueprint.mutable.MutableBeanProperty;
import org.apache.aries.blueprint.mutable.MutableCollectionMetadata;
import org.apache.aries.blueprint.mutable.MutableMapEntry;
import org.apache.aries.blueprint.mutable.MutableMapMetadata;
import org.apache.aries.blueprint.mutable.MutablePropsMetadata;
import org.apache.aries.blueprint.mutable.MutableReferenceListener;
import org.apache.aries.blueprint.mutable.MutableRegistrationListener;
import org.apache.aries.blueprint.mutable.MutableServiceMetadata;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.MapMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.NonNullMetadata;
import org.osgi.service.blueprint.reflect.PropsMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.ReferenceMetadata;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.Target;
import org.osgi.service.blueprint.reflect.ValueMetadata;

/**
 * Abstract class for property placeholders.
 *
 * @version $Rev$, $Date$
 */
public abstract class AbstractPropertyPlaceholder implements ComponentDefinitionRegistryProcessor {

    private String placeholderPrefix = "${";
    private String placeholderSuffix = "}";
    private Pattern pattern;

    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
    }

    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
    }

    public void process(ComponentDefinitionRegistry registry) throws ComponentDefinitionException {
        for (String name : registry.getComponentDefinitionNames()) {
            processMetadata(registry.getComponentDefinition(name));
        }
    }

    protected Metadata processMetadata(Metadata metadata) {
        if (metadata instanceof BeanMetadata) {
            return processBeanMetadata((BeanMetadata) metadata);
        } else if (metadata instanceof ReferenceListMetadata) {
            return processRefCollectionMetadata((ReferenceListMetadata) metadata);
        } else if (metadata instanceof ReferenceMetadata) {
            return processReferenceMetadata((ReferenceMetadata) metadata);
        } else if (metadata instanceof ServiceMetadata) {
            return processServiceMetadata((ServiceMetadata) metadata);
        } else if (metadata instanceof CollectionMetadata) {
            return processCollectionMetadata((CollectionMetadata) metadata);
        } else if (metadata instanceof MapMetadata) {
            return processMapMetadata((MapMetadata) metadata);
        } else if (metadata instanceof PropsMetadata) {
            return processPropsMetadata((PropsMetadata) metadata);
        } else if (metadata instanceof ValueMetadata) {
            return processValueMetadata((ValueMetadata) metadata);
        } else {
            return metadata;
        }
    }

    protected Metadata processBeanMetadata(BeanMetadata component) {
        for (BeanArgument arg :  component.getArguments()) {
            ((MutableBeanArgument) arg).setValue(processMetadata(arg.getValue()));
        }
        for (BeanProperty prop : component.getProperties()) {
            ((MutableBeanProperty) prop).setValue(processMetadata(prop.getValue()));
        }
        ((MutableBeanMetadata) component).setFactoryComponent((Target) processMetadata(component.getFactoryComponent()));
        return component;
    }

    protected Metadata processServiceMetadata(ServiceMetadata component) {
        ((MutableServiceMetadata) component).setServiceComponent((Target) processMetadata(component.getServiceComponent()));
        List<MapEntry> entries = new ArrayList<MapEntry>(component.getServiceProperties());
        for (MapEntry entry : entries) {
            ((MutableServiceMetadata) component).removeServiceProperty(entry);
        }
        for (MapEntry entry : processMapEntries(entries)) {
            ((MutableServiceMetadata) component).addServiceProperty(entry);
        }
        for (RegistrationListener listener : component.getRegistrationListeners()) {
            ((MutableRegistrationListener) listener).setListenerComponent((Target) processMetadata(listener.getListenerComponent()));
        }
        return component;
    }

    protected Metadata processReferenceMetadata(ReferenceMetadata component) {
        for (ReferenceListener listener : component.getReferenceListeners()) {
            ((MutableReferenceListener) listener).setListenerComponent((Target) processMetadata(listener.getListenerComponent()));
        }
        return component;
    }

    protected Metadata processRefCollectionMetadata(ReferenceListMetadata component) {
        for (ReferenceListener listener : component.getReferenceListeners()) {
            ((MutableReferenceListener) listener).setListenerComponent((Target) processMetadata(listener.getListenerComponent()));
        }
        return component;
    }

    protected Metadata processPropsMetadata(PropsMetadata metadata) {
        List<MapEntry> entries = new ArrayList<MapEntry>(metadata.getEntries());
        for (MapEntry entry : entries) {
            ((MutablePropsMetadata) metadata).removeEntry(entry);
        }
        for (MapEntry entry : processMapEntries(entries)) {
            ((MutablePropsMetadata) metadata).addEntry(entry);
        }
        return metadata;
    }

    protected Metadata processMapMetadata(MapMetadata metadata) {
        List<MapEntry> entries = new ArrayList<MapEntry>(metadata.getEntries());
        for (MapEntry entry : entries) {
            ((MutableMapMetadata) metadata).removeEntry(entry);
        }
        for (MapEntry entry : processMapEntries(entries)) {
            ((MutableMapMetadata) metadata).addEntry(entry);
        }
        return metadata;
    }

    protected List<MapEntry> processMapEntries(List<MapEntry> entries) {
        for (MapEntry entry : entries) {
            ((MutableMapEntry) entry).setKey((NonNullMetadata) processMetadata(entry.getKey()));
            ((MutableMapEntry) entry).setValue(processMetadata(entry.getValue()));
        }
        return entries;
    }

    protected Metadata processCollectionMetadata(CollectionMetadata metadata) {
        List<Metadata> values = new ArrayList<Metadata>(metadata.getValues());
        for (Metadata value : values) {
            ((MutableCollectionMetadata) metadata).removeValue(value);
        }
        for (Metadata value : values) {
            ((MutableCollectionMetadata) metadata).addValue(processMetadata(value));
        }
        return metadata;
    }

    protected Metadata processValueMetadata(ValueMetadata metadata) {
        return new LateBindingValueMetadata(metadata);
    }

    protected String processString(String str) {
        // TODO: we need to handle escapes on the prefix / suffix
        Matcher matcher = getPattern().matcher(str);
        while (matcher.find()) {
            String rep = getProperty(matcher.group(1));
            if (rep != null) {
                str = str.replace(matcher.group(0), rep);
                matcher.reset(str);
            }
        }
        return str;
    }

    protected String getProperty(String val) {
        return null;
    }

    protected Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.compile("\\Q" + placeholderPrefix + "\\E(.+)\\Q" + placeholderSuffix + "\\E");
        }
        return pattern;
    }

    public class LateBindingValueMetadata implements ValueMetadata {

        private final ValueMetadata metadata;
        private boolean retrieved;
        private String retrievedValue;

        public LateBindingValueMetadata(ValueMetadata metadata) {
            this.metadata = metadata;
        }

        public String getStringValue() {
            if (!retrieved) {
                retrieved = true;
                retrievedValue = processString(metadata.getStringValue());
            }
            return retrievedValue;
        }

        public String getType() {
            return metadata.getType();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3851.java