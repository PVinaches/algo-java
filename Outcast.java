import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private final WordNet wordNet;

  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
    if (wordnet == null) { 
      throw new IllegalArgumentException("Wordnet must not be null");
    }
    this.wordNet = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    if (nouns == null || nouns.length == 0) { 
      throw new IllegalArgumentException("Wordnet must not be null");
    }
    // calculate all distances
    int numberNouns = nouns.length;
    List<Integer> allDistances = new ArrayList<Integer>();
    for (int i = 0; i < numberNouns; i++) {
      int tempDistSum = 0;
      for (int j = 0; j < numberNouns; j++) {
        int indTempDis = this.wordNet.distance(nouns[i], nouns[j]);
        if (i != j && indTempDis != -1) {
          tempDistSum += indTempDis;
        }
      }
      allDistances.add(tempDistSum);
    }

    int idx = 0;
    for (int i = 0; i < allDistances.size(); i++) {
      if (allDistances.get(i) > allDistances.get(idx)) {
        idx = i;
      }
    }

    return nouns[idx];
  }

  // see test client below
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}


// % cat outcast5.txt
// horse zebra cat bear table

// % cat outcast8.txt
// water soda bed orange_juice milk apple_juice tea coffee

// % cat outcast11.txt
// apple pear peach banana lime lemon blueberry strawberry mango watermelon potato

// % java-algs4 Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
// outcast5.txt: table
// outcast8.txt: bed
// outcast11.txt: potato