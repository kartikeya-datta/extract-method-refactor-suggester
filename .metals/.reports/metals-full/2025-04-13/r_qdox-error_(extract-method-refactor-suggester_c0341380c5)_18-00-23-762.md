error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15042.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15042.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15042.java
text:
```scala
c@@omponent.getEJBLocalObject(primaryKey) : component.getEJBObject(primaryKey);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.cmp.jdbc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.ejb.FinderException;
import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.ejbql.Catalog;
import org.jboss.as.cmp.ejbql.SelectFunction;
import org.jboss.as.cmp.jdbc.bridge.JDBCCMPFieldBridge;
import org.jboss.as.cmp.jdbc.bridge.JDBCEntityBridge;
import org.jboss.as.cmp.jdbc.bridge.JDBCFieldBridge;
import org.jboss.as.cmp.jdbc.metadata.JDBCDynamicQLQueryMetaData;
import org.jboss.as.cmp.jdbc.metadata.JDBCQueryMetaData;
import org.jboss.as.cmp.jdbc.metadata.JDBCReadAheadMetaData;

/**
 * This class generates a query from JBoss-QL.
 *
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>
 * @author <a href="mailto:alex@jboss.org">Alex Loubyansky</a>
 * @version $Revision: 81030 $
 */
public final class JDBCDynamicQLQuery extends JDBCAbstractQueryCommand {
    private final Catalog catalog;
    private final JDBCDynamicQLQueryMetaData metadata;

    public JDBCDynamicQLQuery(JDBCStoreManager manager, JDBCQueryMetaData q) {
        super(manager, q);
        catalog = manager.getCatalog();
        metadata = (JDBCDynamicQLQueryMetaData) q;
    }

    public Collection execute(Method finderMethod, Object[] args, CmpEntityBeanContext ctx, EntityProxyFactory factory) throws FinderException {
        String dynamicQL = (String) args[0];
        if (getLog().isDebugEnabled()) {
            getLog().debug("DYNAMIC-QL: " + dynamicQL);
        }

        QLCompiler compiler = null;
        try {
            compiler = JDBCQueryManager.getInstance(metadata.getQLCompilerClass(), catalog);
        } catch (Throwable e) {
            throw new FinderException(e.getMessage());
        }

        // get the parameters
        Object[] parameters = (Object[]) args[1];
        // parameter types
        Class[] parameterTypes;
        if (parameters == null) {
            parameterTypes = new Class[0];
        } else {
            // get the parameter types
            parameterTypes = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    throw new FinderException("Parameter[" + i + "] is null");
                }
                parameterTypes[i] = parameters[i].getClass();
            }
        }

        // compile the dynamic-ql
        try {
            compiler.compileJBossQL(
                    dynamicQL,
                    finderMethod.getReturnType(),
                    parameterTypes,
                    metadata);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new FinderException("Error compiling ejbql: " + t);
        }

        int offset = toInt(parameters, compiler.getOffsetParam(), compiler.getOffsetValue());
        int limit = toInt(parameters, compiler.getLimitParam(), compiler.getLimitValue());

        JDBCEntityBridge selectEntity = null;
        JDBCCMPFieldBridge selectField = null;
        SelectFunction selectFunction = null;
        if (compiler.isSelectEntity()) {
            selectEntity = (JDBCEntityBridge) compiler.getSelectEntity();
        } else if (compiler.isSelectField()) {
            selectField = (JDBCCMPFieldBridge) compiler.getSelectField();
        } else {
            selectFunction = compiler.getSelectFunction();
        }

        boolean[] mask;
        List leftJoinCMRList;
        JDBCReadAheadMetaData readahead = metadata.getReadAhead();
        if (selectEntity != null && readahead.isOnFind()) {
            mask = selectEntity.getLoadGroupMask(readahead.getEagerLoadGroup());
            boolean modifiedMask = false;
            leftJoinCMRList = compiler.getLeftJoinCMRList();

            // exclude non-searchable columns if distinct is used
            if (compiler.isSelectDistinct()) {
                JDBCFieldBridge[] tableFields = selectEntity.getTableFields();
                for (int i = 0; i < tableFields.length; ++i) {
                    if (mask[i] && !tableFields[i].getJDBCType().isSearchable()) {
                        if (!modifiedMask) {
                            boolean[] original = mask;
                            mask = new boolean[original.length];
                            System.arraycopy(original, 0, mask, 0, mask.length);
                            modifiedMask = true;
                        }
                        mask[i] = false;
                    }
                }
            }
        } else {
            mask = null;
            leftJoinCMRList = Collections.EMPTY_LIST;
        }

        // get the parameter order
        setParameterList(compiler.getInputParameters());

        final CmpEntityBeanComponent component = ((JDBCStoreManager) compiler.getStoreManager()).getComponent();
        EntityProxyFactory factoryToUse = new EntityProxyFactory() {
            public Object getEntityObject(Object primaryKey) {
                return metadata.isResultTypeMappingLocal() && component.getLocalHomeClass() != null ?
                        component.getEjbLocalObject(primaryKey) : component.getEJBObject(primaryKey);
            }
        };

        return execute(
                compiler.getSQL(),
                parameters,
                offset,
                limit,
                selectEntity,
                selectField,
                selectFunction,
                (JDBCStoreManager) compiler.getStoreManager(),
                mask,
                compiler.getInputParameters(),
                leftJoinCMRList,
                metadata,
                factoryToUse,
                log
        );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15042.java