error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1846.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1846.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1846.java
text:
```scala
final c@@har quote = '"';

/* Generated By:JavaCC: Do not edit this line. QueryParser.java */
package org.apache.lucene.queryparser.surround.parser;

import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;


import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.queryparser.surround.query.SrndQuery;
import org.apache.lucene.queryparser.surround.query.FieldsQuery;
import org.apache.lucene.queryparser.surround.query.OrQuery;
import org.apache.lucene.queryparser.surround.query.AndQuery;
import org.apache.lucene.queryparser.surround.query.NotQuery;
import org.apache.lucene.queryparser.surround.query.DistanceQuery;
import org.apache.lucene.queryparser.surround.query.SrndTermQuery;
import org.apache.lucene.queryparser.surround.query.SrndPrefixQuery;
import org.apache.lucene.queryparser.surround.query.SrndTruncQuery;

/**
 * This class is generated by JavaCC.  The only method that clients should need
 * to call is {@link #parse parse()}.
 *

 * <p>This parser generates queries that make use of position information
 *   (Span queries). It provides positional operators (<code>w</code> and
 *   <code>n</code>) that accept a numeric distance, as well as boolean
 *   operators (<code>and</code>, <code>or</code>, and <code>not</code>,
 *   wildcards (<code>*</code> and <code>?</code>), quoting (with
 *   <code>"</code>), and boosting (via <code>^</code>).</p>

 *  <p>The operators (W, N, AND, OR, NOT) can be expressed lower-cased or
 *   upper-cased, and the non-unary operators (everything but NOT) support
 *   both infix <code>(a AND b AND c)</code> and prefix <code>AND(a, b,
 *   c)</code> notation. </p>
 
 *  <p>The W and N operators express a positional relationship among their
 *  operands.  N is ordered, and W is unordered.  The distance is 1 by
 *  default, meaning the operands are adjacent, or may be provided as a
 *  prefix from 2-99.  So, for example, 3W(a, b) means that terms a and b
 *  must appear within three positions of each other, or in other words, up
 *  to two terms may appear between a and b.  </p>
 */

public class QueryParser implements QueryParserConstants {
  final int minimumPrefixLength = 3;
  final int minimumCharsInTrunc = 3;
  final String truncationErrorMessage = "Too unrestrictive truncation: ";
  final String boostErrorMessage = "Cannot handle boost value: ";

  /* CHECKME: These should be the same as for the tokenizer. How? */
  final char truncator = '*';
  final char anyChar = '?';
  final char quote = '\u005c"';
  final char fieldOperator = ':';
  final char comma = ','; /* prefix list separator */
  final char carat = '^'; /* weight operator */

  static public SrndQuery parse(String query) throws ParseException {
    QueryParser parser = new QueryParser();
    return parser.parse2(query);
  }

  public QueryParser() {
    this(new FastCharStream(new StringReader("")));
  }

  public SrndQuery parse2(String query) throws ParseException {
    ReInit(new FastCharStream(new StringReader(query)));
    try {
      return TopSrndQuery();
    } catch (TokenMgrError tme) {
      throw new ParseException(tme.getMessage());
    }
  }

  protected SrndQuery getFieldsQuery(
      SrndQuery q, ArrayList<String> fieldNames) {
    /* FIXME: check acceptable subquery: at least one subquery should not be
     * a fields query.
     */
    return new FieldsQuery(q, fieldNames, fieldOperator);
  }

  protected SrndQuery getOrQuery(List<SrndQuery> queries, boolean infix, Token orToken) {
    return new OrQuery(queries, infix, orToken.image);
  }

  protected SrndQuery getAndQuery(List<SrndQuery> queries, boolean infix, Token andToken) {
    return new AndQuery( queries, infix, andToken.image);
  }

  protected SrndQuery getNotQuery(List<SrndQuery> queries, Token notToken) {
    return new NotQuery( queries, notToken.image);
  }

  protected static int getOpDistance(String distanceOp) {
    /* W, 2W, 3W etc -> 1, 2 3, etc. Same for N, 2N ... */
    return distanceOp.length() == 1
      ? 1
      : Integer.parseInt( distanceOp.substring( 0, distanceOp.length() - 1));
  }

  protected static void checkDistanceSubQueries(DistanceQuery distq, String opName)
  throws ParseException {
    String m = distq.distanceSubQueryNotAllowed();
    if (m != null) {
      throw new ParseException("Operator " + opName + ": " + m);
    }
  }

  protected SrndQuery getDistanceQuery(
        List<SrndQuery> queries,
        boolean infix,
        Token dToken,
        boolean ordered) throws ParseException {
    DistanceQuery dq = new DistanceQuery(queries,
                                        infix,
                                        getOpDistance(dToken.image),
                                        dToken.image,
                                        ordered);
    checkDistanceSubQueries(dq, dToken.image);
    return dq;
  }

  protected SrndQuery getTermQuery(
        String term, boolean quoted) {
    return new SrndTermQuery(term, quoted);
  }

  protected boolean allowedSuffix(String suffixed) {
    return (suffixed.length() - 1) >= minimumPrefixLength;
  }

  protected SrndQuery getPrefixQuery(
      String prefix, boolean quoted) {
    return new SrndPrefixQuery(prefix, quoted, truncator);
  }

  protected boolean allowedTruncation(String truncated) {
    /* At least 3 normal characters needed. */
    int nrNormalChars = 0;
    for (int i = 0; i < truncated.length(); i++) {
      char c = truncated.charAt(i);
      if ((c != truncator) && (c != anyChar)) {
        nrNormalChars++;
      }
    }
    return nrNormalChars >= minimumCharsInTrunc;
  }

  protected SrndQuery getTruncQuery(String truncated) {
    return new SrndTruncQuery(truncated, truncator, anyChar);
  }

  final public SrndQuery TopSrndQuery() throws ParseException {
  SrndQuery q;
    q = FieldsQuery();
    jj_consume_token(0);
   {if (true) return q;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery FieldsQuery() throws ParseException {
  SrndQuery q;
  ArrayList<String> fieldNames;
    fieldNames = OptionalFields();
    q = OrQuery();
   {if (true) return (fieldNames == null) ? q : getFieldsQuery(q, fieldNames);}
    throw new Error("Missing return statement in function");
  }

  final public ArrayList<String> OptionalFields() throws ParseException {
  Token fieldName;
  ArrayList<String> fieldNames = null;
    label_1:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_1;
      }
      // to the colon
          fieldName = jj_consume_token(TERM);
      jj_consume_token(COLON);
      if (fieldNames == null) {
        fieldNames = new ArrayList<String>();
      }
      fieldNames.add(fieldName.image);
    }
   {if (true) return fieldNames;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery OrQuery() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries = null;
  Token oprt = null;
    q = AndQuery();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_2;
      }
      oprt = jj_consume_token(OR);
                  /* keep only last used operator */
      if (queries == null) {
        queries = new ArrayList<SrndQuery>();
        queries.add(q);
      }
      q = AndQuery();
      queries.add(q);
    }
   {if (true) return (queries == null) ? q : getOrQuery(queries, true /* infix */, oprt);}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery AndQuery() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries = null;
  Token oprt = null;
    q = NotQuery();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_3;
      }
      oprt = jj_consume_token(AND);
                   /* keep only last used operator */
      if (queries == null) {
        queries = new ArrayList<SrndQuery>();
        queries.add(q);
      }
      q = NotQuery();
      queries.add(q);
    }
   {if (true) return (queries == null) ? q : getAndQuery(queries, true /* infix */, oprt);}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery NotQuery() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries = null;
  Token oprt = null;
    q = NQuery();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_4;
      }
      oprt = jj_consume_token(NOT);
                    /* keep only last used operator */
      if (queries == null) {
        queries = new ArrayList<SrndQuery>();
        queries.add(q);
      }
      q = NQuery();
      queries.add(q);
    }
   {if (true) return (queries == null) ? q : getNotQuery(queries, oprt);}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery NQuery() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries;
  Token dt;
    q = WQuery();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case N:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_5;
      }
      dt = jj_consume_token(N);
      queries = new ArrayList<SrndQuery>();
      queries.add(q); /* left associative */

      q = WQuery();
      queries.add(q);
      q = getDistanceQuery(queries, true /* infix */, dt, false /* not ordered */);
    }
   {if (true) return q;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery WQuery() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries;
  Token wt;
    q = PrimaryQuery();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case W:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_6;
      }
      wt = jj_consume_token(W);
      queries = new ArrayList<SrndQuery>();
      queries.add(q); /* left associative */

      q = PrimaryQuery();
      queries.add(q);
      q = getDistanceQuery(queries, true /* infix */, wt, true /* ordered */);
    }
   {if (true) return q;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery PrimaryQuery() throws ParseException {
                             /* bracketed weighted query or weighted term */
  SrndQuery q;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      jj_consume_token(LPAREN);
      q = FieldsQuery();
      jj_consume_token(RPAREN);
      break;
    case OR:
    case AND:
    case W:
    case N:
      q = PrefixOperatorQuery();
      break;
    case TRUNCQUOTED:
    case QUOTED:
    case SUFFIXTERM:
    case TRUNCTERM:
    case TERM:
      q = SimpleTerm();
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    OptionalWeights(q);
   {if (true) return q;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery PrefixOperatorQuery() throws ParseException {
  Token oprt;
  List<SrndQuery> queries;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OR:
      oprt = jj_consume_token(OR);
      /* prefix OR */
          queries = FieldsQueryList();
     {if (true) return getOrQuery(queries, false /* not infix */, oprt);}
      break;
    case AND:
      oprt = jj_consume_token(AND);
      /* prefix AND */
          queries = FieldsQueryList();
     {if (true) return getAndQuery(queries, false /* not infix */, oprt);}
      break;
    case N:
      oprt = jj_consume_token(N);
      /* prefix N */
          queries = FieldsQueryList();
     {if (true) return getDistanceQuery(queries, false /* not infix */, oprt, false /* not ordered */);}
      break;
    case W:
      oprt = jj_consume_token(W);
      /* prefix W */
          queries = FieldsQueryList();
     {if (true) return getDistanceQuery(queries, false  /* not infix */, oprt, true /* ordered */);}
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public List<SrndQuery> FieldsQueryList() throws ParseException {
  SrndQuery q;
  ArrayList<SrndQuery> queries = new ArrayList<SrndQuery>();
    jj_consume_token(LPAREN);
    q = FieldsQuery();
                     queries.add(q);
    label_7:
    while (true) {
      jj_consume_token(COMMA);
      q = FieldsQuery();
                              queries.add(q);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_7;
      }
    }
    jj_consume_token(RPAREN);
   {if (true) return queries;}
    throw new Error("Missing return statement in function");
  }

  final public SrndQuery SimpleTerm() throws ParseException {
  Token term;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TERM:
      term = jj_consume_token(TERM);
     {if (true) return getTermQuery(term.image, false /* not quoted */);}
      break;
    case QUOTED:
      term = jj_consume_token(QUOTED);
     {if (true) return getTermQuery(term.image.substring(1, term.image.length()-1), true /* quoted */);}
      break;
    case SUFFIXTERM:
      term = jj_consume_token(SUFFIXTERM);
                        /* ending in * */
      if (! allowedSuffix(term.image)) {
        {if (true) throw new ParseException(truncationErrorMessage + term.image);}
      }
      {if (true) return getPrefixQuery(term.image.substring(0, term.image.length()-1), false /* not quoted */);}
      break;
    case TRUNCTERM:
      term = jj_consume_token(TRUNCTERM);
                       /* with at least one * or ? */
      if (! allowedTruncation(term.image)) {
        {if (true) throw new ParseException(truncationErrorMessage + term.image);}
      }
      {if (true) return getTruncQuery(term.image);}
      break;
    case TRUNCQUOTED:
      term = jj_consume_token(TRUNCQUOTED);
                         /* eg. "9b-b,m"* */
      if ((term.image.length() - 3) < minimumPrefixLength) {
        {if (true) throw new ParseException(truncationErrorMessage + term.image);}
      }
      {if (true) return getPrefixQuery(term.image.substring(1, term.image.length()-2), true /* quoted */);}
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void OptionalWeights(SrndQuery q) throws ParseException {
  Token weight=null;
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CARAT:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_8;
      }
      jj_consume_token(CARAT);
      weight = jj_consume_token(NUMBER);
      float f;
      try {
        f = Float.valueOf(weight.image).floatValue();
      } catch (Exception floatExc) {
        {if (true) throw new ParseException(boostErrorMessage + weight.image + " (" + floatExc + ")");}
      }
      if (f <= 0.0) {
        {if (true) throw new ParseException(boostErrorMessage + weight.image);}
      }
      q.setWeight(f * q.getWeight()); /* left associative, fwiw */

    }
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_3_1() {
    if (jj_scan_token(TERM)) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public QueryParserTokenManager token_source;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[10];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x100,0x200,0x400,0x1000,0x800,0x7c3b00,0x1b00,0x8000,0x7c0000,0x20000,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with user supplied CharStream. */
  public QueryParser(CharStream stream) {
    token_source = new QueryParserTokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(CharStream stream) {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public QueryParser(QueryParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(QueryParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 10; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[24];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 10; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 24; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1846.java