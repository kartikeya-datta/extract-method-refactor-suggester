error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10433.java
text:
```scala
S@@tring priority = reader.getAttributeValue(null, PRIORITY);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.webservices.parser;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.jboss.wsf.spi.util.StAXUtils.match;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;

import org.jboss.wsf.common.JavaUtils;
import org.jboss.wsf.spi.deployment.DeploymentAspect;
import org.jboss.wsf.spi.util.StAXUtils;

/**
 * A parser for WS deployment aspects
 *
 * @author alessio.soldano@jboss.com
 * @since 18-Jan-2011
 *
 */
public class WSDeploymentAspectParser {

    private static final String NS = "urn:jboss:ws:deployment:aspects:1.0";
    private static final String DEPLOYMENT_ASPECTS = "deploymentAspects";
    private static final String DEPLOYMENT_ASPECT = "deploymentAspect";
    private static final String CLASS = "class";
    private static final String PRIORITY = "priority";
    private static final String PROPERTY = "property";
    private static final String NAME = "name";
    private static final String MAP = "map";
    private static final String KEY_CLASS = "keyClass";
    private static final String VALUE_CLASS = "valueClass";
    private static final String ENTRY = "entry";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String LIST = "list";
    private static final String ELEMENT_CLASS = "elementClass";

    public static List<DeploymentAspect> parse(InputStream is) {
        try {
            XMLStreamReader xmlr = StAXUtils.createXMLStreamReader(is);
            return parse(xmlr);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    public static List<DeploymentAspect> parse(XMLStreamReader reader) throws XMLStreamException {
        int iterate;
        try {
            iterate = reader.nextTag();
        } catch (XMLStreamException e) {
            // skip non-tag elements
            iterate = reader.nextTag();
        }
        List<DeploymentAspect> deploymentAspects = null;
        switch (iterate) {
            case END_ELEMENT: {
                // we're done
                break;
            }
            case START_ELEMENT: {

                if (match(reader, NS, DEPLOYMENT_ASPECTS)) {
                    deploymentAspects = parseDeploymentAspects(reader);
                } else {
                    throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                }
            }
        }
        return deploymentAspects;
    }

    private static List<DeploymentAspect> parseDeploymentAspects(XMLStreamReader reader) throws XMLStreamException {
        List<DeploymentAspect> deploymentAspects = new LinkedList<DeploymentAspect>();
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, DEPLOYMENT_ASPECTS)) {
                        return deploymentAspects;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, DEPLOYMENT_ASPECT)) {
                        deploymentAspects.add(parseDeploymentAspect(reader));
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
    }

    private static DeploymentAspect parseDeploymentAspect(XMLStreamReader reader) throws XMLStreamException {
        String deploymentAspectClass = reader.getAttributeValue(null, CLASS);
        if (deploymentAspectClass == null) {
            throw new IllegalStateException("Could not find class attribute for deployment aspect!");
        }
        DeploymentAspect deploymentAspect = null;
        try {
            @SuppressWarnings("unchecked")
            Class<? extends DeploymentAspect> clazz = (Class<? extends DeploymentAspect>) Class.forName(deploymentAspectClass);
            deploymentAspect = clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create a deploymeny aspect of class: " + deploymentAspectClass, e);
        }
        String priority = reader.getAttributeValue("", PRIORITY);
        if (priority != null) {
            deploymentAspect.setRelativeOrder(Integer.parseInt(priority.trim()));
        }
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, DEPLOYMENT_ASPECT)) {
                        return deploymentAspect;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, PROPERTY)) {
                        parseProperty(reader, deploymentAspect);
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
    }

    @SuppressWarnings("rawtypes")
    private static void parseProperty(XMLStreamReader reader, DeploymentAspect deploymentAspect) throws XMLStreamException {
        Class<? extends DeploymentAspect> deploymentAspectClass = deploymentAspect.getClass();
        String propName = reader.getAttributeValue(null, NAME);
        if (propName == null) {
            throw new IllegalStateException("Could not find property name attribute for deployment aspect: " + deploymentAspect);
        }
        String propClass = reader.getAttributeValue(null, CLASS);
        if (propClass == null) {
            throw new IllegalStateException("Could not find property class attribute for deployment aspect: "
                    + deploymentAspect);
        } else {
            try {
                if (isSupportedPropertyClass(propClass)) {
                    Method m = selectMethod(deploymentAspectClass, propName, propClass);
                    m.invoke(deploymentAspect, parseSimpleValue(reader, propClass));
                    return;
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, PROPERTY)) {
                        return;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, MAP)) {
                        try {
                            Method m = selectMethod(deploymentAspectClass, propName, propClass);
                            Map map = parseMapProperty(reader, propClass, reader.getAttributeValue(null, KEY_CLASS),
                                    reader.getAttributeValue(null, VALUE_CLASS));
                            m.invoke(deploymentAspect, map);
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    } else if (match(reader, NS, LIST)) {
                        try {
                            Method m = selectMethod(deploymentAspectClass, propName, propClass);
                            List list = parseListProperty(reader, propClass, reader.getAttributeValue(null, ELEMENT_CLASS));
                            m.invoke(deploymentAspect, list);
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
    }

    private static Method selectMethod(Class<?> deploymentAspectClass, String propName, String propClass) throws ClassNotFoundException {
        //TODO improve this (better support for primitives, edge cases, etc.)
        Method[] methods = deploymentAspectClass.getMethods();
        for (Method m : methods) {
            if (m.getName().equals("set" + JavaUtils.capitalize(propName))) {
                Class<?>[] pars = m.getParameterTypes();
                if (pars.length == 1 && pars[0].isAssignableFrom(Class.forName(propClass))) {
                    return m;
                }
            }
        }
        return null;
    }

    private static boolean isSupportedPropertyClass(String propClass) {
        return (String.class.getName().equals(propClass) || Boolean.class.getName().equals(propClass) || Integer.class
                .getName().equals(propClass));
    }

    private static Object parseSimpleValue(XMLStreamReader reader, String propClass) throws XMLStreamException {
        if (String.class.getName().equals(propClass)) {
            return StAXUtils.elementAsString(reader);
        } else if (Boolean.class.getName().equals(propClass)) {
            return StAXUtils.elementAsBoolean(reader);
        } else if (Integer.class.getName().equals(propClass)) {
            return StAXUtils.elementAsInt(reader);
        } else {
            throw new IllegalArgumentException("Unsupported property class: " + propClass);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List parseListProperty(XMLStreamReader reader, String propClass, String elementClass)
            throws XMLStreamException {
        List list = null;
        try {
            list = (List) Class.forName(propClass).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create list of type: " + propClass, e);
        }
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, LIST)) {
                        return list;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, VALUE)) {
                        list.add(parseSimpleValue(reader, elementClass));
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
    }

    @SuppressWarnings("rawtypes")
    private static Map parseMapProperty(XMLStreamReader reader, String propClass, String keyClass, String valueClass)
            throws XMLStreamException {
        Map map = null;
        try {
            map = (Map) Class.forName(propClass).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create map of type: " + propClass, e);
        }
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, MAP)) {
                        return map;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, ENTRY)) {
                        parseMapEntry(reader, map, keyClass, valueClass);
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void parseMapEntry(XMLStreamReader reader, Map map, String keyClass, String valueClass)
            throws XMLStreamException {
        boolean keyStartDone = false, valueStartDone = false;
        Object key = null;
        Object value = null;
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (match(reader, NS, ENTRY) && keyStartDone && valueStartDone) {
                        map.put(key, value);
                        return;
                    } else {
                        throw new IllegalStateException("Unexpected end tag: " + reader.getLocalName());
                    }
                }
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(reader, NS, KEY)) {
                        keyStartDone = true;
                        key = parseSimpleValue(reader, keyClass);
                    } else if (match(reader, NS, VALUE)) {
                        valueStartDone = true;
                        value = parseSimpleValue(reader, valueClass);
                    } else {
                        throw new IllegalStateException("Unexpected element: " + reader.getLocalName());
                    }
                }
            }
        }
        throw new IllegalStateException("Reached end of xml document unexpectedly");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10433.java