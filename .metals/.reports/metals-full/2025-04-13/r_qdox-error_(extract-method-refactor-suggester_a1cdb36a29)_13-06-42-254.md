error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4797.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4797.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4797.java
text:
```scala
r@@eturn out.toString();

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.table;

import java.util.ArrayList;

/**
 *  A generic table renderer.  Can optionally print header row.
 *  Will justify all cells in a column to the widest one.  All rows need
 *  to have same number of cells.
 *
 *  Eg, new Table.addRow(new Row().addCell("foo").addCell("bar")).render()
 */
public class Table {
    private ArrayList<Column> cols;

    private byte numcols;

    private byte height;

    public Table() {
        this.cols = new ArrayList<Column>();
        this.numcols = 0;
        this.height = 0;
    }

    public Table addRow(Row row) {
        addRow(row, false);
        return this;
    }

    public void ensureCapacity(int size) {
        if (numcols < size) {
            for (int i = 0; i < (size - numcols); i++) {
                cols.add(new Column());
            }
        }
    }

    public Table addRow(Row row, boolean header) {
        ensureCapacity(row.size());
        byte curCol = 0;
        for (Cell cell : row.cells()) {
            Column col = cols.get(curCol);
            col.addCell(cell, header);
            curCol += 1;
        }
        numcols = curCol;
        height += 1;
        return this;
    }

    public String render() {
        return render(false);
    }

    public String render(boolean withHeaders) {
        StringBuilder out = new StringBuilder();
        for (byte i = 0; i < height; i++) {
            StringBuilder row = new StringBuilder();
            for (Column col : cols) {
                Cell cell = col.getCell(i);
                boolean headerRowWhenNotWantingHeaders = i == 0 && !withHeaders && col.hasHeader();
                if (! headerRowWhenNotWantingHeaders) {
                    row.append(cell.toString(col.width(), col.align()));
                    row.append(" ");
                }
            }
            out.append(row.toString().trim());
            out.append("\n");
        }
        return out.toString().trim();
    }

    private class Column {
        private boolean hasHeader;

        private ArrayList<Cell> cells;

        private byte width;

        private Align align;

        Column () {
            cells = new ArrayList<Cell>();
            width = 0;
            hasHeader = false;
            align = Align.LEFT;
        }

        public Column addCell(Cell cell) {
            addCell(cell, false);
            return this;
        }

        public Column addCell(Cell cell, boolean header) {
            cells.add(cell);

            if (header) {
                hasHeader = true;
            }

            if (cell.width() > width) {
                width = cell.width();
            }

            if (align != cell.align()) {
                align = cell.align();
            }

            return this;
        }

        public Cell getCell(int index) {
            return cells.get(index);
        }

        public Align align() {
            return this.align;
        }

        public byte width() {
            return this.width;
        }

        public boolean hasHeader() {
            return this.hasHeader;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4797.java