error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5155.java
text:
```scala
s@@uper.checkConfigurationInternal(issues);

/*
Copyright (c) 2008 Arno Haase, André Arnold.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
    André Arnold
 */
package org.eclipse.xtend.middleend.xpand;


import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.internal.xpand2.ast.Definition;
import org.eclipse.internal.xpand2.ast.ExpandStatement;
import org.eclipse.internal.xpand2.ast.Template;
import org.eclipse.internal.xpand2.codeassist.XpandTokens;
import org.eclipse.internal.xpand2.parser.XpandParseFacade;
import org.eclipse.internal.xtend.xtend.parser.ParseException;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandUtil;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xpand2.output.PostProcessor;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;
import org.eclipse.xtend.middleend.xpand.internal.xpandlib.pr.XpandProtectedRegionResolver;


/**
 * This workflow component executes an Xpand template based on the new backend implementation. It
 *  combines the steps of parsing and transforming the source files, and of invoking the script.
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public class XpandComponent extends AbstractExpressionsUsingWorkflowComponent {
    //TODO profiler
    
    private String _genPath = null;
    private String _srcPath = null;

    private String _expand = null;
    private String _fileEncoding = null;
    private boolean _automaticHyphens = false;
    private Output _output = null;

    private final List<Outlet> _outlets = new ArrayList<Outlet>();

    private List<PostProcessor> _postprocessors = new ArrayList <PostProcessor>();
    private List<Outlet> _initializedOutlets = new ArrayList<Outlet> ();

	private String _ignoreList;
	private boolean _defaultExcludes;
	private boolean _useBase64;
    
    public List<PostProcessor> getBeautifier() {
        return _postprocessors;
    }

    public void addBeautifier (PostProcessor beautifier) {
        _postprocessors.add (beautifier);
    }
    
    public List<PostProcessor> getPostprocessors() {
        return _postprocessors;
    }
    
    public void addPostprocessor (PostProcessor postprocessor) {
        _postprocessors.add (postprocessor);
    }
    
    public void setAutomaticHyphens(boolean automaticHyphens) {
        this._automaticHyphens = automaticHyphens;
    }

    @Override
    public String getLogMessage() {
        return "generating '" + _expand + "' => directory '" + _genPath + "'";
    }
    
    public void setFileEncoding(final String fileEncoding) {
        _fileEncoding = fileEncoding;
    }
    
    public String getFileEncoding() {
        return _fileEncoding;
    }

    public void setExpand (String invoke) {
        _expand = invoke;
    }

    /**
     * @deprecated use outlets instead
     */
    @Deprecated
    public void setGenPath(final String genPath) {
        _genPath = fixPath(genPath);
    }

    /**
     * 
     * @deprecated use outlets instead
     */
    @Deprecated
    public void setSrcPath(final String srcPath) {
        _srcPath = fixPath(srcPath);
    }

	public void setIgnoreList(String ignoreList) {
		_ignoreList = ignoreList;
	}

	public void setDefaultExcludes(boolean defaultExcludes) {
		_defaultExcludes = defaultExcludes;
	}

	public void setUseBase64(boolean useBase64) {
		_useBase64 = useBase64;
	}

    
    private String fixPath(final String p) {
        if (p.endsWith("\\"))
            return p.replace('\\', '/');
        if (p.endsWith("/"))
            return p;
        return p + "/";
    }

    
    @Override
    protected void invokeInternal2 (WorkflowContext wfContext, ProgressMonitor monitor, Issues issues) {

        // set up the execution context
        XpandExecutionContextImpl executionContext = new XpandExecutionContextImpl (getOutput(), null, getGlobalVars (wfContext), null, getNullEvaluationHandler());
        
        if (_fileEncoding != null) 
            executionContext.setFileEncoding (_fileEncoding);
        
        final String code = XpandTokens.LT + "EXPAND " + _expand + XpandTokens.RT;
        final String filename = new String( _expand.substring(0, _expand.lastIndexOf(XpandUtil.NS_DELIM)));
        XpandBackendFacade bf = XpandBackendFacade.createForFile(filename, _fileEncoding, metaModels, _outlets );

        final Map<String, Object> variables = new HashMap<String, Object> ();        
        for (String name: wfContext.getSlotNames())
            variables.put (name, wfContext.get (name));
        
        XpandProtectedRegionResolver resolver = new XpandProtectedRegionResolver(_ignoreList, _defaultExcludes, getInitializedOutlets(), _fileEncoding, _useBase64);
        //XpandBackendFacade.executeStatement (code, _fileEncoding, metaModels, variables, _outlets, _advice);
        bf.executeStatement (code, variables, _advice, resolver);
    }

    public void addOutlet (Outlet outlet) {
        _outlets.add(outlet);
    }
    
    public void setOutput (Output output) {
        _output = output;
    }
    
    private Output getOutput () {
        if (_output == null) {
            // lazy initialization
            OutputImpl out = new OutputImpl();
            out.setAutomaticHyphens (_automaticHyphens);
            _output = out;
        }
        
        return _output;
    }

    
    private List<Outlet> getInitializedOutlets() {
        if (_initializedOutlets.isEmpty()) {
            final List<Outlet> result = new ArrayList<Outlet> (_outlets);
            if (result.isEmpty()) {
                if (_genPath != null) { // backward compatibility
                    result.add (new Outlet (false, _fileEncoding, null, true, _genPath));
                    result.add (new Outlet (true, _fileEncoding, "APPEND", true, _genPath));
                }
                    
                if (_srcPath != null) 
                    result.add (new Outlet (false, _fileEncoding, "ONCE", false, _srcPath));
            }

            for (Outlet o: result) {
                if (o.postprocessors.isEmpty()) 
                    for (PostProcessor pp: _postprocessors)
                        o.addPostprocessor (pp);

                if (_fileEncoding != null) 
                    o.setFileEncoding(_fileEncoding);
            }
        }        
        
        return _initializedOutlets;
    }
    

    private ExpandStatement getStatement () {
        Template tpl = XpandParseFacade.file (new StringReader(XpandTokens.LT + "DEFINE test FOR test" + XpandTokens.RT + XpandTokens.LT + "EXPAND " + _expand + XpandTokens.RT + XpandTokens.LT + "ENDDEFINE" + XpandTokens.RT), null);
        ExpandStatement es = null;
        try {
            es = (ExpandStatement) ((Definition) tpl.getDefinitions()[0]).getBody()[1];
        } catch (final Exception e) {
            log.error(e);
        }
        return es;
    }

    @Override
    public void checkConfigurationInternal (final Issues issues) {
        super.checkConfiguration(issues);
        if (_genPath == null && getInitializedOutlets().isEmpty()) 
            issues.addError(this, "You need to configure at least one outlet!");

        if ((_genPath != null || _srcPath != null) && !_outlets.isEmpty()) 
            issues.addWarning(this, "'genPath' is ignored since you have specified outlets!");
        
        int defaultOutlets = 0;
        for (final Iterator<Outlet> iter = getInitializedOutlets().iterator(); iter.hasNext();) {
            final Outlet o = iter.next();
            if (o.getName() == null)
                defaultOutlets++;
        }
        
        if (defaultOutlets > 1) 
            issues.addError(this, "Only one outlet can be the default outlet. Please specifiy a name for the other outlets!");
        else if (defaultOutlets == 0) 
            issues.addWarning(this, "No default outlet configured!");

        if (_expand == null) 
            issues.addError(this, "property 'expand' not configured!");
        else {
            try {
                final ExpandStatement es = getStatement();
                if (es == null) {
                    issues.addError(this, "property 'expand' has wrong syntax!");
                }
            } catch (ParseException e) {
                issues.addError(this, "property 'expand' has wrong syntax : "+e.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5155.java