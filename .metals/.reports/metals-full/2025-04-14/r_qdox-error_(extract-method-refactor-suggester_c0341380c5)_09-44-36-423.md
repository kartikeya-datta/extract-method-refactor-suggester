error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3582.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3582.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3582.java
text:
```scala
r@@esponse.renderJavaScriptReference(new JavaScriptResourceReference(DebugBar.class, "wicket-debugbar.js"));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.devutils.debugbar;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.devutils.DevUtilsPanel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.CompressedResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * The debug bar is for use during development. It allows contributors to add useful functions or
 * data, making them readily accessible to the developer.<br />
 * <br />
 * To use it, simply add it to your base page so that all of your pages automatically have it.<br />
 * 
 * <br />
 * <code>
 * Java:
 * add(new DebugBar("debug"));
 * 
 * HTML:
 * &lt;div wicket:id="debug"&gt;&lt;/div&gt;
 * </code>
 * 
 * <br />
 * You can also add your own information to the bar by creating a {@link IDebugBarContributor} and
 * registering it with the debug bar.
 * 
 * @author Jeremy Thomerson <jthomerson@apache.org>
 * @see IDebugBarContributor
 */
public class DebugBar extends DevUtilsPanel
{

    private static final MetaDataKey<List<IDebugBarContributor>> CONTRIBS_META_KEY = new MetaDataKey<List<IDebugBarContributor>>()
    {
        private static final long serialVersionUID = 1L;
    };

    private static final long serialVersionUID = 1L;

    public DebugBar(String id)
    {
        super(id);
        setMarkupId("wicketDebugBar");
        setOutputMarkupId(true);
        add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject()
            {
                return "wicketDebugBar" + (DebugBar.this.hasErrorMessage() ? "Error" : "");
            }

        }));

        add(new Image("logo", new PackageResourceReference(DebugBar.class, "wicket.png")));
        add(new Image("removeImg", new PackageResourceReference(DebugBar.class, "remove.png")));
        List<IDebugBarContributor> contributors = getContributors();
        
        // no longer necessary, registered from DebugBarInitializer
        // if (contributors.isEmpty())
        // {
        // we do this so that if you have multiple applications running in
        // the same container,
        // each ends up registering its' own contributors (wicket-examples
        // for example)
        // registerStandardContributors(Application.get());
        // contributors = getContributors();
        // }
        add(new ListView<IDebugBarContributor>("contributors", contributors)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<IDebugBarContributor> item)
            {
                IDebugBarContributor contrib = item.getModelObject();
                Component comp = contrib.createComponent("contrib", DebugBar.this);
                if (comp == null)
                {
                    // some contributors only add information to the debug bar
                    // and don't actually create a contributed component
                    item.setVisibilityAllowed(false);
                }
                else
                {
                    item.add(comp);
                }
            }
        });
    }

    @Override
    public boolean isVisible()
    {
        return getApplication().getDebugSettings().isDevelopmentUtilitiesEnabled();
    }

    @Override
    public void renderHead(IHeaderResponse response)
    {
        response.renderCSSReference(new CompressedResourceReference(DebugBar.class, "wicket-debugbar.css"));
        response.renderJavascriptReference(new JavaScriptResourceReference(DebugBar.class, "wicket-debugbar.js"));
    }

    /**
     * Register your own custom contributor that will be part of the debug bar. You must have the
     * context of an application for this thread at the time of calling this method.
     * 
     * @param application
     * @param contrib
     *            custom contributor - can not be null
     */
    public static void registerContributor(IDebugBarContributor contrib)
    {
        registerContributor(contrib, Application.get());
    }

    /**
     * Register your own custom contributor that will be part of the debug bar. You must have the
     * context of an application for this thread at the time of calling this method.
     * 
     * @param application
     * @param contrib
     *            custom contributor - can not be null
     */
    public static void registerContributor(IDebugBarContributor contrib, Application application)
    {
        if (contrib == null)
        {
            throw new IllegalArgumentException("contrib can not be null");
        }

        List<IDebugBarContributor> contributors = getContributors(application);
        contributors.add(contrib);
        application.setMetaData(CONTRIBS_META_KEY, contributors);
    }

    private static List<IDebugBarContributor> getContributors()
    {
        return getContributors(Application.get());
    }

    private static List<IDebugBarContributor> getContributors(Application application)
    {
        List<IDebugBarContributor> list = application.getMetaData(CONTRIBS_META_KEY);
        return list == null ? new ArrayList<IDebugBarContributor>() : list;
    }


    /**
     * Called from {@link DebugBarInitializer}
     */
    static void registerStandardContributors(Application application)
    {
        registerContributor(VersionDebugContributor.DEBUG_BAR_CONTRIB, application);
        registerContributor(InspectorDebugPanel.DEBUG_BAR_CONTRIB, application);
        registerContributor(SessionSizeDebugPanel.DEBUG_BAR_CONTRIB, application);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3582.java