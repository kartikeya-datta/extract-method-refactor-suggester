error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1880.java
text:
```scala
r@@eturn getColumnFamily(CliUtils.unescapeSQLString(astNode.getChild(0).getText()), cfDefs);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.cli;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.KsDef;


public class CliCompiler
{

    // ANTLR does not provide case-insensitive tokenization support
    // out of the box. So we override the LA (lookahead) function
    // of the ANTLRStringStream class. Note: This doesn't change the
    // token text-- but just relaxes the matching rules to match
    // in upper case. [Logic borrowed from Hive code.]
    // 
    // Also see discussion on this topic in:
    // http://www.antlr.org/wiki/pages/viewpage.action?pageId=1782.
    public static class ANTLRNoCaseStringStream  extends ANTLRStringStream
    {
        public ANTLRNoCaseStringStream(String input)
        {
            super(input);
        }
    
        public int LA(int i)
        {
            int returnChar = super.LA(i);
            if (returnChar == CharStream.EOF)
            {
                return returnChar; 
            }
            else if (returnChar == 0) 
            {
                return returnChar;
            }

            return Character.toUpperCase((char)returnChar);
        }
    }

    public static Tree compileQuery(String query)
    {
        Tree queryTree;
        
        try
        {
            ANTLRStringStream input = new ANTLRNoCaseStringStream(query);

            CliLexer lexer = new CliLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CliParser parser = new CliParser(tokens);

            // start parsing...
            queryTree = (Tree)(parser.root().getTree());

            // semantic analysis if any...
            //  [tbd]

        }
        catch(Exception e)
        {
            // if there was an exception we don't want to process request any further
            throw new RuntimeException(e.getMessage(), e);
        }
        
        return queryTree;
    }
    /*
     * NODE_COLUMN_ACCESS related functions.
     */

    public static String getColumnFamily(Tree astNode, List<CfDef> cfDefs)
    {
        return getColumnFamily(astNode.getChild(0).getText(), cfDefs);
    }

    public static String getColumnFamily(String cfName, List<CfDef> cfDefs)
    {
        int matches = 0;
        String lastMatchedName = "";

        for (CfDef cfDef : cfDefs)
        {
            if (cfDef.name.equals(cfName))
            {
                return cfName;
            }
            else if (cfDef.name.toUpperCase().equals(cfName.toUpperCase()))
            {
                lastMatchedName = cfDef.name;
                matches++;
            }
        }

        if (matches > 1 || matches == 0)
            throw new RuntimeException(cfName + " not found in current keyspace.");

        return lastMatchedName;
    }

    public static String getKeySpace(Tree statement, List<KsDef> keyspaces)
    {
        return getKeySpace(statement.getChild(0).getText(), keyspaces);
    }

    public static String getKeySpace(String ksName, List<KsDef> keyspaces)
    {
        int matches = 0;
        String lastMatchedName = "";

        for (KsDef ksDef : keyspaces)
        {
            if (ksDef.name.equals(ksName))
            {
                return ksName;
            }
            else if (ksDef.name.toUpperCase().equals(ksName.toUpperCase()))
            {
                lastMatchedName = ksDef.name;
                matches++;
            }
        }

        if (matches > 1 || matches == 0)
            throw new RuntimeException("Keyspace '" + ksName + "' not found.");

        return lastMatchedName;
    }

    public static String getKey(Tree astNode)
    {
        return CliUtils.unescapeSQLString(astNode.getChild(1).getText());
    }

    public static int numColumnSpecifiers(Tree astNode)
    {
        // Skip over table, column family and rowKey
        return astNode.getChildCount() - 2;
    }

    // Returns the pos'th (0-based index) column specifier in the astNode
    public static String getColumn(Tree astNode, int pos)
    {
        // Skip over table, column family and rowKey
        return CliUtils.unescapeSQLString(astNode.getChild(pos + 2).getText()); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1880.java