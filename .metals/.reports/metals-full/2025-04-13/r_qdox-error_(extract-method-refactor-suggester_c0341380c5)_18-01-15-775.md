error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17280.java
text:
```scala
d@@isc.getClassMapping(), col).getMessage());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.jdbc.meta.strats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.JavaTypes;

/**
 * Stores the class name along with each database object record.
 *
 * @author Abe White
 */
public class ClassNameDiscriminatorStrategy
    extends InValueDiscriminatorStrategy {

    private static final Localizer _loc = Localizer.forPackage
        (ClassNameDiscriminatorStrategy.class);

    public static final String ALIAS = "class-name";

    public String getAlias() {
        return ALIAS;
    }

    protected int getJavaType() {
        return JavaTypes.STRING;
    }

    protected Object getDiscriminatorValue(ClassMapping cls) {
        return cls.getDescribedType().getName();
    }

    protected Class getClass(Object val, JDBCStore store)
        throws ClassNotFoundException {
        ClassLoader loader = getClassLoader(store);
        return Class.forName((String) val, true, loader);
    }

    public void loadSubclasses(JDBCStore store)
        throws SQLException, ClassNotFoundException {
        if (isFinal) {
            disc.setSubclassesLoaded(true);
            return;
        }

        Column col = disc.getColumns()[0];
        DBDictionary dict = store.getDBDictionary();
        SQLBuffer select = dict.toSelect(new SQLBuffer(dict).append(col),
            store.getFetchConfiguration(),
            new SQLBuffer(dict).append(col.getTable()), null, null,
            null, null, true, false, 0, Long.MAX_VALUE);

        Log log = disc.getMappingRepository().getLog();
        if (log.isTraceEnabled())
            log.trace(_loc.get("load-subs", col.getTable().getFullName()));

        ClassLoader loader = getClassLoader(store);
        Connection conn = store.getConnection();
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = select.prepareStatement(conn);
            rs = stmnt.executeQuery();
            String className;
            while (rs.next()) {
                className = dict.getString(rs, 1);
                if (className == null || className.length() == 0)
                    throw new ClassNotFoundException(_loc.get("no-class-name",
                        disc.getClassMapping(), col));
                Class.forName(className, true, loader);
            }
            disc.setSubclassesLoaded(true);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException se) {
                }
            if (stmnt != null)
                try {
                    stmnt.close();
                } catch (SQLException se) {
                }
            try {
                conn.close();
            } catch (SQLException se) {
            }
        }
    }

    /**
     * Return the class loader to use for loading class names.
     */
    private ClassLoader getClassLoader(JDBCStore store) {
        return store.getConfiguration().getClassResolverInstance().
            getClassLoader(disc.getClassMapping().getDescribedType(),
                store.getContext().getClassLoader());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17280.java