error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2055.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2055.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2055.java
text:
```scala
final E@@xecutionContext backendCtx = BackendFacade.createExecutionContext (fdc, ts, true); //TODO configure isLogStacktrace

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.middleend.old;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.internal.xpand2.ast.Definition;
import org.eclipse.internal.xpand2.ast.Statement;
import org.eclipse.internal.xpand2.ast.Template;
import org.eclipse.internal.xpand2.codeassist.XpandTokens;
import org.eclipse.internal.xpand2.parser.XpandParseFacade;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.output.FileHandle;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xpand2.output.PostProcessor;
import org.eclipse.xtend.backend.BackendFacade;
import org.eclipse.xtend.backend.common.BackendTypesystem;
import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.ExpressionBase;
import org.eclipse.xtend.backend.common.FunctionDefContext;
import org.eclipse.xtend.backend.common.NamedFunction;
import org.eclipse.xtend.backend.functions.FunctionDefContextFactory;
import org.eclipse.xtend.backend.functions.FunctionDefContextInternal;
import org.eclipse.xtend.backend.functions.SourceDefinedFunction;
import org.eclipse.xtend.backend.syslib.FileIoOperations;
import org.eclipse.xtend.backend.syslib.FileOutlet;
import org.eclipse.xtend.backend.syslib.InMemoryPostprocessor;
import org.eclipse.xtend.backend.syslib.SysLibNames;
import org.eclipse.xtend.backend.syslib.UriBasedPostprocessor;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.MetaModel;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public final class XpandBackendFacade {
    private final OldXtendRegistry _extensions;
    private final OldXpandRegistry _registry;
    private final String _xpandFile;
    private final BackendTypesystem _ts;
    
    
    /**
     * This method executes Xpand code that is passed in as a string, script language style.<br>
     * This method executes Xpand code that is passed in as a string, script language style.<br>
     * 
     * There are two restrictions. Firstly, no DEFINEs are allowed - the string that is passed in must be a valid body for a DEFINE. Never
     *  mind the "parameters" - the "variables" parameter defines all variables that will be defined during execution. Use "this" as a 
     *  variable name to specify the variable that is implicitly bound as the "special" parameter passed to a definition.<br>
     *  
     * Secondly, no IMPORT or EXTENSION statements are possible. So types must be referenced by their fully qualified names, and no calls
     *  to extensions are possible. Calls to other templates that are available as files are possible, just as you would expect.<br>
     *  
     * Both the "variables" and "outlets" parameter may be null.
     */
    public static Object executeStatement (String code, Collection<MetaModel> mms, Map<String, Object> variables, Collection <Outlet> outlets) {
        return executeStatement (code, null, mms, variables, outlets);
    }
    
    /**
     * This method executes Xpand code that is passed in as a string, script language style.<br>
     * This method executes Xpand code that is passed in as a string, script language style.<br>
     * 
     * There are two restrictions. Firstly, no DEFINEs are allowed - the string that is passed in must be a valid body for a DEFINE. Never
     *  mind the "parameters" - the "variables" parameter defines all variables that will be defined during execution. Use "this" as a 
     *  variable name to specify the variable that is implicitly bound as the "special" parameter passed to a definition.<br>
     *  
     * Secondly, no IMPORT or EXTENSION statements are possible. So types must be referenced by their fully qualified names, and no calls
     *  to extensions are possible. Calls to other templates that are available as files are possible, just as you would expect.<br>
     *  
     * Both the "variables" and "outlets" parameter may be null.
     */
    public static Object executeStatement (String code, String fileEncoding, Collection<MetaModel> mms, Map<String, Object> variables, Collection <Outlet> outlets) {
        fileEncoding = OldXtendHelper.normalizedFileEncoding (fileEncoding);
        final BackendTypesystem ts = OldXtendHelper.guessTypesystem (mms);
        
        if (variables == null)
            variables = new HashMap<String, Object> ();
        if (outlets == null)
            outlets = new ArrayList<Outlet> ();
        
        final Template tpl = XpandParseFacade.file (new StringReader (XpandTokens.LT + "DEFINE dUmMy FOR dUmMy" + XpandTokens.RT + code + XpandTokens.RT + XpandTokens.LT + "ENDDEFINE" + XpandTokens.RT), null);
        final Statement[] statements = ((Definition) tpl.getDefinitions()[0]).getBody();
        
        
        XpandExecutionContext ctx = createOldExecutionContext (fileEncoding, mms, outlets);
        for (String varName: variables.keySet())
            ctx = (XpandExecutionContext) ctx.cloneWithVariable (new Variable (varName, ctx.getType (variables.get (varName))));
        
        final Set<XpandDefinitionName> referenced = new HashSet<XpandDefinitionName>();
        final OldDefinitionConverter defConverter = new OldDefinitionConverter (ctx, new TypeToBackendType (ts, ctx));
        final ExpressionBase converted = defConverter.convertStatementSequence (statements, tpl, referenced);

        
        final FunctionDefContextInternal fdc = new FunctionDefContextFactory (ts).create();
        
        //TODO refactor this to extract common code - both from here and from OldXpandRegistry
        final OldXpandRegistry oxr = new OldXpandRegistry (ctx, ts, new OldXtendRegistry (ctx, ts));
        for (XpandDefinitionName xdn: referenced) {
            oxr.registerXpandFile (xdn.getCanonicalTemplateFileName());
            
            for (NamedFunction f: oxr.getContributedFunctions (xdn.getCanonicalTemplateFileName()))
                fdc.register (f);
        }
        
        final ExecutionContext backendCtx = BackendFacade.createExecutionContext (fdc, ts);
        backendCtx.getLocalVarContext().getLocalVars().putAll (variables);
        registerOutlets (backendCtx, outlets);

        return converted.evaluate (backendCtx);
    }
    
    public static void registerOutlets (ExecutionContext ctx, Collection<Outlet> outlets) {
        for (Outlet oldOutlet: outlets) {
            final FileOutlet newOutlet = new FileOutlet ();
            newOutlet.setAppend (oldOutlet.isAppend());
            newOutlet.setBaseDir (new File (oldOutlet.getPath()));
            if (oldOutlet.getFileEncoding() != null)
                newOutlet.setFileEncoding (oldOutlet.getFileEncoding());
            newOutlet.setOverwrite (oldOutlet.isOverwrite());

            for (PostProcessor pp: oldOutlet.postprocessors) {
                newOutlet.register (new InMemoryPpAdapter (pp, oldOutlet));
                newOutlet.register (new UriBasedPpAdapter (pp, oldOutlet));
            }
            
            final String outletName = (oldOutlet.getName() != null) ? oldOutlet.getName() : FileIoOperations.DEFAULT_OUTLET_NAME;
            ctx.getFunctionDefContext ().invoke (ctx, SysLibNames.REGISTER_OUTLET, Arrays.asList (outletName, newOutlet));
        }
    }
    
    private static class InMemoryPpAdapter implements InMemoryPostprocessor {
        private final PostProcessor _oldPp;
        private final Outlet _outlet;

        public InMemoryPpAdapter (PostProcessor oldPp, Outlet outlet) {
            _oldPp = oldPp;
            _outlet = outlet;
        }

        public CharSequence process (CharSequence unprocessed, String uri) {
            final FileHandle fh = new FileHandleImpl (unprocessed, _outlet, new File (uri));
            _oldPp.beforeWriteAndClose (fh);
            return fh.getBuffer();
        }
    }

    private static class FileHandleImpl implements FileHandle {
        private CharSequence _buffer;
        private final Outlet _outlet;
        private final File _file;

        public FileHandleImpl (CharSequence buffer, Outlet outlet, File file) {
            _buffer = buffer;
            _outlet = outlet;
            _file = file;
        }

        public CharSequence getBuffer () {
            return _buffer;
        }

        public String getFileEncoding () {
            return _outlet.getFileEncoding();
        }

        public Outlet getOutlet () {
            return _outlet;
        }

        public File getTargetFile () {
            return _file;
        }

        public boolean isAppend () {
            return _outlet.isAppend();
        }

        public boolean isOverwrite () {
            return _outlet.isOverwrite();
        }

        public void setBuffer (CharSequence buffer) {
            _buffer = buffer;
        }

        public void writeAndClose () {
            throw new UnsupportedOperationException ();
        }
    }
    
    private static class UriBasedPpAdapter implements UriBasedPostprocessor {
        private final PostProcessor _oldPp;
        private final Outlet _outlet;
        
        public UriBasedPpAdapter (PostProcessor oldPp, Outlet outlet) {
            _oldPp = oldPp;
            _outlet = outlet;
        }

        public void process (String uri) {
            final FileHandle fh = new FileHandleImpl ("", _outlet, new File (uri));
            _oldPp.afterClose (fh);
        }
    }
    
    public static XpandBackendFacade createForFile (String xpandFilename, String fileEncoding, Collection<MetaModel> mms, Collection <Outlet> outlets) {
        return new XpandBackendFacade (xpandFilename, fileEncoding, mms, outlets);
    }
    
    
    private static XpandExecutionContext createOldExecutionContext (String fileEncoding, Collection<MetaModel> mms, Collection<Outlet> outlets) {
        fileEncoding = OldXtendHelper.normalizedFileEncoding (fileEncoding);
        
        final Output output = new OutputImpl ();
        for (Outlet outlet: outlets)
            output.addOutlet (outlet);
        //TODO ProtectedRegionResolver
        
        final XpandExecutionContextImpl ctx = new XpandExecutionContextImpl (output, null);
        for (MetaModel mm: mms)
            ctx.registerMetaModel (mm);
        ctx.setFileEncoding (fileEncoding);
        
        return ctx;
    }
    
    private XpandBackendFacade (String xpandFilename, String fileEncoding, Collection<MetaModel> mms, Collection <Outlet> outlets) {
        _xpandFile = OldXtendHelper.normalizeXpandResourceName (xpandFilename);
        _ts = OldXtendHelper.guessTypesystem (mms);
        
        final XpandExecutionContext ctx = createOldExecutionContext (fileEncoding, mms, outlets);
        
        _extensions = new OldXtendRegistry (ctx, _ts);
        _registry = new OldXpandRegistry (ctx, _ts, _extensions);
        _registry.registerXpandFile (xpandFilename);
    }
    
    public Collection<NamedFunction> getContributedFunctions () {
        return _registry.getContributedFunctions (_xpandFile);
    }
    
    public FunctionDefContext getFunctionDefContext () {
        if (getContributedFunctions().isEmpty())
            return new FunctionDefContextFactory(_ts).create();
        
        return ((SourceDefinedFunction) getContributedFunctions().iterator().next().getFunction()).getFunctionDefContext();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2055.java