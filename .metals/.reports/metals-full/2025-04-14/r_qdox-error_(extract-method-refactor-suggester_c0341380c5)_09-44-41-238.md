error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2096.java
text:
```scala
r@@eturn fullJoin;

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
package org.apache.openjpa.jdbc.meta;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.openjpa.jdbc.meta.strats.FullClassStrategy;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.schema.Schema;
import org.apache.openjpa.jdbc.schema.SchemaGroup;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.lib.meta.SourceTracker;
import org.apache.openjpa.lib.xml.Commentable;

/**
 * Information about the mapping from a class to the schema, in raw form.
 * The columns and tables used in mapping info will not be part of the
 * {@link SchemaGroup} used at runtime. Rather, they will be structs
 * with the relevant pieces of information filled in.
 *
 * @author Abe White
 */
public class ClassMappingInfo
    extends MappingInfo
    implements SourceTracker, Commentable {

    private String _className = Object.class.getName();
    private String _tableName = null;
    private boolean _joined = false;
    private Map _seconds = null;
    private String _subStrat = null;
    private File _file = null;
    private int _srcType = SRC_OTHER;
    private String[] _comments = null;

    /**
     * The described class name.
     */
    public String getClassName() {
        return _className;
    }

    /**
     * The described class name.
     */
    public void setClassName(String name) {
        _className = name;
    }

    /**
     * The default strategy for subclasses in this hierarchy.
     */
    public String getHierarchyStrategy() {
        return _subStrat;
    }

    /**
     * The default strategy for subclasses in this hierarchy.
     */
    public void setHierarchyStrategy(String strategy) {
        _subStrat = strategy;
    }

    /**
     * The given table name.
     */
    public String getTableName() {
        return _tableName;
    }

    /**
     * The given table name.
     */
    public void setTableName(String table) {
        _tableName = table;
    }

    /**
     * Whether there is a join to the superclass table.
     */
    public boolean isJoinedSubclass() {
        return _joined;
    }

    /**
     * Whether there is a join to the superclass table.
     */
    public void setJoinedSubclass(boolean joined) {
        _joined = joined;
    }

    /**
     * Return the class-level joined tables.
     */
    public String[] getSecondaryTableNames() {
        if (_seconds == null)
            return new String[0];
        return (String[]) _seconds.keySet().toArray(new String[]{ });
    }

    /**
     * We allow fields to reference class-level joins using just the table
     * name, whereas the class join might have schema, etc information.
     * This method returns the name of the given table as listed in a
     * class-level join, or the given name if no join exists.
     */
    public String getSecondaryTableName(String tableName) {
        // if no secondary table joins, bad table name, exact match,
        // or an already-qualified table name, nothing to do
        if (_seconds == null || tableName == null
 _seconds.containsKey(tableName)
 tableName.indexOf('.') != -1)
            return tableName;

        // decide which class-level join table is best match
        String best = tableName;
        int pts = 0;
        String fullJoin;
        String join;
        int idx;
        for (Iterator itr = _seconds.keySet().iterator(); itr.hasNext();) {
            // award a caseless match without schema 2 points
            fullJoin = (String) itr.next();
            idx = fullJoin.lastIndexOf('.');
            if (idx == -1 && pts < 2 && fullJoin.equalsIgnoreCase(tableName)) {
                best = fullJoin;
                pts = 2;
            } else if (idx == -1)
                continue;

            // immediately return an exact match with schema
            join = fullJoin.substring(idx + 1);
            if (join.equals(tableName))
                return join;

            // caseless match with schema worth 1 point
            if (pts < 1 && join.equalsIgnoreCase(tableName)) {
                best = fullJoin;
                pts = 1;
            }
        }
        return best;
    }

    /**
     * Return any columns defined for the given class level join, or empty
     * list if none.
     */
    public List getSecondaryTableJoinColumns(String tableName) {
        if (_seconds == null || tableName == null)
            return Collections.EMPTY_LIST;

        // get the columns for the join with the best match for table name
        List cols = (List) _seconds.get(getSecondaryTableName(tableName));
        if (cols == null) {
            // possible that given table has extra info the join table
            // doesn't have; strip it
            int idx = tableName.lastIndexOf('.');
            if (idx != -1) {
                tableName = tableName.substring(idx + 1);
                cols = (List) _seconds.get(getSecondaryTableName(tableName));
            }
        }
        return (cols == null) ? Collections.EMPTY_LIST : cols;
    }

    /**
     * Declare the given class-level join.
     */
    public void setSecondaryTableJoinColumns(String tableName, List cols) {
        if (cols == null)
            cols = Collections.EMPTY_LIST;
        if (_seconds == null)
            _seconds = new HashMap();
        _seconds.put(tableName, cols);
    }

    /**
     * Return the table for the given class.
     */
    public Table getTable(final ClassMapping cls, boolean adapt) {
        return createTable(cls, new TableDefaults() {
            public String get(Schema schema) {
                // delay this so that we don't do schema reflection for unique
                // table name unless necessary
                return cls.getMappingRepository().getMappingDefaults().
                    getTableName(cls, schema);
            }
        }, null, _tableName, adapt);
    }

    /**
     * Return the datastore identity columns for the given class, based on the
     * given templates.
     */
    public Column[] getDataStoreIdColumns(ClassMapping cls, Column[] tmplates,
        Table table, boolean adapt) {
        cls.getMappingRepository().getMappingDefaults().
            populateDataStoreIdColumns(cls, table, tmplates);
        return createColumns(cls, "datastoreid", tmplates, table, adapt);
    }

    /**
     * Return the join from this class to its superclass. The table for
     * this class must be set.
     */
    public ForeignKey getSuperclassJoin(final ClassMapping cls, Table table,
        boolean adapt) {
        ClassMapping sup = cls.getJoinablePCSuperclassMapping();
        if (sup == null)
            return null;

        ForeignKeyDefaults def = new ForeignKeyDefaults() {
            public ForeignKey get(Table local, Table foreign, boolean inverse) {
                return cls.getMappingRepository().getMappingDefaults().
                    getJoinForeignKey(cls, local, foreign);
            }

            public void populate(Table local, Table foreign, Column col,
                Object target, boolean inverse, int pos, int cols) {
                cls.getMappingRepository().getMappingDefaults().
                    populateJoinColumn(cls, local, foreign, col, target,
                        pos, cols);
            }
        };
        return createForeignKey(cls, "superclass", getColumns(), def, table,
            cls, sup, false, adapt);
    }

    /**
     * Synchronize internal information with the mapping data for the given
     * class.
     */
    public void syncWith(ClassMapping cls) {
        clear(false);

        ClassMapping sup = cls.getMappedPCSuperclassMapping();
        if (cls.getTable() != null && (sup == null
 sup.getTable() != cls.getTable()))
            _tableName = cls.getMappingRepository().getDBDictionary().
                getFullName(cls.getTable(), true);

        // set io before syncing cols
        setColumnIO(cls.getColumnIO());
        if (cls.getJoinForeignKey() != null && sup != null
            && sup.getTable() != null)
            syncForeignKey(cls, cls.getJoinForeignKey(), cls.getTable(),
                sup.getTable());
        else if (cls.getIdentityType() == ClassMapping.ID_DATASTORE)
            syncColumns(cls, cls.getPrimaryKeyColumns(), false);

        // record inheritance strategy if class does not use default strategy
        // for base classes, and for all subclasses so we can be sure subsequent
        // mapping runs don't think subclass is unmapped
        String strat = (cls.getStrategy() == null) ? null
            : cls.getStrategy().getAlias();
        if (strat != null && (cls.getPCSuperclass() != null
 !FullClassStrategy.ALIAS.equals(strat)))
            setStrategy(strat);
    }

    public boolean hasSchemaComponents() {
        return super.hasSchemaComponents() || _tableName != null;
    }

    protected void clear(boolean canFlags) {
        super.clear(canFlags);
        _tableName = null;
    }

    public void copy(MappingInfo info) {
        super.copy(info);
        if (!(info instanceof ClassMappingInfo))
            return;

        ClassMappingInfo cinfo = (ClassMappingInfo) info;
        if (_tableName == null)
            _tableName = cinfo.getTableName();
        if (_subStrat == null)
            _subStrat = cinfo.getHierarchyStrategy();
        if (cinfo._seconds != null) {
            if (_seconds == null)
                _seconds = new HashMap();
            Object key;
            for (Iterator itr = cinfo._seconds.keySet().iterator();
                itr.hasNext();) {
                key = itr.next();
                if (!_seconds.containsKey(key))
                    _seconds.put(key, cinfo._seconds.get(key));
            }
        }
    }

    public File getSourceFile() {
        return _file;
    }

    public Object getSourceScope() {
        return null;
    }

    public int getSourceType() {
        return _srcType;
    }

    public void setSource(File file, int srcType) {
        _file = file;
        _srcType = srcType;
    }

    public String getResourceName() {
        return _className;
    }

    public String[] getComments() {
        return (_comments == null) ? EMPTY_COMMENTS : _comments;
    }

    public void setComments(String[] comments) {
        _comments = comments;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2096.java