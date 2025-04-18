error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14242.java
text:
```scala
i@@f (message.contains("14807") || message.contains("14883") || message.contains("11340")) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.test.integration.management.interfaces;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ACCESS_CONTROL;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESPONSE_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 * @author jcechace
 * @author Ladislav Thon <lthon@redhat.com>
 */
public class JmxManagementInterface implements ManagementInterface {
    private final JMXConnector jmxConnector;
    private final String domain;

    protected JmxManagementInterface(JMXConnector jmxConnector, String domain) {
        this.jmxConnector = jmxConnector;
        this.domain = domain;
    }

    public void close() {
        try {
            jmxConnector.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public ModelNode execute(ModelNode operation) {
        try {
            ObjectName objectName = objectName(operation);
            return doExecute(objectName, operation);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    private ModelNode doExecute(ObjectName objectName, ModelNode op) {
        String opName = op.get(OP).asString();
        if (READ_ATTRIBUTE_OPERATION.equals(opName)) {
            String name = JmxInterfaceStringUtils.toCamelCase(op.get(NAME).asString());
            return getAttribute(objectName, name);
        } else if (WRITE_ATTRIBUTE_OPERATION.equals(opName)) {
            String name = JmxInterfaceStringUtils.toCamelCase(op.get(NAME).asString());
            Object value = object(op.get(VALUE));
            return setAttribute(objectName, name, value);
        } else if (READ_RESOURCE_OPERATION.equals(opName)) {
            return getInfo(objectName);
        } else {
            Set<String> keys = new HashSet<String>(op.keys()); // must copy
            keys.remove(OP);
            keys.remove(OP_ADDR);
            if (!keys.isEmpty()) {
                throw new UnsupportedOperationException("Operations with arguments are not supported: " + opName + " " + keys);
            }
            String name = JmxInterfaceStringUtils.toCamelCase(op.get(OP).asString());
            return invoke(objectName, name);
        }
    }

    private ModelNode getAttribute(ObjectName objectName, String name) {
        MBeanServerConnection connection = getConnection();

        Object result = null;
        JMException exception = null;
        try {
            result = connection.getAttribute(objectName, name);
        } catch (JMException e) {
            exception = e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modelNodeResult(result, exception);
    }

    private ModelNode setAttribute(ObjectName objectName, String name, Object value) {
        MBeanServerConnection connection = getConnection();

        Attribute attribute = new Attribute(name, value);
        JMException exception = null;
        try {
            connection.setAttribute(objectName, attribute);
        } catch (JMException e) {
            exception = e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modelNodeResult(null, exception);
    }

    private ModelNode invoke(ObjectName objectName, String name) {
        MBeanServerConnection connection = getConnection();

        Object result = null;
        JMException exception = null;
        try {
            result = connection.invoke(objectName, name, null, null);
        } catch (JMException e) {
            exception = e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modelNodeResult(result, exception);
    }

    private ModelNode getInfo(ObjectName objectName) {
        MBeanServerConnection connection = getConnection();
        ModelNode attributes = null;
        ModelNode headers = null;
        JMException exception = null;
        try {
            MBeanInfo mBeanInfo = connection.getMBeanInfo(objectName);
            MBeanAttributeInfo[] attributeInfos = mBeanInfo.getAttributes();
            ModelNode[] data = modelNodeAttributesInfo(attributeInfos, objectName);
            attributes = data[0];
            headers = data[1];
        } catch (JMException e) {
            exception = e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return modelNodeResult(attributes, exception, headers);
    }

    private ModelNode[] modelNodeAttributesInfo(MBeanAttributeInfo[] attributeInfos, ObjectName objectName) throws IOException, JMException {
        MBeanServerConnection connection = getConnection();
        ModelNode attributes = new ModelNode();
        ModelNode filtered = new ModelNode().setEmptyList();
        ModelNode headers = null;

        for (MBeanAttributeInfo attribute : attributeInfos) {
            String attributeName = attribute.getName();
            try {
                Object attributeValue = connection.getAttribute(objectName, attributeName);
                try {
                    attributes.get(JmxInterfaceStringUtils.toDashCase(attributeName)).set(modelNode(attributeValue));
                } catch (UnsupportedOperationException e) {
                    // happens for some attributes that are represented as a Tabular***; let's just ignore them
                }
            } catch (JMException e) {
                // see RbacUtil.checkOperationResult for error codes
                // TODO could possibly use MBeanAttributeInfo#isReadable instead of error codes, but it's currently broken
                String message = e.getMessage();
                if (message.contains("14807") || message.contains("14883")) {
                    throw e;
                } else if (message.contains("13456")) {
                    filtered.add(JmxInterfaceStringUtils.toDashCase(attributeName));
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!filtered.asList().isEmpty()) {
            headers = new ModelNode();
            headers.get(ACCESS_CONTROL, "filtered-attributes").set(filtered);
        }
        return new ModelNode[]{attributes, headers};
    }

    private ModelNode modelNodeResult(Object result, JMException exception) {
        return modelNodeResult(result, exception, null);
    }

    private ModelNode modelNodeResult(Object result, JMException exception, ModelNode headers) {
        ModelNode root = new ModelNode();
        if (exception == null) {
            root.get(OUTCOME).set(SUCCESS);
            if (result != null) {
                root.get(RESULT).set(modelNode(result));
            }
        } else {
            root.get(OUTCOME).set(FAILED);
            root.get(FAILURE_DESCRIPTION).set(JmxInterfaceStringUtils.rawString(exception.getMessage()));
            if (result != null) {
                root.get(RESULT).set(modelNode(result));
            }
        }

        if (headers != null) {
            root.get(RESPONSE_HEADERS).set(headers);
        }
        return root;
    }

    private static ModelNode modelNode(Object obj) {
        if (obj == null) {
            return new ModelNode();
        } else if (obj instanceof ModelNode) {
            return ((ModelNode) obj);
        } else if (obj instanceof BigDecimal) {
            return new ModelNode((BigDecimal) obj);
        } else if (obj instanceof BigInteger) {
            return new ModelNode((BigInteger) obj);
        } else if (obj instanceof Boolean) {
            return new ModelNode((Boolean) obj);
        } else if (obj instanceof byte[]) {
            return new ModelNode((byte[]) obj);
        } else if (obj instanceof Double) {
            return new ModelNode((Double) obj);
        } else if (obj instanceof Integer) {
            return new ModelNode((Integer) obj);
        } else if (obj instanceof Long) {
            return new ModelNode((Long) obj);
        } else if (obj instanceof String) {
            return JmxInterfaceStringUtils.nodeFromString((String) obj);
        } else {
            throw new UnsupportedOperationException("Can't convert '" + obj.getClass() + "' to ModelNode: " + obj);
        }
    }

    private static Object object(ModelNode node) {
        switch (node.getType()) {
            case BIG_DECIMAL:
                return node.asBigDecimal();
            case BIG_INTEGER:
                return node.asBigInteger();
            case BOOLEAN:
                return node.asBoolean();
            case BYTES:
                return node.asBytes();
            case DOUBLE:
                return node.asDouble();
            case EXPRESSION:
                return node.asExpression();
            case INT:
                return node.asInt();
            case LIST:
                return node.asList();
            case LONG:
                return node.asLong();
            case PROPERTY:
                return node.asProperty();
            case STRING:
                return node.asString();
            case UNDEFINED:
                return null;
            default:
                throw new UnsupportedOperationException("Can't convert '" + node.getType() + "' to object");
        }
    }

    private ObjectName objectName(ModelNode operation) throws MalformedObjectNameException {
        StringBuilder builder = new StringBuilder();
        String opName = operation.get(OP).asString();
        if (operation.has(OP_ADDR)) {
            ModelNode address = operation.get(OP_ADDR);
            Iterator<ModelNode> it = address.asList().iterator();
            while (it.hasNext()) {
                Property segment = it.next().asProperty();
                if (opName.equals(ADD) && !it.hasNext()) { // skip the last one in case of 'add'
                    continue;
                }
                builder.append(segment.getName()).append("=").append(segment.getValue().asString()).append(",");
            }
        }
        if (builder.toString().isEmpty()) {
            builder.append("management-root=server,");
        }

        builder.deleteCharAt(builder.length() - 1);
        return new ObjectName(domain + ":" + builder.toString());
    }

    public MBeanServerConnection getConnection() {
        MBeanServerConnection mBeanServerConnection = null;
        try {
            mBeanServerConnection = jmxConnector.getMBeanServerConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mBeanServerConnection;
    }

    public static JmxManagementInterface create(JMXServiceURL url, String username, String password, String domain) {
        try {
            Map<String, String[]> env = new HashMap<String, String[]>();
            env.put(JMXConnector.CREDENTIALS, new String[]{username, password});
            JMXConnector jmxConnector = JMXConnectorFactory.connect(url, env);
            return new JmxManagementInterface(jmxConnector, domain);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14242.java