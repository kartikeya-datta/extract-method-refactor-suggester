error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8247.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8247.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8247.java
text:
```scala
final M@@odelNode node = HeadersArgumentValueConverter.INSTANCE.fromString(ctx, "{ rollout " +

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
package org.jboss.as.cli.parsing.test;


import java.util.Collection;
import java.util.Iterator;

import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.completion.mock.MockCommandContext;
import org.jboss.as.cli.impl.HeadersArgumentValueConverter;
import org.jboss.as.cli.operation.CommandLineParser;
import org.jboss.as.cli.operation.ParsedOperationRequestHeader;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestParser;
import org.jboss.as.cli.operation.impl.ParsedRolloutPlanHeader;
import org.jboss.as.cli.operation.impl.SingleRolloutPlanGroup;
import org.jboss.dmr.ModelNode;
import org.junit.Test;

import junit.framework.TestCase;

/**
 *
 * @author Alexey Loubyansky
 */
public class RolloutPlanParsingTestCase extends TestCase {

    private final CommandLineParser parser = DefaultOperationRequestParser.INSTANCE;
    private final DefaultCallbackHandler handler = new DefaultCallbackHandler();
    private final MockCommandContext ctx = new MockCommandContext();

    @Test
    public void testHeaderListStart() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertTrue(handler.endsOnSeparator());
        assertTrue(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());
        assertFalse(handler.hasHeaders());
    }

    @Test
    public void testEmptyHeaders() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{}");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());
        assertFalse(handler.hasHeaders());
    }

    @Test
    public void testSingleHeader() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ name = value }");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();

        assertEquals("name", header.getName());
        final ModelNode node = new ModelNode();
        node.get("name").set("value");

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testEndsOnHeaderSeparator() throws Exception {

        parse(":do{ name = value;");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertTrue(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();

        assertEquals("name", header.getName());
        final ModelNode node = new ModelNode();
        node.get("name").set("value");

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);

        assertTrue(handler.endsOnHeaderSeparator());
    }

    @Test
    public void testTwoHeaders() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ name1 = value1 ; name2=value2 }");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(2, headers.size());

        final Iterator<ParsedOperationRequestHeader> i = headers.iterator();
        ParsedOperationRequestHeader header = i.next();
        assertEquals("name1", header.getName());
        ModelNode node = new ModelNode();
        node.get("name1").set("value1");

        ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);

        header = i.next();
        assertEquals("name2", header.getName());
        node = new ModelNode();
        node.get("name2").set("value2");

        headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

/*    @Test
    public void testRolloutWithAProp() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout prop=value");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertTrue(handler.endsOnSeparator());
        assertTrue(handler.endsOnHeaderListStart()); // TODO this is kind of strange but ok...
        assertFalse(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final List<OperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final OperationRequestHeader header = headers.get(0);
        assertTrue(header instanceof RolloutPlanHeader);
        final RolloutPlanHeader rollout = (RolloutPlanHeader) header;
        assertEquals("value", rollout.getProperty("prop"));
    }

    @Test
    public void testRolloutWithTwoProps() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout prop1=value1 prop2 = value2");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertTrue(handler.endsOnSeparator());
        assertTrue(handler.endsOnHeaderListStart()); // TODO this is kind of strange but ok...
        assertFalse(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final List<OperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final OperationRequestHeader header = headers.get(0);
        assertTrue(header instanceof RolloutPlanHeader);
        final RolloutPlanHeader rollout = (RolloutPlanHeader) header;
        assertEquals("value1", rollout.getProperty("prop1"));
        assertEquals("value2", rollout.getProperty("prop2"));
    }
*/
    @Test
    public void testRolloutSingleGroupName() throws Exception {

        //parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout in-series = groupA}");
        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA}");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);
        final ModelNode groupA = new ModelNode();
        groupA.get("groupA");
        inSeries.add().get(Util.SERVER_GROUP).set(groupA);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testEndsOnGroupPropertiesStart() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA(");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertTrue(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertFalse(group.hasProperties());
    }

    public void testSpaceBeforeGroupPropertiesStart() throws Exception {

        parse(":do{ rollout groupA (");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertTrue(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertFalse(group.hasProperties());
    }

    @Test
    public void testEndsOnGroupPropertiesEnd() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA()");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertFalse(group.endsOnPropertyListStart());
        assertTrue(group.endsOnPropertyListEnd());
        assertFalse(group.hasProperties());
    }

    @Test
    public void testEndsOnGroupPropertyName() throws Exception {

        parse(":do{ rollout groupA( prop");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertFalse(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertTrue(group.hasProperties());
        assertFalse(group.endsOnPropertyValueSeparator());
        assertEquals("prop", group.getLastPropertyName());
        assertNull(group.getLastPropertyValue());
        assertEquals(21, group.getLastChunkIndex());
    }

    @Test
    public void testEndsOnGroupPropertyNameValueSeparator() throws Exception {

        parse(":do{ rollout groupA( prop =");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertFalse(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertTrue(group.hasProperties());
        assertTrue(group.endsOnPropertyValueSeparator());
        assertEquals("prop", group.getLastPropertyName());
        assertNull(group.getLastPropertyValue());
        assertEquals(26, group.getLastSeparatorIndex());
    }

    @Test
    public void testEndsOnGroupPropertyValue() throws Exception {

        parse(":do{ rollout groupA( prop = v");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertFalse(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertTrue(group.hasProperties());
        assertFalse(group.endsOnPropertyValueSeparator());
        assertEquals("prop", group.getLastPropertyName());
        assertEquals("v", group.getLastPropertyValue());
        assertEquals(28, group.getLastChunkIndex());
    }

    @Test
    public void testEndsOnGroupPropertySeparator() throws Exception {

        parse(":do{ rollout groupA( prop = v,");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        final SingleRolloutPlanGroup group = rollout.getLastGroup();
        assertNotNull(group);

        assertEquals("groupA", group.getGroupName());
        assertFalse(group.endsOnPropertyListStart());
        assertFalse(group.endsOnPropertyListEnd());
        assertTrue(group.hasProperties());
        assertFalse(group.endsOnPropertyValueSeparator());
        assertNull(group.getLastPropertyName());
        assertNull(group.getLastPropertyValue());

        assertTrue(group.endsOnPropertySeparator());
        assertEquals(29, group.getLastSeparatorIndex());
    }

    @Test
    public void testRolloutSingleGroupWithProps() throws Exception {

        //parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout in-series=groupA(rolling-to-servers=true,max-failure-percentage=20)");
        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA(rolling-to-servers=true,max-failure-percentage=20)");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);
        final ModelNode groupA = new ModelNode();
        final ModelNode groupProps = groupA.get("groupA");
        groupProps.get("rolling-to-servers").set("true");
        groupProps.get("max-failure-percentage").set("20");
        inSeries.add().get(Util.SERVER_GROUP).set(groupA);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testEndsOnGroupComma() throws Exception {

        parse(":do{ rollout groupA( prop = v ),");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        final ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        assertTrue(rollout.endsOnGroupSeparator());
        assertEquals(31, rollout.getLastSeparatorIndex());
    }

    @Test
    public void testEndsOnGroupConcurrent() throws Exception {

        parse(":do{ rollout groupA( prop = v ) ^");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);
        final ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        assertTrue(rollout.endsOnGroupSeparator());
        assertEquals(32, rollout.getLastSeparatorIndex());
    }

    @Test
    public void testNonConcurrentGroups() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout " +
        		"groupA(rolling-to-servers=true,max-failure-percentage=20) , groupB");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);
        ModelNode group = new ModelNode();
        final ModelNode groupProps = group.get("groupA");
        groupProps.get("rolling-to-servers").set("true");
        groupProps.get("max-failure-percentage").set("20");
        inSeries.add().get(Util.SERVER_GROUP).set(group);

        group = new ModelNode();
        group.get("groupB");
        inSeries.add().get(Util.SERVER_GROUP).set(group);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testNonConcurrentGroupNames() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA ,  groupB");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);
        ModelNode group = new ModelNode();
        group.get("groupA");
        inSeries.add().get(Util.SERVER_GROUP).set(group);

        group = new ModelNode();
        group.get("groupB");
        inSeries.add().get(Util.SERVER_GROUP).set(group);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testTwoConcurrentGroups() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout " +
                "groupA(rolling-to-servers=true,max-failure-percentage=20) ^  groupB");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);

        final ModelNode concurrent = new ModelNode();
        final ModelNode cg = concurrent.get(Util.CONCURRENT_GROUPS);

        ModelNode group = cg.get("groupA");
        group.get("rolling-to-servers").set("true");
        group.get("max-failure-percentage").set("20");

        group = cg.get("groupB");

        inSeries.add().set(concurrent);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testTwoConcurrentGroupNames() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout groupA ^ groupB");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);

        final ModelNode concurrent = new ModelNode();
        final ModelNode cg = concurrent.get(Util.CONCURRENT_GROUPS);
        cg.get("groupA");
        cg.get("groupB");

        inSeries.add().set(concurrent);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testEndsOnHeaderListEnd() throws Exception {

        parse(":do{ rollout groupA }");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        assertFalse(rollout.hasProperties());
//        assertTrue(rollout.endsOnPropertyListStart());
    }

    @Test
    public void testMix() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout " +
                "groupA(rolling-to-servers=true,max-failure-percentage=20) ^ groupB, groupC," +
                "groupD(rolling-to-servers=true,max-failed-servers=1) ^ groupE");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ModelNode node = new ModelNode();
        final ModelNode inSeries = node.get(Util.ROLLOUT_PLAN).get(Util.IN_SERIES);

        ModelNode concurrent = new ModelNode();
        ModelNode cg = concurrent.get(Util.CONCURRENT_GROUPS);

        ModelNode group = cg.get("groupA");
        group.get("rolling-to-servers").set("true");
        group.get("max-failure-percentage").set("20");

        group = cg.get("groupB");

        inSeries.add().set(concurrent);

        ModelNode sg = new ModelNode();
        group = sg.get(Util.SERVER_GROUP);
        group.get("groupC");
        inSeries.add().set(sg);

        concurrent = new ModelNode();
        cg = concurrent.get(Util.CONCURRENT_GROUPS);

        group = cg.get("groupD");
        group.get("rolling-to-servers").set("true");
        group.get("max-failed-servers").set("1");

        group = cg.get("groupE");

        inSeries.add().set(concurrent);

        final ModelNode headersNode = new ModelNode();
        header.addTo(ctx, headersNode);
        assertEquals(node, headersNode);
    }

    @Test
    public void testMixAgainstWholeRequest() throws Exception {

        parse("/profile=default/subsystem=threads/thread-factory=mytf:do{ rollout " +
                "groupA(rolling-to-servers=true,max-failure-percentage=20) ^ groupB, groupC," +
                "groupD(rolling-to-servers=true,max-failed-servers=1) ^ groupE rollback-across-groups}");

        assertTrue(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());

        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());

        final ModelNode op = handler.toOperationRequest(ctx);
        assertTrue(op.hasDefined(Util.OPERATION_HEADERS));
        final ModelNode headersNode = op.get(Util.OPERATION_HEADERS);

        final ModelNode expectedHeaders = new ModelNode();
        final ModelNode rolloutPlan = expectedHeaders.get(Util.ROLLOUT_PLAN);
        final ModelNode inSeries = rolloutPlan.get(Util.IN_SERIES);

        ModelNode concurrent = new ModelNode();
        ModelNode cg = concurrent.get(Util.CONCURRENT_GROUPS);

        ModelNode group = cg.get("groupA");
        group.get("rolling-to-servers").set("true");
        group.get("max-failure-percentage").set("20");

        group = cg.get("groupB");

        inSeries.add().set(concurrent);

        ModelNode sg = new ModelNode();
        group = sg.get(Util.SERVER_GROUP);
        group.get("groupC");
        inSeries.add().set(sg);

        concurrent = new ModelNode();
        cg = concurrent.get(Util.CONCURRENT_GROUPS);

        group = cg.get("groupD");
        group.get("rolling-to-servers").set("true");
        group.get("max-failed-servers").set("1");

        cg.get("groupE");

        inSeries.add().set(concurrent);

        rolloutPlan.get("rollback-across-groups").set("true");

        assertEquals(expectedHeaders, headersNode);
    }

    @Test
    public void testRolloutIdWithValue() throws Exception {

        parse(":do{ rollout id = myplan}");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertTrue(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        assertEquals("myplan", rollout.getPlanRef());
        assertEquals(18, rollout.getLastChunkIndex());
    }

    @Test
    public void testEndsOnPlanRef() throws Exception {

        parse(":do{ rollout id=");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(1, headers.size());
        final ParsedOperationRequestHeader header = headers.iterator().next();
        assertTrue(header instanceof ParsedRolloutPlanHeader);

        final ParsedRolloutPlanHeader rollout = (ParsedRolloutPlanHeader) header;
        assertNull(rollout.getPlanRef());
        assertFalse(rollout.hasProperties());
        assertNull(rollout.getLastGroup());
        assertTrue(rollout.endsOnPlanIdValueSeparator());
        assertEquals(15, rollout.getLastSeparatorIndex());
    }

    @Test
    public void testArgumentValueConverter() throws Exception {

        final ModelNode node = new HeadersArgumentValueConverter(new MockCommandContext()).fromString("{ rollout " +
                "groupA(rolling-to-servers=true,max-failure-percentage=20) ^ groupB, groupC," +
                "groupD(rolling-to-servers=true,max-failed-servers=1) ^ groupE rollback-across-groups}");

        final ModelNode expectedHeaders = new ModelNode();
        final ModelNode rolloutPlan = expectedHeaders.get(Util.ROLLOUT_PLAN);
        final ModelNode inSeries = rolloutPlan.get(Util.IN_SERIES);

        ModelNode concurrent = new ModelNode();
        ModelNode cg = concurrent.get(Util.CONCURRENT_GROUPS);

        ModelNode group = cg.get("groupA");
        group.get("rolling-to-servers").set("true");
        group.get("max-failure-percentage").set("20");

        group = cg.get("groupB");

        inSeries.add().set(concurrent);

        ModelNode sg = new ModelNode();
        group = sg.get(Util.SERVER_GROUP);
        group.get("groupC");
        inSeries.add().set(sg);

        concurrent = new ModelNode();
        cg = concurrent.get(Util.CONCURRENT_GROUPS);

        group = cg.get("groupD");
        group.get("rolling-to-servers").set("true");
        group.get("max-failed-servers").set("1");

        cg.get("groupE");

        inSeries.add().set(concurrent);

        rolloutPlan.get("rollback-across-groups").set("true");

        assertEquals(expectedHeaders, node);
    }

    @Test
    public void testRollout() throws Exception {

        parse(":do{rollout");

        assertFalse(handler.hasAddress());
        assertTrue(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertFalse(handler.endsOnSeparator());
        assertFalse(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());
        assertTrue(handler.hasHeaders());

        final Collection<ParsedOperationRequestHeader> headers = handler.getHeaders();
        assertEquals(0, headers.size());
        assertEquals("rollout", handler.getLastHeaderName());
    }

    @Test
    public void testOnlyHeaderListStart() throws Exception {

        parse("{");

        assertFalse(handler.hasAddress());
        assertFalse(handler.hasOperationName());
        assertFalse(handler.hasProperties());
        assertFalse(handler.endsOnAddressOperationNameSeparator());
        assertFalse(handler.endsOnPropertyListStart());
        assertFalse(handler.endsOnPropertySeparator());
        assertFalse(handler.endsOnPropertyValueSeparator());
        assertFalse(handler.endsOnNodeSeparator());
        assertFalse(handler.endsOnNodeTypeNameSeparator());
        assertTrue(handler.endsOnSeparator());
        assertTrue(handler.endsOnHeaderListStart());
        assertFalse(handler.isRequestComplete());
        assertFalse(handler.hasHeaders());
    }

    protected void parse(String opReq) throws CommandFormatException {
        handler.reset();
        parser.parse(opReq, handler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8247.java