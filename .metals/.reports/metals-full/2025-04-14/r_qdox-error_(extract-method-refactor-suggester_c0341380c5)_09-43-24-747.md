error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8246.java
text:
```scala
final M@@odelNode toSet = ArgumentValueConverter.DEFAULT.fromString(ctx, value);

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
package org.jboss.as.cli.operation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.cli.ArgumentValueConverter;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineFormat;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.ParsedOperationRequestHeader;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.OperationRequestAddress.Node;
import org.jboss.as.cli.parsing.ParserUtil;
import org.jboss.as.cli.parsing.ParsingStateCallbackHandler;
import org.jboss.as.cli.parsing.operation.header.RolloutPlanHeaderCallbackHandler;
import org.jboss.dmr.ModelNode;

/**
*
* @author Alexey Loubyansky
*/
public class DefaultCallbackHandler extends ValidatingCallbackHandler implements ParsedCommandLine {

    private static final int SEPARATOR_NONE = 0;
    private static final int SEPARATOR_NODE_TYPE_NAME = 1;
    private static final int SEPARATOR_NODE = 2;
    private static final int SEPARATOR_ADDRESS_OPERATION = 3;
    private static final int SEPARATOR_OPERATION_ARGUMENTS = 4;
    private static final int SEPARATOR_ARG_NAME_VALUE = 5;
    private static final int SEPARATOR_ARG = 6;
    private static final int SEPARATOR_ARG_LIST_END = 7;
    private static final int SEPARATOR_HEADERS_START = 8;
    private static final int SEPARATOR_HEADER = 9;

    private static final DefaultOperationRequestAddress EMPTY_ADDRESS = new DefaultOperationRequestAddress();

    private int separator = SEPARATOR_NONE;
    private int lastSeparatorIndex = -1;
    private int lastChunkIndex = 0;

    private boolean operationComplete;
    private String operationName;
    private OperationRequestAddress address;
    private Map<String, String> props = new HashMap<String, String>();
    private List<String> otherArgs = new ArrayList<String>();
    private String outputTarget;

    private String lastPropName;
    private String lastPropValue;
    private String lastHeaderName;

    private CommandLineFormat format;

    private boolean validation;

    private String originalLine;

    private LinkedHashMap<String,ParsedOperationRequestHeader> headers;
    private ParsedOperationRequestHeader lastHeader;

    public DefaultCallbackHandler() {
        this(true);
    }

    public DefaultCallbackHandler(boolean validate) {
        this.validation = validate;
    }

    public DefaultCallbackHandler(OperationRequestAddress prefix) {
        address = prefix;
    }

    public void parse(OperationRequestAddress initialAddress, String argsStr) throws CommandFormatException {
        reset();
        if(initialAddress != null) {
            address = new DefaultOperationRequestAddress(initialAddress);
        }
        this.originalLine = argsStr;
        ParserUtil.parse(argsStr, this);
    }

    public void parse(OperationRequestAddress initialAddress, String argsStr, boolean validation) throws CommandFormatException {
        final boolean defaultValidation = this.validation;
        this.validation = validation;
        try {
            parse(initialAddress, argsStr);
        } finally {
            this.validation = defaultValidation;
        }
    }

    public void parseOperation(OperationRequestAddress prefix, String argsStr) throws CommandFormatException {
        reset();
        if(prefix != null) {
            address = new DefaultOperationRequestAddress(prefix);
        }
        this.originalLine = argsStr;
        ParserUtil.parseOperationRequest(argsStr, this);
    }

    public void parseHeaders(String argsStr) throws CommandFormatException {
        reset();
        this.originalLine = argsStr;
        ParserUtil.parseHeaders(argsStr, this);
    }

    public void reset() {
        operationComplete = false;
        operationName = null;
        address = null;
        props.clear();
        otherArgs.clear();
        outputTarget = null;
        lastPropName = null;
        lastPropValue = null;
        lastSeparatorIndex = -1;
        lastChunkIndex = 0;
        format = null;
        originalLine = null;
        headers = null;
        lastHeaderName = null;
        lastHeader = null;
    }

    @Override
    public String getOriginalLine() {
        return originalLine;
    }

    public List<String> getOtherProperties() {
        return otherArgs;
    }

    @Override
    public boolean isRequestComplete() {
        return operationComplete;
    }

    @Override
    public boolean endsOnPropertySeparator() {
        return separator == SEPARATOR_ARG;
    }

    @Override
    public boolean endsOnPropertyValueSeparator() {
        return separator == SEPARATOR_ARG_NAME_VALUE;
    }

    @Override
    public boolean endsOnPropertyListStart() {
        return separator == SEPARATOR_OPERATION_ARGUMENTS;
    }

    @Override
    public boolean endsOnPropertyListEnd() {
        return separator == SEPARATOR_ARG_LIST_END;
    }

    @Override
    public boolean endsOnHeaderListStart() {
        return separator == SEPARATOR_HEADERS_START;
    }

    @Override
    public boolean endsOnAddressOperationNameSeparator() {
        return separator == SEPARATOR_ADDRESS_OPERATION;
    }

    @Override
    public boolean endsOnNodeSeparator() {
        return separator == SEPARATOR_NODE;
    }

    @Override
    public boolean endsOnNodeTypeNameSeparator() {
        return separator == SEPARATOR_NODE_TYPE_NAME;
    }

    @Override
    public boolean endsOnSeparator() {
        return separator != SEPARATOR_NONE;
    }

    @Override
    public boolean hasAddress() {
        return address != null;
    }

    @Override
    public OperationRequestAddress getAddress() {
        return address == null ? EMPTY_ADDRESS : address;
    }

    @Override
    public boolean hasOperationName() {
        return operationName != null;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public boolean hasProperties() {
        return !props.isEmpty() || !otherArgs.isEmpty();
    }

    @Override
    public boolean hasProperty(String propertyName) {
        return props.containsKey(propertyName);
    }

    @Override
    public void validatedNodeType(int index, String nodeType) throws OperationFormatException {

        if(address == null) {
            address = new DefaultOperationRequestAddress();
        } else if (address.endsOnType()) {
            throw new OperationFormatException(
                    "Can't proceed with node type '"
                            + nodeType
                            + "' until the node name for the previous node type has been specified.");
        }

        address.toNodeType(nodeType);
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void nodeTypeNameSeparator(int index) {
        separator = SEPARATOR_NODE_TYPE_NAME;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void validatedNodeName(int index, String nodeName) throws OperationFormatException {

        if(address == null) {
            address = new DefaultOperationRequestAddress();
        }

        if(!address.endsOnType()) {
            throw new OperationFormatException("Node path format is wrong around '" + nodeName + "' (index=" + index + ").");
        }

        address.toNode(nodeName);
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void nodeSeparator(int index) {
        separator = SEPARATOR_NODE;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void addressOperationSeparator(int index) {
        separator = SEPARATOR_ADDRESS_OPERATION;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void operationName(int index, String operationName) throws OperationFormatException {
        if(validation) {
            super.operationName(index, operationName);
        } else {
            validatedOperationName(index, operationName);
        }
    }

    public void validatedOperationName(int index, String operationName) throws OperationFormatException {
        this.operationName = operationName;
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void propertyListStart(int index) {
        separator = SEPARATOR_OPERATION_ARGUMENTS;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void propertyName(int index, String propertyName) throws OperationFormatException {
        if(validation) {
            super.propertyName(index, propertyName);
        } else {
            validatedPropertyName(index, propertyName);
        }
    }

    @Override
    protected void validatedPropertyName(int index, String propertyName) throws OperationFormatException {
        props.put(propertyName, null);
        lastPropName = propertyName;
        lastPropValue = null;
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void propertyNameValueSeparator(int index) {
        separator = SEPARATOR_ARG_NAME_VALUE;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void property(String name, String value, int nameValueSeparatorIndex)
            throws OperationFormatException {

/*        if (value.isEmpty()) {
            throw new OperationFormatException(
                    "The argument value is missing or the format is wrong for argument '"
                            + value + "'");
        }
*/
        if(validation) {
            super.property(name, value, nameValueSeparatorIndex);
        } else {
            validatedProperty(name, value, nameValueSeparatorIndex);
        }
    }

    @Override
    protected void validatedProperty(String name, String value, int nameValueSeparatorIndex) throws OperationFormatException {
        if(name == null) {
            otherArgs.add(value);
        } else {
            props.put(name, value);
        }
        lastPropName = name;
        lastPropValue = value;
        separator = SEPARATOR_NONE;
        if(nameValueSeparatorIndex >= 0) {
            this.lastSeparatorIndex = nameValueSeparatorIndex;
        }
        lastChunkIndex = nameValueSeparatorIndex;
    }

    @Override
    public void propertySeparator(int index) {
        separator = SEPARATOR_ARG;
        this.lastSeparatorIndex = index;
        this.lastPropName = null;
        this.lastPropValue = null;
    }

    @Override
    public void propertyListEnd(int index) {
        separator = SEPARATOR_ARG_LIST_END;
        this.lastSeparatorIndex = index;
        this.lastPropName = null;
        this.lastPropValue = null;
    }

    @Override
    public void headerListStart(int index) {
        separator = SEPARATOR_HEADERS_START;
        this.lastSeparatorIndex = index;
    }

    @Override
    public void headerListEnd(int index) {
        separator = SEPARATOR_NONE;
        //operationComplete = true;
        this.lastSeparatorIndex = index;
        //this.lastPropName = null;
        //this.lastPropValue = null;
        operationComplete = true;
    }

    public void headerSeparator(int index) {
        this.separator = SEPARATOR_HEADER;
        this.lastSeparatorIndex = index;
    }

    public void headerNameValueSeparator(int index) {
        this.separator = SEPARATOR_ARG_NAME_VALUE;
        this.lastSeparatorIndex = index;
    }

    @Override
    public ParsingStateCallbackHandler headerName(int index, String headerName) throws CommandFormatException {
        this.separator = SEPARATOR_NONE;
        lastChunkIndex = index;
        this.lastHeaderName = headerName;
        lastHeader = null;
        if(headerName.equals("rollout")) {
            return new RolloutPlanHeaderCallbackHandler(this);
        }
        return null;
    }

    @Override
    public void header(String name, String value, int valueIndex) throws CommandFormatException {
        if(headers == null) {
            headers = new LinkedHashMap<String,ParsedOperationRequestHeader>();
        }
        lastHeader = new SimpleParsedOperationRequestHeader(name, value);
        headers.put(name, lastHeader);
        separator = SEPARATOR_NONE;
        this.lastSeparatorIndex = valueIndex -1;
        this.lastChunkIndex = valueIndex;
        this.lastHeaderName = null;
    }

    public void header(ParsedOperationRequestHeader header) {
        if(headers == null) {
            headers = new LinkedHashMap<String,ParsedOperationRequestHeader>();
        }
        lastHeader = header;
        headers.put(header.getName(), header);
        separator = SEPARATOR_NONE;
        this.lastHeaderName = null;
    }

    @Override
    public boolean hasHeaders() {
        return headers != null || lastHeaderName != null;
    }

    @Override
    public boolean hasHeader(String name) {
        return headers != null && headers.containsKey(name);
    }

    @Override
    public String getLastHeaderName() {
        return lastHeaderName;
    }

    @Override
    public Collection<ParsedOperationRequestHeader> getHeaders() {
        return headers == null ? Collections.<ParsedOperationRequestHeader>emptyList() : headers.values();
    }

    @Override
    public ParsedOperationRequestHeader getLastHeader() {
        return lastHeader;
    }

    @Override
    public void rootNode(int index) {
        if(address == null) {
            address = new DefaultOperationRequestAddress();
        } else {
            address.reset();
        }
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void parentNode(int index) {
        if(address == null) {
            throw new IllegalStateException("The address hasn't been initialized yet.");
        }
        address.toParentNode();
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void nodeType(int index) {
        if(address == null) {
            throw new IllegalStateException("The address hasn't been initialized yet.");
        }
        address.toNodeType();
        separator = SEPARATOR_NONE;
        lastChunkIndex = index;
    }

    @Override
    public void nodeTypeOrName(int index, String typeOrName) throws OperationFormatException {

        if(address == null) {
            address = new DefaultOperationRequestAddress();
        }

        if(address.endsOnType()) {
            nodeName(index, typeOrName);
        } else {
            nodeType(index, typeOrName);
        }
        separator = SEPARATOR_NONE;
    }

    @Override
    public Set<String> getPropertyNames() {
        return props.keySet();
    }

    @Override
    public String getPropertyValue(String name) {
        return props.get(name);
    }

    @Override
    public int getLastSeparatorIndex() {
        return lastSeparatorIndex;
    }

    @Override
    public int getLastChunkIndex() {
        return lastChunkIndex;
    }

    @Override
    public void outputTarget(int index, String outputTarget) {
        this.outputTarget = outputTarget;
        lastChunkIndex = index;
    }

    public String getOutputTarget() {
        return outputTarget;
    }

    @Override
    public String getLastParsedPropertyName() {
        return lastPropName;
    }

    @Override
    public String getLastParsedPropertyValue() {
        return lastPropValue;
    }

    public ModelNode toOperationRequest(CommandContext ctx) throws CommandFormatException {
        ModelNode request = new ModelNode();
        ModelNode addressNode = request.get(Util.ADDRESS);
        if(address.isEmpty()) {
            addressNode.setEmptyList();
        } else {
            Iterator<Node> iterator = address.iterator();
            while (iterator.hasNext()) {
                OperationRequestAddress.Node node = iterator.next();
                if (node.getName() != null) {
                    addressNode.add(node.getType(), node.getName());
                } else if (iterator.hasNext()) {
                    throw new OperationFormatException(
                            "The node name is not specified for type '"
                                    + node.getType() + "'");
                }
            }
        }

        if(operationName == null || operationName.isEmpty()) {
            throw new OperationFormatException("The operation name is missing or the format of the operation request is wrong.");
        }
        request.get(Util.OPERATION).set(operationName);

        for(String propName : props.keySet()) {
            final String value = props.get(propName);
            if(propName == null || propName.trim().isEmpty())
                throw new OperationFormatException("The argument name is not specified: '" + propName + "'");
            if(value == null || value.trim().isEmpty())
                throw new OperationFormatException("The argument value is not specified for " + propName + ": '" + value + "'");
            final ModelNode toSet = ArgumentValueConverter.DEFAULT.fromString(value);
            request.get(propName).set(toSet);
        }

        if(this.lastHeaderName != null) {
            throw new OperationFormatException("Header '" + this.lastHeaderName + "' is not complete.");
        }
        if(headers != null) {
            final ModelNode headersNode = request.get(Util.OPERATION_HEADERS);
            for(ParsedOperationRequestHeader header : headers.values()) {
                header.addTo(ctx, headersNode);
            }
        }
        return request;
    }

    @Override
    public void setFormat(CommandLineFormat format) {
        this.format = format;
    }

    @Override
    public CommandLineFormat getFormat() {
        return format;
    }

    @Override
    public boolean endsOnHeaderSeparator() {
        return separator == SEPARATOR_HEADER;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8246.java