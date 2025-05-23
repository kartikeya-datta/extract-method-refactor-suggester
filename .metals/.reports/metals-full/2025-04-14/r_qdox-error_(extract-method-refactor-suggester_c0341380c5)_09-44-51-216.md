error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2037.java
text:
```scala
i@@ndexerNamespace.put("request", new HashMap<String,Object>(reqParams.getRawParams()));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.handler.dataimport;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.config.ConfigNameConstants;
import org.apache.solr.handler.dataimport.config.DIHConfiguration;
import org.apache.solr.handler.dataimport.config.Entity;
import org.apache.solr.handler.dataimport.config.EntityField;

import static org.apache.solr.handler.dataimport.SolrWriter.LAST_INDEX_KEY;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.*;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p> {@link DocBuilder} is responsible for creating Solr documents out of the given configuration. It also maintains
 * statistics information. It depends on the {@link EntityProcessor} implementations to fetch data. </p>
 * <p/>
 * <b>This API is experimental and subject to change</b>
 *
 * @since solr 1.3
 */
public class DocBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(DocBuilder.class);

  private static final Date EPOCH = new Date(0);

  DataImporter dataImporter;

  private DIHConfiguration config;

  private EntityProcessorWrapper currentEntityProcessorWrapper;

  @SuppressWarnings("unchecked")
  private Map statusMessages = Collections.synchronizedMap(new LinkedHashMap());

  public Statistics importStatistics = new Statistics();

  DIHWriter writer;

  boolean verboseDebug = false;

  Map<String, Object> session = new HashMap<String, Object>();

  static final ThreadLocal<DocBuilder> INSTANCE = new ThreadLocal<DocBuilder>();
  private Map<String, Object> persistedProperties;
  
  private DIHProperties propWriter;
  private static final String PARAM_WRITER_IMPL = "writerImpl";
  private static final String DEFAULT_WRITER_NAME = "SolrWriter";
  private DebugLogger debugLogger;
  private final RequestInfo reqParams;
  
  @SuppressWarnings("unchecked")
  public DocBuilder(DataImporter dataImporter, SolrWriter solrWriter, DIHProperties propWriter, RequestInfo reqParams) {
    INSTANCE.set(this);
    this.dataImporter = dataImporter;
    this.reqParams = reqParams;
    this.propWriter = propWriter;
    DataImporter.QUERY_COUNT.set(importStatistics.queryCount);
    verboseDebug = reqParams.isDebug() && reqParams.getDebugInfo().verbose;
    persistedProperties = propWriter.readIndexerProperties();
     
    String writerClassStr = null;
    if(reqParams!=null && reqParams.getRawParams() != null) {
      writerClassStr = (String) reqParams.getRawParams().get(PARAM_WRITER_IMPL);
    }
    if(writerClassStr != null && !writerClassStr.equals(DEFAULT_WRITER_NAME) && !writerClassStr.equals(DocBuilder.class.getPackage().getName() + "." + DEFAULT_WRITER_NAME)) {
      try {
        Class<DIHWriter> writerClass = loadClass(writerClassStr, dataImporter.getCore());
        this.writer = writerClass.newInstance();
      } catch (Exception e) {
        throw new DataImportHandlerException(DataImportHandlerException.SEVERE, "Unable to load Writer implementation:" + writerClassStr, e);
      }
     } else {
      writer = solrWriter;
    }
    ContextImpl ctx = new ContextImpl(null, null, null, null, reqParams.getRawParams(), null, this);
    writer.init(ctx);
  }


  DebugLogger getDebugLogger(){
    if (debugLogger == null) {
      debugLogger = new DebugLogger();
    }
    return debugLogger;
  }

  private VariableResolver getVariableResolver() {
    try {
      VariableResolver resolver = null;
      if(dataImporter != null && dataImporter.getCore() != null
          && dataImporter.getCore().getResourceLoader().getCoreProperties() != null){
        resolver =  new VariableResolver(dataImporter.getCore().getResourceLoader().getCoreProperties());
      } else {
        resolver = new VariableResolver();
      }
      resolver.setEvaluators(dataImporter.getEvaluators());
      Map<String, Object> indexerNamespace = new HashMap<String, Object>();
      if (persistedProperties.get(LAST_INDEX_TIME) != null) {
        indexerNamespace.put(LAST_INDEX_TIME, persistedProperties.get(LAST_INDEX_TIME));
      } else  {
        // set epoch
        indexerNamespace.put(LAST_INDEX_TIME, EPOCH);
      }
      indexerNamespace.put(INDEX_START_TIME, dataImporter.getIndexStartTime());
      indexerNamespace.put("request", reqParams.getRawParams());
      for (Entity entity : dataImporter.getConfig().getEntities()) {
        String key = entity.getName() + "." + SolrWriter.LAST_INDEX_KEY;
        Object lastIndex = persistedProperties.get(key);
        if (lastIndex != null && lastIndex instanceof Date) {
          indexerNamespace.put(key, lastIndex);
        } else  {
          indexerNamespace.put(key, EPOCH);
        }
      }
      resolver.addNamespace(ConfigNameConstants.IMPORTER_NS_SHORT, indexerNamespace);
      resolver.addNamespace(ConfigNameConstants.IMPORTER_NS, indexerNamespace);
      return resolver;
    } catch (Exception e) {
      wrapAndThrow(SEVERE, e);
      // unreachable statement
      return null;
    }
  }
  

  private void invokeEventListener(String className) {
    try {
      EventListener listener = (EventListener) loadClass(className, dataImporter.getCore()).newInstance();
      notifyListener(listener);
    } catch (Exception e) {
      wrapAndThrow(SEVERE, e, "Unable to load class : " + className);
    }
  }

  private void notifyListener(EventListener listener) {
    String currentProcess;
    if (dataImporter.getStatus() == DataImporter.Status.RUNNING_DELTA_DUMP) {
      currentProcess = Context.DELTA_DUMP;
    } else {
      currentProcess = Context.FULL_DUMP;
    }
    listener.onEvent(new ContextImpl(null, getVariableResolver(), null, currentProcess, session, null, this));
  }

  @SuppressWarnings("unchecked")
  public void execute() {
    List<EntityProcessorWrapper> epwList = null;
    try {
      dataImporter.store(DataImporter.STATUS_MSGS, statusMessages);
      config = dataImporter.getConfig();
      final AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
      statusMessages.put(TIME_ELAPSED, new Object() {
        @Override
        public String toString() {
          return getTimeElapsedSince(startTime.get());
        }
      });

      statusMessages.put(DataImporter.MSG.TOTAL_QUERIES_EXECUTED,
              importStatistics.queryCount);
      statusMessages.put(DataImporter.MSG.TOTAL_ROWS_EXECUTED,
              importStatistics.rowsCount);
      statusMessages.put(DataImporter.MSG.TOTAL_DOC_PROCESSED,
              importStatistics.docCount);
      statusMessages.put(DataImporter.MSG.TOTAL_DOCS_SKIPPED,
              importStatistics.skipDocCount);

      List<String> entities = reqParams.getEntitiesToRun();

      // Trigger onImportStart
      if (config.getOnImportStart() != null) {
        invokeEventListener(config.getOnImportStart());
      }
      AtomicBoolean fullCleanDone = new AtomicBoolean(false);
      //we must not do a delete of *:* multiple times if there are multiple root entities to be run
      Map<String,Object> lastIndexTimeProps = new HashMap<String,Object>();
      lastIndexTimeProps.put(LAST_INDEX_KEY, dataImporter.getIndexStartTime());

      epwList = new ArrayList<EntityProcessorWrapper>(config.getEntities().size());
      for (Entity e : config.getEntities()) {
        epwList.add(getEntityProcessorWrapper(e));
      }
      for (EntityProcessorWrapper epw : epwList) {
        if (entities != null && !entities.contains(epw.getEntity().getName()))
          continue;
        lastIndexTimeProps.put(epw.getEntity().getName() + "." + LAST_INDEX_KEY, propWriter.getCurrentTimestamp());
        currentEntityProcessorWrapper = epw;
        String delQuery = epw.getEntity().getAllAttributes().get("preImportDeleteQuery");
        if (dataImporter.getStatus() == DataImporter.Status.RUNNING_DELTA_DUMP) {
          cleanByQuery(delQuery, fullCleanDone);
          doDelta();
          delQuery = epw.getEntity().getAllAttributes().get("postImportDeleteQuery");
          if (delQuery != null) {
            fullCleanDone.set(false);
            cleanByQuery(delQuery, fullCleanDone);
          }
        } else {
          cleanByQuery(delQuery, fullCleanDone);
          doFullDump();
          delQuery = epw.getEntity().getAllAttributes().get("postImportDeleteQuery");
          if (delQuery != null) {
            fullCleanDone.set(false);
            cleanByQuery(delQuery, fullCleanDone);
          }
        }
        statusMessages.remove(DataImporter.MSG.TOTAL_DOC_PROCESSED);
      }

      if (stop.get()) {
        // Dont commit if aborted using command=abort
        statusMessages.put("Aborted", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).format(new Date()));
        rollback();
      } else {
        // Do not commit unnecessarily if this is a delta-import and no documents were created or deleted
        if (!reqParams.isClean()) {
          if (importStatistics.docCount.get() > 0 || importStatistics.deletedDocCount.get() > 0) {
            finish(lastIndexTimeProps);
          }
        } else {
          // Finished operation normally, commit now
          finish(lastIndexTimeProps);
        }

        if (config.getOnImportEnd() != null) {
          invokeEventListener(config.getOnImportEnd());
        }
      }

      statusMessages.remove(TIME_ELAPSED);
      statusMessages.put(DataImporter.MSG.TOTAL_DOC_PROCESSED, ""+ importStatistics.docCount.get());
      if(importStatistics.failedDocCount.get() > 0)
        statusMessages.put(DataImporter.MSG.TOTAL_FAILED_DOCS, ""+ importStatistics.failedDocCount.get());

      statusMessages.put("Time taken", getTimeElapsedSince(startTime.get()));
      LOG.info("Time taken = " + getTimeElapsedSince(startTime.get()));
    } catch(Exception e)
    {
      throw new RuntimeException(e);
    } finally
    {
      if (writer != null) {
        writer.close();
      }
      if (epwList != null) {
        closeEntityProcessorWrappers(epwList);
      }
      if(reqParams.isDebug()) {
        reqParams.getDebugInfo().debugVerboseOutput = getDebugLogger().output;
      }
    }
  }
  private void closeEntityProcessorWrappers(List<EntityProcessorWrapper> epwList) {
    for(EntityProcessorWrapper epw : epwList) {
      epw.close();
      if(epw.getDatasource()!=null) {
        epw.getDatasource().close();
      }
      closeEntityProcessorWrappers(epw.getChildren());
    }
  }

  @SuppressWarnings("unchecked")
  private void finish(Map<String,Object> lastIndexTimeProps) {
    LOG.info("Import completed successfully");
    statusMessages.put("", "Indexing completed. Added/Updated: "
            + importStatistics.docCount + " documents. Deleted "
            + importStatistics.deletedDocCount + " documents.");
    if(reqParams.isCommit()) {
      writer.commit(reqParams.isOptimize());
      addStatusMessage("Committed");
      if (reqParams.isOptimize())
        addStatusMessage("Optimized");
    }
    try {
      propWriter.persist(lastIndexTimeProps);
    } catch (Exception e) {
      LOG.error("Could not write property file", e);
      statusMessages.put("error", "Could not write property file. Delta imports will not work. " +
          "Make sure your conf directory is writable");
    }
  }

  void rollback() {
    writer.rollback();
    statusMessages.put("", "Indexing failed. Rolled back all changes.");
    addStatusMessage("Rolledback");
  }

  private void doFullDump() {
    addStatusMessage("Full Dump Started");    
    buildDocument(getVariableResolver(), null, null, currentEntityProcessorWrapper, true, null);
  }

  @SuppressWarnings("unchecked")
  private void doDelta() {
    addStatusMessage("Delta Dump started");
    VariableResolver resolver = getVariableResolver();

    if (config.getDeleteQuery() != null) {
      writer.deleteByQuery(config.getDeleteQuery());
    }

    addStatusMessage("Identifying Delta");
    LOG.info("Starting delta collection.");
    Set<Map<String, Object>> deletedKeys = new HashSet<Map<String, Object>>();
    Set<Map<String, Object>> allPks = collectDelta(currentEntityProcessorWrapper, resolver, deletedKeys);
    if (stop.get())
      return;
    addStatusMessage("Deltas Obtained");
    addStatusMessage("Building documents");
    if (!deletedKeys.isEmpty()) {
      allPks.removeAll(deletedKeys);
      deleteAll(deletedKeys);
      // Make sure that documents are not re-created
    }
    deletedKeys = null;
    writer.setDeltaKeys(allPks);

    statusMessages.put("Total Changed Documents", allPks.size());
    VariableResolver vri = getVariableResolver();
    Iterator<Map<String, Object>> pkIter = allPks.iterator();
    while (pkIter.hasNext()) {
      Map<String, Object> map = pkIter.next();
      vri.addNamespace(ConfigNameConstants.IMPORTER_NS_SHORT + ".delta", map);
      buildDocument(vri, null, map, currentEntityProcessorWrapper, true, null);
      pkIter.remove();
      // check for abort
      if (stop.get())
        break;
    }

    if (!stop.get()) {
      LOG.info("Delta Import completed successfully");
    }
  }

  private void deleteAll(Set<Map<String, Object>> deletedKeys) {
    LOG.info("Deleting stale documents ");
    Iterator<Map<String, Object>> iter = deletedKeys.iterator();
    while (iter.hasNext()) {
      Map<String, Object> map = iter.next();
      String keyName = currentEntityProcessorWrapper.getEntity().isDocRoot() ? currentEntityProcessorWrapper.getEntity().getPk() : currentEntityProcessorWrapper.getEntity().getSchemaPk();
      Object key = map.get(keyName);
      if(key == null) {
        keyName = findMatchingPkColumn(keyName, map);
        key = map.get(keyName);
      }
      if(key == null) {
        LOG.warn("no key was available for deleted pk query. keyName = " + keyName);
        continue;
      }
      writer.deleteDoc(key);
      importStatistics.deletedDocCount.incrementAndGet();
      iter.remove();
    }
  }
  
  @SuppressWarnings("unchecked")
  public void addStatusMessage(String msg) {
    statusMessages.put(msg, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).format(new Date()));
  }

  private void resetEntity(EntityProcessorWrapper epw) {
    epw.setInitalized(false);
    for (EntityProcessorWrapper child : epw.getChildren()) {
      resetEntity(child);
    }
    
  }
  
  private void buildDocument(VariableResolver vr, DocWrapper doc,
      Map<String,Object> pk, EntityProcessorWrapper epw, boolean isRoot,
      ContextImpl parentCtx) {
    List<EntityProcessorWrapper> entitiesToDestroy = new ArrayList<EntityProcessorWrapper>();
    try {
      buildDocument(vr, doc, pk, epw, isRoot, parentCtx, entitiesToDestroy);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      for (EntityProcessorWrapper entityWrapper : entitiesToDestroy) {
        entityWrapper.destroy();
      }
      resetEntity(epw);
    }
  }

  @SuppressWarnings("unchecked")
  private void buildDocument(VariableResolver vr, DocWrapper doc,
                             Map<String, Object> pk, EntityProcessorWrapper epw, boolean isRoot,
                             ContextImpl parentCtx, List<EntityProcessorWrapper> entitiesToDestroy) {

    ContextImpl ctx = new ContextImpl(epw, vr, null,
            pk == null ? Context.FULL_DUMP : Context.DELTA_DUMP,
            session, parentCtx, this);
    epw.init(ctx);
    if (!epw.isInitalized()) {
      entitiesToDestroy.add(epw);
      epw.setInitalized(true);
    }
    
    if (reqParams.getStart() > 0) {
      getDebugLogger().log(DIHLogLevels.DISABLE_LOGGING, null, null);
    }

    if (verboseDebug) {
      getDebugLogger().log(DIHLogLevels.START_ENTITY, epw.getEntity().getName(), null);
    }

    int seenDocCount = 0;

    try {
      while (true) {
        if (stop.get())
          return;
        if(importStatistics.docCount.get() > (reqParams.getStart() + reqParams.getRows())) break;
        try {
          seenDocCount++;

          if (seenDocCount > reqParams.getStart()) {
            getDebugLogger().log(DIHLogLevels.ENABLE_LOGGING, null, null);
          }

          if (verboseDebug && epw.getEntity().isDocRoot()) {
            getDebugLogger().log(DIHLogLevels.START_DOC, epw.getEntity().getName(), null);
          }
          if (doc == null && epw.getEntity().isDocRoot()) {
            doc = new DocWrapper();
            ctx.setDoc(doc);
            Entity e = epw.getEntity();
            while (e.getParentEntity() != null) {
              addFields(e.getParentEntity(), doc, (Map<String, Object>) vr
                      .resolve(e.getParentEntity().getName()), vr);
              e = e.getParentEntity();
            }
          }

          Map<String, Object> arow = epw.nextRow();
          if (arow == null) {
            break;
          }

          // Support for start parameter in debug mode
          if (epw.getEntity().isDocRoot()) {
            if (seenDocCount <= reqParams.getStart())
              continue;
            if (seenDocCount > reqParams.getStart() + reqParams.getRows()) {
              LOG.info("Indexing stopped at docCount = " + importStatistics.docCount);
              break;
            }
          }

          if (verboseDebug) {
            getDebugLogger().log(DIHLogLevels.ENTITY_OUT, epw.getEntity().getName(), arow);
          }
          importStatistics.rowsCount.incrementAndGet();
          if (doc != null) {
            handleSpecialCommands(arow, doc);
            addFields(epw.getEntity(), doc, arow, vr);
          }
          if (epw.getEntity().getChildren() != null) {
            vr.addNamespace(epw.getEntity().getName(), arow);
            for (EntityProcessorWrapper child : epw.getChildren()) {
              buildDocument(vr, doc,
                  child.getEntity().isDocRoot() ? pk : null, child, false, ctx, entitiesToDestroy);
            }
            vr.removeNamespace(epw.getEntity().getName());
          }
          if (epw.getEntity().isDocRoot()) {
            if (stop.get())
              return;
            if (!doc.isEmpty()) {
              boolean result = writer.upload(doc);
              if(reqParams.isDebug()) {
                reqParams.getDebugInfo().debugDocuments.add(doc);
              }
              doc = null;
              if (result){
                importStatistics.docCount.incrementAndGet();
              } else {
                importStatistics.failedDocCount.incrementAndGet();
              }
            }
          }
        } catch (DataImportHandlerException e) {
          if (verboseDebug) {
            getDebugLogger().log(DIHLogLevels.ENTITY_EXCEPTION, epw.getEntity().getName(), e);
          }
          if(e.getErrCode() == DataImportHandlerException.SKIP_ROW){
            continue;
          }
          if (isRoot) {
            if (e.getErrCode() == DataImportHandlerException.SKIP) {
              importStatistics.skipDocCount.getAndIncrement();
              doc = null;
            } else {
              SolrException.log(LOG, "Exception while processing: "
                      + epw.getEntity().getName() + " document : " + doc, e);
            }
            if (e.getErrCode() == DataImportHandlerException.SEVERE)
              throw e;
          } else
            throw e;
        } catch (Throwable t) {
          if (verboseDebug) {
            getDebugLogger().log(DIHLogLevels.ENTITY_EXCEPTION, epw.getEntity().getName(), t);
          }
          throw new DataImportHandlerException(DataImportHandlerException.SEVERE, t);
        } finally {
          if (verboseDebug) {
            getDebugLogger().log(DIHLogLevels.ROW_END, epw.getEntity().getName(), null);
            if (epw.getEntity().isDocRoot())
              getDebugLogger().log(DIHLogLevels.END_DOC, null, null);
          }
        }
      }
    } finally {
      if (verboseDebug) {
        getDebugLogger().log(DIHLogLevels.END_ENTITY, null, null);
      }
    }
  }

  static class DocWrapper extends SolrInputDocument {
    //final SolrInputDocument solrDocument = new SolrInputDocument();
    Map<String ,Object> session;

    public void setSessionAttribute(String key, Object val){
      if(session == null) session = new HashMap<String, Object>();
      session.put(key, val);
    }

    public Object getSessionAttribute(String key) {
      return session == null ? null : session.get(key);
    }
  }

  private void handleSpecialCommands(Map<String, Object> arow, DocWrapper doc) {
    Object value = arow.get("$deleteDocById");
    if (value != null) {
      if (value instanceof Collection) {
        Collection collection = (Collection) value;
        for (Object o : collection) {
          writer.deleteDoc(o.toString());
          importStatistics.deletedDocCount.incrementAndGet();
        }
      } else {
        writer.deleteDoc(value);
        importStatistics.deletedDocCount.incrementAndGet();
      }
    }    
    value = arow.get("$deleteDocByQuery");
    if (value != null) {
      if (value instanceof Collection) {
        Collection collection = (Collection) value;
        for (Object o : collection) {
          writer.deleteByQuery(o.toString());
          importStatistics.deletedDocCount.incrementAndGet();
        }
      } else {
        writer.deleteByQuery(value.toString());
        importStatistics.deletedDocCount.incrementAndGet();
      }
    }
    value = arow.get("$docBoost");
    if (value != null) {
      float value1 = 1.0f;
      if (value instanceof Number) {
        value1 = ((Number) value).floatValue();
      } else {
        value1 = Float.parseFloat(value.toString());
      }
      doc.setDocumentBoost(value1);
    }

    value = arow.get("$skipDoc");
    if (value != null) {
      if (Boolean.parseBoolean(value.toString())) {
        throw new DataImportHandlerException(DataImportHandlerException.SKIP,
                "Document skipped :" + arow);
      }
    }

    value = arow.get("$skipRow");
    if (value != null) {
      if (Boolean.parseBoolean(value.toString())) {
        throw new DataImportHandlerException(DataImportHandlerException.SKIP_ROW);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void addFields(Entity entity, DocWrapper doc,
                         Map<String, Object> arow, VariableResolver vr) {
    for (Map.Entry<String, Object> entry : arow.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value == null)  continue;
      if (key.startsWith("$")) continue;
      Set<EntityField> field = entity.getColNameVsField().get(key);
      if (field == null && dataImporter.getSchema() != null) {
        // This can be a dynamic field or a field which does not have an entry in data-config ( an implicit field)
        SchemaField sf = dataImporter.getSchema().getFieldOrNull(key);
        if (sf == null) {
          sf = dataImporter.getSchemaField(key);
        }
        if (sf != null) {
          addFieldToDoc(entry.getValue(), sf.getName(), 1.0f, sf.multiValued(), doc);
        }
        //else do nothing. if we add it it may fail
      } else {
        if (field != null) {
          for (EntityField f : field) {
            String name = f.getName();
            boolean multiValued = f.isMultiValued();
            boolean toWrite = f.isToWrite();
            if(f.isDynamicName()){
              name =  vr.replaceTokens(name);
              SchemaField schemaField = dataImporter.getSchemaField(name);
              if(schemaField == null) {
                toWrite = false;
              } else {
                multiValued = schemaField.multiValued();
                toWrite = true;
              }
            }
            if (toWrite) {
              addFieldToDoc(entry.getValue(), name, f.getBoost(), multiValued, doc);
            }
          }
        }
      }
    }
  }

  private void addFieldToDoc(Object value, String name, float boost, boolean multiValued, DocWrapper doc) {
    if (value instanceof Collection) {
      Collection collection = (Collection) value;
      if (multiValued) {
        for (Object o : collection) {
          if (o != null)
            doc.addField(name, o, boost);
        }
      } else {
        if (doc.getField(name) == null)
          for (Object o : collection) {
            if (o != null)  {
              doc.addField(name, o, boost);
              break;
            }
          }
      }
    } else if (multiValued) {
      if (value != null)  {
        doc.addField(name, value, boost);
      }
    } else {
      if (doc.getField(name) == null && value != null)
        doc.addField(name, value, boost);
    }
  }

  private EntityProcessorWrapper getEntityProcessorWrapper(Entity entity) {
    EntityProcessor entityProcessor = null;
    if (entity.getProcessorName() == null) {
      entityProcessor = new SqlEntityProcessor();
    } else {
      try {
        entityProcessor = (EntityProcessor) loadClass(entity.getProcessorName(), dataImporter.getCore())
                .newInstance();
      } catch (Exception e) {
        wrapAndThrow (SEVERE,e,
                "Unable to load EntityProcessor implementation for entity:" + entity.getName());
      }
    }
    EntityProcessorWrapper epw = new EntityProcessorWrapper(entityProcessor, entity, this);
    for(Entity e1 : entity.getChildren()) {
      epw.getChildren().add(getEntityProcessorWrapper(e1));
    }
      
    return epw;
  }

  private String findMatchingPkColumn(String pk, Map<String, Object> row) {
    if (row.containsKey(pk)) {
      throw new IllegalArgumentException(String.format(Locale.ROOT,
          "deltaQuery returned a row with null for primary key %s", pk));
    }
    String resolvedPk = null;
    for (String columnName : row.keySet()) {
      if (columnName.endsWith("." + pk) || pk.endsWith("." + columnName)) {
        if (resolvedPk != null)
          throw new IllegalArgumentException(
            String.format(Locale.ROOT, 
              "deltaQuery has more than one column (%s and %s) that might resolve to declared primary key pk='%s'",
              resolvedPk, columnName, pk));
        resolvedPk = columnName;
      }
    }
    if (resolvedPk == null) {
      throw new IllegalArgumentException(
          String
              .format(
                  Locale.ROOT,
                  "deltaQuery has no column to resolve to declared primary key pk='%s'",
                  pk));
    }
    LOG.info(String.format(Locale.ROOT,
        "Resolving deltaQuery column '%s' to match entity's declared pk '%s'",
        resolvedPk, pk));
    return resolvedPk;
  }

  /**
   * <p> Collects unique keys of all Solr documents for whom one or more source tables have been changed since the last
   * indexed time. </p> <p> Note: In our definition, unique key of Solr document is the primary key of the top level
   * entity (unless skipped using docRoot=false) in the Solr document in data-config.xml </p>
   *
   * @return an iterator to the list of keys for which Solr documents should be updated.
   */
  @SuppressWarnings("unchecked")
  public Set<Map<String, Object>> collectDelta(EntityProcessorWrapper epw, VariableResolver resolver,
                                               Set<Map<String, Object>> deletedRows) {
    //someone called abort
    if (stop.get())
      return new HashSet();

    ContextImpl context1 = new ContextImpl(epw, resolver, null, Context.FIND_DELTA, session, null, this);
    epw.init(context1);

    Set<Map<String, Object>> myModifiedPks = new HashSet<Map<String, Object>>();

   

    for (EntityProcessorWrapper childEpw : epw.getChildren()) {
      //this ensures that we start from the leaf nodes
      myModifiedPks.addAll(collectDelta(childEpw, resolver, deletedRows));
      //someone called abort
      if (stop.get())
        return new HashSet();
    }
    
    // identifying the modified rows for this entity
    Map<String, Map<String, Object>> deltaSet = new HashMap<String, Map<String, Object>>();
    LOG.info("Running ModifiedRowKey() for Entity: " + epw.getEntity().getName());
    //get the modified rows in this entity
    String pk = epw.getEntity().getPk();
    while (true) {
      Map<String, Object> row = epw.nextModifiedRowKey();

      if (row == null)
        break;

      Object pkValue = row.get(pk);
      if (pkValue == null) {
        pk = findMatchingPkColumn(pk, row);
        pkValue = row.get(pk);
      }

      deltaSet.put(pkValue.toString(), row);
      importStatistics.rowsCount.incrementAndGet();
      // check for abort
      if (stop.get())
        return new HashSet();
    }
    //get the deleted rows for this entity
    Set<Map<String, Object>> deletedSet = new HashSet<Map<String, Object>>();
    while (true) {
      Map<String, Object> row = epw.nextDeletedRowKey();
      if (row == null)
        break;

      deletedSet.add(row);
      
      Object pkValue = row.get(pk);
      if (pkValue == null) {
        pk = findMatchingPkColumn(pk, row);
        pkValue = row.get(pk);
      }

      // Remove deleted rows from the delta rows
      String deletedRowPk = pkValue.toString();
      if (deltaSet.containsKey(deletedRowPk)) {
        deltaSet.remove(deletedRowPk);
      }

      importStatistics.rowsCount.incrementAndGet();
      // check for abort
      if (stop.get())
        return new HashSet();
    }

    LOG.info("Completed ModifiedRowKey for Entity: " + epw.getEntity().getName() + " rows obtained : " + deltaSet.size());
    LOG.info("Completed DeletedRowKey for Entity: " + epw.getEntity().getName() + " rows obtained : " + deletedSet.size());

    myModifiedPks.addAll(deltaSet.values());
    Set<Map<String, Object>> parentKeyList = new HashSet<Map<String, Object>>();
    //all that we have captured is useless (in a sub-entity) if no rows in the parent is modified because of these
    //propogate up the changes in the chain
    if (epw.getEntity().getParentEntity() != null) {
      // identifying deleted rows with deltas

      for (Map<String, Object> row : myModifiedPks) {
        resolver.addNamespace(epw.getEntity().getName(), row);
        getModifiedParentRows(resolver, epw.getEntity().getName(), epw, parentKeyList);
        // check for abort
        if (stop.get())
          return new HashSet();
      }
      // running the same for deletedrows
      for (Map<String, Object> row : deletedSet) {
        resolver.addNamespace(epw.getEntity().getName(), row);
        getModifiedParentRows(resolver, epw.getEntity().getName(), epw, parentKeyList);
        // check for abort
        if (stop.get())
          return new HashSet();
      }
    }
    LOG.info("Completed parentDeltaQuery for Entity: " + epw.getEntity().getName());
    if (epw.getEntity().isDocRoot())
      deletedRows.addAll(deletedSet);

    // Do not use entity.isDocRoot here because one of descendant entities may set rootEntity="true"
    return epw.getEntity().getParentEntity() == null ?
        myModifiedPks : new HashSet<Map<String, Object>>(parentKeyList);
  }

  private void getModifiedParentRows(VariableResolver resolver,
                                     String entity, EntityProcessor entityProcessor,
                                     Set<Map<String, Object>> parentKeyList) {
    try {
      while (true) {
        Map<String, Object> parentRow = entityProcessor
                .nextModifiedParentRowKey();
        if (parentRow == null)
          break;

        parentKeyList.add(parentRow);
        importStatistics.rowsCount.incrementAndGet();
        // check for abort
        if (stop.get())
          return;
      }

    } finally {
      resolver.removeNamespace(entity);
    }
  }

  public void abort() {
    stop.set(true);
  }

  private AtomicBoolean stop = new AtomicBoolean(false);

  public static final String TIME_ELAPSED = "Time Elapsed";

  static String getTimeElapsedSince(long l) {
    l = System.currentTimeMillis() - l;
    return (l / (60000 * 60)) + ":" + (l / 60000) % 60 + ":" + (l / 1000)
            % 60 + "." + l % 1000;
  }

  public RequestInfo getReqParams() {
    return reqParams;
  }




  @SuppressWarnings("unchecked")
  static Class loadClass(String name, SolrCore core) throws ClassNotFoundException {
    try {
      return core != null ?
              core.getResourceLoader().findClass(name, Object.class) :
              Class.forName(name);
    } catch (Exception e) {
      try {
        String n = DocBuilder.class.getPackage().getName() + "." + name;
        return core != null ?
                core.getResourceLoader().findClass(n, Object.class) :
                Class.forName(n);
      } catch (Exception e1) {
        throw new ClassNotFoundException("Unable to load " + name + " or " + DocBuilder.class.getPackage().getName() + "." + name, e);
      }
    }
  }

  public static class Statistics {
    public AtomicLong docCount = new AtomicLong();

    public AtomicLong deletedDocCount = new AtomicLong();

    public AtomicLong failedDocCount = new AtomicLong();

    public AtomicLong rowsCount = new AtomicLong();

    public AtomicLong queryCount = new AtomicLong();

    public AtomicLong skipDocCount = new AtomicLong();

    public Statistics add(Statistics stats) {
      this.docCount.addAndGet(stats.docCount.get());
      this.deletedDocCount.addAndGet(stats.deletedDocCount.get());
      this.rowsCount.addAndGet(stats.rowsCount.get());
      this.queryCount.addAndGet(stats.queryCount.get());

      return this;
    }

    public Map<String, Object> getStatsSnapshot() {
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("docCount", docCount.get());
      result.put("deletedDocCount", deletedDocCount.get());
      result.put("rowCount", rowsCount.get());
      result.put("queryCount", rowsCount.get());
      result.put("skipDocCount", skipDocCount.get());
      return result;
    }

  }

  private void cleanByQuery(String delQuery, AtomicBoolean completeCleanDone) {
    delQuery = getVariableResolver().replaceTokens(delQuery);
    if (reqParams.isClean()) {
      if (delQuery == null && !completeCleanDone.get()) {
        writer.doDeleteAll();
        completeCleanDone.set(true);
      } else if (delQuery != null) {
        writer.deleteByQuery(delQuery);
      }
    }
  }

  public static final String LAST_INDEX_TIME = "last_index_time";
  public static final String INDEX_START_TIME = "index_start_time";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2037.java