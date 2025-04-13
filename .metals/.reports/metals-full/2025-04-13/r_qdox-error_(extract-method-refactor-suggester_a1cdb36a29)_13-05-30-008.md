error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4414.java
text:
```scala
s@@uper("Server Log (" + session.getServiceURI() + ")", w,h, false);

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
package org.apache.openjpa.trader.client;

import java.util.List;

import org.apache.openjpa.trader.client.event.ServiceEvent;
import org.apache.openjpa.trader.client.event.ServiceEventHandler.AddTradableHandler;
import org.apache.openjpa.trader.client.event.ServiceEventHandler.AddTradeHandler;
import org.apache.openjpa.trader.client.event.ServiceEventHandler.RemoveTradableHandler;
import org.apache.openjpa.trader.client.event.ServiceEventHandler.UpdateStockHandler;
import org.apache.openjpa.trader.client.ui.GridCellRenderer;
import org.apache.openjpa.trader.client.ui.ScrollableTable;
import org.apache.openjpa.trader.domain.LogStatement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * One of the component widgets to display the server logs.
 * The log messages are parsed to determine whether they are SQL statements and further
 * categorized as SELECT, INSERT, UPDATE or DELETE to add a decorative CSS style.
 * <br>
 * CSS styles are
 * <LI>sql-insert
 * <LI>sql-update
 * <LI>sql-select
 * <LI>sql-delete
 * 
 * @author Pinaki Poddar
 *
 */
public class ServerLogPanel extends ScrollableTable<LogStatement> 
    implements AddTradableHandler, RemoveTradableHandler, 
    AddTradeHandler, UpdateStockHandler {
    final OpenTrader session;
    public static final String[] MARKERS_AND_STYLES = {"SELECT", "INSERT", "UPDATE", "DELETE"}; 
    
    
    public ServerLogPanel(final OpenTrader session, final int w, final int h) {
        super("Server Logs", w,h, false);
        this.session = session;
        
        session.registerHandler(ServiceEvent.TradableAdded.TYPE, this);
        session.registerHandler(ServiceEvent.TradableRemoved.TYPE, this);
        session.registerHandler(ServiceEvent.TradeCommitted.TYPE, this);
        session.registerHandler(ServiceEvent.StockUpdated.TYPE, this);

        setColumnHeader(0, "Context", "10%");
        setColumnHeader(1, "Message", "90%");
        
        setRenderer(0, new GridCellRenderer<LogStatement>() {
            public Widget render(LogStatement log) {
                return new Label(log.getContext());
           }
        });
        setRenderer(1, new GridCellRenderer<LogStatement>() {
            public Widget render(LogStatement log) {
                return decorate(log.getMessage(), MARKERS_AND_STYLES);
           }
        });
    }
    
    HTML decorate(String s, String[] markersAndStyles) {
            HTML html = new HTML(s);
            String style = getStyle(s, MARKERS_AND_STYLES);
            if (style != null)
                html.addStyleName(style);
            return html;
    }
    
    static String getStyle(String s, String[] markersAndStyles) {
        String style = null;
        for (int i = 0; i < markersAndStyles.length; i++) {
            String marker = markersAndStyles[i];
            int n = marker.length();
            if (s.length() < n) {
                continue;
            }
            String preamble = s.substring(0,n);
            if (preamble.equalsIgnoreCase(marker)) {
                style = "sql-"+marker.toLowerCase();
                return style;
            }
        }
        return null;
    }
    
    private void log() {
        session.getService().getLog(new LoggingCallback());
    }

    /**
     * ---------------------------------------------------------------------------------
     * Service Event Response Management
     * 
     * This widget receives all service event update and logs the corresponding server
     * logs.
     * ---------------------------------------------------------------------------------
     */
    public void onTradableAdded(ServiceEvent.TradableAdded event) {
        log();
    }

    public void onTradableRemoved(ServiceEvent.TradableRemoved event) {
        log();
    }

    public void onTradeCommitted(ServiceEvent.TradeCommitted event) {
        log();
    }

    public void onStockUpdated(ServiceEvent.StockUpdated event) {
        log();
    }
    
    /**
     * ---------------------------------------------------------------------------------
     * Asynchronous RPC service callbacks
     * ---------------------------------------------------------------------------------
     */
    
    /**
     * Unlike other callbacks, this callback on completion does not broadcast the log messages.
     * Instead it simply inserts the message in its own tabular display.  
     */
    public class LoggingCallback implements AsyncCallback<List<LogStatement>> {
        public void onFailure(Throwable caught) {
            session.handleError(caught);
        }

        public void onSuccess(List<LogStatement> messages) {
            if (messages == null)
                return;
            int N = messages.size();
            for (int i = 0; i < N; i++) {
               insert(messages.get(i));
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4414.java