error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6348.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6348.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6348.java
text:
```scala
a@@rg.getValue().length() > 0);

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package org.apache.jmeter.protocol.http.gui;

import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

public class HTTPArgumentsPanel extends ArgumentsPanel
{
	/* NOTUSED
    private static final String ENCODED_VALUE =
        JMeterUtils.getResString("encoded_value");
        */
    private static final String ENCODE_OR_NOT =
        JMeterUtils.getResString("encode?");
    private static final String INCLUDE_EQUALS =
        JMeterUtils.getResString("include_equals");

    protected void initializeTableModel()
    {
        tableModel =
            new ObjectTableModel(
                new String[] {
                    ArgumentsPanel.COLUMN_NAMES_0,
                    ArgumentsPanel.COLUMN_NAMES_1,
                    ENCODE_OR_NOT,
                    INCLUDE_EQUALS },
                new Functor[] { new Functor("getName"), 
                      new Functor("getValue"), 
                      new Functor("isAlwaysEncoded"), 
                      new Functor("isUseEquals") },
                new Functor[] { new Functor("setName"), 
                      new Functor("setValue"), 
                      new Functor("setAlwaysEncoded"), 
                      new Functor("setUseEquals") },
                new Class[] {
                    String.class,
                    String.class,
                    Boolean.class,
                    Boolean.class });
    }

    protected void sizeColumns(JTable table)
    {
        int resizeMode = table.getAutoResizeMode();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fixSize(table.getColumn(INCLUDE_EQUALS));
        fixSize(table.getColumn(ENCODE_OR_NOT));
        table.setAutoResizeMode(resizeMode);
    }

    protected Object makeNewArgument()
    {
        HTTPArgument arg = new HTTPArgument("", "");
        arg.setAlwaysEncoded(false);
        arg.setUseEquals(true);
        return arg;
    }

    private void fixSize(TableColumn column)
    {
        column.sizeWidthToFit();
        //column.setMinWidth(column.getWidth());
        column.setMaxWidth((int) (column.getWidth() * 1.5));
        column.setWidth(column.getMaxWidth());
        column.setResizable(false);
    }

    public HTTPArgumentsPanel()
    {
        super(JMeterUtils.getResString("paramtable"));
    }

    public TestElement createTestElement()
    {
       stopTableEditing();
        Iterator modelData = tableModel.iterator();
        Arguments args = new Arguments();
        while (modelData.hasNext())
        {
            HTTPArgument arg = (HTTPArgument) modelData.next();
            args.addArgument(arg);
        }
        this.configureTestElement(args);
        return (TestElement) args.clone();
    }

    public void configure(TestElement el)
    {
        super.configure(el);
        if (el instanceof Arguments)
        {
            tableModel.clearData();
            HTTPArgument.convertArgumentsToHTTP((Arguments) el);
            PropertyIterator iter = ((Arguments) el).getArguments().iterator();
            while (iter.hasNext())
            {
                HTTPArgument arg = (HTTPArgument) iter.next().getObjectValue();
                tableModel.addRow(arg);
            }
        }
        checkDeleteStatus();
    }

    protected boolean isMetaDataNormal(HTTPArgument arg)
    {
        return arg.getMetaData() == null
 arg.getMetaData().equals("=")
 (arg.getValue() != null &&
                arg.getValue().toString().length() > 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6348.java