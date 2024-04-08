import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
  private final Digraph wordNetG;
  private final Iterable<String> words;
  private final HashMap<String, List<Integer>> wordsHashMap;
  private final HashMap<Integer, String> valuesHashMap;

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) { 
      throw new IllegalArgumentException("Synsets or hypernyms must not be null");
    }

    // Count vertex and save words
    In in = new In(synsets);
    int vertexNum = 0;
    this.wordsHashMap = new HashMap<>();
    this.valuesHashMap = new HashMap<>();
    while (!in.isEmpty()) {
      String line = in.readLine();
      String[] lineParts = line.split(",");
      this.valuesHashMap.put(Integer.valueOf(lineParts[0]), lineParts[1]);
      String[] wordList = lineParts[1].split(" ");
      for (int i = 0; i < wordList.length; i++) {
        this.wordsHashMap.putIfAbsent(wordList[i], new ArrayList<>());
        this.wordsHashMap.get(wordList[i]).add(Integer.valueOf(lineParts[0]));
      }
      vertexNum++;
    }
    this.words = this.wordsHashMap.keySet();
    
    // Construct the digraph with the vertex index
    In inH = new In(hypernyms);
    this.wordNetG = new Digraph(vertexNum);
    while (!inH.isEmpty()) {
      String line = inH.readLine();
      String[] lineParts = line.split(",");
      for (int i = 1; i < lineParts.length; i++) {
        this.wordNetG.addEdge(Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[i]));
      }
    }
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
    return this.words;
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("Argument to isNoun() is null");
    }
    return this.wordsHashMap.containsKey(word);
  }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("Arguments to distance() are null");
    }
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException("Arguments to distance() are not WordNet nouns");
    }

    // Obtain the index of the nouns:
    Iterable<Integer> nounAId = this.getNounIds(nounA);
    Iterable<Integer> nounBId = this.getNounIds(nounB);

    SAP sapG = new SAP(wordNetG);
    return sapG.length(nounAId, nounBId);
   }

   private Iterable<Integer> getNounIds(String noun) {
    return this.wordsHashMap.get(noun);
   }

   private String getSynset(int id) {
    return this.valuesHashMap.get(id);
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("Arguments to sap() are null");
    }
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException("Arguments to sap() are not WordNet nouns");
    }

    // Obtain the index of the nouns:
    Iterable<Integer> nounAId = this.getNounIds(nounA);
    Iterable<Integer> nounBId = this.getNounIds(nounB);

    // Obtain the common ancestor
    SAP sapG = new SAP(wordNetG);
    int ancestor = sapG.ancestor(nounAId, nounBId);
    return this.getSynset(ancestor);
   }

   // do unit testing of this class
   public static void main(String[] args) {
    WordNet wordNetTest = new WordNet(args[0], args[1]);
    System.out.println(wordNetTest.nouns());
    System.out.println(wordNetTest.isNoun("house"));
    System.out.println(wordNetTest.isNoun("1820s"));
   }
}


// Throw an IllegalArgumentException in the following situations:
// Any argument to the constructor or an instance method is null
// The input to the constructor does not correspond to a rooted DAG.
// Any of the noun arguments in distance() or sap() is not a WordNet noun.


// Performance requirements.  
// Your data type should use space linear in the input size (size of synsets and hypernyms files). 
// The constructor should take time linearithmic (or better) in the input size. 
// The method isNoun() should run in time logarithmic (or better) in the number of nouns. 
// The methods distance() and sap() should run in time linear in the size of the WordNet digraph. 
// For the analysis, assume that the number of nouns per synset is bounded by a constant.