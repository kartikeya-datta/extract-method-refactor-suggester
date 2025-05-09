error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1732.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

package org.apache.solr.update.processor;

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

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.update.AddUpdateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Identifies the language of a set of input fields.
 * Also supports mapping of field names based
 * on detected language.
 * <p>
 * See <a href="http://wiki.apache.org/solr/LanguageDetection">http://wiki.apache.org/solr/LanguageDetection</a>
 * @since 3.5
 * @lucene.experimental
 */
public abstract class LanguageIdentifierUpdateProcessor extends UpdateRequestProcessor implements LangIdParams {

  protected final static Logger log = LoggerFactory
          .getLogger(LanguageIdentifierUpdateProcessor.class);

  protected boolean enabled;

  protected String[] inputFields = {};
  protected String[] mapFields = {};
  protected Pattern mapPattern;
  protected String mapReplaceStr;
  protected String langField;
  protected String langsField; // MultiValued, contains all languages detected
  protected String docIdField;
  protected String fallbackValue;
  protected String[] fallbackFields = {};
  protected boolean enableMapping;
  protected boolean mapKeepOrig;
  protected boolean overwrite;
  protected boolean mapOverwrite;
  protected boolean mapIndividual;
  protected boolean enforceSchema;
  protected double threshold;
  protected HashSet<String> langWhitelist;
  protected HashSet<String> mapIndividualFieldsSet;
  protected HashSet<String> allMapFieldsSet;
  protected HashMap<String,String> lcMap;
  protected HashMap<String,String> mapLcMap;
  protected IndexSchema schema;

  // Regex patterns
  protected final Pattern tikaSimilarityPattern = Pattern.compile(".*\\((.*?)\\)");
  protected final Pattern langPattern = Pattern.compile("\\{lang\\}");

  public LanguageIdentifierUpdateProcessor(SolrQueryRequest req,
                                           SolrQueryResponse rsp, UpdateRequestProcessor next) {
    super(next);
    schema = req.getSchema();

    initParams(req.getParams());
  }

  private void initParams(SolrParams params) {
    if (params != null) {
      // Document-centric langId params
      setEnabled(params.getBool(LANGUAGE_ID, true));
      if(params.get(FIELDS_PARAM, "").length() > 0) {
        inputFields = params.get(FIELDS_PARAM, "").split(",");
      }
      langField = params.get(LANG_FIELD, DOCID_LANGFIELD_DEFAULT);
      langsField = params.get(LANGS_FIELD, DOCID_LANGSFIELD_DEFAULT);
      SchemaField uniqueKeyField = schema.getUniqueKeyField();
      docIdField = params.get(DOCID_PARAM, uniqueKeyField == null ? DOCID_FIELD_DEFAULT : uniqueKeyField.getName());
      fallbackValue = params.get(FALLBACK);
      if(params.get(FALLBACK_FIELDS, "").length() > 0) {
        fallbackFields = params.get(FALLBACK_FIELDS).split(",");
      }
      overwrite = params.getBool(OVERWRITE, false);
      langWhitelist = new HashSet<String>();
      threshold = params.getDouble(THRESHOLD, DOCID_THRESHOLD_DEFAULT);
      if(params.get(LANG_WHITELIST, "").length() > 0) {
        for(String lang : params.get(LANG_WHITELIST, "").split(",")) {
          langWhitelist.add(lang);
        }
      }

      // Mapping params (field centric)
      enableMapping = params.getBool(MAP_ENABLE, false);
      if(params.get(MAP_FL, "").length() > 0) {
        mapFields = params.get(MAP_FL, "").split(",");
      } else {
        mapFields = inputFields;
      }
      mapKeepOrig = params.getBool(MAP_KEEP_ORIG, false);
      mapOverwrite = params.getBool(MAP_OVERWRITE, false);
      mapIndividual = params.getBool(MAP_INDIVIDUAL, false);

      // Process individual fields
      String[] mapIndividualFields = {};
      if(params.get(MAP_INDIVIDUAL_FL, "").length() > 0) {
        mapIndividualFields = params.get(MAP_INDIVIDUAL_FL, "").split(",");
      } else {
        mapIndividualFields = mapFields;
      }
      mapIndividualFieldsSet = new HashSet<String>(Arrays.asList(mapIndividualFields));
      // Compile a union of the lists of fields to map
      allMapFieldsSet = new HashSet<String>(Arrays.asList(mapFields));
      if(Arrays.equals(mapFields, mapIndividualFields)) {
        allMapFieldsSet.addAll(mapIndividualFieldsSet);
      }

      // Normalize detected langcode onto normalized langcode
      lcMap = new HashMap<String,String>();
      if(params.get(LCMAP) != null) {
        for(String mapping : params.get(LCMAP).split("[, ]")) {
          String[] keyVal = mapping.split(":");
          if(keyVal.length == 2) {
            lcMap.put(keyVal[0], keyVal[1]);
          } else {
            log.error("Unsupported format for langid.lcmap: "+mapping+". Skipping this mapping.");
          }
        }
      }

      // Language Code mapping
      mapLcMap = new HashMap<String,String>();
      if(params.get(MAP_LCMAP) != null) {
        for(String mapping : params.get(MAP_LCMAP).split("[, ]")) {
          String[] keyVal = mapping.split(":");
          if(keyVal.length == 2) {
            mapLcMap.put(keyVal[0], keyVal[1]);
          } else {
            log.error("Unsupported format for langid.map.lcmap: "+mapping+". Skipping this mapping.");
          }
        }
      }
      enforceSchema = params.getBool(ENFORCE_SCHEMA, true);

      mapPattern = Pattern.compile(params.get(MAP_PATTERN, MAP_PATTERN_DEFAULT));
      mapReplaceStr = params.get(MAP_REPLACE, MAP_REPLACE_DEFAULT);


    }
    log.debug("LangId configured");


    if (inputFields.length == 0) {
      throw new SolrException(ErrorCode.BAD_REQUEST,
              "Missing or faulty configuration of LanguageIdentifierUpdateProcessor. Input fields must be specified as a comma separated list");
    }

  }

  @Override
  public void processAdd(AddUpdateCommand cmd) throws IOException {
    if (isEnabled()) {
      process(cmd.getSolrInputDocument());
    } else {
      log.debug("Processor not enabled, not running");
    }
    super.processAdd(cmd);
  }

  /**
   * This is the main, testable process method called from processAdd()
   * @param doc the SolrInputDocument to work on
   * @return the modified SolrInputDocument
   */
  protected SolrInputDocument process(SolrInputDocument doc) {
    String docLang = null;
    HashSet<String> docLangs = new HashSet<String>();
    String fallbackLang = getFallbackLang(doc, fallbackFields, fallbackValue);

    if(langField == null || !doc.containsKey(langField) || (doc.containsKey(langField) && overwrite)) {
      String allText = concatFields(doc, inputFields);
      List<DetectedLanguage> languagelist = detectLanguage(allText);
      docLang = resolveLanguage(languagelist, fallbackLang);
      docLangs.add(docLang);
      log.debug("Detected main document language from fields "+inputFields+": "+docLang);

      if(doc.containsKey(langField) && overwrite) {
        log.debug("Overwritten old value "+doc.getFieldValue(langField));
      }
      if(langField != null && langField.length() != 0) {
        doc.setField(langField, docLang);
      }
    } else {
      // langField is set, we sanity check it against whitelist and fallback
      docLang = resolveLanguage((String) doc.getFieldValue(langField), fallbackLang);
      docLangs.add(docLang);
      log.debug("Field "+langField+" already contained value "+docLang+", not overwriting.");
    }

    if(enableMapping) {
      for (String fieldName : allMapFieldsSet) {
        if(doc.containsKey(fieldName)) {
          String fieldLang;
          if(mapIndividual && mapIndividualFieldsSet.contains(fieldName)) {
            String text = (String) doc.getFieldValue(fieldName);
            List<DetectedLanguage> languagelist = detectLanguage(text);
            fieldLang = resolveLanguage(languagelist, docLang);
            docLangs.add(fieldLang);
            log.debug("Mapping field "+fieldName+" using individually detected language "+fieldLang);
          } else {
            fieldLang = docLang;
            log.debug("Mapping field "+fieldName+" using document global language "+fieldLang);
          }
          String mappedOutputField = getMappedField(fieldName, fieldLang);

          if (mappedOutputField != null) {
            log.debug("Mapping field {} to {}", doc.getFieldValue(docIdField), fieldLang);
            SolrInputField inField = doc.getField(fieldName);
            doc.setField(mappedOutputField, inField.getValue(), inField.getBoost());
            if(!mapKeepOrig) {
              log.debug("Removing old field {}", fieldName);
              doc.removeField(fieldName);
            }
          } else {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Invalid output field mapping for "
                    + fieldName + " field and language: " + fieldLang);
          }
        }
      }
    }

    // Set the languages field to an array of all detected languages
    if(langsField != null && langsField.length() != 0) {
      doc.setField(langsField, docLangs.toArray());
    }

    return doc;
  }

  /**
   * Decides the fallback language, either from content of fallback field or fallback value
   * @param doc the Solr document
   * @param fallbackFields an array of strings with field names containing fallback language codes
   * @param fallbackValue a language code to use in case no fallbackFields are found
   */
  private String getFallbackLang(SolrInputDocument doc, String[] fallbackFields, String fallbackValue) {
    String lang = null;
    for(String field : fallbackFields) {
      if(doc.containsKey(field)) {
        lang = (String) doc.getFieldValue(field);
        log.debug("Language fallback to field "+field);
        break;
      }
    }
    if(lang == null) {
      log.debug("Language fallback to value "+fallbackValue);
      lang = fallbackValue;
    }
    return lang;
  }

  /*
   * Concatenates content from multiple fields
   */
  protected String concatFields(SolrInputDocument doc, String[] fields) {
    StringBuffer sb = new StringBuffer();
    for (String fieldName : inputFields) {
      log.debug("Appending field "+fieldName);
      if (doc.containsKey(fieldName)) {
        Object content = doc.getFieldValue(fieldName);
        if(content instanceof String) {
          sb.append((String) doc.getFieldValue(fieldName));
          sb.append(" ");
        } else {
          log.warn("Field "+fieldName+" not a String value, not including in detection");
        }
      }
    }
    return sb.toString();
  }

  /**
   * Detects language(s) from a string.
   * Classes wishing to implement their own language detection module should override this method.
   * @param content The content to identify
   * @return List of detected language(s) according to RFC-3066
   */
  protected abstract List<DetectedLanguage> detectLanguage(String content);

  /**
   * Chooses a language based on the list of candidates detected
   * @param language language code as a string
   * @param fallbackLang the language code to use as a fallback
   * @return a string of the chosen language
   */
  protected String resolveLanguage(String language, String fallbackLang) {
    List<DetectedLanguage> l = new ArrayList<DetectedLanguage>();
    l.add(new DetectedLanguage(language, 1.0));
    return resolveLanguage(l, fallbackLang);
  }

  /**
   * Chooses a language based on the list of candidates detected
   * @param languages a List of DetectedLanguages with certainty score
   * @param fallbackLang the language code to use as a fallback
   * @return a string of the chosen language
   */
  protected String resolveLanguage(List<DetectedLanguage> languages, String fallbackLang) {
    String langStr;
    if(languages.size() == 0) {
      log.debug("No language detected, using fallback {}", fallbackLang);
      langStr = fallbackLang;
    } else {
      DetectedLanguage lang = languages.get(0);
      String normalizedLang = normalizeLangCode(lang.getLangCode());
      if(langWhitelist.isEmpty() || langWhitelist.contains(normalizedLang)) {
        log.debug("Language detected {} with certainty {}", normalizedLang, lang.getCertainty());
        if(lang.getCertainty() >= threshold) {
          langStr = normalizedLang;
        } else {
          log.debug("Detected language below threshold {}, using fallback {}", threshold, fallbackLang);
          langStr = fallbackLang;
        }
      } else {
        log.debug("Detected a language not in whitelist ({}), using fallback {}", lang.getLangCode(), fallbackLang);
        langStr = fallbackLang;
      }
    }

    if(langStr == null || langStr.length() == 0) {
      log.warn("Language resolved to null or empty string. Fallback not configured?");
      langStr = "";
    }

    return langStr;
  }

  /**
   * Looks up language code in map (langid.lcmap) and returns mapped value
   * @param langCode the language code string returned from detector
   * @return the normalized/mapped language code
   */
  protected String normalizeLangCode(String langCode) {
    if (lcMap.containsKey(langCode)) {
      String lc = lcMap.get(langCode);
      log.debug("Doing langcode normalization mapping from "+langCode+" to "+lc);
      return lc;
    }
    return langCode;
  }

  /**
   * Returns the name of the field to map the current contents into, so that they are properly analyzed.  For instance
   * if the currentField is "text" and the code is "en", the new field would by default be "text_en".
   * This method also performs custom regex pattern replace if configured. If enforceSchema=true
   * and the resulting field name doesn't exist, then null is returned.
   *
   * @param currentField The current field name
   * @param language the language code
   * @return The new schema field name, based on pattern and replace, or null if illegal
   */
  protected String getMappedField(String currentField, String language) {
    String lc = mapLcMap.containsKey(language) ? mapLcMap.get(language) : language;
    String newFieldName = langPattern.matcher(mapPattern.matcher(currentField).replaceFirst(mapReplaceStr)).replaceFirst(lc);
    if(enforceSchema && schema.getFieldOrNull(newFieldName) == null) {
      log.warn("Unsuccessful field name mapping from {} to {}, field does not exist and enforceSchema=true; skipping mapping.", currentField, newFieldName);
      return null;
    } else {
      log.debug("Doing mapping from "+currentField+" with language "+language+" to field "+newFieldName);
    }
    return newFieldName;
  }

  /**
   * Tells if this processor is enabled or not
   * @return true if enabled, else false
   */
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1732.java