error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10940.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10940.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10940.java
text:
```scala
S@@tringBuilder retv = new StringBuilder();

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
package org.apache.openjpa.lib.conf;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.ParseException;

import serp.util.Strings;

/**
 * A comma-separated list of string values.
 *
 * @author Abe White
 */
public class StringListValue extends Value {

    public static final String[] EMPTY = new String[0];
    private static final Localizer s_loc = Localizer.forPackage
        (StringListValue.class);

    private String[] _values = EMPTY;

    public StringListValue(String prop) {
        super(prop);
    }

    /**
     * The internal value.
     */
    public void set(String[] values) {
        assertChangeable();
        _values = (values == null) ? EMPTY : values;
        valueChanged();
    }

    /**
     * The internal value.
     */
    public String[] get() {
        return _values;
    }

    public Class<String []> getValueType() {
        return String[].class;
    }
    
    /**
     * Unalias the value list.  This method defers to super.unalias()
     * UNLESS the string passed is a list of values for a property that
     * has aliases.
     */
    public String unalias(String str) {
        
        // defer to super.unalias
        String[] aliases = getAliases();
        if (aliases.length <= 0 || str == null)
            return super.unalias(str);
        str = str.trim();
        if (str.length() <= 0)
            return super.unalias(str);
        
        // snag this case early as it only causes problems
        if (str.equals(","))
            throw new ParseException(s_loc.get("invalid-list-config",
                getProperty(), str, getAliasList()));
        
        // unalias the list and concatenate the list of
        // canonical values.  Also, catch any bad aliases.
        boolean found;
        String iString;
        StringBuffer retv = new StringBuffer();
        String[] vals = str.split(",", 0);
        
        for (int i = 0; i < vals.length; i++) {
            iString = vals[i] = vals[i].trim();
            
            found = false;
            if (i > 0)
                retv.append(',');
            
            for (int x = 0; x < aliases.length; x += 2)
                if (StringUtils.equals(iString, aliases[x])
 StringUtils.equals(iString, aliases[x + 1])) {
                    retv.append(aliases[x + 1]);
                    found = true;
                    break;
                }
            
            // If the alias list is not comprehensive, add any unknown
            // values back onto the list
            if (!found) {
                if (isAliasListComprehensive())
                    throw new ParseException(s_loc.get("invalid-list-config",
                        getProperty(), str, getAliasList()));
                else
                    retv.append(iString);
            }
        }
        return retv.toString();
    }

    protected String getInternalString() {
        return Strings.join(_values, ", ");
    }

    protected void setInternalString(String val) {
        String[] vals = Strings.split(val, ",", 0);
        if (vals != null) {
            for (int i = 0; i < vals.length; i++)
                vals[i] = vals[i].trim();
        }

        set(vals);
    }

    protected void setInternalObject(Object obj) {
        set((String[]) obj);
    }
    
    protected List<String> getAliasList() {
        return Arrays.asList(getAliases());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10940.java