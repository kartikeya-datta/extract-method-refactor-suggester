error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8991.java
text:
```scala
f@@k, store, joins);

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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.strats.NoneClassStrategy;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.schema.Schemas;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.RowManager;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.PCState;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.ValueMetaData;
import org.apache.openjpa.util.ApplicationIds;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.MetaDataException;
import org.apache.openjpa.util.OpenJPAId;

/**
 * Specialization of metadata for relational databases.
 *
 * @author Abe White
 */
public class ClassMapping
    extends ClassMetaData
    implements ClassStrategy {

    public static final ClassMapping[] EMPTY_MAPPINGS = new ClassMapping[0];

    private static final Localizer _loc = Localizer.forPackage
        (ClassMapping.class);

    private final ClassMappingInfo _info;
    private final Discriminator _discrim;
    private final Version _version;
    private ClassStrategy _strategy = null;

    private Table _table = null;
    private ColumnIO _io = null;
    private Column[] _cols = Schemas.EMPTY_COLUMNS;
    private ForeignKey _fk = null;
    private int _subclassMode = Integer.MAX_VALUE;

    private ClassMapping[] _joinSubMaps = null;
    private ClassMapping[] _assignMaps = null;

    // maps columns to joinables
    private final Map _joinables = Collections.synchronizedMap(new HashMap());

    /**
     * Constructor. Supply described type and owning repository.
     */
    protected ClassMapping(Class type, MappingRepository repos) {
        super(type, repos);
        _discrim = repos.newDiscriminator(this);
        _version = repos.newVersion(this);
        _info = repos.newMappingInfo(this);
    }

    /**
     * Embedded constructor. Supply embedding value and owning repository.
     */
    protected ClassMapping(ValueMetaData vmd) {
        super(vmd);
        _discrim = getMappingRepository().newDiscriminator(this);
        _version = getMappingRepository().newVersion(this);
        _info = getMappingRepository().newMappingInfo(this);
    }

    /**
     * The class discriminator.
     */
    public Discriminator getDiscriminator() {
        return _discrim;
    }

    /**
     * The version indicator.
     */
    public Version getVersion() {
        return _version;
    }

    ///////////
    // Runtime
    ///////////

    /**
     * Return the oid value stored in the result. This implementation will
     * recurse until it finds an ancestor class who uses oid values for its
     * primary key.
     *
     * @param fk if non-null, use the local columns of the given foreign
     * key in place of this class' primary key columns
     * @see #isPrimaryKeyObjectId
     */
    public Object getObjectId(JDBCStore store, Result res, ForeignKey fk,
        boolean subs, Joins joins)
        throws SQLException {
        ValueMapping embed = getEmbeddingMapping();
        if (embed != null)
            return embed.getFieldMapping().getDefiningMapping().
                getObjectId(store, res, fk, subs, joins);

        return getObjectId(this, store, res, fk, subs, joins);
    }

    /**
     * Recursive helper for public <code>getObjectId</code> method.
     */
    private Object getObjectId(ClassMapping cls, JDBCStore store, Result res,
        ForeignKey fk, boolean subs, Joins joins)
        throws SQLException {
        if (!isPrimaryKeyObjectId(true))
            return getPCSuperclassMapping().getObjectId(cls, store, res, fk,
                subs, joins);
        if (getIdentityType() == ID_UNKNOWN)
            throw new InternalException();

        Column[] pks = getPrimaryKeyColumns();
        if (getIdentityType() == ID_DATASTORE) {
            Column col = (fk == null) ? pks[0] : fk.getColumn(pks[0]);
            long id = res.getLong(col, joins);
            return (id == 0 && res.wasNull()) ? null
                : store.newDataStoreId(id, cls, subs);
        }

        // application identity
        Object[] vals = new Object[getPrimaryKeyFields().length];
        FieldMapping fm;
        Joinable join;
        int pkIdx;
        for (int i = 0; i < pks.length; i++) {
            // we know that all pk column join mappings use primary key fields,
            // cause this mapping uses the oid as its primary key (we recursed
            // at the beginning of the method to ensure this)
            join = assertJoinable(pks[i]);
            fm = getFieldMapping(join.getFieldIndex());
            pkIdx = fm.getPrimaryKeyIndex();

            // could have already set value with previous multi-column joinable
            if (vals[pkIdx] == null) {
                res.startDataRequest(fm);
                vals[pkIdx] = join.getPrimaryKeyValue(res, join.getColumns(),
                    fk, joins);
                res.endDataRequest();
                if (vals[pkIdx] == null)
                    return null;
            }
        }
        Object oid = ApplicationIds.fromPKValues(vals, cls);
        if (!subs && oid instanceof OpenJPAId)
            ((OpenJPAId) oid).setManagedInstanceType(cls.getDescribedType());
        return oid;
    }

    /**
     * Return the given column value(s) for the given object. The given
     * columns will be primary key columns of this mapping, but may be in
     * any order. If there is only one column, return its value. If there
     * are multiple columns, return an object array of their values, in the
     * same order the columns are given.
     */
    public Object toDataStoreValue(Object obj, Column[] cols, JDBCStore store) {
        Object ret = (cols.length == 1) ? null : new Object[cols.length];

        // in the past we've been lenient about being able to translate objects
        // from other persistence contexts, so try to get sm directly from
        // instance before asking our context
        OpenJPAStateManager sm;
        if (obj instanceof PersistenceCapable)
            sm = (OpenJPAStateManager) ((PersistenceCapable) obj).
                pcGetStateManager();
        else
            sm = store.getContext().getStateManager(obj);
        if (sm == null)
            return ret;

        Object val;
        for (int i = 0; i < cols.length; i++) {
            val = assertJoinable(cols[i]).getJoinValue(sm, cols[i], store);
            if (cols.length == 1)
                ret = val;
            else
                ((Object[]) ret)[i] = val;
        }
        return ret;
    }

    /**
     * Return the joinable for the given column, or throw an exception if
     * none is available.
     */
    public Joinable assertJoinable(Column col) {
        Joinable join = getJoinable(col);
        if (join == null)
            throw new MetaDataException(_loc.get("no-joinable",
                col.getFullName()));
        return join;
    }

    /**
     * Return the {@link Joinable} for the given column. Any column that
     * another mapping joins to must be controlled by a joinable.
     */
    public Joinable getJoinable(Column col) {
        Joinable join;
        if (getEmbeddingMetaData() != null) {
            join = getEmbeddingMapping().getFieldMapping().
                getDefiningMapping().getJoinable(col);
            if (join != null)
                return join;
        }
        ClassMapping sup = getJoinablePCSuperclassMapping();
        if (sup != null) {
            join = sup.getJoinable(col);
            if (join != null)
                return join;
        }
        return (Joinable) _joinables.get(col);
    }

    /**
     * Add the given column-to-joinable mapping.
     */
    public void setJoinable(Column col, Joinable joinable) {
        // don't let non-pk override pk
        Joinable join = (Joinable) _joinables.get(col);
        if (join == null || (join.getFieldIndex() != -1
            && getField(join.getFieldIndex()).getPrimaryKeyIndex() == -1))
            _joinables.put(col, joinable);
    }

    /**
     * Return whether the columns of the given foreign key to this mapping
     * can be used to construct an object id for this type. This is a
     * relatively expensive operation; its results should be cached.
     *
     * @return {@link Boolean#TRUE} if the foreign key contains all oid
     * columns, <code>null</code> if it contains only some columns,
     * or {@link Boolean#FALSE} if it contains non-oid columns
     */
    public Boolean isForeignKeyObjectId(ForeignKey fk) {
        // if this mapping's primary key can't construct an oid, then no way
        // foreign key can
        if (getIdentityType() == ID_UNKNOWN || !isPrimaryKeyObjectId(false))
            return Boolean.FALSE;

        // with datastore identity, it's all or nothing
        Column[] cols = fk.getPrimaryKeyColumns();
        if (getIdentityType() == ID_DATASTORE) {
            if (cols.length != 1 || cols[0] != getPrimaryKeyColumns()[0])
                return Boolean.FALSE;
            return Boolean.TRUE;
        }

        // check the join mapping for each pk column to see if it links up to
        // a primary key field
        Joinable join;
        for (int i = 0; i < cols.length; i++) {
            join = assertJoinable(cols[i]);
            if (join.getFieldIndex() != -1
                && getField(join.getFieldIndex()).getPrimaryKeyIndex() == -1)
                return Boolean.FALSE;
        }

        // if all primary key links, see whether we join to all pks
        if (isPrimaryKeyObjectId(true)
            && cols.length == getPrimaryKeyColumns().length)
            return Boolean.TRUE;
        return null;
    }

    ///////
    // ORM
    ///////

    /**
     * Raw mapping data.
     */
    public ClassMappingInfo getMappingInfo() {
        return _info;
    }

    /**
     * The strategy used to map this mapping.
     */
    public ClassStrategy getStrategy() {
        return _strategy;
    }

    /**
     * The strategy used to map this mapping. The <code>adapt</code>
     * parameter determines whether to adapt when mapping the strategy;
     * use null if the strategy should not be mapped.
     */
    public void setStrategy(ClassStrategy strategy, Boolean adapt) {
        // set strategy first so we can access it during mapping
        ClassStrategy orig = _strategy;
        _strategy = strategy;
        if (strategy != null) {
            try {
                strategy.setClassMapping(this);
                if (adapt != null)
                    strategy.map(adapt.booleanValue());
            } catch (RuntimeException re) {
                // reset strategy
                _strategy = orig;
                throw re;
            }
        }
    }

    /**
     * The mapping's primary table.
     */
    public Table getTable() {
        return _table;
    }

    /**
     * The mapping's primary table.
     */
    public void setTable(Table table) {
        _table = table;
    }

    /**
     * The columns this mapping uses to uniquely identify an object.
     * These will typically be the primary key columns or the columns this
     * class uses to link to its superclass table.
     */
    public Column[] getPrimaryKeyColumns() {
        if (_cols.length == 0 && getIdentityType() == ID_APPLICATION
            && isMapped()) {
            FieldMapping[] pks = getPrimaryKeyFieldMappings();
            Collection cols = new ArrayList(pks.length);
            Column[] fieldCols;
            for (int i = 0; i < pks.length; i++) {
                fieldCols = pks[i].getColumns();
                for (int j = 0; j < fieldCols.length; j++)
                    cols.add(fieldCols[j]);
            }
            _cols = (Column[]) cols.toArray(new Column[cols.size()]);
        }
        return _cols;
    }

    /**
     * The columns this mapping uses to uniquely identify an object.
     * These will typically be the primary key columns or the columns this
     * class uses to link to its superclass table.
     */
    public void setPrimaryKeyColumns(Column[] cols) {
        if (cols == null)
            cols = Schemas.EMPTY_COLUMNS;
        _cols = cols;
    }

    /**
     * I/O information on the key columns / join key.
     */
    public ColumnIO getColumnIO() {
        return (_io == null) ? ColumnIO.UNRESTRICTED : _io;
    }

    /**
     * I/O information on the key columns / join key.
     */
    public void setColumnIO(ColumnIO io) {
        _io = io;
    }

    /**
     * Foreign key linking the primary key columns to the superclass table,
     * or null if none.
     */
    public ForeignKey getJoinForeignKey() {
        return _fk;
    }

    /**
     * Foreign key linking the primary key columns to the superclass table,
     * or null if none.
     */
    public void setJoinForeignKey(ForeignKey fk) {
        _fk = fk;
    }

    public void refSchemaComponents() {
        if (getEmbeddingMetaData() == null) {
            if (_table != null && _table.getPrimaryKey() != null)
                _table.getPrimaryKey().ref();
            if (_fk != null)
                _fk.ref();
            Column[] pks = getPrimaryKeyColumns();
            for (int i = 0; i < pks.length; i++)
                pks[i].ref();
        } else {
            FieldMapping[] fields = getFieldMappings();
            for (int i = 0; i < fields.length; i++)
                fields[i].refSchemaComponents();
        }
    }

    /**
     * Clear mapping information, including strategy.
     */
    public void clearMapping() {
        _strategy = null;
        _cols = Schemas.EMPTY_COLUMNS;
        _fk = null;
        _table = null;
        _info.clear();
        setResolve(MODE_MAPPING | MODE_MAPPING_INIT, false);
    }

    /**
     * Update {@link MappingInfo} with our current mapping information.
     */
    public void syncMappingInfo() {
        if (getEmbeddingMetaData() == null)
            _info.syncWith(this);
        else {
            _info.clear();
            FieldMapping[] fields = getFieldMappings();
            for (int i = 0; i < fields.length; i++)
                fields[i].syncMappingInfo();
        }
    }

    //////////////////////
    // MetaData interface
    //////////////////////

    protected void setDescribedType(Class type) {
        super.setDescribedType(type);
        // this method called from superclass constructor, so _info not yet
        // initialized
        if (_info != null)
            _info.setClassName(type.getName());
    }

    /**
     * The subclass fetch mode, as one of the eager constants in
     * {@link JDBCFetchConfiguration}.
     */
    public int getSubclassFetchMode() {
        if (_subclassMode == Integer.MAX_VALUE) {
            if (getPCSuperclass() != null)
                _subclassMode = getPCSuperclassMapping().
                    getSubclassFetchMode();
            else
                _subclassMode = FetchConfiguration.DEFAULT;
        }
        return _subclassMode;
    }

    /**
     * The subclass fetch mode, as one of the eager constants in
     * {@link JDBCFetchConfiguration}.
     */
    public void setSubclassFetchMode(int mode) {
        _subclassMode = mode;
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getRepository}.
     */
    public MappingRepository getMappingRepository() {
        return (MappingRepository) getRepository();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getEmbeddingMetaData}
     */
    public ValueMapping getEmbeddingMapping() {
        return (ValueMapping) getEmbeddingMetaData();
    }

    /**
     * Returns true if this class does not use the "none" strategy (including
     * if it has a null strategy, and therefore is probably in the process of
     * being mapped).
     */
    public boolean isMapped() {
        if (!super.isMapped())
            return false;
        if (_strategy != null)
            return _strategy != NoneClassStrategy.getInstance();
        return !NoneClassStrategy.ALIAS.equals(_info.getStrategy());
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getPCSuperclassMetaData}.
     */
    public ClassMapping getPCSuperclassMapping() {
        return (ClassMapping) getPCSuperclassMetaData();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getMappedPCSuperclassMetaData}.
     */
    public ClassMapping getMappedPCSuperclassMapping() {
        return (ClassMapping) getMappedPCSuperclassMetaData();
    }

    /**
     * Return the nearest mapped superclass that can join to this class.
     */
    public ClassMapping getJoinablePCSuperclassMapping() {
        ClassMapping sup = getMappedPCSuperclassMapping();
        if (sup == null)
            return null;
        if (_fk != null || _table == null || _table.equals(sup.getTable()))
            return sup;
        return null;
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getPCSubclassMetaDatas}.
     */
    public ClassMapping[] getPCSubclassMappings() {
        return (ClassMapping[]) getPCSubclassMetaDatas();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getMappedPCSubclassMetaDatas}.
     */
    public ClassMapping[] getMappedPCSubclassMappings() {
        return (ClassMapping[]) getMappedPCSubclassMetaDatas();
    }

    /**
     * Return mapped subclasses that are reachable via joins.
     */
    public ClassMapping[] getJoinablePCSubclassMappings() {
        ClassMapping[] subs = getMappedPCSubclassMappings(); // checks for new
        if (_joinSubMaps == null) {
            if (subs.length == 0)
                _joinSubMaps = subs;
            else {
                List joinable = new ArrayList(subs.length);
                for (int i = 0; i < subs.length; i++)
                    if (isSubJoinable(subs[i]))
                        joinable.add(subs[i]);
                _joinSubMaps = (ClassMapping[]) joinable.toArray
                    (new ClassMapping[joinable.size()]);
            }
        }
        return _joinSubMaps;
    }

    /**
     * Return whether we can reach the given subclass via joins.
     */
    private boolean isSubJoinable(ClassMapping sub) {
        if (sub == null)
            return false;
        if (sub == this)
            return true;
        return isSubJoinable(sub.getJoinablePCSuperclassMapping());
    }

    /**
     * Returns the closest-derived list of non-inter-joinable mapped types
     * assignable to this type. May return this mapping.
     */
    public ClassMapping[] getIndependentAssignableMappings() {
        ClassMapping[] subs = getMappedPCSubclassMappings(); // checks for new
        if (_assignMaps == null) {
            // remove unmapped subs
            if (subs.length == 0) {
                if (isMapped())
                    _assignMaps = new ClassMapping[]{ this };
                else
                    _assignMaps = subs;
            } else {
                int size = (int) (subs.length * 1.33 + 2);
                Set independent = ListOrderedSet.decorate(new HashSet(size),
                    new ArrayList(subs.length + 1));
                if (isMapped())
                    independent.add(this);
                independent.addAll(Arrays.asList(subs));

                // remove all mappings that have a superclass mapping in the set
                ClassMapping map, sup;
                List clear = null;
                for (Iterator itr = independent.iterator(); itr.hasNext();) {
                    map = (ClassMapping) itr.next();
                    sup = map.getJoinablePCSuperclassMapping();
                    if (sup != null && independent.contains(sup)) {
                        if (clear == null)
                            clear = new ArrayList(independent.size() - 1);
                        clear.add(map);
                    }
                }
                if (clear != null)
                    independent.removeAll(clear);

                _assignMaps = (ClassMapping[]) independent.toArray
                    (new ClassMapping[independent.size()]);
            }
        }
        return _assignMaps;
    }

    /**
     * Convenience method to perform cast from {@link ClassMetaData#getFields}.
     */
    public FieldMapping[] getFieldMappings() {
        return (FieldMapping[]) getFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDeclaredFields}.
     */
    public FieldMapping[] getDeclaredFieldMappings() {
        return (FieldMapping[]) getDeclaredFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getPrimaryKeyFields}.
     */
    public FieldMapping[] getPrimaryKeyFieldMappings() {
        return (FieldMapping[]) getPrimaryKeyFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getVersionField}.
     */
    public FieldMapping getVersionFieldMapping() {
        return (FieldMapping) getVersionField();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDefaultFetchGroupFields}.
     */
    public FieldMapping[] getDefaultFetchGroupFieldMappings() {
        return (FieldMapping[]) getDefaultFetchGroupFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDefinedFields}.
     */
    public FieldMapping[] getDefinedFieldMappings() {
        return (FieldMapping[]) getDefinedFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getFieldsInListingOrder}.
     */
    public FieldMapping[] getFieldMappingsInListingOrder() {
        return (FieldMapping[]) getFieldsInListingOrder();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDefinedFieldsInListingOrder}.
     */
    public FieldMapping[] getDefinedFieldMappingsInListingOrder() {
        return (FieldMapping[]) getDefinedFieldsInListingOrder();
    }

    /**
     * Convenience method to perform cast from {@link ClassMetaData#getField}.
     */
    public FieldMapping getFieldMapping(int index) {
        return (FieldMapping) getField(index);
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDeclaredField}.
     */
    public FieldMapping getDeclaredFieldMapping(int index) {
        return (FieldMapping) getDeclaredField(index);
    }

    /**
     * Convenience method to perform cast from {@link ClassMetaData#getField}.
     */
    public FieldMapping getFieldMapping(String name) {
        return (FieldMapping) getField(name);
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDeclaredField}.
     */
    public FieldMapping getDeclaredFieldMapping(String name) {
        return (FieldMapping) getDeclaredField(name);
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#getDeclaredUnmanagedFields}.
     */
    public FieldMapping[] getDeclaredUnmanagedFieldMappings() {
        return (FieldMapping[]) getDeclaredUnmanagedFields();
    }

    /**
     * Convenience method to perform cast from
     * {@link ClassMetaData#addDeclaredField}.
     */
    public FieldMapping addDeclaredFieldMapping(String name, Class type) {
        return (FieldMapping) addDeclaredField(name, type);
    }

    protected void resolveMapping(boolean runtime) {
        super.resolveMapping(runtime);

        // map class strategy; it may already be mapped by the repository before
        // the resolve process begins
        MappingRepository repos = getMappingRepository();
        if (_strategy == null)
            repos.getStrategyInstaller().installStrategy(this);
        Log log = getRepository().getLog();
        if (log.isTraceEnabled())
            log.trace(_loc.get("strategy", this, _strategy.getAlias()));

        // make sure unmapped superclass fields are defined if we're mapped;
        // also may have been done by repository already
        defineSuperclassFields(getJoinablePCSuperclassMapping() == null);

        // resolve everything that doesn't rely on any relations to avoid
        // recursion, then resolve all fields
        resolveNonRelationMappings();
        FieldMapping[] fms = getFieldMappings();
        for (int i = 0; i < fms.length; i++)
            if (fms[i].getDefiningMetaData() == this)
                fms[i].resolve(MODE_MAPPING);
        fms = getDeclaredUnmanagedFieldMappings();
        for (int i = 0; i < fms.length; i++)
            fms[i].resolve(MODE_MAPPING);

        // mark mapped columns
        if (_cols != null) {
            ColumnIO io = getColumnIO();
            for (int i = 0; i < _cols.length; i++) {
                if (io.isInsertable(i, false))
                    _cols[i].setFlag(Column.FLAG_DIRECT_INSERT, true);
                if (io.isUpdatable(i, false))
                    _cols[i].setFlag(Column.FLAG_DIRECT_UPDATE, true);
            }
        }
    }

    /**
     * Resolve non-relation field mappings so that when we do relation
     * mappings they can rely on them for joins.
     */
    void resolveNonRelationMappings() {
        // make sure primary key fields are resolved first because other
        // fields might rely on them
        FieldMapping[] fms = getPrimaryKeyFieldMappings();
        for (int i = 0; i < fms.length; i++)
            fms[i].resolve(MODE_MAPPING);

        // resolve defined fields that are safe; that don't rely on other types
        // also being resolved.  don't use getDefinedFields b/c it relies on
        // whether fields are mapped, which isn't known yet
        fms = getFieldMappings();
        for (int i = 0; i < fms.length; i++)
            if (fms[i].getDefiningMetaData() == this
                && !fms[i].isTypePC() && !fms[i].getKey().isTypePC()
                && !fms[i].getElement().isTypePC())
                fms[i].resolve(MODE_MAPPING);

        _discrim.resolve(MODE_MAPPING);
        _version.resolve(MODE_MAPPING);
    }

    protected void initializeMapping() {
        super.initializeMapping();

        FieldMapping[] fields = getDefinedFieldMappings();
        for (int i = 0; i < fields.length; i++)
            fields[i].resolve(MODE_MAPPING_INIT);
        _discrim.resolve(MODE_MAPPING_INIT);
        _version.resolve(MODE_MAPPING_INIT);
        _strategy.initialize();
    }

    protected void clearDefinedFieldCache() {
        // just make this method available to other classes in this package
        super.clearDefinedFieldCache();
    }

    protected void clearSubclassCache() {
        super.clearSubclassCache();
        _joinSubMaps = null;
        _assignMaps = null;
    }

    public void copy(ClassMetaData cls) {
        super.copy(cls);
        if (_subclassMode == Integer.MAX_VALUE)
            _subclassMode = ((ClassMapping) cls).getSubclassFetchMode();
    }

    protected boolean validateDataStoreExtensionPrefix(String prefix) {
        return "jdbc-".equals(prefix);
    }

    ////////////////////////////////
    // ClassStrategy implementation
    ////////////////////////////////

    public String getAlias() {
        return assertStrategy().getAlias();
    }

    public void map(boolean adapt) {
        assertStrategy().map(adapt);
    }

    public void initialize() {
        assertStrategy().initialize();
    }

    public void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        assertStrategy().insert(sm, store, rm);
    }

    public void update(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        assertStrategy().update(sm, store, rm);
    }

    public void delete(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
        assertStrategy().delete(sm, store, rm);
    }

    public Boolean isCustomInsert(OpenJPAStateManager sm, JDBCStore store) {
        return assertStrategy().isCustomInsert(sm, store);
    }

    public Boolean isCustomUpdate(OpenJPAStateManager sm, JDBCStore store) {
        return assertStrategy().isCustomUpdate(sm, store);
    }

    public Boolean isCustomDelete(OpenJPAStateManager sm, JDBCStore store) {
        return assertStrategy().isCustomDelete(sm, store);
    }

    public void customInsert(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
        assertStrategy().customInsert(sm, store);
    }

    public void customUpdate(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
        assertStrategy().customUpdate(sm, store);
    }

    public void customDelete(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
        assertStrategy().customDelete(sm, store);
    }

    public void setClassMapping(ClassMapping owner) {
        assertStrategy().setClassMapping(owner);
    }

    public boolean isPrimaryKeyObjectId(boolean hasAll) {
        return assertStrategy().isPrimaryKeyObjectId(hasAll);
    }

    public Joins joinSuperclass(Joins joins, boolean toThis) {
        return assertStrategy().joinSuperclass(joins, toThis);
    }

    public boolean supportsEagerSelect(Select sel, OpenJPAStateManager sm,
        JDBCStore store, ClassMapping base, JDBCFetchConfiguration fetch) {
        return assertStrategy().supportsEagerSelect(sel, sm, store, base,
            fetch);
    }

    public ResultObjectProvider customLoad(JDBCStore store, boolean subclasses,
        JDBCFetchConfiguration fetch, long startIdx, long endIdx)
        throws SQLException {
        return assertStrategy().customLoad(store, subclasses, fetch,
            startIdx, endIdx);
    }

    public boolean customLoad(OpenJPAStateManager sm, JDBCStore store,
        PCState state, JDBCFetchConfiguration fetch)
        throws SQLException, ClassNotFoundException {
        return assertStrategy().customLoad(sm, store, state, fetch);
    }

    public boolean customLoad(OpenJPAStateManager sm, JDBCStore store,
        JDBCFetchConfiguration fetch, Result result)
        throws SQLException {
        return assertStrategy().customLoad(sm, store, fetch, result);
    }

    private ClassStrategy assertStrategy() {
        if (_strategy == null)
            throw new InternalException();
        return _strategy;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8991.java