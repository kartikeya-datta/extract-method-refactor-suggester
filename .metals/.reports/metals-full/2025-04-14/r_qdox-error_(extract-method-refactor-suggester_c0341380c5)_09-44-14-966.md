error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13137.java
text:
```scala
c@@tx.setPersistenceContext(new PersistentContext(this, table.getRow(ctx.getPrimaryKeyUnchecked())));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.cmp.jdbc2.bridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import org.jboss.as.cmp.CmpMessages;
import org.jboss.as.cmp.bridge.FieldBridge;
import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCEntityPersistenceStore;
import org.jboss.as.cmp.jdbc.SQLUtil;
import org.jboss.as.cmp.jdbc.bridge.JDBCAbstractCMRFieldBridge;
import org.jboss.as.cmp.jdbc.bridge.JDBCAbstractEntityBridge;
import org.jboss.as.cmp.jdbc.bridge.JDBCFieldBridge;
import org.jboss.as.cmp.jdbc.metadata.JDBCCMPFieldMetaData;
import org.jboss.as.cmp.jdbc.metadata.JDBCEntityMetaData;
import org.jboss.as.cmp.jdbc.metadata.JDBCOptimisticLockingMetaData;
import org.jboss.as.cmp.jdbc.metadata.JDBCRelationshipRoleMetaData;
import org.jboss.as.cmp.jdbc2.JDBCStoreManager2;
import org.jboss.as.cmp.jdbc2.PersistentContext;
import org.jboss.as.cmp.jdbc2.schema.EntityTable;
import org.jboss.logging.Logger;


/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 81030 $</tt>
 */
public class JDBCEntityBridge2 implements JDBCAbstractEntityBridge {
    private final JDBCStoreManager2 manager;
    private final JDBCEntityMetaData metadata;
    private final EntityTable table;
    private final String tableName;
    private final String qualifiedTableName;
    private final Logger log;

    private JDBCCMPFieldBridge2[] pkFields;
    private JDBCCMPFieldBridge2[] cmpFields;
    private JDBCCMPFieldBridge2[] tableFields;
    private JDBCCMRFieldBridge2[] cmrFields;
    private JDBCCMPFieldBridge2 versionField;

    private int cmrCount;

    public JDBCEntityBridge2(JDBCStoreManager2 manager, JDBCEntityMetaData metadata) {
        this.manager = manager;
        this.metadata = metadata;
        log = Logger.getLogger(this.getClass().getName() + "." + metadata.getName());

        table = manager.getSchema().createEntityTable(metadata, this);
        tableName = SQLUtil.getTableNameWithoutSchema(metadata.getDefaultTableName());
        qualifiedTableName = SQLUtil.fixTableName(metadata.getDefaultTableName(), table.getDataSource());
    }

    // Public

    public void init() {
        loadCMPFields(metadata);
        loadCMRFields(metadata);

        JDBCOptimisticLockingMetaData olMD = metadata.getOptimisticLocking();
        if (olMD != null) {
            if (olMD.getLockingStrategy() != JDBCOptimisticLockingMetaData.LockingStrategy.VERSION_COLUMN_STRATEGY) {
                throw CmpMessages.MESSAGES.onlyVersionLockingSupported();
            }

            JDBCCMPFieldMetaData versionMD = olMD.getLockingField();
            versionField = (JDBCCMPFieldBridge2) getFieldByName(versionMD.getFieldName());
        }
    }

    public JDBCCMPFieldBridge2 getVersionField() {
        return versionField;
    }

    public void resolveRelationships() {
        for (int i = 0; i < cmrFields.length; ++i) {
            cmrFields[i].resolveRelationship();
        }
    }

    public void start() {
        if (versionField != null) {
            versionField.initVersion();
        }

        table.start();

        if (cmrFields != null) {
            for (int i = 0; i < cmrFields.length; ++i) {
                cmrFields[i].initLoader();
            }
        }
    }

    public JDBCEntityMetaData getMetaData() {
        return metadata;
    }

    public EntityTable getTable() {
        return table;
    }

    public JDBCFieldBridge[] getPrimaryKeyFields() {
        return pkFields;
    }

    public JDBCFieldBridge[] getTableFields() {
        return tableFields;
    }

    public JDBCAbstractCMRFieldBridge[] getCMRFields() {
        return cmrFields;
    }

    public JDBCEntityPersistenceStore getManager() {
        return manager;
    }

    public CmpEntityBeanComponent getComponent() {
        return manager.getComponent();
    }

    public Object extractPrimaryKeyFromInstance(CmpEntityBeanContext ctx) {
        try {
            Object pk = null;
            for (int i = 0; i < pkFields.length; ++i) {
                JDBCCMPFieldBridge2 pkField = pkFields[i];
                Object fieldValue = pkField.getValue(ctx);
                pk = pkField.setPrimaryKeyValue(pk, fieldValue);
            }
            return pk;
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw CmpMessages.MESSAGES.errorExtractingPk(e);
        }
    }

    public static void destroyPersistenceContext(CmpEntityBeanContext ctx) {
        // If we have an EJB 2.0 dynamic proxy,
        // notify the handler of the assigned context.
        // Object instance = ctx.getComponent().getCache().get(ctx.getPrimaryKey());
        // TODO: jeb - Set context on proxy
        ctx.setPersistenceContext(null);
    }

    public void initPersistenceContext(CmpEntityBeanContext ctx) {
        // If we have an EJB 2.0 dynamic proxy,
        // notify the handler of the assigned context.
        //Object instance = ctx.getComponent().getCache().get(ctx.getPrimaryKey());
        // TODO: jeb - Set context on proxy
    }

    public void initInstance(CmpEntityBeanContext ctx) {
        ctx.setPersistenceContext(new PersistentContext(this, table.getRow(ctx.getPrimaryKey())));
        for (int i = 0; i < tableFields.length; ++i) {
            tableFields[i].initInstance(ctx);
        }

        for (int i = 0; i < cmrFields.length; ++i) {
            cmrFields[i].initInstance(ctx);
        }
    }

    /**
     * hacky method needed at deployment time
     */
    public List<FieldBridge> getFields() {
        List<FieldBridge> fields = new ArrayList<FieldBridge>();
        for (int i = 0; i < pkFields.length; ++i) {
            fields.add(pkFields[i]);
        }

        for (int i = 0; i < cmpFields.length; ++i) {
            fields.add(cmpFields[i]);
        }

        for (int i = 0; i < cmrFields.length; ++i) {
            fields.add(cmrFields[i]);
        }

        return fields;
    }

    public boolean isStoreRequired(CmpEntityBeanContext instance) {
        PersistentContext pctx = (PersistentContext) instance.getPersistenceContext();
        return pctx.isDirty();
    }

    public boolean isModified(CmpEntityBeanContext instance) {
        PersistentContext pctx = (PersistentContext) instance.getPersistenceContext();
        boolean modified = pctx.isDirty();

        if (!modified && cmrFields != null) {
            for (int i = 0; i < cmrFields.length; ++i) {
                final JDBCCMRFieldBridge2.FieldState cmrState = pctx.getCMRState(i);
                if (cmrState != null && cmrState.isModified()) {
                    modified = true;
                    break;
                }
            }
        }
        return modified;
    }

    public Class getPrimaryKeyClass() {
        return metadata.getPrimaryKeyClass();
    }

    public Class getHomeClass() {
        return metadata.getHomeClass();
    }

    public Class getLocalHomeClass() {
        return metadata.getLocalHomeClass();
    }

    public String getTableName() {
        return tableName;
    }

    public String getQualifiedTableName() {
        return qualifiedTableName;
    }

    public DataSource getDataSource() {
        return table.getDataSource();
    }

    public boolean[] getLoadGroupMask(String eagerLoadGroupName) {
        // todo
        throw CmpMessages.MESSAGES.methodNotSupported();
    }

    public int getNextCMRIndex() {
        return cmrCount++;
    }

    public void remove(CmpEntityBeanContext ctx) throws RemoveException {
        if (cmrFields != null) {
            for (int i = 0; i < cmrFields.length; ++i) {
                cmrFields[i].remove(ctx);
            }
        }
    }

    // EntityBridge implementation

    public String getEntityName() {
        return metadata.getName();
    }

    public String getAbstractSchemaName() {
        return metadata.getAbstractSchemaName();
    }

    public FieldBridge getFieldByName(String fieldName) {
        FieldBridge field;
        for (int i = 0; i < pkFields.length; ++i) {
            field = pkFields[i];
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }

        for (int i = 0; i < cmpFields.length; ++i) {
            field = cmpFields[i];
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }

        for (int i = 0; i < cmrFields.length; ++i) {
            field = cmrFields[i];
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }

        throw CmpMessages.MESSAGES.fieldNotFound(fieldName, getEntityName());
    }

    public Class getRemoteInterface() {
        return metadata.getRemoteClass();
    }

    public Class getLocalInterface() {
        return metadata.getLocalClass();
    }

    // Package

    JDBCCMPFieldBridge2 addTableField(JDBCCMPFieldMetaData metadata) {
        table.addField();
        if (tableFields == null) {
            tableFields = new JDBCCMPFieldBridge2[1];
        } else {
            JDBCCMPFieldBridge2[] tmp = tableFields;
            tableFields = new JDBCCMPFieldBridge2[tableFields.length + 1];
            System.arraycopy(tmp, 0, tableFields, 0, tmp.length);
        }
        int tableIndex = tableFields.length - 1;
        JDBCCMPFieldBridge2 cmpField = new JDBCCMPFieldBridge2(manager, this, metadata, tableIndex);
        tableFields[tableFields.length - 1] = cmpField;
        return cmpField;
    }

    // Private

    private void loadCMPFields(JDBCEntityMetaData metadata) {
        // only non pk fields are stored here at first and then later
        // the pk fields are added to the front (makes sql easier to read)
        List cmpFieldsList = new ArrayList(metadata.getCMPFields().size());
        // primary key cmp fields
        List pkFieldsList = new ArrayList(metadata.getCMPFields().size());

        // create each field
        Iterator iter = metadata.getCMPFields().iterator();
        while (iter.hasNext()) {
            JDBCCMPFieldMetaData cmpFieldMetaData = (JDBCCMPFieldMetaData) iter.next();
            JDBCCMPFieldBridge2 cmpField = addTableField(cmpFieldMetaData);
            if (cmpFieldMetaData.isPrimaryKeyMember()) {
                pkFieldsList.add(cmpField);
            } else {
                cmpFieldsList.add(cmpField);
            }
        }

        // save the pk fields in the pk field array
        pkFields = new JDBCCMPFieldBridge2[pkFieldsList.size()];
        for (int i = 0; i < pkFieldsList.size(); ++i) {
            pkFields[i] = (JDBCCMPFieldBridge2) pkFieldsList.get(i);
        }

        // add the pk fields to the front of the cmp list, per guarantee above
        cmpFields = new JDBCCMPFieldBridge2[metadata.getCMPFields().size() - pkFields.length];
        int cmpFieldIndex = 0;
        for (int i = 0; i < cmpFieldsList.size(); ++i) {
            cmpFields[cmpFieldIndex++] = (JDBCCMPFieldBridge2) cmpFieldsList.get(i);
        }
    }

    private void loadCMRFields(JDBCEntityMetaData metadata) {
        cmrFields = new JDBCCMRFieldBridge2[metadata.getRelationshipRoles().size()];
        // create each field
        int cmrFieldIndex = 0;
        for (Iterator iter = metadata.getRelationshipRoles().iterator(); iter.hasNext(); ) {
            JDBCRelationshipRoleMetaData relationshipRole = (JDBCRelationshipRoleMetaData) iter.next();
            JDBCCMRFieldBridge2 cmrField = new JDBCCMRFieldBridge2(this, manager, relationshipRole);
            cmrFields[cmrFieldIndex++] = cmrField;
        }
    }

    public void stop() throws Exception {
        table.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13137.java