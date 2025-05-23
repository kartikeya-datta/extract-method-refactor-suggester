error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10169.java
text:
```scala
Q@@uery q = getFieldQuerySingle(mField, queryText, quoted);

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.lucene.queryparser.classic;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.automaton.RegExp;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.support.QueryParsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.elasticsearch.common.lucene.search.Queries.fixNegativeQueryIfNeeded;
import static org.elasticsearch.common.lucene.search.Queries.optimizeQuery;
import static org.elasticsearch.index.query.support.QueryParsers.wrapSmartNameQuery;

/**
 * A query parser that uses the {@link MapperService} in order to build smarter
 * queries based on the mapping information.
 * <p/>
 * <p>Also breaks fields with [type].[name] into a boolean query that must include the type
 * as well as the query on the name.
 */
public class MapperQueryParser extends QueryParser {

    public static final ImmutableMap<String, FieldQueryExtension> fieldQueryExtensions;

    static {
        fieldQueryExtensions = ImmutableMap.<String, FieldQueryExtension>builder()
                .put(ExistsFieldQueryExtension.NAME, new ExistsFieldQueryExtension())
                .put(MissingFieldQueryExtension.NAME, new MissingFieldQueryExtension())
                .build();
    }

    private final QueryParseContext parseContext;

    private QueryParserSettings settings;

    private Analyzer quoteAnalyzer;

    private boolean forcedAnalyzer;
    private boolean forcedQuoteAnalyzer;

    private FieldMapper currentMapper;

    private boolean analyzeWildcard;

    private String quoteFieldSuffix;

    public MapperQueryParser(QueryParseContext parseContext) {
        super(Lucene.QUERYPARSER_VERSION, null, null);
        this.parseContext = parseContext;
    }

    public MapperQueryParser(QueryParserSettings settings, QueryParseContext parseContext) {
        super(Lucene.QUERYPARSER_VERSION, settings.defaultField(), settings.defaultAnalyzer());
        this.parseContext = parseContext;
        reset(settings);
    }

    public void reset(QueryParserSettings settings) {
        this.settings = settings;
        this.field = settings.defaultField();

        if (settings.fields() != null) {
            if (settings.fields.size() == 1) {
                // just mark it as the default field
                this.field = settings.fields().get(0);
            } else {
                // otherwise, we need to have the default field being null...
                this.field = null;
            }
        }

        this.forcedAnalyzer = settings.forcedAnalyzer() != null;
        this.analyzer = forcedAnalyzer ? settings.forcedAnalyzer() : settings.defaultAnalyzer();
        if (settings.forcedQuoteAnalyzer() != null) {
            this.forcedQuoteAnalyzer = true;
            this.quoteAnalyzer = settings.forcedQuoteAnalyzer();
        } else if (forcedAnalyzer) {
            this.forcedQuoteAnalyzer = true;
            this.quoteAnalyzer = settings.forcedAnalyzer();
        } else {
            this.forcedAnalyzer = false;
            this.quoteAnalyzer = settings.defaultQuoteAnalyzer();
        }
        this.quoteFieldSuffix = settings.quoteFieldSuffix();
        setMultiTermRewriteMethod(settings.rewriteMethod());
        setEnablePositionIncrements(settings.enablePositionIncrements());
        setAutoGeneratePhraseQueries(settings.autoGeneratePhraseQueries());
        setAllowLeadingWildcard(settings.allowLeadingWildcard());
        setLowercaseExpandedTerms(settings.lowercaseExpandedTerms());
        setPhraseSlop(settings.phraseSlop());
        setDefaultOperator(settings.defaultOperator());
        setFuzzyMinSim(settings.fuzzyMinSim());
        setFuzzyPrefixLength(settings.fuzzyPrefixLength());
        this.analyzeWildcard = settings.analyzeWildcard();
    }

    /**
     * We override this one so we can get the fuzzy part to be treated as string, so people can do: "age:10~5" or "timestamp:2012-10-10~5d"
     */
    @Override
    Query handleBareFuzzy(String qfield, Token fuzzySlop, String termImage) throws ParseException {
        if (fuzzySlop.image.length() == 1) {
            return getFuzzyQuery(qfield, termImage, Float.toString(fuzzyMinSim));
        }
        return getFuzzyQuery(qfield, termImage, fuzzySlop.image.substring(1));
    }

    @Override
    protected Query newTermQuery(Term term) {
        if (currentMapper != null) {
            Query termQuery = currentMapper.queryStringTermQuery(term);
            if (termQuery != null) {
                return termQuery;
            }
        }
        return super.newTermQuery(term);
    }

    @Override
    protected Query newMatchAllDocsQuery() {
        return Queries.newMatchAllQuery();
    }

    @Override
    public Query getFieldQuery(String field, String queryText, boolean quoted) throws ParseException {
        FieldQueryExtension fieldQueryExtension = fieldQueryExtensions.get(field);
        if (fieldQueryExtension != null) {
            return fieldQueryExtension.query(parseContext, queryText);
        }
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (fields.size() == 1) {
                return getFieldQuerySingle(fields.iterator().next(), queryText, quoted);
            }
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = getFieldQuerySingle(mField, queryText, quoted);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = getFieldQuerySingle(mField, queryText, true);
                    if (q != null) {
                        applyBoost(mField, q);
                        clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                    }
                }
                if (clauses.size() == 0)  // happens for stopwords
                    return null;
                return getBooleanQuery(clauses, true);
            }
        } else {
            return getFieldQuerySingle(field, queryText, quoted);
        }
    }

    private Query getFieldQuerySingle(String field, String queryText, boolean quoted) throws ParseException {
        if (!quoted && queryText.length() > 1) {
            if (queryText.charAt(0) == '>') {
                if (queryText.length() > 2) {
                    if (queryText.charAt(1) == '=') {
                        return getRangeQuerySingle(field, queryText.substring(2), null, true, true);
                    }
                }
                return getRangeQuerySingle(field, queryText.substring(1), null, false, true);
            } else if (queryText.charAt(0) == '<') {
                if (queryText.length() > 2) {
                    if (queryText.charAt(1) == '=') {
                        return getRangeQuerySingle(field, null, queryText.substring(2), true, true);
                    }
                }
                return getRangeQuerySingle(field, null, queryText.substring(1), true, false);
            }
        }
        currentMapper = null;
        Analyzer oldAnalyzer = analyzer;
        try {
            MapperService.SmartNameFieldMappers fieldMappers = null;
            if (quoted) {
                analyzer = quoteAnalyzer;
                if (quoteFieldSuffix != null) {
                    fieldMappers = parseContext.smartFieldMappers(field + quoteFieldSuffix);
                }
            }
            if (fieldMappers == null) {
                fieldMappers = parseContext.smartFieldMappers(field);
            }
            if (fieldMappers != null) {
                if (quoted) {
                    if (!forcedQuoteAnalyzer) {
                        analyzer = fieldMappers.searchQuoteAnalyzer();
                    }
                } else {
                    if (!forcedAnalyzer) {
                        analyzer = fieldMappers.searchAnalyzer();
                    }
                }
                currentMapper = fieldMappers.fieldMappers().mapper();
                if (currentMapper != null) {
                    Query query = null;
                    if (currentMapper.useTermQueryWithQueryString()) {
                        try {
                            if (fieldMappers.explicitTypeInNameWithDocMapper()) {
                                String[] previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{fieldMappers.docMapper().type()});
                                try {
                                    query = currentMapper.termQuery(queryText, parseContext);
                                } finally {
                                    QueryParseContext.setTypes(previousTypes);
                                }
                            } else {
                                query = currentMapper.termQuery(queryText, parseContext);
                            }
                        } catch (RuntimeException e) {
                            if (settings.lenient()) {
                                return null;
                            } else {
                                throw e;
                            }
                        }
                    }
                    if (query == null) {
                        query = super.getFieldQuery(currentMapper.names().indexName(), queryText, quoted);
                    }
                    return wrapSmartNameQuery(query, fieldMappers, parseContext);
                }
            }
            return super.getFieldQuery(field, queryText, quoted);
        } finally {
            analyzer = oldAnalyzer;
        }
    }

    @Override
    protected Query getFieldQuery(String field, String queryText, int slop) throws ParseException {
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = super.getFieldQuery(mField, queryText, slop);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        applySlop(q, slop);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = super.getFieldQuery(mField, queryText, slop);
                    if (q != null) {
                        applyBoost(mField, q);
                        applySlop(q, slop);
                        clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                    }
                }
                if (clauses.size() == 0)  // happens for stopwords
                    return null;
                return getBooleanQuery(clauses, true);
            }
        } else {
            return super.getFieldQuery(field, queryText, slop);
        }
    }

    @Override
    protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive, boolean endInclusive) throws ParseException {
        if ("*".equals(part1)) {
            part1 = null;
        }
        if ("*".equals(part2)) {
            part2 = null;
        }

        Collection<String> fields = extractMultiFields(field);

        if (fields == null) {
            return getRangeQuerySingle(field, part1, part2, startInclusive, endInclusive);
        }


        if (fields.size() == 1) {
            return getRangeQuerySingle(fields.iterator().next(), part1, part2, startInclusive, endInclusive);
        }

        if (settings.useDisMax()) {
            DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
            boolean added = false;
            for (String mField : fields) {
                Query q = getRangeQuerySingle(mField, part1, part2, startInclusive, endInclusive);
                if (q != null) {
                    added = true;
                    applyBoost(mField, q);
                    disMaxQuery.add(q);
                }
            }
            if (!added) {
                return null;
            }
            return disMaxQuery;
        } else {
            List<BooleanClause> clauses = new ArrayList<BooleanClause>();
            for (String mField : fields) {
                Query q = getRangeQuerySingle(mField, part1, part2, startInclusive, endInclusive);
                if (q != null) {
                    applyBoost(mField, q);
                    clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                }
            }
            if (clauses.size() == 0)  // happens for stopwords
                return null;
            return getBooleanQuery(clauses, true);
        }
    }

    private Query getRangeQuerySingle(String field, String part1, String part2, boolean startInclusive, boolean endInclusive) {
        currentMapper = null;
        MapperService.SmartNameFieldMappers fieldMappers = parseContext.smartFieldMappers(field);
        if (fieldMappers != null) {
            currentMapper = fieldMappers.fieldMappers().mapper();
            if (currentMapper != null) {

                if (lowercaseExpandedTerms && !currentMapper.isNumeric()) {
                    part1 = part1 == null ? null : part1.toLowerCase(locale);
                    part2 = part2 == null ? null : part2.toLowerCase(locale);
                }

                try {
                    Query rangeQuery = currentMapper.rangeQuery(part1, part2, startInclusive, endInclusive, parseContext);
                    return wrapSmartNameQuery(rangeQuery, fieldMappers, parseContext);
                } catch (RuntimeException e) {
                    if (settings.lenient()) {
                        return null;
                    }
                    throw e;
                }
            }
        }
        return newRangeQuery(field, part1, part2, startInclusive, endInclusive);
    }

    protected Query getFuzzyQuery(String field, String termStr, String minSimilarity) throws ParseException {
        if (lowercaseExpandedTerms) {
            termStr = termStr.toLowerCase(locale);
        }
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (fields.size() == 1) {
                return getFuzzyQuerySingle(fields.iterator().next(), termStr, minSimilarity);
            }
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = getFuzzyQuerySingle(mField, termStr, minSimilarity);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = getFuzzyQuerySingle(mField, termStr, minSimilarity);
                    applyBoost(mField, q);
                    clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                }
                return getBooleanQuery(clauses, true);
            }
        } else {
            return getFuzzyQuerySingle(field, termStr, minSimilarity);
        }
    }

    private Query getFuzzyQuerySingle(String field, String termStr, String minSimilarity) throws ParseException {
        currentMapper = null;
        MapperService.SmartNameFieldMappers fieldMappers = parseContext.smartFieldMappers(field);
        if (fieldMappers != null) {
            currentMapper = fieldMappers.fieldMappers().mapper();
            if (currentMapper != null) {
                try {
                    //LUCENE 4 UPGRADE I disabled transpositions here by default - maybe this needs to be changed
                    Query fuzzyQuery = currentMapper.fuzzyQuery(termStr, minSimilarity, fuzzyPrefixLength, settings.fuzzyMaxExpansions(), false);
                    return wrapSmartNameQuery(fuzzyQuery, fieldMappers, parseContext);
                } catch (RuntimeException e) {
                    if (settings.lenient()) {
                        return null;
                    }
                    throw e;
                }
            }
        }
        return super.getFuzzyQuery(field, termStr, Float.parseFloat(minSimilarity));
    }

    @Override
    protected Query newFuzzyQuery(Term term, float minimumSimilarity, int prefixLength) {
        String text = term.text();
        int numEdits = FuzzyQuery.floatToEdits(minimumSimilarity, text.codePointCount(0, text.length()));
        //LUCENE 4 UPGRADE I disabled transpositions here by default - maybe this needs to be changed 
        FuzzyQuery query = new FuzzyQuery(term, numEdits, prefixLength, settings.fuzzyMaxExpansions(), false);
        QueryParsers.setRewriteMethod(query, settings.fuzzyRewriteMethod());
        return query;
    }

    @Override
    protected Query getPrefixQuery(String field, String termStr) throws ParseException {
        if (lowercaseExpandedTerms) {
            termStr = termStr.toLowerCase(locale);
        }
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (fields.size() == 1) {
                return getPrefixQuerySingle(fields.iterator().next(), termStr);
            }
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = getPrefixQuerySingle(mField, termStr);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = getPrefixQuerySingle(mField, termStr);
                    if (q != null) {
                        applyBoost(mField, q);
                        clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                    }
                }
                if (clauses.size() == 0)  // happens for stopwords
                    return null;
                return getBooleanQuery(clauses, true);
            }
        } else {
            return getPrefixQuerySingle(field, termStr);
        }
    }

    private Query getPrefixQuerySingle(String field, String termStr) throws ParseException {
        currentMapper = null;
        Analyzer oldAnalyzer = analyzer;
        try {
            MapperService.SmartNameFieldMappers fieldMappers = parseContext.smartFieldMappers(field);
            if (fieldMappers != null) {
                if (!forcedAnalyzer) {
                    analyzer = fieldMappers.searchAnalyzer();
                }
                currentMapper = fieldMappers.fieldMappers().mapper();
                if (currentMapper != null) {
                    Query query = null;
                    if (currentMapper.useTermQueryWithQueryString()) {
                        if (fieldMappers.explicitTypeInNameWithDocMapper()) {
                            String[] previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{fieldMappers.docMapper().type()});
                            try {
                                query = currentMapper.prefixQuery(termStr, multiTermRewriteMethod, parseContext);
                            } finally {
                                QueryParseContext.setTypes(previousTypes);
                            }
                        } else {
                            query = currentMapper.prefixQuery(termStr, multiTermRewriteMethod, parseContext);
                        }
                    }
                    if (query == null) {
                        query = getPossiblyAnalyzedPrefixQuery(currentMapper.names().indexName(), termStr);
                    }
                    return wrapSmartNameQuery(query, fieldMappers, parseContext);
                }
            }
            return getPossiblyAnalyzedPrefixQuery(field, termStr);
        } catch (RuntimeException e) {
            if (settings.lenient()) {
                return null;
            }
            throw e;
        } finally {
            analyzer = oldAnalyzer;
        }
    }

    private Query getPossiblyAnalyzedPrefixQuery(String field, String termStr) throws ParseException {
        if (!analyzeWildcard) {
            return super.getPrefixQuery(field, termStr);
        }
        // get Analyzer from superclass and tokenize the term
        TokenStream source;
        try {
            source = getAnalyzer().tokenStream(field, termStr);
            source.reset();
        } catch (IOException e) {
            return super.getPrefixQuery(field, termStr);
        }
        List<String> tlist = new ArrayList<String>();
        CharTermAttribute termAtt = source.addAttribute(CharTermAttribute.class);

        while (true) {
            try {
                if (!source.incrementToken()) break;
            } catch (IOException e) {
                break;
            }
            tlist.add(termAtt.toString());
        }

        try {
            source.close();
        } catch (IOException e) {
            // ignore
        }

        if (tlist.size() == 1) {
            return super.getPrefixQuery(field, tlist.get(0));
        } else {
            // build a boolean query with prefix on each one...
            List<BooleanClause> clauses = new ArrayList<BooleanClause>();
            for (String token : tlist) {
                clauses.add(new BooleanClause(super.getPrefixQuery(field, token), BooleanClause.Occur.SHOULD));
            }
            return getBooleanQuery(clauses, true);

            //return super.getPrefixQuery(field, termStr);

            /* this means that the analyzer used either added or consumed
* (common for a stemmer) tokens, and we can't build a PrefixQuery */
//            throw new ParseException("Cannot build PrefixQuery with analyzer "
//                    + getAnalyzer().getClass()
//                    + (tlist.size() > 1 ? " - token(s) added" : " - token consumed"));
        }

    }

    @Override
    protected Query getWildcardQuery(String field, String termStr) throws ParseException {
        if (termStr.equals("*")) {
            // we want to optimize for match all query for the "*:*", and "*" cases
            if ("*".equals(field) || Objects.equal(field, this.field)) {
                String actualField = field;
                if (actualField == null) {
                    actualField = this.field;
                }
                if (actualField == null) {
                    return newMatchAllDocsQuery();
                }
                if ("*".equals(actualField) || "_all".equals(actualField)) {
                    return newMatchAllDocsQuery();
                }
                // effectively, we check if a field exists or not
                return fieldQueryExtensions.get(ExistsFieldQueryExtension.NAME).query(parseContext, actualField);
            }
        }
        if (lowercaseExpandedTerms) {
            termStr = termStr.toLowerCase(locale);
        }
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (fields.size() == 1) {
                return getWildcardQuerySingle(fields.iterator().next(), termStr);
            }
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = getWildcardQuerySingle(mField, termStr);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = getWildcardQuerySingle(mField, termStr);
                    if (q != null) {
                        applyBoost(mField, q);
                        clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                    }
                }
                if (clauses.size() == 0)  // happens for stopwords
                    return null;
                return getBooleanQuery(clauses, true);
            }
        } else {
            return getWildcardQuerySingle(field, termStr);
        }
    }

    private Query getWildcardQuerySingle(String field, String termStr) throws ParseException {
        String indexedNameField = field;
        currentMapper = null;
        Analyzer oldAnalyzer = analyzer;
        try {
            MapperService.SmartNameFieldMappers fieldMappers = parseContext.smartFieldMappers(field);
            if (fieldMappers != null) {
                if (!forcedAnalyzer) {
                    analyzer = fieldMappers.searchAnalyzer();
                }
                currentMapper = fieldMappers.fieldMappers().mapper();
                if (currentMapper != null) {
                    indexedNameField = currentMapper.names().indexName();
                }
                return wrapSmartNameQuery(getPossiblyAnalyzedWildcardQuery(indexedNameField, termStr), fieldMappers, parseContext);
            }
            return getPossiblyAnalyzedWildcardQuery(indexedNameField, termStr);
        } catch (RuntimeException e) {
            if (settings.lenient()) {
                return null;
            }
            throw e;
        } finally {
            analyzer = oldAnalyzer;
        }
    }

    private Query getPossiblyAnalyzedWildcardQuery(String field, String termStr) throws ParseException {
        if (!analyzeWildcard) {
            return super.getWildcardQuery(field, termStr);
        }
        boolean isWithinToken = (!termStr.startsWith("?") && !termStr.startsWith("*"));
        StringBuilder aggStr = new StringBuilder();
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < termStr.length(); i++) {
            char c = termStr.charAt(i);
            if (c == '?' || c == '*') {
                if (isWithinToken) {
                    try {
                        TokenStream source = getAnalyzer().tokenStream(field, tmp.toString());
                        source.reset();
                        CharTermAttribute termAtt = source.addAttribute(CharTermAttribute.class);
                        if (source.incrementToken()) {
                            String term = termAtt.toString();
                            if (term.length() == 0) {
                                // no tokens, just use what we have now
                                aggStr.append(tmp);
                            } else {
                                aggStr.append(term);
                            }
                        } else {
                            // no tokens, just use what we have now
                            aggStr.append(tmp);
                        }
                        source.close();
                    } catch (IOException e) {
                        aggStr.append(tmp);
                    }
                    tmp.setLength(0);
                }
                isWithinToken = false;
                aggStr.append(c);
            } else {
                tmp.append(c);
                isWithinToken = true;
            }
        }
        if (isWithinToken) {
            try {
                TokenStream source = getAnalyzer().tokenStream(field, tmp.toString());
                source.reset();
                CharTermAttribute termAtt = source.addAttribute(CharTermAttribute.class);
                if (source.incrementToken()) {
                    String term = termAtt.toString();
                    if (term.length() == 0) {
                        // no tokens, just use what we have now
                        aggStr.append(tmp);
                    } else {
                        aggStr.append(term);
                    }
                } else {
                    // no tokens, just use what we have now
                    aggStr.append(tmp);
                }
                source.close();
            } catch (IOException e) {
                aggStr.append(tmp);
            }
        }

        return super.getWildcardQuery(field, aggStr.toString());
    }

    @Override
    protected Query getRegexpQuery(String field, String termStr) throws ParseException {
        if (lowercaseExpandedTerms) {
            termStr = termStr.toLowerCase(locale);
        }
        Collection<String> fields = extractMultiFields(field);
        if (fields != null) {
            if (fields.size() == 1) {
                return getRegexpQuerySingle(fields.iterator().next(), termStr);
            }
            if (settings.useDisMax()) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(settings.tieBreaker());
                boolean added = false;
                for (String mField : fields) {
                    Query q = getRegexpQuerySingle(mField, termStr);
                    if (q != null) {
                        added = true;
                        applyBoost(mField, q);
                        disMaxQuery.add(q);
                    }
                }
                if (!added) {
                    return null;
                }
                return disMaxQuery;
            } else {
                List<BooleanClause> clauses = new ArrayList<BooleanClause>();
                for (String mField : fields) {
                    Query q = getRegexpQuerySingle(mField, termStr);
                    if (q != null) {
                        applyBoost(mField, q);
                        clauses.add(new BooleanClause(q, BooleanClause.Occur.SHOULD));
                    }
                }
                if (clauses.size() == 0)  // happens for stopwords
                    return null;
                return getBooleanQuery(clauses, true);
            }
        } else {
            return getRegexpQuerySingle(field, termStr);
        }
    }

    private Query getRegexpQuerySingle(String field, String termStr) throws ParseException {
        currentMapper = null;
        Analyzer oldAnalyzer = analyzer;
        try {
            MapperService.SmartNameFieldMappers fieldMappers = parseContext.smartFieldMappers(field);
            if (fieldMappers != null) {
                if (!forcedAnalyzer) {
                    analyzer = fieldMappers.searchAnalyzer();
                }
                currentMapper = fieldMappers.fieldMappers().mapper();
                if (currentMapper != null) {
                    Query query = null;
                    if (currentMapper.useTermQueryWithQueryString()) {
                        if (fieldMappers.explicitTypeInNameWithDocMapper()) {
                            String[] previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{fieldMappers.docMapper().type()});
                            try {
                                query = currentMapper.regexpQuery(termStr, RegExp.ALL, multiTermRewriteMethod, parseContext);
                            } finally {
                                QueryParseContext.setTypes(previousTypes);
                            }
                        } else {
                            query = currentMapper.regexpQuery(termStr, RegExp.ALL, multiTermRewriteMethod, parseContext);
                        }
                    }
                    if (query == null) {
                        query = super.getRegexpQuery(field, termStr);
                    }
                    return wrapSmartNameQuery(query, fieldMappers, parseContext);
                }
            }
            return super.getRegexpQuery(field, termStr);
        } catch (RuntimeException e) {
            if (settings.lenient()) {
                return null;
            }
            throw e;
        } finally {
            analyzer = oldAnalyzer;
        }
    }

    @Override
    protected Query getBooleanQuery(List<BooleanClause> clauses, boolean disableCoord) throws ParseException {
        Query q = super.getBooleanQuery(clauses, disableCoord);
        if (q == null) {
            return null;
        }
        return optimizeQuery(fixNegativeQueryIfNeeded(q));
    }

    private void applyBoost(String field, Query q) {
        if (settings.boosts() != null) {
            float boost = 1f;
            if (settings.boosts().containsKey(field)) {
                boost = settings.boosts().lget();
            }
            q.setBoost(boost);
        }
    }

    private void applySlop(Query q, int slop) {
        if (q instanceof PhraseQuery) {
            ((PhraseQuery) q).setSlop(slop);
        } else if (q instanceof MultiPhraseQuery) {
            ((MultiPhraseQuery) q).setSlop(slop);
        }
    }

    private Collection<String> extractMultiFields(String field) {
        Collection<String> fields = null;
        if (field != null) {
            fields = parseContext.simpleMatchToIndexNames(field);
        } else {
            fields = settings.fields();
        }
        return fields;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10169.java