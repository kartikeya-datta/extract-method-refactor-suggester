error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1007.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1007.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1007.java
text:
```scala
F@@ieldsEnum fenum2 = MultiFields.getFields(index2).iterator();

package org.apache.lucene.index;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.codecs.lucene40.Lucene40PostingsFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NoSuchDirectoryException;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.junit.Assume;

public class TestDirectoryReader extends LuceneTestCase {
  
  public void testDocument() throws IOException {
    SegmentReader [] readers = new SegmentReader[2];
    Directory dir = newDirectory();
    Document doc1 = new Document();
    Document doc2 = new Document();
    DocHelper.setupDoc(doc1);
    DocHelper.setupDoc(doc2);
    DocHelper.writeDoc(random(), dir, doc1);
    DocHelper.writeDoc(random(), dir, doc2);
    DirectoryReader reader = DirectoryReader.open(dir);
    assertTrue(reader != null);
    assertTrue(reader instanceof StandardDirectoryReader);
    
    Document newDoc1 = reader.document(0);
    assertTrue(newDoc1 != null);
    assertTrue(DocHelper.numFields(newDoc1) == DocHelper.numFields(doc1) - DocHelper.unstored.size());
    Document newDoc2 = reader.document(1);
    assertTrue(newDoc2 != null);
    assertTrue(DocHelper.numFields(newDoc2) == DocHelper.numFields(doc2) - DocHelper.unstored.size());
    Terms vector = reader.getTermVectors(0).terms(DocHelper.TEXT_FIELD_2_KEY);
    assertNotNull(vector);

    reader.close();
    if (readers[0] != null) readers[0].close();
    if (readers[1] != null) readers[1].close();
    dir.close();
  }
        
  public void testMultiTermDocs() throws IOException {
    Directory ramDir1=newDirectory();
    addDoc(random(), ramDir1, "test foo", true);
    Directory ramDir2=newDirectory();
    addDoc(random(), ramDir2, "test blah", true);
    Directory ramDir3=newDirectory();
    addDoc(random(), ramDir3, "test wow", true);

    IndexReader[] readers1 = new IndexReader[]{DirectoryReader.open(ramDir1), DirectoryReader.open(ramDir3)};
    IndexReader[] readers2 = new IndexReader[]{DirectoryReader.open(ramDir1), DirectoryReader.open(ramDir2), DirectoryReader.open(ramDir3)};
    MultiReader mr2 = new MultiReader(readers1);
    MultiReader mr3 = new MultiReader(readers2);

    // test mixing up TermDocs and TermEnums from different readers.
    TermsEnum te2 = MultiFields.getTerms(mr2, "body").iterator(null);
    te2.seekCeil(new BytesRef("wow"));
    DocsEnum td = _TestUtil.docs(random(), mr2,
                                 "body",
                                 te2.term(),
                                 MultiFields.getLiveDocs(mr2),
                                 null,
                                 0);

    TermsEnum te3 = MultiFields.getTerms(mr3, "body").iterator(null);
    te3.seekCeil(new BytesRef("wow"));
    td = _TestUtil.docs(random(), te3, MultiFields.getLiveDocs(mr3),
                        td,
                        0);
    
    int ret = 0;

    // This should blow up if we forget to check that the TermEnum is from the same
    // reader as the TermDocs.
    while (td.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) ret += td.docID();

    // really a dummy assert to ensure that we got some docs and to ensure that
    // nothing is eliminated by hotspot
    assertTrue(ret > 0);
    readers1[0].close();
    readers1[1].close();
    readers2[0].close();
    readers2[1].close();
    readers2[2].close();
    ramDir1.close();
    ramDir2.close();
    ramDir3.close();
  }

  private void addDoc(Random random, Directory ramDir1, String s, boolean create) throws IOException {
    IndexWriter iw = new IndexWriter(ramDir1, newIndexWriterConfig( 
        TEST_VERSION_CURRENT, 
        new MockAnalyzer(random)).setOpenMode(
        create ? OpenMode.CREATE : OpenMode.APPEND));
    Document doc = new Document();
    doc.add(newTextField("body", s, Field.Store.NO));
    iw.addDocument(doc);
    iw.close();
  }
  
  public void testIsCurrent() throws Exception {
    Directory d = newDirectory();
    IndexWriter writer = new IndexWriter(d, newIndexWriterConfig( 
      TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    addDocumentWithFields(writer);
    writer.close();
    // set up reader:
    DirectoryReader reader = DirectoryReader.open(d);
    assertTrue(reader.isCurrent());
    // modify index by adding another document:
    writer = new IndexWriter(d, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    addDocumentWithFields(writer);
    writer.close();
    assertFalse(reader.isCurrent());
    // re-create index:
    writer = new IndexWriter(d, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random())).setOpenMode(OpenMode.CREATE));
    addDocumentWithFields(writer);
    writer.close();
    assertFalse(reader.isCurrent());
    reader.close();
    d.close();
  }

  /**
   * Tests the IndexReader.getFieldNames implementation
   * @throws Exception on error
   */
  public void testGetFieldNames() throws Exception {
      Directory d = newDirectory();
      // set up writer
      IndexWriter writer = new IndexWriter(
          d,
          newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
      );

      Document doc = new Document();

      FieldType customType3 = new FieldType();
      customType3.setStored(true);
      
      doc.add(new StringField("keyword", "test1", Field.Store.YES));
      doc.add(new TextField("text", "test1", Field.Store.YES));
      doc.add(new Field("unindexed", "test1", customType3));
      doc.add(new TextField("unstored","test1", Field.Store.NO));
      writer.addDocument(doc);

      writer.close();
      // set up reader
      DirectoryReader reader = DirectoryReader.open(d);
      FieldInfos fieldInfos = MultiFields.getMergedFieldInfos(reader);
      assertNotNull(fieldInfos.fieldInfo("keyword"));
      assertNotNull(fieldInfos.fieldInfo("text"));
      assertNotNull(fieldInfos.fieldInfo("unindexed"));
      assertNotNull(fieldInfos.fieldInfo("unstored"));
      reader.close();
      // add more documents
      writer = new IndexWriter(
          d,
          newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
              setOpenMode(OpenMode.APPEND).
              setMergePolicy(newLogMergePolicy())
      );
      // want to get some more segments here
      int mergeFactor = ((LogMergePolicy) writer.getConfig().getMergePolicy()).getMergeFactor();
      for (int i = 0; i < 5*mergeFactor; i++) {
        doc = new Document();
        doc.add(new StringField("keyword", "test1", Field.Store.YES));
        doc.add(new TextField("text", "test1", Field.Store.YES));
        doc.add(new Field("unindexed", "test1", customType3));
        doc.add(new TextField("unstored","test1", Field.Store.NO));
        writer.addDocument(doc);
      }
      // new fields are in some different segments (we hope)
      for (int i = 0; i < 5*mergeFactor; i++) {
        doc = new Document();
        doc.add(new StringField("keyword2", "test1", Field.Store.YES));
        doc.add(new TextField("text2", "test1", Field.Store.YES));
        doc.add(new Field("unindexed2", "test1", customType3));
        doc.add(new TextField("unstored2","test1", Field.Store.NO));
        writer.addDocument(doc);
      }
      // new termvector fields

      FieldType customType5 = new FieldType(TextField.TYPE_STORED);
      customType5.setStoreTermVectors(true);
      FieldType customType6 = new FieldType(TextField.TYPE_STORED);
      customType6.setStoreTermVectors(true);
      customType6.setStoreTermVectorOffsets(true);
      FieldType customType7 = new FieldType(TextField.TYPE_STORED);
      customType7.setStoreTermVectors(true);
      customType7.setStoreTermVectorPositions(true);
      FieldType customType8 = new FieldType(TextField.TYPE_STORED);
      customType8.setStoreTermVectors(true);
      customType8.setStoreTermVectorOffsets(true);
      customType8.setStoreTermVectorPositions(true);
      
      for (int i = 0; i < 5*mergeFactor; i++) {
        doc = new Document();
        doc.add(new TextField("tvnot", "tvnot", Field.Store.YES));
        doc.add(new Field("termvector", "termvector", customType5));
        doc.add(new Field("tvoffset", "tvoffset", customType6));
        doc.add(new Field("tvposition", "tvposition", customType7));
        doc.add(new Field("tvpositionoffset", "tvpositionoffset", customType8));
        writer.addDocument(doc);
      }
      
      writer.close();

      // verify fields again
      reader = DirectoryReader.open(d);
      fieldInfos = MultiFields.getMergedFieldInfos(reader);

      Collection<String> allFieldNames = new HashSet<String>();
      Collection<String> indexedFieldNames = new HashSet<String>();
      Collection<String> notIndexedFieldNames = new HashSet<String>();
      Collection<String> tvFieldNames = new HashSet<String>();

      for(FieldInfo fieldInfo : fieldInfos) {
        final String name = fieldInfo.name;
        allFieldNames.add(name);
        if (fieldInfo.isIndexed()) {
          indexedFieldNames.add(name);
        } else {
          notIndexedFieldNames.add(name);
        }
        if (fieldInfo.hasVectors()) {
          tvFieldNames.add(name);
        }
      }

      assertTrue(allFieldNames.contains("keyword"));
      assertTrue(allFieldNames.contains("text"));
      assertTrue(allFieldNames.contains("unindexed"));
      assertTrue(allFieldNames.contains("unstored"));
      assertTrue(allFieldNames.contains("keyword2"));
      assertTrue(allFieldNames.contains("text2"));
      assertTrue(allFieldNames.contains("unindexed2"));
      assertTrue(allFieldNames.contains("unstored2"));
      assertTrue(allFieldNames.contains("tvnot"));
      assertTrue(allFieldNames.contains("termvector"));
      assertTrue(allFieldNames.contains("tvposition"));
      assertTrue(allFieldNames.contains("tvoffset"));
      assertTrue(allFieldNames.contains("tvpositionoffset"));
      
      // verify that only indexed fields were returned
      assertEquals(11, indexedFieldNames.size());    // 6 original + the 5 termvector fields 
      assertTrue(indexedFieldNames.contains("keyword"));
      assertTrue(indexedFieldNames.contains("text"));
      assertTrue(indexedFieldNames.contains("unstored"));
      assertTrue(indexedFieldNames.contains("keyword2"));
      assertTrue(indexedFieldNames.contains("text2"));
      assertTrue(indexedFieldNames.contains("unstored2"));
      assertTrue(indexedFieldNames.contains("tvnot"));
      assertTrue(indexedFieldNames.contains("termvector"));
      assertTrue(indexedFieldNames.contains("tvposition"));
      assertTrue(indexedFieldNames.contains("tvoffset"));
      assertTrue(indexedFieldNames.contains("tvpositionoffset"));
      
      // verify that only unindexed fields were returned
      assertEquals(2, notIndexedFieldNames.size());    // the following fields
      assertTrue(notIndexedFieldNames.contains("unindexed"));
      assertTrue(notIndexedFieldNames.contains("unindexed2"));
              
      // verify index term vector fields  
      assertEquals(tvFieldNames.toString(), 4, tvFieldNames.size());    // 4 field has term vector only
      assertTrue(tvFieldNames.contains("termvector"));

      reader.close();
      d.close();
  }

public void testTermVectors() throws Exception {
  Directory d = newDirectory();
  // set up writer
  IndexWriter writer = new IndexWriter(
      d,
      newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
          setMergePolicy(newLogMergePolicy())
  );
  // want to get some more segments here
  // new termvector fields
  int mergeFactor = ((LogMergePolicy) writer.getConfig().getMergePolicy()).getMergeFactor();
  FieldType customType5 = new FieldType(TextField.TYPE_STORED);
  customType5.setStoreTermVectors(true);
  FieldType customType6 = new FieldType(TextField.TYPE_STORED);
  customType6.setStoreTermVectors(true);
  customType6.setStoreTermVectorOffsets(true);
  FieldType customType7 = new FieldType(TextField.TYPE_STORED);
  customType7.setStoreTermVectors(true);
  customType7.setStoreTermVectorPositions(true);
  FieldType customType8 = new FieldType(TextField.TYPE_STORED);
  customType8.setStoreTermVectors(true);
  customType8.setStoreTermVectorOffsets(true);
  customType8.setStoreTermVectorPositions(true);
  for (int i = 0; i < 5 * mergeFactor; i++) {
    Document doc = new Document();
      doc.add(new TextField("tvnot", "one two two three three three", Field.Store.YES));
      doc.add(new Field("termvector", "one two two three three three", customType5));
      doc.add(new Field("tvoffset", "one two two three three three", customType6));
      doc.add(new Field("tvposition", "one two two three three three", customType7));
      doc.add(new Field("tvpositionoffset", "one two two three three three", customType8));
      
      writer.addDocument(doc);
  }
  writer.close();
  d.close();
}

void assertTermDocsCount(String msg,
                                   IndexReader reader,
                                   Term term,
                                   int expected)
  throws IOException {
  DocsEnum tdocs = _TestUtil.docs(random(), reader,
                                  term.field(),
                                  new BytesRef(term.text()),
                                  MultiFields.getLiveDocs(reader),
                                  null,
                                  0);
  int count = 0;
  if (tdocs != null) {
    while(tdocs.nextDoc()!= DocIdSetIterator.NO_MORE_DOCS) {
      count++;
    }
  }
  assertEquals(msg + ", count mismatch", expected, count);
}

  
  public void testBinaryFields() throws IOException {
      Directory dir = newDirectory();
      byte[] bin = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
      
      IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
      
      for (int i = 0; i < 10; i++) {
        addDoc(writer, "document number " + (i + 1));
        addDocumentWithFields(writer);
        addDocumentWithDifferentFields(writer);
        addDocumentWithTermVectorFields(writer);
      }
      writer.close();
      writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND).setMergePolicy(newLogMergePolicy()));
      Document doc = new Document();
      doc.add(new StoredField("bin1", bin));
      doc.add(new TextField("junk", "junk text", Field.Store.NO));
      writer.addDocument(doc);
      writer.close();
      DirectoryReader reader = DirectoryReader.open(dir);
      Document doc2 = reader.document(reader.maxDoc() - 1);
      IndexableField[] fields = doc2.getFields("bin1");
      assertNotNull(fields);
      assertEquals(1, fields.length);
      IndexableField b1 = fields[0];
      assertTrue(b1.binaryValue() != null);
      BytesRef bytesRef = b1.binaryValue();
      assertEquals(bin.length, bytesRef.length);
      for (int i = 0; i < bin.length; i++) {
        assertEquals(bin[i], bytesRef.bytes[i + bytesRef.offset]);
      }
      reader.close();
      // force merge


      writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND).setMergePolicy(newLogMergePolicy()));
      writer.forceMerge(1);
      writer.close();
      reader = DirectoryReader.open(dir);
      doc2 = reader.document(reader.maxDoc() - 1);
      fields = doc2.getFields("bin1");
      assertNotNull(fields);
      assertEquals(1, fields.length);
      b1 = fields[0];
      assertTrue(b1.binaryValue() != null);
      bytesRef = b1.binaryValue();
      assertEquals(bin.length, bytesRef.length);
      for (int i = 0; i < bin.length; i++) {
        assertEquals(bin[i], bytesRef.bytes[i + bytesRef.offset]);
      }
      reader.close();
      dir.close();
  }

  /* ??? public void testOpenEmptyDirectory() throws IOException{
    String dirName = "test.empty";
    File fileDirName = new File(dirName);
    if (!fileDirName.exists()) {
      fileDirName.mkdir();
    }
    try {
      DirectoryReader.open(fileDirName);
      fail("opening DirectoryReader on empty directory failed to produce FileNotFoundException");
    } catch (FileNotFoundException e) {
      // GOOD
    }
    rmDir(fileDirName);
  }*/
  
public void testFilesOpenClose() throws IOException {
      // Create initial data set
      File dirFile = _TestUtil.getTempDir("TestIndexReader.testFilesOpenClose");
      Directory dir = newFSDirectory(dirFile);
      IndexWriter writer  = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      addDoc(writer, "test");
      writer.close();
      dir.close();

      // Try to erase the data - this ensures that the writer closed all files
      _TestUtil.rmDir(dirFile);
      dir = newFSDirectory(dirFile);

      // Now create the data set again, just as before
      writer  = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.CREATE));
      addDoc(writer, "test");
      writer.close();
      dir.close();

      // Now open existing directory and test that reader closes all files
      dir = newFSDirectory(dirFile);
      DirectoryReader reader1 = DirectoryReader.open(dir);
      reader1.close();
      dir.close();

      // The following will fail if reader did not close
      // all files
      _TestUtil.rmDir(dirFile);
  }

  public void testOpenReaderAfterDelete() throws IOException {
    File dirFile = _TestUtil.getTempDir("deletetest");
    Directory dir = newFSDirectory(dirFile);
    try {
      DirectoryReader.open(dir);
      fail("expected FileNotFoundException");
    } catch (FileNotFoundException e) {
      // expected
    }

    dirFile.delete();

    // Make sure we still get a CorruptIndexException (not NPE):
    try {
      DirectoryReader.open(dir);
      fail("expected FileNotFoundException");
    } catch (FileNotFoundException e) {
      // expected
    }
    
    dir.close();
  }

  static void addDocumentWithFields(IndexWriter writer) throws IOException
  {
      Document doc = new Document();
      
      FieldType customType3 = new FieldType();
      customType3.setStored(true);
      doc.add(newStringField("keyword", "test1", Field.Store.YES));
      doc.add(newTextField("text", "test1", Field.Store.YES));
      doc.add(newField("unindexed", "test1", customType3));
      doc.add(new TextField("unstored","test1", Field.Store.NO));
      writer.addDocument(doc);
  }

  static void addDocumentWithDifferentFields(IndexWriter writer) throws IOException
  {
    Document doc = new Document();
    
    FieldType customType3 = new FieldType();
    customType3.setStored(true);
    doc.add(newStringField("keyword2", "test1", Field.Store.YES));
    doc.add(newTextField("text2", "test1", Field.Store.YES));
    doc.add(newField("unindexed2", "test1", customType3));
    doc.add(new TextField("unstored2","test1", Field.Store.NO));
    writer.addDocument(doc);
  }

  static void addDocumentWithTermVectorFields(IndexWriter writer) throws IOException
  {
      Document doc = new Document();
      FieldType customType5 = new FieldType(TextField.TYPE_STORED);
      customType5.setStoreTermVectors(true);
      FieldType customType6 = new FieldType(TextField.TYPE_STORED);
      customType6.setStoreTermVectors(true);
      customType6.setStoreTermVectorOffsets(true);
      FieldType customType7 = new FieldType(TextField.TYPE_STORED);
      customType7.setStoreTermVectors(true);
      customType7.setStoreTermVectorPositions(true);
      FieldType customType8 = new FieldType(TextField.TYPE_STORED);
      customType8.setStoreTermVectors(true);
      customType8.setStoreTermVectorOffsets(true);
      customType8.setStoreTermVectorPositions(true);
      doc.add(newTextField("tvnot", "tvnot", Field.Store.YES));
      doc.add(newField("termvector","termvector",customType5));
      doc.add(newField("tvoffset","tvoffset", customType6));
      doc.add(newField("tvposition","tvposition", customType7));
      doc.add(newField("tvpositionoffset","tvpositionoffset", customType8));
      
      writer.addDocument(doc);
  }
  
  static void addDoc(IndexWriter writer, String value) throws IOException {
      Document doc = new Document();
      doc.add(newTextField("content", value, Field.Store.NO));
      writer.addDocument(doc);
  }

  // TODO: maybe this can reuse the logic of test dueling codecs?
  public static void assertIndexEquals(DirectoryReader index1, DirectoryReader index2) throws IOException {
    assertEquals("IndexReaders have different values for numDocs.", index1.numDocs(), index2.numDocs());
    assertEquals("IndexReaders have different values for maxDoc.", index1.maxDoc(), index2.maxDoc());
    assertEquals("Only one IndexReader has deletions.", index1.hasDeletions(), index2.hasDeletions());
    assertEquals("Single segment test differs.", index1.getSequentialSubReaders().size() == 1, index2.getSequentialSubReaders().size() == 1);
    
    // check field names
    FieldInfos fieldInfos1 = MultiFields.getMergedFieldInfos(index1);
    FieldInfos fieldInfos2 = MultiFields.getMergedFieldInfos(index2);
    assertEquals("IndexReaders have different numbers of fields.", fieldInfos1.size(), fieldInfos2.size());
    final int numFields = fieldInfos1.size();
    for(int fieldID=0;fieldID<numFields;fieldID++) {
      final FieldInfo fieldInfo1 = fieldInfos1.fieldInfo(fieldID);
      final FieldInfo fieldInfo2 = fieldInfos2.fieldInfo(fieldID);
      assertEquals("Different field names.", fieldInfo1.name, fieldInfo2.name);
    }
    
    // check norms
    for(FieldInfo fieldInfo : fieldInfos1) {
      String curField = fieldInfo.name;
      DocValues norms1 = MultiDocValues.getNormDocValues(index1, curField);
      DocValues norms2 = MultiDocValues.getNormDocValues(index2, curField);
      if (norms1 != null && norms2 != null)
      {
        // todo: generalize this (like TestDuelingCodecs assert)
        byte[] b1 = (byte[]) norms1.getSource().getArray();
        byte[] b2 = (byte[]) norms2.getSource().getArray();
        assertEquals(b1.length, b2.length);
        for (int i = 0; i < b1.length; i++) {
          assertEquals("Norm different for doc " + i + " and field '" + curField + "'.", b1[i], b2[i]);
        }
      }
      else
      {
        assertSame(norms1, norms2);
      }
    }
    
    // check deletions
    final Bits liveDocs1 = MultiFields.getLiveDocs(index1);
    final Bits liveDocs2 = MultiFields.getLiveDocs(index2);
    for (int i = 0; i < index1.maxDoc(); i++) {
      assertEquals("Doc " + i + " only deleted in one index.",
                   liveDocs1 == null || !liveDocs1.get(i),
                   liveDocs2 == null || !liveDocs2.get(i));
    }
    
    // check stored fields
    for (int i = 0; i < index1.maxDoc(); i++) {
      if (liveDocs1 == null || liveDocs1.get(i)) {
        Document doc1 = index1.document(i);
        Document doc2 = index2.document(i);
        List<IndexableField> field1 = doc1.getFields();
        List<IndexableField> field2 = doc2.getFields();
        assertEquals("Different numbers of fields for doc " + i + ".", field1.size(), field2.size());
        Iterator<IndexableField> itField1 = field1.iterator();
        Iterator<IndexableField> itField2 = field2.iterator();
        while (itField1.hasNext()) {
          Field curField1 = (Field) itField1.next();
          Field curField2 = (Field) itField2.next();
          assertEquals("Different fields names for doc " + i + ".", curField1.name(), curField2.name());
          assertEquals("Different field values for doc " + i + ".", curField1.stringValue(), curField2.stringValue());
        }          
      }
    }
    
    // check dictionary and posting lists
    FieldsEnum fenum1 = MultiFields.getFields(index1).iterator();
    FieldsEnum fenum2 = MultiFields.getFields(index1).iterator();
    String field1 = null;
    Bits liveDocs = MultiFields.getLiveDocs(index1);
    while((field1=fenum1.next()) != null) {
      assertEquals("Different fields", field1, fenum2.next());
      Terms terms1 = fenum1.terms();
      if (terms1 == null) {
        assertNull(fenum2.terms());
        continue;
      }
      TermsEnum enum1 = terms1.iterator(null);

      Terms terms2 = fenum2.terms();
      assertNotNull(terms2);
      TermsEnum enum2 = terms2.iterator(null);

      while(enum1.next() != null) {
        assertEquals("Different terms", enum1.term(), enum2.next());
        DocsAndPositionsEnum tp1 = enum1.docsAndPositions(liveDocs, null);
        DocsAndPositionsEnum tp2 = enum2.docsAndPositions(liveDocs, null);

        while(tp1.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
          assertTrue(tp2.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
          assertEquals("Different doc id in postinglist of term " + enum1.term() + ".", tp1.docID(), tp2.docID());
          assertEquals("Different term frequence in postinglist of term " + enum1.term() + ".", tp1.freq(), tp2.freq());
          for (int i = 0; i < tp1.freq(); i++) {
            assertEquals("Different positions in postinglist of term " + enum1.term() + ".", tp1.nextPosition(), tp2.nextPosition());
          }
        }
      }
    }
  }

  public void testGetIndexCommit() throws IOException {

    Directory d = newDirectory();

    // set up writer
    IndexWriter writer = new IndexWriter(
        d,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setMaxBufferedDocs(2).
            setMergePolicy(newLogMergePolicy(10))
    );
    for(int i=0;i<27;i++)
      addDocumentWithFields(writer);
    writer.close();

    SegmentInfos sis = new SegmentInfos();
    sis.read(d);
    DirectoryReader r = DirectoryReader.open(d);
    IndexCommit c = r.getIndexCommit();

    assertEquals(sis.getSegmentsFileName(), c.getSegmentsFileName());

    assertTrue(c.equals(r.getIndexCommit()));

    // Change the index
    writer = new IndexWriter(
        d,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(2).
            setMergePolicy(newLogMergePolicy(10))
    );
    for(int i=0;i<7;i++)
      addDocumentWithFields(writer);
    writer.close();

    DirectoryReader r2 = DirectoryReader.openIfChanged(r);
    assertNotNull(r2);
    assertFalse(c.equals(r2.getIndexCommit()));
    assertFalse(r2.getIndexCommit().getSegmentCount() == 1);
    r2.close();

    writer = new IndexWriter(d, newIndexWriterConfig(TEST_VERSION_CURRENT,
      new MockAnalyzer(random()))
      .setOpenMode(OpenMode.APPEND));
    writer.forceMerge(1);
    writer.close();

    r2 = DirectoryReader.openIfChanged(r);
    assertNotNull(r2);
    assertNull(DirectoryReader.openIfChanged(r2));
    assertEquals(1, r2.getIndexCommit().getSegmentCount());

    r.close();
    r2.close();
    d.close();
  }      

  static Document createDocument(String id) {
    Document doc = new Document();
    FieldType customType = new FieldType(TextField.TYPE_STORED);
    customType.setTokenized(false);
    customType.setOmitNorms(true);
    
    doc.add(newField("id", id, customType));
    return doc;
  }
  
  // LUCENE-1468 -- make sure on attempting to open an
  // DirectoryReader on a non-existent directory, you get a
  // good exception
  public void testNoDir() throws Throwable {
    Directory dir = newFSDirectory(_TestUtil.getTempDir("doesnotexist"));
    try {
      DirectoryReader.open(dir);
      fail("did not hit expected exception");
    } catch (NoSuchDirectoryException nsde) {
      // expected
    }
    dir.close();
  }
  
  // LUCENE-1509
  public void testNoDupCommitFileNames() throws Throwable {
  
    Directory dir = newDirectory();
    
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random()))
        .setMaxBufferedDocs(2));
    writer.addDocument(createDocument("a"));
    writer.addDocument(createDocument("a"));
    writer.addDocument(createDocument("a"));
    writer.close();
    
    Collection<IndexCommit> commits = DirectoryReader.listCommits(dir);
    for (final IndexCommit commit : commits) {
      Collection<String> files = commit.getFileNames();
      HashSet<String> seen = new HashSet<String>();
      for (final String fileName : files) { 
        assertTrue("file " + fileName + " was duplicated", !seen.contains(fileName));
        seen.add(fileName);
      }
    }
  
    dir.close();
  }
  
  // LUCENE-1579: Ensure that on a reopened reader, that any
  // shared segments reuse the doc values arrays in
  // FieldCache
  public void testFieldCacheReuseAfterReopen() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setMergePolicy(newLogMergePolicy(10))
    );
    Document doc = new Document();
    doc.add(newStringField("number", "17", Field.Store.NO));
    writer.addDocument(doc);
    writer.commit();
  
    // Open reader1
    DirectoryReader r = DirectoryReader.open(dir);
    AtomicReader r1 = getOnlySegmentReader(r);
    final int[] ints = FieldCache.DEFAULT.getInts(r1, "number", false);
    assertEquals(1, ints.length);
    assertEquals(17, ints[0]);
  
    // Add new segment
    writer.addDocument(doc);
    writer.commit();
  
    // Reopen reader1 --> reader2
    DirectoryReader r2 = DirectoryReader.openIfChanged(r);
    assertNotNull(r2);
    r.close();
    AtomicReader sub0 = r2.getSequentialSubReaders().get(0);
    final int[] ints2 = FieldCache.DEFAULT.getInts(sub0, "number", false);
    r2.close();
    assertTrue(ints == ints2);
  
    writer.close();
    dir.close();
  }
  
  // LUCENE-1586: getUniqueTermCount
  public void testUniqueTermCount() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    Document doc = new Document();
    doc.add(newTextField("field", "a b c d e f g h i j k l m n o p q r s t u v w x y z", Field.Store.NO));
    doc.add(newTextField("number", "0 1 2 3 4 5 6 7 8 9", Field.Store.NO));
    writer.addDocument(doc);
    writer.addDocument(doc);
    writer.commit();
  
    DirectoryReader r = DirectoryReader.open(dir);
    AtomicReader r1 = getOnlySegmentReader(r);
    assertEquals(36, r1.getUniqueTermCount());
    writer.addDocument(doc);
    writer.commit();
    DirectoryReader r2 = DirectoryReader.openIfChanged(r);
    assertNotNull(r2);
    r.close();
  
    List<? extends AtomicReader> subs = r2.getSequentialSubReaders();
    for(AtomicReader s : subs) {
      assertEquals(36, s.getUniqueTermCount());
    }
    r2.close();
    writer.close();
    dir.close();
  }
  
  // LUCENE-1609: don't load terms index
  public void testNoTermsIndex() throws Throwable {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setCodec(_TestUtil.alwaysPostingsFormat(new Lucene40PostingsFormat())));
    Document doc = new Document();
    doc.add(newTextField("field", "a b c d e f g h i j k l m n o p q r s t u v w x y z", Field.Store.NO));
    doc.add(newTextField("number", "0 1 2 3 4 5 6 7 8 9", Field.Store.NO));
    writer.addDocument(doc);
    writer.addDocument(doc);
    writer.close();
  
    DirectoryReader r = DirectoryReader.open(dir, -1);
    try {
      r.docFreq(new Term("field", "f"));
      fail("did not hit expected exception");
    } catch (IllegalStateException ise) {
      // expected
    }
  
    assertEquals(-1, ((SegmentReader) r.getSequentialSubReaders().get(0)).getTermInfosIndexDivisor());
    writer = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setCodec(_TestUtil.alwaysPostingsFormat(new Lucene40PostingsFormat())).
            setMergePolicy(newLogMergePolicy(10))
    );
    writer.addDocument(doc);
    writer.close();
  
    // LUCENE-1718: ensure re-open carries over no terms index:
    DirectoryReader r2 = DirectoryReader.openIfChanged(r);
    assertNotNull(r2);
    assertNull(DirectoryReader.openIfChanged(r2));
    r.close();
    List<? extends AtomicReader> subReaders = r2.getSequentialSubReaders();
    assertEquals(2, subReaders.size());
    for(AtomicReader s : subReaders) {
      try {
        s.docFreq(new Term("field", "f"));
        fail("did not hit expected exception");
      } catch (IllegalStateException ise) {
        // expected
      }
    }
    r2.close();
    dir.close();
  }
  
  // LUCENE-2046
  public void testPrepareCommitIsCurrent() throws Throwable {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig( 
        TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    writer.commit();
    Document doc = new Document();
    writer.addDocument(doc);
    DirectoryReader r = DirectoryReader.open(dir);
    assertTrue(r.isCurrent());
    writer.addDocument(doc);
    writer.prepareCommit();
    assertTrue(r.isCurrent());
    DirectoryReader r2 = DirectoryReader.openIfChanged(r);
    assertNull(r2);
    writer.commit();
    assertFalse(r.isCurrent());
    writer.close();
    r.close();
    dir.close();
  }
  
  // LUCENE-2753
  public void testListCommits() throws Exception {
    Directory dir = newDirectory();
    SnapshotDeletionPolicy sdp = new SnapshotDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy());
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig( 
        TEST_VERSION_CURRENT, null).setIndexDeletionPolicy(sdp));
    writer.addDocument(new Document());
    writer.commit();
    sdp.snapshot("c1");
    writer.addDocument(new Document());
    writer.commit();
    sdp.snapshot("c2");
    writer.addDocument(new Document());
    writer.commit();
    sdp.snapshot("c3");
    writer.close();
    long currentGen = 0;
    for (IndexCommit ic : DirectoryReader.listCommits(dir)) {
      assertTrue("currentGen=" + currentGen + " commitGen=" + ic.getGeneration(), currentGen < ic.getGeneration());
      currentGen = ic.getGeneration();
    }
    dir.close();
  }
  
  // LUCENE-2812
  public void testIndexExists() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    writer.addDocument(new Document());
    writer.prepareCommit();
    assertFalse(DirectoryReader.indexExists(dir));
    writer.commit();
    writer.close();
    assertTrue(DirectoryReader.indexExists(dir));
    dir.close();
  }
  
  // Make sure totalTermFreq works correctly in the terms
  // dict cache
  public void testTotalTermFreqCached() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    Document d = new Document();
    d.add(newTextField("f", "a a b", Field.Store.NO));
    writer.addDocument(d);
    DirectoryReader r = writer.getReader();
    writer.close();
    try {
      // Make sure codec impls totalTermFreq (eg PreFlex doesn't)
      Assume.assumeTrue(MultiFields.totalTermFreq(r, "f", new BytesRef("b")) != -1);
      assertEquals(1, MultiFields.totalTermFreq(r, "f", new BytesRef("b")));
      assertEquals(2, MultiFields.totalTermFreq(r, "f", new BytesRef("a")));
      assertEquals(1, MultiFields.totalTermFreq(r, "f", new BytesRef("b")));
    } finally {
      r.close();
      dir.close();
    }
  }
  
  // LUCENE-2474
  public void testReaderFinishedListener() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(3);
    writer.addDocument(new Document());
    writer.commit();
    writer.addDocument(new Document());
    writer.commit();
    final DirectoryReader reader = writer.getReader();
    final int[] closeCount = new int[1];
    final IndexReader.ReaderClosedListener listener = new IndexReader.ReaderClosedListener() {
      public void onClose(IndexReader reader) {
        closeCount[0]++;
      }
    };
  
    reader.addReaderClosedListener(listener);
  
    reader.close();
  
    // Close the top reader, its the only one that should be closed
    assertEquals(1, closeCount[0]);
    writer.close();
  
    DirectoryReader reader2 = DirectoryReader.open(dir);
    reader2.addReaderClosedListener(listener);
  
    closeCount[0] = 0;
    reader2.close();
    assertEquals(1, closeCount[0]);
    dir.close();
  }
  
  public void testOOBDocID() throws Exception {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    writer.addDocument(new Document());
    DirectoryReader r = writer.getReader();
    writer.close();
    r.document(0);
    try {
      r.document(1);
      fail("did not hit exception");
    } catch (IllegalArgumentException iae) {
      // expected
    }
    r.close();
    dir.close();
  }
  
  public void testTryIncRef() throws IOException {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    writer.addDocument(new Document());
    writer.commit();
    DirectoryReader r = DirectoryReader.open(dir);
    assertTrue(r.tryIncRef());
    r.decRef();
    r.close();
    assertFalse(r.tryIncRef());
    writer.close();
    dir.close();
  }
  
  public void testStressTryIncRef() throws IOException, InterruptedException {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    writer.addDocument(new Document());
    writer.commit();
    DirectoryReader r = DirectoryReader.open(dir);
    int numThreads = atLeast(2);
    
    IncThread[] threads = new IncThread[numThreads];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new IncThread(r, random());
      threads[i].start();
    }
    Thread.sleep(100);
  
    assertTrue(r.tryIncRef());
    r.decRef();
    r.close();
  
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
      assertNull(threads[i].failed);
    }
    assertFalse(r.tryIncRef());
    writer.close();
    dir.close();
  }
  
  static class IncThread extends Thread {
    final IndexReader toInc;
    final Random random;
    Throwable failed;
    
    IncThread(IndexReader toInc, Random random) {
      this.toInc = toInc;
      this.random = random;
    }
    
    @Override
    public void run() {
      try {
        while (toInc.tryIncRef()) {
          assertFalse(toInc.hasDeletions());
          toInc.decRef();
        }
        assertFalse(toInc.tryIncRef());
      } catch (Throwable e) {
        failed = e;
      }
    }
  }
  
  public void testLoadCertainFields() throws Exception {
    Directory dir = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir);
    Document doc = new Document();
    doc.add(newStringField("field1", "foobar", Field.Store.YES));
    doc.add(newStringField("field2", "foobaz", Field.Store.YES));
    writer.addDocument(doc);
    DirectoryReader r = writer.getReader();
    writer.close();
    Set<String> fieldsToLoad = new HashSet<String>();
    assertEquals(0, r.document(0, fieldsToLoad).getFields().size());
    fieldsToLoad.add("field1");
    Document doc2 = r.document(0, fieldsToLoad);
    assertEquals(1, doc2.getFields().size());
    assertEquals("foobar", doc2.get("field1"));
    r.close();
    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1007.java