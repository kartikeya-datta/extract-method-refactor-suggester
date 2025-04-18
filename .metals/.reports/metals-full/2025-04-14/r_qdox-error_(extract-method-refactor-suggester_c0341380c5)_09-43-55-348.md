error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5924.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5924.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5924.java
text:
```scala
.@@setValidator(new EnumValidator(ParticipantStatus.class, true, false))

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.txn.subsystem;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author @author <a href="stefano.maestri@redhat.com">Stefano Maestri</a> 2011 Red Hat Inc.
 */

class LogStoreConstants {
    static final String PROBE = "probe";
    static final String RECOVER = "recover";
    static final String DELETE = "delete";
    static final String REFRESH = "refresh";

    public static final String LOG_STORE = "log-store";
    public static final String TRANSACTIONS = "transactions";
    public static final String PARTICIPANTS = "participants";




    static enum ParticipantStatus {
        PENDING,
        PREPARED,
        FAILED,
        HEURISTIC,
        READONLY
    }

    static final String JMX_ON_ATTRIBUTE = "jmx-name";
    static final String JNDI_ATTRIBUTE = "jndi-name";
    static final String LOG_STORE_TYPE_ATTRIBUTE = "type";

    static final Map<String, String> MODEL_TO_JMX_TXN_NAMES =
            Collections.unmodifiableMap(new HashMap<String, String>() {{
                put(JMX_ON_ATTRIBUTE, null);
                put("id", "Id");
                put("age-in-seconds", "AgeInSeconds");
                put("type", "Type");
            }});

    static final Map<String, String> MODEL_TO_JMX_PARTICIPANT_NAMES =
            Collections.unmodifiableMap(new HashMap<String, String>() {{
                put(JMX_ON_ATTRIBUTE, null);
                put("type", "Type");
                put("status", "Status");
                put(JNDI_ATTRIBUTE, "JndiName");
                put("eis-product-name", "EisProductName");
                put("eis-product-version", "EisProductVersion");
            }});

    static final String[] TXN_JMX_NAMES = MODEL_TO_JMX_TXN_NAMES.values().toArray(new String[MODEL_TO_JMX_TXN_NAMES.size()]);
    static final String[] PARTICIPANT_JMX_NAMES = MODEL_TO_JMX_PARTICIPANT_NAMES.values().toArray(new String[MODEL_TO_JMX_PARTICIPANT_NAMES.size()]);

    static SimpleAttributeDefinition LOG_STORE_TYPE = (new SimpleAttributeDefinitionBuilder(LOG_STORE_TYPE_ATTRIBUTE, ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode("default"))
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();

    static SimpleAttributeDefinition JMX_NAME = (new SimpleAttributeDefinitionBuilder(JMX_ON_ATTRIBUTE, ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .setValidator(new StringLengthValidator(0, true))
            .build();

    static SimpleAttributeDefinition TRANSACTION_AGE = (new SimpleAttributeDefinitionBuilder("age-in-seconds", ModelType.LONG))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.SECONDS)
            .build();

    static SimpleAttributeDefinition TRANSACTION_ID = (new SimpleAttributeDefinitionBuilder("id", ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();

    static SimpleAttributeDefinition PARTICIPANT_STATUS = (new SimpleAttributeDefinitionBuilder("status", ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .setValidator(new EnumValidator(ParticipantStatus.class, false, false))
            .build();

    static SimpleAttributeDefinition PARTICIPANT_JNDI_NAME = (new SimpleAttributeDefinitionBuilder(JNDI_ATTRIBUTE, ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();

    static SimpleAttributeDefinition EIS_NAME = (new SimpleAttributeDefinitionBuilder("eis-product-name", ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .setValidator(new StringLengthValidator(0, true))
            .build();

    static SimpleAttributeDefinition EIS_VERSION = (new SimpleAttributeDefinitionBuilder("eis-product-version", ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .setValidator(new StringLengthValidator(0, true))
            .build();

    static SimpleAttributeDefinition RECORD_TYPE = (new SimpleAttributeDefinitionBuilder("type", ModelType.STRING))
            .setAllowExpression(false)
            .setAllowNull(true)
            .setDefaultValue(new ModelNode())
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();

    static String jmxNameToModelName(Map<String, String> map, String jmxName) {
        for(Map.Entry<String, String> e : map.entrySet()) {
            if (jmxName.equals(e.getValue()))
                return e.getKey();
        }

        return null;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5924.java