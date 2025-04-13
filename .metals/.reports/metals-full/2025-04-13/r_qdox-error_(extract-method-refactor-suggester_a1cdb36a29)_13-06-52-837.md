error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8762.java
text:
```scala
i@@f (value == null || "".equals(value)) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.property;

import java.text.ParsePosition;
import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.Project;

/**
 * Parse properties using a collection of expanders.
 * @since Ant 1.8.0
 */
public class ParseProperties implements ParseNextProperty {

    private final Project project;
    private final GetProperty getProperty;
    private final Collection expanders;

    /**
     * Constructor with a getProperty.
     * @param project the current ant project.
     * @param expanders a sequence of exapanders
     * @param getProperty property resolver.
     */
    public ParseProperties(Project project, Collection expanders, GetProperty getProperty) {
        this.project = project;
        this.expanders = expanders;
        this.getProperty = getProperty;
    }

    /**
     * Get the project.
     * @return the current ant project.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Decode properties from a String representation.  If the entire
     * contents of the String resolve to a single property, that value
     * is returned.  Otherwise a String is returned.
     *
     * @param value The string to be scanned for property references.
     *              May be <code>null</code>, in which case this
     *              method returns immediately with no effect.
     *
     * @return the original string with the properties replaced, or
     *         <code>null</code> if the original string is <code>null</code>.
     */
    public Object parseProperties(String value) {
        if (value == null || "".equals(value) || value.indexOf('$') == -1) {
            return value;
        }
        ParsePosition pos = new ParsePosition(0);
        Object o = parseNextProperty(value, pos);
        if (o != null && pos.getIndex() == value.length()) {
            return o;
        }
        StringBuffer sb = new StringBuffer(value.length() * 2);
        if (o == null) {
            sb.append(value.charAt(pos.getIndex()));
            pos.setIndex(pos.getIndex() + 1);
        } else {
            sb.append(o);
        }
        while (pos.getIndex() < value.length()) {
            o = parseNextProperty(value, pos);
            if (o == null) {
                sb.append(value.charAt(pos.getIndex()));
                pos.setIndex(pos.getIndex() + 1);
            } else {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    /**
     * Learn whether a String contains replaceable properties.
     * @param value the String to check.
     * @return <code>true</code> if <code>value</code> contains property notation.
     */
    public boolean containsProperties(String value) {
        if (value == null) {
            return false;
        }
        for (ParsePosition pos = new ParsePosition(0); pos.getIndex() < value.length();) {
            if (parsePropertyName(value, pos) != null) {
                return true;
            }
            pos.setIndex(pos.getIndex() + 1);
        }
        return false;
    }

    /**
     * Return any property that can be parsed from the specified position
     * in the specified String.
     * @param value String to parse
     * @param pos ParsePosition
     * @return Object or null if no property is at the current location.
     */
    public Object parseNextProperty(String value, ParsePosition pos) {
        int start = pos.getIndex();
        String propertyName = parsePropertyName(value, pos);
        if (propertyName != null) {
            Object result = getProperty(propertyName);
            if (result != null) {
                return result;
            }
            if (project != null) {
                project.log(
                    "Property \"" + propertyName
                    + "\" has not been set", Project.MSG_VERBOSE);
            }
            return value.substring(start, pos.getIndex());
        }
        return null;
    }

    private String parsePropertyName(String value, ParsePosition pos) {
        for (Iterator iter = expanders.iterator(); iter.hasNext();) {
            String propertyName = ((PropertyExpander) iter.next())
                .parsePropertyName(value, pos, this);
            if (propertyName == null) {
                continue;
            }
            return propertyName;
        }
        return null;
    }

    private Object getProperty(String propertyName) {
        return getProperty.getProperty(propertyName);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8762.java