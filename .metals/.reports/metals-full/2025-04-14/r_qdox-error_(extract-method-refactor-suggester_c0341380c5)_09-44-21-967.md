error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13733.java
text:
```scala
L@@ist<Column> cols = new ArrayList<Column>();

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
package org.apache.openjpa.persistence.jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.ColumnResult;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FieldResult;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyJoinColumns;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.kernel.EagerFetchModes;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.ClassMappingInfo;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.FieldMappingInfo;
import org.apache.openjpa.jdbc.meta.MappingInfo;
import org.apache.openjpa.jdbc.meta.MappingRepository;
import org.apache.openjpa.jdbc.meta.QueryResultMapping;
import org.apache.openjpa.jdbc.meta.SequenceMapping;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.ValueMappingInfo;
import org.apache.openjpa.jdbc.meta.strats.EnumValueHandler;
import org.apache.openjpa.jdbc.meta.strats.FlatClassStrategy;
import org.apache.openjpa.jdbc.meta.strats.FullClassStrategy;
import org.apache.openjpa.jdbc.meta.strats.VerticalClassStrategy;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Schemas;
import org.apache.openjpa.jdbc.schema.Unique;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.meta.MetaDataContext;
import org.apache.openjpa.persistence.AnnotationPersistenceMetaDataParser;
import static org.apache.openjpa.persistence.jdbc.MappingTag.*;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.MetaDataException;
import org.apache.openjpa.util.UnsupportedException;
import org.apache.openjpa.util.UserException;

/**
 * Persistence annotation mapping parser.
 *
 * @author Pinaki Poddar
 * @author Steve Kim
 * @author Abe White
 * @nojavadoc
 */
public class AnnotationPersistenceMappingParser
    extends AnnotationPersistenceMetaDataParser {

    protected static final int TRUE = 1;
    protected static final int FALSE = 2;

    private static final Localizer _loc = Localizer.forPackage
        (AnnotationPersistenceMappingParser.class);

    private static final Map<Class, MappingTag> _tags =
        new HashMap<Class, MappingTag>();

    static {
        _tags.put(AssociationOverride.class, ASSOC_OVERRIDE);
        _tags.put(AssociationOverrides.class, ASSOC_OVERRIDES);
        _tags.put(AttributeOverride.class, ATTR_OVERRIDE);
        _tags.put(AttributeOverrides.class, ATTR_OVERRIDES);
        _tags.put(javax.persistence.Column.class, COL);
        _tags.put(ColumnResult.class, COLUMN_RESULT);
        _tags.put(DiscriminatorColumn.class, DISCRIM_COL);
        _tags.put(DiscriminatorValue.class, DISCRIM_VAL);
        _tags.put(ElementColumn.class, ELEM_COL);
        _tags.put(ElementColumns.class, ELEM_COLS);
        _tags.put(ElementEmbeddedMapping.class, ELEM_EMBEDDED_MAPPING);
        _tags.put(ElementStrategy.class, ELEM_STRAT);
        _tags.put(EntityResult.class, ENTITY_RESULT);
        _tags.put(Enumerated.class, ENUMERATED);
        _tags.put(FieldResult.class, FIELD_RESULT);
        _tags.put(Inheritance.class, INHERITANCE);
        _tags.put(JoinColumn.class, JOIN_COL);
        _tags.put(JoinColumns.class, JOIN_COLS);
        _tags.put(JoinTable.class, JOIN_TABLE);
        _tags.put(KeyColumn.class, KEY_COL);
        _tags.put(KeyColumns.class, KEY_COLS);
        _tags.put(KeyClassCriteria.class, KEY_CLASS_CRIT);
        _tags.put(KeyEmbeddedMapping.class, KEY_EMBEDDED_MAPPING);
        _tags.put(KeyForeignKey.class, KEY_FK);
        _tags.put(KeyIndex.class, KEY_INDEX);
        _tags.put(KeyJoinColumn.class, KEY_JOIN_COL);
        _tags.put(KeyJoinColumns.class, KEY_JOIN_COLS);
        _tags.put(KeyNonpolymorphic.class, KEY_NONPOLY);
        _tags.put(KeyStrategy.class, KEY_STRAT);
        _tags.put(MapKeyColumn.class, MAP_KEY_COL);
        _tags.put(MapKeyJoinColumn.class, MAP_KEY_JOIN_COL);
        _tags.put(MapKeyJoinColumns.class, MAP_KEY_JOIN_COLS);
        _tags.put(PrimaryKeyJoinColumn.class, PK_JOIN_COL);
        _tags.put(PrimaryKeyJoinColumns.class, PK_JOIN_COLS);
        _tags.put(SecondaryTable.class, SECONDARY_TABLE);
        _tags.put(SecondaryTables.class, SECONDARY_TABLES);
        _tags.put(SqlResultSetMapping.class, SQL_RESULT_SET_MAPPING);
        _tags.put(SqlResultSetMappings.class, SQL_RESULT_SET_MAPPINGS);
        _tags.put(Table.class, TABLE);
        _tags.put(Temporal.class, TEMPORAL);
        _tags.put(TableGenerator.class, TABLE_GEN);
        _tags.put(ClassCriteria.class, CLASS_CRIT);
        _tags.put(Columns.class, COLS);
        _tags.put(ContainerTable.class, CONTAINER_TABLE);
        _tags.put(CollectionTable.class, COLLECTION_TABLE);
        _tags.put(DataStoreIdColumn.class, DATASTORE_ID_COL);
        _tags.put(DiscriminatorStrategy.class, DISCRIM_STRAT);
        _tags.put(EagerFetchMode.class, EAGER_FETCH_MODE);
        _tags.put(ElementClassCriteria.class, ELEM_CLASS_CRIT);
        _tags.put(ElementForeignKey.class, ELEM_FK);
        _tags.put(ElementIndex.class, ELEM_INDEX);
        _tags.put(ElementJoinColumn.class, ELEM_JOIN_COL);
        _tags.put(ElementJoinColumns.class, ELEM_JOIN_COLS);
        _tags.put(ElementNonpolymorphic.class, ELEM_NONPOLY);
        _tags.put(EmbeddedMapping.class, EMBEDDED_MAPPING);
        _tags.put(ForeignKey.class, FK);
        _tags.put(Index.class, INDEX);
        _tags.put(MappingOverride.class, MAPPING_OVERRIDE);
        _tags.put(MappingOverrides.class, MAPPING_OVERRIDES);
        _tags.put(Nonpolymorphic.class, NONPOLY);
        _tags.put(OrderColumn.class, ORDER_COL);
        _tags.put(javax.persistence.OrderColumn.class, ORDER_COLUMN);
        _tags.put(Strategy.class, STRAT);
        _tags.put(SubclassFetchMode.class, SUBCLASS_FETCH_MODE);
        _tags.put(Unique.class, UNIQUE);
        _tags.put(VersionColumn.class, VERSION_COL);
        _tags.put(VersionColumns.class, VERSION_COLS);
        _tags.put(VersionStrategy.class, VERSION_STRAT);
        _tags.put(XEmbeddedMapping.class, X_EMBEDDED_MAPPING);
        _tags.put(XJoinColumn.class, X_JOIN_COL);
        _tags.put(XJoinColumns.class, X_JOIN_COLS);
        _tags.put(XMappingOverride.class, X_MAPPING_OVERRIDE);
        _tags.put(XMappingOverrides.class, X_MAPPING_OVERRIDES);
        _tags.put(XSecondaryTable.class, X_SECONDARY_TABLE);
        _tags.put(XSecondaryTables.class, X_SECONDARY_TABLES);
        _tags.put(XTable.class, X_TABLE);
    }

    public AnnotationPersistenceMappingParser(JDBCConfiguration conf) {
        super(conf);
    }

    @Override
    protected void parsePackageMappingAnnotations(Package pkg) {
        MappingTag tag;
        for (Annotation anno : pkg.getDeclaredAnnotations()) {
            tag = _tags.get(anno.annotationType());
            if (tag == null) {
                handleUnknownPackageMappingAnnotation(pkg, anno);
                continue;
            }

            switch (tag) {
                case TABLE_GEN:
                    parseTableGenerator(pkg, (TableGenerator) anno);
                    break;
                default:
                    throw new UnsupportedException(_loc.get("unsupported", pkg,
                        anno.toString()));
            }
        }
    }

    /**
     * Allow subclasses to handle unknown annotations.
     */
    protected boolean handleUnknownPackageMappingAnnotation(Package pkg,
        Annotation anno) {
        return false;
    }

    /**
     * Parse @TableGenerator.
     */
    private void parseTableGenerator(AnnotatedElement el, TableGenerator gen) {
        String name = gen.name();
        if (StringUtils.isEmpty(name))
            throw new MetaDataException(_loc.get("no-gen-name", el));

        Log log = getLog();
        if (log.isTraceEnabled())
            log.trace(_loc.get("parse-gen", name));

        SequenceMapping meta = (SequenceMapping) getRepository().
            getCachedSequenceMetaData(name);
        if (meta != null) {
            if (log.isWarnEnabled())
                log.warn(_loc.get("dup-gen", name, el));
            return;
        }

        meta = (SequenceMapping) getRepository().addSequenceMetaData(name);
        meta.setSequencePlugin(SequenceMapping.IMPL_VALUE_TABLE);
        meta.setTable(toTableName(gen.schema(), gen.table()));
        meta.setPrimaryKeyColumn(gen.pkColumnName());
        meta.setSequenceColumn(gen.valueColumnName());
        meta.setPrimaryKeyValue(gen.pkColumnValue());
        meta.setInitialValue(gen.initialValue());
        meta.setAllocate(gen.allocationSize());
        meta.setSource(getSourceFile(), (el instanceof Class) ? el : null,
            meta.SRC_ANNOTATIONS);
        
        switch (gen.uniqueConstraints().length) {
        case 0: 
        	break; // nothing to do
        case 1: 
        	meta.setUniqueColumns(gen.uniqueConstraints()[0].columnNames());
        	break;
        default:
        	log.warn(_loc.get("unique-many-on-seq-unsupported", el, name));
        }
    }

    @Override
    protected void parseClassMappingAnnotations(ClassMetaData meta) {
        ClassMapping cm = (ClassMapping) meta;
        Class cls = cm.getDescribedType();

        MappingTag tag;
        for (Annotation anno : cls.getDeclaredAnnotations()) {
            tag = _tags.get(anno.annotationType());
            if (tag == null) {
                handleUnknownClassMappingAnnotation(cm, anno);
                continue;
            }

            switch (tag) {
                case ASSOC_OVERRIDE:
                    parseAssociationOverrides(cm, (AssociationOverride) anno);
                    break;
                case ASSOC_OVERRIDES:
                    parseAssociationOverrides(cm, ((AssociationOverrides) anno).
                        value());
                    break;
                case ATTR_OVERRIDE:
                    parseAttributeOverrides(cm, (AttributeOverride) anno);
                    break;
                case ATTR_OVERRIDES:
                    parseAttributeOverrides(cm, ((AttributeOverrides) anno).
                        value());
                    break;
                case DISCRIM_COL:
                    parseDiscriminatorColumn(cm, (DiscriminatorColumn) anno);
                    break;
                case DISCRIM_VAL:
                    cm.getDiscriminator().getMappingInfo().setValue
                        (((DiscriminatorValue) anno).value());
                    if (Modifier.isAbstract(cm.getDescribedType().
                            getModifiers()) && getLog().isInfoEnabled()) {
                        getLog().info(
                            _loc.get("discriminator-on-abstract-class", cm
                                    .getDescribedType().getName()));
                    }
                    break;
                case INHERITANCE:
                    parseInheritance(cm, (Inheritance) anno);
                    break;
                case PK_JOIN_COL:
                    parsePrimaryKeyJoinColumns(cm, (PrimaryKeyJoinColumn) anno);
                    break;
                case PK_JOIN_COLS:
                    parsePrimaryKeyJoinColumns(cm,
                        ((PrimaryKeyJoinColumns) anno).
                            value());
                    break;
                case SECONDARY_TABLE:
                    parseSecondaryTables(cm, (SecondaryTable) anno);
                    break;
                case SECONDARY_TABLES:
                    parseSecondaryTables(cm, ((SecondaryTables) anno).value());
                    break;
                case SQL_RESULT_SET_MAPPING:
                    parseSQLResultSetMappings(cm, (SqlResultSetMapping) anno);
                    break;
                case SQL_RESULT_SET_MAPPINGS:
                    parseSQLResultSetMappings(cm, ((SqlResultSetMappings) anno).
                        value());
                    break;
                case TABLE:
                    parseTable(cm, (Table) anno);
                    break;
                case TABLE_GEN:
                    parseTableGenerator(cls, (TableGenerator) anno);
                    break;
                case DATASTORE_ID_COL:
                    parseDataStoreIdColumn(cm, (DataStoreIdColumn) anno);
                    break;
                case DISCRIM_STRAT:
                    cm.getDiscriminator().getMappingInfo().setStrategy
                        (((DiscriminatorStrategy) anno).value());
                    break;
                case FK:
                    parseForeignKey(cm.getMappingInfo(), (ForeignKey) anno);
                    break;
                case MAPPING_OVERRIDE:
                    parseMappingOverrides(cm, (MappingOverride) anno);
                    break;
                case MAPPING_OVERRIDES:
                    parseMappingOverrides(cm,
                        ((MappingOverrides) anno).value());
                    break;
                case STRAT:
                    cm.getMappingInfo().setStrategy(((Strategy) anno).value());
                    break;
                case SUBCLASS_FETCH_MODE:
                    cm.setSubclassFetchMode(toEagerFetchModeConstant
                        (((SubclassFetchMode) anno).value()));
                    break;
                case VERSION_COL:
                    parseVersionColumns(cm, (VersionColumn) anno);
                    break;
                case VERSION_COLS:
                    parseVersionColumns(cm, ((VersionColumns) anno).value());
                    break;
                case VERSION_STRAT:
                    cm.getVersion().getMappingInfo().setStrategy
                        (((VersionStrategy) anno).value());
                    break;
                case X_MAPPING_OVERRIDE:
                    parseMappingOverrides(cm, (XMappingOverride) anno);
                    break;
                case X_MAPPING_OVERRIDES:
                    parseMappingOverrides(cm,
                        ((XMappingOverrides) anno).value());
                    break;
                case X_TABLE:
                case X_SECONDARY_TABLE:
                case X_SECONDARY_TABLES:
                    // no break; not supported yet
                default:
                    throw new UnsupportedException(_loc.get("unsupported", cm,
                        anno));
            }
        }
    }

    /**
     * Allow subclasses to handle unknown annotations.
     */
    protected boolean handleUnknownClassMappingAnnotation(ClassMapping cls,
        Annotation anno) {
        return false;
    }

    /**
     * Parse @AssociationOverride(s).
     */
    private void parseAssociationOverrides(ClassMapping cm,
        AssociationOverride... assocs) {
        FieldMapping sup;
        JoinColumn[] scols;
        int unique;
        List<Column> jcols;
        JoinTable joinTbl;
        for (AssociationOverride assoc : assocs) {
            if (StringUtils.isEmpty(assoc.name()))
                throw new MetaDataException(_loc.get("no-override-name", cm));
            sup = (FieldMapping) cm.getDefinedSuperclassField(assoc.name());
            if (sup == null)
                sup = (FieldMapping) cm.addDefinedSuperclassField
                    (assoc.name(), Object.class, Object.class);
            scols = assoc.joinColumns();
            joinTbl = assoc.joinTable();
            if ((scols == null || scols.length == 0) && joinTbl == null)
                //continue;
                throw new MetaDataException(_loc.get("embed-override-name",
                        sup, assoc.name()));

            if (scols != null && scols.length > 0) {
                jcols = new ArrayList<Column>(scols.length);
                unique = 0;
                for (JoinColumn scol : scols) {
                    unique |= (scol.unique()) ? TRUE : FALSE;
                    jcols.add(newColumn(scol));
                }
                setColumns(sup, sup.getValueInfo(), jcols, unique);
            } else if (joinTbl != null) {
                parseJoinTable(sup, joinTbl);
            }
        }
    }

    /**
     * Parse @AttributeOverride(s).
     */
    private void parseAttributeOverrides(ClassMapping cm,
        AttributeOverride... attrs) {
        FieldMapping sup;
        for (AttributeOverride attr : attrs) {
            if (StringUtils.isEmpty(attr.name()))
                throw new MetaDataException(_loc.get("no-override-name", cm));
            sup = (FieldMapping) cm.getDefinedSuperclassField(attr.name());
            if (sup == null)
                sup = (FieldMapping) cm.addDefinedSuperclassField(attr.name(),
                    Object.class, Object.class);
            if (attr.column() != null)
                parseColumns(sup, attr.column());
        }
    }

    /**
     * Parse inheritance @PrimaryKeyJoinColumn(s).
     */
    private void parsePrimaryKeyJoinColumns(ClassMapping cm,
        PrimaryKeyJoinColumn... joins) {
        List<Column> cols = new ArrayList<Column>(joins.length);
        for (PrimaryKeyJoinColumn join : joins)
            cols.add(newColumn(join));
        cm.getMappingInfo().setColumns(cols);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(PrimaryKeyJoinColumn join) {
        Column col = new Column();
        col.setFlag(Column.FLAG_PK_JOIN, true);
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setTypeName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        return col;
    }

    /**
     * Parse @SecondaryTable(s).
     */
    private void parseSecondaryTables(ClassMapping cm,
        SecondaryTable... tables) {
        ClassMappingInfo info = cm.getMappingInfo();
        Log log = getLog();

        String name;
        List<Column> joins = null;
        for (SecondaryTable table : tables) {
            name = table.name();
            if (StringUtils.isEmpty(name))
                throw new MetaDataException(_loc.get("second-name", cm));
            if (!StringUtils.isEmpty(table.schema()))
                name = table.schema() + "." + name;
            if (table.pkJoinColumns().length > 0) {
                joins = new ArrayList<Column>(table.pkJoinColumns().length);
                for (PrimaryKeyJoinColumn join : table.pkJoinColumns())
                    joins.add(newColumn(join));
                info.setSecondaryTableJoinColumns(name, joins);
            } else {
            	info.addSecondaryTable(name);
            }
            addUniqueConstraints(name, cm, info, table.uniqueConstraints());
        }
    }

    /**
     * Set class table.
     */
    private void parseTable(ClassMapping cm, Table table) {
        String tableName = toTableName(table.schema(), table.name());
        if (tableName != null)
            cm.getMappingInfo().setTableName(tableName);

        addUniqueConstraints(tableName, cm, cm.getMappingInfo(), 
        		table.uniqueConstraints());
    }
    
    Unique createUniqueConstraint(MetaDataContext ctx, UniqueConstraint anno) {
		String[] columnNames = anno.columnNames();
		if (columnNames == null || columnNames.length == 0)
            throw new UserException(_loc.get("unique-no-column", ctx));
		Unique uniqueConstraint = new Unique();
		for (int i=0; i<columnNames.length; i++) {
			if (StringUtils.isEmpty(columnNames[i]))
                throw new UserException(_loc.get("unique-empty-column",
                        Arrays.toString(columnNames), ctx));
			Column column = new Column();
			column.setName(columnNames[i]);
			uniqueConstraint.addColumn(column);
		}
		return uniqueConstraint;
    }
    
    void addUniqueConstraints(String table, MetaDataContext ctx, 
    		MappingInfo info, UniqueConstraint...uniqueConstraints) {
    	for (UniqueConstraint anno : uniqueConstraints) {
    		Unique unique = createUniqueConstraint(ctx, anno);
    		unique.setTableName(table);
    		if (info instanceof ClassMappingInfo)
    			((ClassMappingInfo)info).addUnique(table, unique);
    		else if (info instanceof FieldMappingInfo)
    			((FieldMappingInfo)info).addJoinTableUnique(unique);
    		else
    			throw new InternalException();
    	}
    }

    /**
     * Form a qualified table name from a schema and table name.
     */
    private static String toTableName(String schema, String table) {
        if (StringUtils.isEmpty(table))
            return null;
        if (StringUtils.isEmpty(schema))
            return table;
        return schema + "." + table;
    }

    /**
     * Parses the given annotation to create and cache a
     * {@link SQLResultSetMappingMetaData}.
     */
    private void parseSQLResultSetMappings(ClassMapping cm,
        SqlResultSetMapping... annos) {
        MappingRepository repos = (MappingRepository) getRepository();
        Log log = getLog();
        for (SqlResultSetMapping anno : annos) {
            if (log.isTraceEnabled())
                log.trace(_loc.get("parse-sqlrsmapping", anno.name()));

            QueryResultMapping result = repos.getCachedQueryResultMapping
                (null, anno.name());
            if (result != null) {
                if (log.isWarnEnabled())
                    log.warn(_loc.get("dup-sqlrsmapping", anno.name(), cm));
                continue;
            }

            result = repos.addQueryResultMapping(null, anno.name());
            result.setSource(getSourceFile(), cm.getDescribedType(),
                result.SRC_ANNOTATIONS);

            for (EntityResult entity : anno.entities()) {
                QueryResultMapping.PCResult entityResult = result.addPCResult
                    (entity.entityClass());
                if (!StringUtils.isEmpty(entity.discriminatorColumn()))
                    entityResult.addMapping(entityResult.DISCRIMINATOR,
                        entity.discriminatorColumn());

                for (FieldResult field : entity.fields())
                    entityResult.addMapping(field.name(), field.column());
            }
            for (ColumnResult column : anno.columns())
                result.addColumnResult(column.name());
        }
    }

    /**
     * Parse @DiscriminatorColumn.
     */
    private void parseDiscriminatorColumn(ClassMapping cm,
        DiscriminatorColumn dcol) {
        Column col = new Column();
        if (!StringUtils.isEmpty(dcol.name()))
            col.setName(dcol.name());
        if (!StringUtils.isEmpty(dcol.columnDefinition()))
            col.setTypeName(dcol.columnDefinition());
        Discriminator discrim = cm.getDiscriminator();
        switch (dcol.discriminatorType()) {
            case CHAR:
                col.setJavaType(JavaTypes.CHAR);
                discrim.setJavaType(JavaTypes.CHAR);
                break;
            case INTEGER:
                col.setJavaType(JavaTypes.INT);
                if (dcol.length() != 31)
                    col.setSize(dcol.length());
                discrim.setJavaType(JavaTypes.INT);
                break;
            default:
                col.setJavaType(JavaTypes.STRING);
                col.setSize(dcol.length());
                discrim.setJavaType(JavaTypes.STRING);
        }
        cm.getDiscriminator().getMappingInfo().setColumns
            (Arrays.asList(new Column[]{ col }));
    }

    /**
     * Parse @Inheritance.
     */
    private void parseInheritance(ClassMapping cm, Inheritance inherit) {
        ClassMappingInfo info = cm.getMappingInfo();
        switch (inherit.strategy()) {
            case SINGLE_TABLE:
                info.setHierarchyStrategy(FlatClassStrategy.ALIAS);
                break;
            case JOINED:
                info.setHierarchyStrategy(VerticalClassStrategy.ALIAS);
                break;
            case TABLE_PER_CLASS:
                info.setHierarchyStrategy(FullClassStrategy.ALIAS);
                break;
            default:
                throw new InternalException();
        }
    }

    /**
     * Parse class-level @MappingOverride(s).
     */
    private void parseMappingOverrides(ClassMapping cm,
        MappingOverride... overs) {
        FieldMapping sup;
        for (MappingOverride over : overs) {
            if (StringUtils.isEmpty(over.name()))
                throw new MetaDataException(_loc.get("no-override-name", cm));
            sup = (FieldMapping) cm.getDefinedSuperclassField(over.name());
            if (sup == null)
                sup = (FieldMapping) cm.addDefinedSuperclassField(over.name(),
                    Object.class, Object.class);
            populate(sup, over);
        }
    }

    /**
     * Populate the given field from override data.
     */
    private void populate(FieldMapping fm, MappingOverride over) {
        if (over.containerTable().specified())
            parseContainerTable(fm, over.containerTable());
        parseColumns(fm, over.columns());
        parseXJoinColumns(fm, fm.getValueInfo(), true, over.joinColumns());
        parseElementJoinColumns(fm, over.elementJoinColumns());
    }

    /**
     * Parse datastore identity information in @DataStoreIdColumn.
     */
    private void parseDataStoreIdColumn(ClassMapping cm, DataStoreIdColumn id) {
        Column col = new Column();
        if (!StringUtils.isEmpty(id.name()))
            col.setName(id.name());
        if (!StringUtils.isEmpty(id.columnDefinition()))
            col.setTypeName(id.columnDefinition());
        if (id.precision() != 0)
            col.setSize(id.precision());
        col.setFlag(Column.FLAG_UNINSERTABLE, !id.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !id.updatable());
        cm.getMappingInfo().setColumns(Arrays.asList(new Column[]{ col }));
    }

    /**
     * Parse the given foreign key.
     */
    private void parseForeignKey(MappingInfo info, ForeignKey fk) {
    	if (!fk.implicit()) {
    		parseForeignKey(info, fk.name(), fk.enabled(), fk.deferred(),
    				fk.deleteAction(), fk.updateAction());
    	} else {
            info.setImplicitRelation(true);
            assertDefault(fk);
    	}
    }

    /**
     * Set foreign key data on the given mapping info.
     */
    protected void parseForeignKey(MappingInfo info, String name,
        boolean enabled, boolean deferred, ForeignKeyAction deleteAction,
        ForeignKeyAction updateAction) {
        if (!enabled) {
            info.setCanForeignKey(false);
            return;
        }

        org.apache.openjpa.jdbc.schema.ForeignKey fk =
            new org.apache.openjpa.jdbc.schema.ForeignKey();
        if (!StringUtils.isEmpty(name))
            fk.setName(name);
        fk.setDeferred(deferred);
        fk.setDeleteAction(toForeignKeyAction(deleteAction));
        fk.setUpdateAction(toForeignKeyAction(updateAction));
        info.setForeignKey(fk);
    }
    
    void assertDefault(ForeignKey fk) {
    	boolean isDefault = StringUtils.isEmpty(fk.name()) 
    		&& fk.enabled() 
    		&& !fk.deferred() 
    		&& fk.deleteAction() == ForeignKeyAction.RESTRICT
    		&& fk.updateAction() == ForeignKeyAction.RESTRICT
    		&& fk.columnNames().length == 0
    		&& fk.specified();
    	if (!isDefault)
            throw new UserException(_loc.get("implicit-non-default-fk", _cls,
    				getSourceFile()).getMessage());
    }
    

    /**
     * Convert our FK action enum to an internal OpenJPA action.
     */
    private int toForeignKeyAction(ForeignKeyAction action) {
        switch (action) {
            case RESTRICT:
                return org.apache.openjpa.jdbc.schema.ForeignKey.
                        ACTION_RESTRICT;
            case CASCADE:
                return org.apache.openjpa.jdbc.schema.ForeignKey.ACTION_CASCADE;
            case NULL:
                return org.apache.openjpa.jdbc.schema.ForeignKey.ACTION_NULL;
            case DEFAULT:
                return org.apache.openjpa.jdbc.schema.ForeignKey.ACTION_DEFAULT;
            default:
                throw new InternalException();
        }
    }

    /**
     * Parse the given index.
     */
    private void parseIndex(MappingInfo info, Index idx) {
        parseIndex(info, idx.name(), idx.enabled(), idx.unique());
    }

    /**
     * Set index data on the given mapping info.
     */
    protected void parseIndex(MappingInfo info, String name,
        boolean enabled, boolean unique) {
        if (!enabled) {
            info.setCanIndex(false);
            return;
        }

        org.apache.openjpa.jdbc.schema.Index idx =
            new org.apache.openjpa.jdbc.schema.Index();
        if (!StringUtils.isEmpty(name))
            idx.setName(name);
        idx.setUnique(unique);
        info.setIndex(idx);
    }

    /**
     * Set unique data on the given mapping info.
     */
    private void parseUnique(FieldMapping fm, 
        org.apache.openjpa.persistence.jdbc.Unique anno) {
        ValueMappingInfo info = fm.getValueInfo();
        if (!anno.enabled()) {
            info.setCanUnique(false);
            return;
        }

        org.apache.openjpa.jdbc.schema.Unique unq = 
            new org.apache.openjpa.jdbc.schema.Unique();
        if (!StringUtils.isEmpty(anno.name()))
            unq.setName(anno.name());
        unq.setDeferred(anno.deferred());
        info.setUnique(unq);
    }

    /**
     * Parse @VersionColumn(s).
     */
    private void parseVersionColumns(ClassMapping cm, VersionColumn... vcols) {
        if (vcols.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(vcols.length);
        for (VersionColumn vcol : vcols)
            cols.add(newColumn(vcol));
        cm.getVersion().getMappingInfo().setColumns(cols);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(VersionColumn anno) {
        Column col = new Column();
        col.setTableName(anno.table());
        if (!StringUtils.isEmpty(anno.name()))
            col.setName(anno.name());
        if (anno.precision() != 0)
            col.setSize(anno.precision());
        else if (anno.length() != 255)
            col.setSize(anno.length());
        col.setNotNull(!anno.nullable());
        col.setDecimalDigits(anno.scale());
        if (!StringUtils.isEmpty(anno.columnDefinition())) {
            col.setTypeName(anno.columnDefinition());
            col.setType(Schemas.getJDBCType(col.getTypeName()));
            col.setJavaType(JavaTypes.getTypeCode(Schemas.getJavaType
            	(col.getType(), col.getSize(), col.getDecimalDigits())));
        }
        col.setFlag(Column.FLAG_UNINSERTABLE, !anno.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !anno.updatable());
        return col;
    }

    /**
     * Parse class-level @XMappingOverride(s).
     */
    private void parseMappingOverrides(ClassMapping cm,
        XMappingOverride... overs) {
        FieldMapping sup;
        for (XMappingOverride over : overs) {
            if (StringUtils.isEmpty(over.name()))
                throw new MetaDataException(_loc.get("no-override-name", cm));
            sup = (FieldMapping) cm.getDefinedSuperclassField(over.name());
            if (sup == null)
                sup = (FieldMapping) cm.addDefinedSuperclassField(over.name(),
                    Object.class, Object.class);
            populate(sup, over);
        }
    }

    /**
     * Populate the given field from override data.
     */
    private void populate(FieldMapping fm, XMappingOverride over) {
        if (over.containerTable().specified())
            parseContainerTable(fm, over.containerTable());
        parseColumns(fm, over.columns());
        parseXJoinColumns(fm, fm.getValueInfo(), true, over.joinColumns());
        parseElementColumns(fm, over.elementColumns());
        parseElementJoinColumns(fm, over.elementJoinColumns());
        parseKeyColumns(fm, over.keyColumns());
        parseKeyJoinColumns(fm, over.keyJoinColumns());
    }

    /**
     * Parse @ElementColumn(s).
     */
    private void parseElementColumns(FieldMapping fm, ElementColumn... pcols) {
        if (pcols.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(pcols.length);
        int unique = 0;
        for (int i = 0; i < pcols.length; i++) {
            cols.add(newColumn(pcols[i]));
            unique |= (pcols[i].unique()) ? TRUE : FALSE;
        }
        setColumns(fm, fm.getElementMapping().getValueInfo(), cols, unique);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(ElementColumn anno) {
        Column col = new Column();
        if (!StringUtils.isEmpty(anno.name()))
            col.setName(anno.name());
        if (!StringUtils.isEmpty(anno.columnDefinition()))
            col.setTypeName(anno.columnDefinition());
        if (anno.precision() != 0)
            col.setSize(anno.precision());
        else if (anno.length() != 255)
            col.setSize(anno.length());
        col.setNotNull(!anno.nullable());
        col.setDecimalDigits(anno.scale());
        col.setFlag(Column.FLAG_UNINSERTABLE, !anno.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !anno.updatable());
        return col;
    }

    /**
     * Parse @KeyJoinColumn(s).
     */
    private void parseKeyJoinColumns(FieldMapping fm, KeyJoinColumn... joins) {
        if (joins.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(joins.length);
        int unique = 0;
        for (int i = 0; i < joins.length; i++) {
            cols.add(newColumn(joins[i]));
            unique |= (joins[i].unique()) ? TRUE : FALSE;
        }
        setColumns(fm, fm.getKeyMapping().getValueInfo(), cols, unique);
    }

    /**
     *  Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(KeyJoinColumn join) {
        Column col = new Column();
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        if (!StringUtils.isEmpty(join.referencedAttributeName()))
            col.setTargetField(join.referencedAttributeName());
        col.setNotNull(!join.nullable());
        col.setFlag(Column.FLAG_UNINSERTABLE, !join.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !join.updatable ());
        return col;
    }
    
    /**
     * Translate the fetch mode enum value to the internal OpenJPA constant.
     */
    private static int toEagerFetchModeConstant(FetchMode mode) {
        switch (mode) {
            case NONE:
                return EagerFetchModes.EAGER_NONE;
            case JOIN:
                return EagerFetchModes.EAGER_JOIN;
            case PARALLEL:
                return EagerFetchModes.EAGER_PARALLEL;
            default:
                throw new InternalException();
        }
    }

    @Override
    protected void parseLobMapping(FieldMetaData fmd) {
        Column col = new Column();
        if (fmd.getDeclaredTypeCode() == JavaTypes.STRING
 fmd.getDeclaredType() == char[].class
 fmd.getDeclaredType() == Character[].class)
            col.setType(Types.CLOB);
        else
            col.setType(Types.BLOB);
        ((FieldMapping) fmd).getValueInfo().setColumns(Arrays.asList
            (new Column[]{ col }));
    }

    @Override
    protected void parseMemberMappingAnnotations(FieldMetaData fmd) {
        FieldMapping fm = (FieldMapping) fmd;
        AnnotatedElement el = (AnnotatedElement) getRepository().
            getMetaDataFactory().getDefaults().getBackingMember(fmd);

        MappingTag tag;
        for (Annotation anno : el.getDeclaredAnnotations()) {
            tag = _tags.get(anno.annotationType());
            if (tag == null) {
                handleUnknownMemberMappingAnnotation(fm, anno);
                continue;
            }

            switch (tag) {
                case ASSOC_OVERRIDE:
                    parseAssociationOverrides(fm, (AssociationOverride) anno);
                    break;
                case ASSOC_OVERRIDES:
                    parseAssociationOverrides(fm, ((AssociationOverrides) anno).
                        value());
                    break;
                case ATTR_OVERRIDE:
                    parseAttributeOverrides(fm, (AttributeOverride) anno);
                    break;
                case ATTR_OVERRIDES:
                    parseAttributeOverrides(fm, ((AttributeOverrides) anno).
                        value());
                    break;
                case COL:
                    parseColumns(fm, (javax.persistence.Column) anno);
                    break;
                case COLS:
                    parseColumns(fm, ((Columns) anno).value());
                    break;
                case ENUMERATED:
                    parseEnumerated(fm, (Enumerated) anno);
                    break;
                case JOIN_COL:
                    parseJoinColumns(fm, fm.getValueInfo(), true,
                        (JoinColumn) anno);
                    break;
                case JOIN_COLS:
                    parseJoinColumns(fm, fm.getValueInfo(), true,
                        ((JoinColumns) anno).value());
                    break;
                case JOIN_TABLE:
                    parseJoinTable(fm, (JoinTable) anno);
                    break;
                case KEY_CLASS_CRIT:
                    fm.getKeyMapping().getValueInfo().setUseClassCriteria
                        (((KeyClassCriteria) anno).value());
                    break;
                case KEY_COL:
                    parseKeyColumns(fm, (KeyColumn) anno);
                    break;
                case KEY_COLS:
                    parseKeyColumns(fm, ((KeyColumns) anno).value());
                    break;
                case KEY_EMBEDDED_MAPPING:
                    KeyEmbeddedMapping kembed = (KeyEmbeddedMapping) anno;
                    parseEmbeddedMapping(fm.getKeyMapping(),
                        kembed.nullIndicatorColumnName(),
                        kembed.nullIndicatorAttributeName(),
                        kembed.overrides());
                    break;
                case KEY_FK:
                    KeyForeignKey kfk = (KeyForeignKey) anno;
                    parseForeignKey(fm.getKeyMapping().getValueInfo(),
                        kfk.name(), kfk.enabled(), kfk.deferred(),
                        kfk.deleteAction(), kfk.updateAction());
                    break;
                case KEY_INDEX:
                    KeyIndex kidx = (KeyIndex) anno;
                    parseIndex(fm.getKeyMapping().getValueInfo(), kidx.name(),
                        kidx.enabled(), kidx.unique());
                    break;
                case KEY_JOIN_COL:
                    parseKeyJoinColumns(fm, (KeyJoinColumn) anno);
                    break;
                case KEY_JOIN_COLS:
                    parseKeyJoinColumns(fm, ((KeyJoinColumns) anno).value());
                    break;
                case KEY_NONPOLY:
                    fm.getKeyMapping().setPolymorphic(toPolymorphicConstant
                        (((KeyNonpolymorphic) anno).value()));
                    break;
                case KEY_STRAT:
                    fm.getKeyMapping().getValueInfo()
                        .setStrategy(((KeyStrategy) anno).value());
                    break;
                case MAP_KEY_COL:
                    parseMapKeyColumn(fm, (MapKeyColumn) anno);
                    break;
                case MAP_KEY_JOIN_COL:
                    parseMapKeyJoinColumns(fm, (MapKeyJoinColumn) anno);
                    break;
                case MAP_KEY_JOIN_COLS:
                    parseMapKeyJoinColumns(fm,
                        ((MapKeyJoinColumns) anno).value());
                    break;
                case PK_JOIN_COL:
                    parsePrimaryKeyJoinColumns(fm, (PrimaryKeyJoinColumn) anno);
                    break;
                case PK_JOIN_COLS:
                    parsePrimaryKeyJoinColumns(fm,
                        ((PrimaryKeyJoinColumns) anno).
                            value());
                    break;
                case TABLE_GEN:
                    parseTableGenerator(el, (TableGenerator) anno);
                    break;
                case TEMPORAL:
                    parseTemporal(fm, (Temporal) anno);
                    break;
                case CLASS_CRIT:
                    fm.getValueInfo().setUseClassCriteria
                        (((ClassCriteria) anno).value());
                    break;
                case CONTAINER_TABLE:
                    parseContainerTable(fm, (ContainerTable) anno);
                    break;
                case COLLECTION_TABLE:
                    parseCollectionTable(fm, (CollectionTable) anno);
                    break;
                case EAGER_FETCH_MODE:
                    fm.setEagerFetchMode(toEagerFetchModeConstant
                        (((EagerFetchMode) anno).value()));
                    break;
                case ELEM_CLASS_CRIT:
                    fm.getElementMapping().getValueInfo().setUseClassCriteria
                        (((ElementClassCriteria) anno).value());
                    break;
                case ELEM_COL:
                    parseElementColumns(fm, (ElementColumn) anno);
                    break;
                case ELEM_COLS:
                    parseElementColumns(fm, ((ElementColumns) anno).value());
                    break;
                case ELEM_EMBEDDED_MAPPING:
                    ElementEmbeddedMapping ee = (ElementEmbeddedMapping) anno;
                    parseEmbeddedMapping(fm.getElementMapping(),
                        ee.nullIndicatorAttributeName(),
                        ee.nullIndicatorColumnName(),
                        ee.overrides());
                    break;
                case ELEM_FK:
                    ElementForeignKey efk = (ElementForeignKey) anno;
                    parseForeignKey(fm.getElementMapping().getValueInfo(),
                        efk.name(), efk.enabled(), efk.deferred(),
                        efk.deleteAction(), efk.updateAction());
                    break;
                case ELEM_INDEX:
                    ElementIndex eidx = (ElementIndex) anno;
                    parseIndex(fm.getElementMapping().getValueInfo(),
                        eidx.name(), eidx.enabled(), eidx.unique());
                    break;
                case ELEM_JOIN_COL:
                    parseElementJoinColumns(fm, (ElementJoinColumn) anno);
                    break;
                case ELEM_JOIN_COLS:
                    parseElementJoinColumns(fm, ((ElementJoinColumns) anno).
                        value());
                    break;
                case ELEM_NONPOLY:
                    fm.getElementMapping().setPolymorphic(toPolymorphicConstant
                        (((ElementNonpolymorphic) anno).value()));
                    break;
                case ELEM_STRAT:
                    fm.getElementMapping().getValueInfo()
                        .setStrategy(((ElementStrategy) anno).value());
                    break;
                case EMBEDDED_MAPPING:
                    parseEmbeddedMapping(fm, (EmbeddedMapping) anno);
                    break;
                case FK:
                    parseForeignKey(fm.getValueInfo(), (ForeignKey) anno);
                    break;
                case INDEX:
                    parseIndex(fm.getValueInfo(), (Index) anno);
                    break;
                case NONPOLY:
                    fm.setPolymorphic(toPolymorphicConstant
                        (((Nonpolymorphic) anno).value()));
                    break;
                case ORDER_COLUMN:
                    parseJavaxOrderColumn(fm, 
                        (javax.persistence.OrderColumn)anno);
                    break;
                case ORDER_COL:
                    parseOrderColumn(fm, (OrderColumn) anno);
                    break;
                case STRAT:
                    fm.getValueInfo().setStrategy(((Strategy) anno).value());
                    break;
                case UNIQUE:
                    parseUnique(fm,
                        (org.apache.openjpa.persistence.jdbc.Unique) anno);
                    break;
                case X_EMBEDDED_MAPPING:
                    XEmbeddedMapping embed = (XEmbeddedMapping) anno;
                    parseEmbeddedMapping(fm, embed.nullIndicatorColumnName(),
                        embed.nullIndicatorAttributeName(), embed.overrides());
                    break;
                case X_JOIN_COL:
                    parseXJoinColumns(fm, fm.getValueInfo(), true,
                        (XJoinColumn) anno);
                    break;
                case X_JOIN_COLS:
                    parseXJoinColumns(fm, fm.getValueInfo(), true,
                        ((XJoinColumns) anno).value());
                    break;
                default:
                    throw new UnsupportedException(_loc.get("unsupported", fm,
                        anno.toString()));
            }
        }
    }

    /**
     * Allow subclasses to handle unknown annotations.
     */
    protected boolean handleUnknownMemberMappingAnnotation(FieldMapping fm,
        Annotation anno) {
        return false;
    }

    /**
     * Return the {@link ValueMapping} <code>POLY_*</code> constant for
     * the given enum value.
     */
    protected static int toPolymorphicConstant(NonpolymorphicType val) {
        switch (val) {
            case EXACT:
                return ValueMapping.POLY_FALSE;
            case JOINABLE:
                return ValueMapping.POLY_JOINABLE;
            case FALSE:
                return ValueMapping.POLY_TRUE;
            default:
                throw new InternalException();
        }
    }

    /**
     * Parse given @AssociationOverride annotations on an embedded mapping.
     */
    private void parseAssociationOverrides(FieldMapping fm,
        AssociationOverride... assocs) {

        FieldMapping efm;
        JoinColumn[] ecols;
        int unique;
        List<Column> jcols;
        JoinTable joinTbl;
        for (AssociationOverride assoc : assocs) {
            efm = getEmbeddedFieldMapping(fm, assoc.name());
            if (efm == null)
                throw new MetaDataException(_loc.get("embed-override-name",
                    fm, assoc.name()));
            ecols = assoc.joinColumns();
            joinTbl = assoc.joinTable();
            if ((ecols == null || ecols.length == 0) && joinTbl == null)
                throw new MetaDataException(_loc.get("embed-override-name",
                    fm, assoc.name()));
            if (ecols != null && ecols.length > 0) {
                unique = 0;
                jcols = new ArrayList<Column>(ecols.length);
                for (JoinColumn ecol : ecols) {
                    unique |= (ecol.unique()) ? TRUE : FALSE;
                    jcols.add(newColumn(ecol));
                }
                setColumns(efm, efm.getValueInfo(), jcols, unique);
            } else if (joinTbl != null) {
                parseJoinTable(efm, joinTbl);
            }
        }
    }

    /**
     * Parse given @AttributeOverride annotations on an embedded mapping.
     */
    private void parseAttributeOverrides(FieldMapping fm,
        AttributeOverride... attrs) {
        for (AttributeOverride attr : attrs) {
            String attrName = attr.name();
            FieldMapping efm = getEmbeddedFieldMapping(fm, attrName);
            if (attr.column() != null)
                parseColumns(efm, attr.column());
        }
    }
    
    public static FieldMapping getEmbeddedFieldMapping(FieldMapping fm,
            String attrName) {
        ClassMapping embed = null;
        boolean isKey = false;
        boolean isValue = false;
        if (attrName != null && attrName.startsWith("key."))
            isKey = true;
        else if (attrName != null && attrName.startsWith("value."))
            isValue = true;
        if (isKey || isValue)
            attrName = attrName.substring(attrName.indexOf(".")+1);
            
        int typeCode = fm.getValue().getDeclaredTypeCode();
        switch (typeCode) {
            case JavaTypes.COLLECTION : // a collection of embeddables
                if (isKey || isValue)
                    throw new MetaDataException(_loc.get("embed-override-name",
                        fm, attrName));
                embed = fm.getElementMapping().getEmbeddedMapping();
                break;
            case JavaTypes.MAP: // a map
                if (!isKey && !isValue)
                    throw new MetaDataException(_loc.get("embed-override-name",
                        fm, attrName));
                if (isKey) 
                    embed = getEmbeddedMapping(fm.getKeyMapping());
                else if (isValue)     
                    embed = getEmbeddedMapping(fm.getElementMapping());
                break;
            default: // an embeddable
                if (isKey || isValue)
                    throw new MetaDataException(_loc.get("embed-override-name",
                        fm, attrName));
                embed = getEmbeddedMapping(fm.getValueMapping());
                break;
        }
        
        if (embed == null) 
            throw new MetaDataException(_loc.get("not-embedded", fm));
        return getAttributeOverrideField(attrName, fm, embed);
    }
    
    public static ClassMapping getEmbeddedMapping(ValueMapping val) {
        ClassMapping embed = val.getEmbeddedMapping();
        if (embed != null) 
            return embed;
        
        val.addEmbeddedMetaData();
        return val.getEmbeddedMapping();

    }
    
    public static FieldMapping getAttributeOverrideField(String attrName,
            FieldMapping fm, ClassMapping embed) {
        FieldMapping efm;
        int idxOfDot = attrName.indexOf("."); 
        if (idxOfDot == -1) {
            efm = embed.getFieldMapping(attrName);
            if (efm == null)
                throw new MetaDataException(_loc.get("embed-override-name",
                    fm, attrName));
            return efm;
        } 
        String attrName1 = attrName.substring(0, idxOfDot);
        String attrName2 = attrName.substring(idxOfDot+1);
        efm = embed.getFieldMapping(attrName1);
        if (efm == null)
            throw new MetaDataException(_loc.get("embed-override-name",
                fm, attrName1));
        ClassMapping embed1 = getEmbeddedMapping(efm.getValueMapping());
        return getAttributeOverrideField(attrName2, efm, embed1);
    }
    
    /**
     * Parse @Enumerated.
     */
    private void parseEnumerated(FieldMapping fm, Enumerated anno) {
        String strat = EnumValueHandler.class.getName() + "(StoreOrdinal="
            + String.valueOf(anno.value() == EnumType.ORDINAL) + ")";
        fm.getValueInfo().setStrategy(strat);
    }

    /**
     * Parse @Temporal.
     */
    private void parseTemporal(FieldMapping fm, Temporal anno) {
        List cols = fm.getValueInfo().getColumns();
        if (!cols.isEmpty() && cols.size() != 1)
            throw new MetaDataException(_loc.get("num-cols-mismatch", fm,
                String.valueOf(cols.size()), "1"));
        if (cols.isEmpty()) {
            cols = Arrays.asList(new Column[]{ new Column() });
            fm.getValueInfo().setColumns(cols);
        }

        Column col = (Column) cols.get(0);
        switch (anno.value()) {
            case DATE:
                col.setType(Types.DATE);
                break;
            case TIME:
                col.setType(Types.TIME);
                break;
            case TIMESTAMP:
                col.setType(Types.TIMESTAMP);
                break;
        }
    }

    /**
     * Parse @Column(s).
     */
    protected void parseColumns(FieldMapping fm,
        javax.persistence.Column... pcols) {
        if (pcols.length == 0)
            return;

        // might already have some column information from mapping annotation
        List cols = fm.getValueInfo().getColumns();
        if (!cols.isEmpty() && cols.size() != pcols.length)
            throw new MetaDataException(_loc.get("num-cols-mismatch", fm,
                String.valueOf(cols.size()), String.valueOf(pcols.length)));

        // cache the JAXB XmlType class if it is present so we do not
        // have a hard-wired dependency on JAXB here
        Class xmlTypeClass = null;
        try {
            xmlTypeClass = Class.forName("javax.xml.bind.annotation.XmlType");
        } catch (Exception e) {
        }

        int unique = 0;
        String secondary = null;
        for (int i = 0; i < pcols.length; i++) {
            if (cols.size() > i)
                setupColumn((Column) cols.get(i), pcols[i]);
            else {
                if (cols.isEmpty())
                    cols = new ArrayList<Column>(pcols.length);
                cols.add(newColumn(pcols[i]));
            }
            
            if (xmlTypeClass != null
                && StringUtils.isEmpty(pcols[i].columnDefinition())
                && (AccessController.doPrivileged(J2DoPrivHelper
                    .isAnnotationPresentAction(fm.getDeclaredType(),
                        xmlTypeClass))).booleanValue()) {
                DBDictionary dict = ((MappingRepository) getRepository())
                    .getDBDictionary();
                if (dict.supportsXMLColumn)
                    // column maps to xml type
                    ((Column) cols.get(i)).setTypeName(dict.xmlTypeName);
            }

            unique |= (pcols[i].unique()) ? TRUE : FALSE;
            secondary = trackSecondaryTable(fm, secondary, pcols[i].table(), i);
        }

        if (fm.isElementCollection())
            setColumns(fm, fm.getElementMapping().getValueInfo(), cols, unique);
        else
            setColumns(fm, fm.getValueInfo(), cols, unique);
        if (secondary != null)
            fm.getMappingInfo().setTableName(secondary);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(javax.persistence.Column anno) {
        Column col = new Column();
        setupColumn(col, anno);
        return col;
    }

    /**
     * Setup the given column with information from the given annotation.
     */
    private static void setupColumn(Column col, javax.persistence.Column anno) {
        if (!StringUtils.isEmpty(anno.name()))
            col.setName(anno.name());
        if (!StringUtils.isEmpty(anno.columnDefinition()))
            col.setTypeName(anno.columnDefinition());
        if (anno.precision() != 0)
            col.setSize(anno.precision());
        else if (anno.length() != 255)
            col.setSize(anno.length());
        col.setNotNull(!anno.nullable());
        col.setDecimalDigits(anno.scale());
        col.setFlag(Column.FLAG_UNINSERTABLE, !anno.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !anno.updatable());
    }

    /**
     * Set the given columns as the columns for <code>fm</code>.
     *
     * @param unique bitwise combination of TRUE and FALSE for the
     * unique attribute of each column
     */
    protected void setColumns(FieldMapping fm, MappingInfo info,
        List<Column> cols, int unique) {
        info.setColumns(cols);
        if (unique == TRUE)
            info.setUnique(new org.apache.openjpa.jdbc.schema.Unique());

        //### EJB3
        Log log = getLog();
        if (log.isWarnEnabled() && unique == (TRUE | FALSE))
            log.warn(_loc.get("inconsist-col-attrs", fm));
    }

    /**
     * Helper to track the secondary table for a set of columns.
     *
     * @param secondary secondary table for last column
     * @param colSecondary secondary table for current column
     * @return secondary table for field
     */
    private String trackSecondaryTable(FieldMapping fm, String secondary,
        String colSecondary, int col) {
        if (StringUtils.isEmpty(colSecondary))
            colSecondary = null;
        if (col == 0)
            return colSecondary;
        if (!StringUtils.equalsIgnoreCase(secondary, colSecondary))
            throw new MetaDataException(_loc.get("second-inconsist", fm));
        return secondary;
    }

    /**
     * Parse @JoinTable.
     */
    private void parseJoinTable(FieldMapping fm, JoinTable join) {
    	FieldMappingInfo info = fm.getMappingInfo();
        info.setTableName(toTableName(join.schema(), join.name()));
        parseJoinColumns(fm, info, false, join.joinColumns());
        parseJoinColumns(fm, fm.getElementMapping().getValueInfo(), false,
            join.inverseJoinColumns());
        addUniqueConstraints(info.getTableName(), fm, info,  
            join.uniqueConstraints());
    }

    /**
     * Parse given @JoinColumn annotations.
     */
    private void parseJoinColumns(FieldMapping fm, MappingInfo info,
        boolean secondaryAllowed, JoinColumn... joins) {
        if (joins.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(joins.length);
        int unique = 0;
        String secondary = null;
        for (int i = 0; i < joins.length; i++) {
            cols.add(newColumn(joins[i]));
            unique |= (joins[i].unique()) ? TRUE : FALSE;
            secondary = trackSecondaryTable(fm, secondary,
                joins[i].table(), i);
            if (!secondaryAllowed && secondary != null)
                throw new MetaDataException(_loc.get("bad-second", fm));
        }

        setColumns(fm, info, cols, unique);
        if (secondary != null)
            fm.getMappingInfo().setTableName(secondary);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(JoinColumn join) {
        Column col = new Column();
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setTypeName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        col.setNotNull(!join.nullable());
        col.setFlag(Column.FLAG_UNINSERTABLE, !join.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !join.updatable());
        return col;
    }

    /**
     * Parse @KeyColumn(s).
     */
    private void parseKeyColumns(FieldMapping fm, KeyColumn... pcols) {
        if (pcols.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(pcols.length);
        int unique = 0;
        for (int i = 0; i < pcols.length; i++) {
            cols.add(newColumn(pcols[i]));
            unique |= (pcols[i].unique()) ? TRUE : FALSE;
        }
        setColumns(fm, fm.getKeyMapping().getValueInfo(), cols, unique);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(KeyColumn anno) {
        Column col = new Column();
        if (!StringUtils.isEmpty(anno.name()))
            col.setName(anno.name());
        if (!StringUtils.isEmpty(anno.columnDefinition()))
            col.setTypeName(anno.columnDefinition());
        if (anno.precision() != 0)
            col.setSize(anno.precision());
        else if (anno.length() != 255)
            col.setSize(anno.length());
        col.setNotNull(!anno.nullable());
        col.setDecimalDigits(anno.scale());
        col.setFlag(Column.FLAG_UNINSERTABLE, !anno.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !anno.updatable());
        return col;
    }

    /**
     * Parse given @PrimaryKeyJoinColumn annotations.
     */
    private void parsePrimaryKeyJoinColumns(FieldMapping fm,
        PrimaryKeyJoinColumn... joins) {
        List<Column> cols = new ArrayList<Column>(joins.length);
        for (PrimaryKeyJoinColumn join : joins)
            cols.add(newColumn(join));
        setColumns(fm, fm.getValueInfo(), cols, 0);
    }

    /**
     * Parse given @XJoinColumn annotations.
     */
    protected void parseXJoinColumns(FieldMapping fm, MappingInfo info,
        boolean secondaryAllowed, XJoinColumn... joins) {
        if (joins.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(joins.length);
        int unique = 0;
        String secondary = null;
        for (int i = 0; i < joins.length; i++) {
            cols.add(newColumn(joins[i]));
            unique |= (joins[i].unique()) ? TRUE : FALSE;
            secondary = trackSecondaryTable(fm, secondary,
                joins[i].table(), i);
            if (!secondaryAllowed && secondary != null)
                throw new MetaDataException(_loc.get("bad-second", fm));
        }

        setColumns(fm, info, cols, unique);
        if (secondary != null)
            fm.getMappingInfo().setTableName(secondary);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(XJoinColumn join) {
        Column col = new Column();
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setTypeName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        if (!StringUtils.isEmpty(join.referencedAttributeName()))
            col.setTargetField(join.referencedAttributeName());
        col.setNotNull(!join.nullable());
        col.setFlag(Column.FLAG_UNINSERTABLE, !join.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !join.updatable());
        return col;
    }

    /**
     * Parse embedded info for the given mapping.
     */
    private void parseEmbeddedMapping(FieldMapping fm, EmbeddedMapping anno) {
        ClassMapping embed = fm.getEmbeddedMapping();
        if (embed == null)
            throw new MetaDataException(_loc.get("not-embedded", fm));

        FieldMapping efm;
        for (MappingOverride over : anno.overrides()) {
            efm = embed.getFieldMapping(over.name());
            if (efm == null)
                throw new MetaDataException(_loc.get("embed-override-name",
                    fm, over.name()));
            populate(efm, over);
        }

        String nullInd = null;
        if (!StringUtils.isEmpty(anno.nullIndicatorAttributeName()))
            nullInd = anno.nullIndicatorAttributeName();
        else if (!StringUtils.isEmpty(anno.nullIndicatorColumnName()))
            nullInd = anno.nullIndicatorColumnName();
        if (nullInd == null)
            return;

        ValueMappingInfo info = fm.getValueInfo();
        populateNullIndicator(nullInd, info);
    }

    /**
     * Parse embedded info for the given mapping.
     */
    private void parseEmbeddedMapping(ValueMapping vm, 
        String nullIndicatorAttribute, String nullIndicatorColumn,
        XMappingOverride[] overrides) {
        ClassMapping embed = vm.getEmbeddedMapping();
        if (embed == null)
            throw new MetaDataException(_loc.get("not-embedded", vm));

        FieldMapping efm;
        for (XMappingOverride over : overrides) {
            efm = embed.getFieldMapping(over.name());
            if (efm == null)
                throw new MetaDataException(_loc.get("embed-override-name",
                    vm, over.name()));
            populate(efm, over);
        }

        String nullInd = null;
        if (!StringUtils.isEmpty(nullIndicatorAttribute))
            nullInd = nullIndicatorAttribute;
        else if (!StringUtils.isEmpty(nullIndicatorColumn))
            nullInd = nullIndicatorColumn;
        if (nullInd == null)
            return;

        ValueMappingInfo info = vm.getValueInfo();
        populateNullIndicator(nullInd, info);
    }

    private void populateNullIndicator(String nullInd, ValueMappingInfo info) {
        if ("false".equals(nullInd))
            info.setCanIndicateNull(false);
        else {
            Column col = new Column();
            if (!"true".equals(nullInd))
                col.setName(nullInd);
            info.setColumns(Arrays.asList(new Column[]{ col }));
        }
    }

    /**
     * Parse @ContainerTable.
     */
    protected void parseContainerTable(FieldMapping fm, ContainerTable ctbl) {
        fm.getMappingInfo().setTableName(toTableName(ctbl.schema(),
            ctbl.name()));
        parseXJoinColumns(fm, fm.getMappingInfo(), false, ctbl.joinColumns());
        if (ctbl.joinForeignKey().specified())
            parseForeignKey(fm.getMappingInfo(), ctbl.joinForeignKey());
        if (ctbl.joinIndex().specified())
            parseIndex(fm.getMappingInfo(), ctbl.joinIndex());
    }

    /**
     * Parse @CollectionTable.
     */
    protected void parseCollectionTable(FieldMapping fm, CollectionTable ctbl) {
        FieldMappingInfo info = fm.getMappingInfo(); 
        info.setTableName(toTableName(ctbl.schema(),
            ctbl.name()));
        //ctbl.catalog()
        parseJoinColumns(fm, fm.getMappingInfo(), false, ctbl.joinColumns());
        addUniqueConstraints(info.getTableName(), fm.getDefiningMetaData(), 
            info, ctbl.uniqueConstraints());
    }
    
    /**
     * Parse @org.apache.openjpa.persistence.jdbc.OrderColumn.
     */
    private void parseOrderColumn(FieldMapping fm, OrderColumn order) {
        if (!order.enabled()) {
            fm.getMappingInfo().setCanOrderColumn(false);
            return;
        }

        Column col = new Column();
        if (!StringUtils.isEmpty(order.name()))
            col.setName(order.name());
        if (!StringUtils.isEmpty(order.columnDefinition()))
            col.setTypeName(order.columnDefinition());
        if (order.precision() != 0)
            col.setSize(order.precision());
        col.setFlag(Column.FLAG_UNINSERTABLE, !order.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !order.updatable());
        fm.getMappingInfo().setOrderColumn(col);
    }
    
    /**
     * Parse @javax.persistence.OrderColumn
     */
    private void parseJavaxOrderColumn(FieldMapping fm, 
        javax.persistence.OrderColumn order) {
        // If a table name is specified on the annotation and a table
        // name has not been defined, set the table name to the name
        // specified.  This will be the name of the join table or
        // collection table.
        if (!StringUtils.isEmpty(order.table()) &&
            StringUtils.isEmpty(fm.getMappingInfo().getTableName())) {
            fm.getMappingInfo().setTableName(order.table());
        }
        
        Column col = new Column();
        if (!StringUtils.isEmpty(order.name()))
            col.setName(order.name());
        if (!StringUtils.isEmpty(order.columnDefinition()))
            col.setTypeName(order.columnDefinition());
        col.setNotNull(!order.nullable());
        col.setFlag(Column.FLAG_UNINSERTABLE, !order.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !order.updatable());
        if (!StringUtils.isEmpty(order.table()))
            col.setTableName(order.table());
        
        fm.getMappingInfo().setOrderColumn(col);        
    }

    /**
     * Parse @ElementJoinColumn(s).
     */
    protected void parseElementJoinColumns(FieldMapping fm,
        ElementJoinColumn... joins) {
        if (joins.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(joins.length);
        int unique = 0;
        for (int i = 0; i < joins.length; i++) {
            cols.add(newColumn(joins[i]));
            unique |= (joins[i].unique()) ? TRUE : FALSE;
        }
        setColumns(fm, fm.getElementMapping().getValueInfo(), cols, unique);
    }

    /**
     * Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(ElementJoinColumn join) {
        Column col = new Column();
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setTypeName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        if (!StringUtils.isEmpty(join.referencedAttributeName()))
            col.setTargetField(join.referencedAttributeName());
        col.setNotNull(!join.nullable());
        col.setFlag (Column.FLAG_UNINSERTABLE, !join.insertable ());
		col.setFlag (Column.FLAG_UNUPDATABLE, !join.updatable ());
		return col;
	}
    
    /**
     * Parse @MapKeyColumn.
     */
    protected void parseMapKeyColumn(FieldMapping fm, MapKeyColumn anno) {
        int unique = 0;
        Column col = new Column();
        setupMapKeyColumn(fm, col, anno);
        unique |= (anno.unique()) ? TRUE : FALSE;
        setMapKeyColumn(fm, fm.getKeyMapping().getValueInfo(), col, unique);
    }

    /**
     * Setup the given column with information from the given annotation.
     */
    private static void setupMapKeyColumn(FieldMapping fm, Column col, 
        MapKeyColumn anno) {
        if (!StringUtils.isEmpty(anno.name()))
            col.setName(anno.name());
        else 
            col.setName(fm.getName() + "_" + "KEY");
        if (!StringUtils.isEmpty(anno.columnDefinition()))
            col.setTypeName(anno.columnDefinition());
        if (anno.precision() != 0)
            col.setSize(anno.precision());
        else if (anno.length() != 255)
            col.setSize(anno.length());
        col.setNotNull(!anno.nullable());
        col.setDecimalDigits(anno.scale());
        col.setFlag(Column.FLAG_UNINSERTABLE, !anno.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !anno.updatable());
    }

    /**
     * Set the given map key column as the map key column for <code>fm</code>.
     *
     * @param unique bitwise combination of TRUE and FALSE for the
     * unique attribute of the column
     */
    protected void setMapKeyColumn(FieldMapping fm, MappingInfo info,
        Column col, int unique) {
        List cols = new ArrayList();
        cols.add(col);
        info.setColumns(cols);
        if (unique == TRUE)
            info.setUnique(new org.apache.openjpa.jdbc.schema.Unique());
    }
    
    /**
     * Parse @MapKeyJoinColumn(s).
     */
    private void parseMapKeyJoinColumns(FieldMapping fm,
            MapKeyJoinColumn... joins) {
        if (joins.length == 0)
            return;

        List<Column> cols = new ArrayList<Column>(joins.length);
        int unique = 0;
        for (int i = 0; i < joins.length; i++) {
            cols.add(newColumn(joins[i]));
            unique |= (joins[i].unique()) ? TRUE : FALSE;
        }
        setColumns(fm, fm.getKeyMapping().getValueInfo(), cols, unique);
    }
    
    /**
     *  Create a new schema column with information from the given annotation.
     */
    private static Column newColumn(MapKeyJoinColumn join) {
        Column col = new Column();
        if (!StringUtils.isEmpty(join.name()))
            col.setName(join.name());
        if (!StringUtils.isEmpty(join.columnDefinition()))
            col.setName(join.columnDefinition());
        if (!StringUtils.isEmpty(join.referencedColumnName()))
            col.setTarget(join.referencedColumnName());
        col.setNotNull(!join.nullable());
        col.setFlag(Column.FLAG_UNINSERTABLE, !join.insertable());
        col.setFlag(Column.FLAG_UNUPDATABLE, !join.updatable ());
        return col;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13733.java