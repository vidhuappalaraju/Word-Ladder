package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.*;

public class Main {

	static Set<String> dict = new HashSet<String>();
	static Set<String> dict2 = new HashSet<String>();
	static ArrayList<String> ladder = new ArrayList<String>();
	static ArrayList<Character> alphabet = new ArrayList<Character>();
	static int count;
	static boolean reverseflag;
	static ArrayList<String> words = new ArrayList<String>();
	// static variables and constants only here.

	public static void main(String[] args) throws Exception {

		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}
		words = parse(kb);
		ArrayList<String> newladder = new ArrayList<String>();
		initialize();
		newladder = getWordLadderBFS(words.get(0), words.get(1));
		printLadder(newladder);
		newladder = getWordLadderDFS(words.get(0), words.get(1));
		printLadder(newladder);

		//ladder.clear();
		//ladder = getWordLadderBFS(words.get(0), words.get(1));
		//printLadder(ladder);

		kb.close();

		// TODO methods to read in words, output ladder
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
		dict = makeDictionary();
		dict2 = makeDictionary();
		reverseflag = false;
		alphabet.add('a');
		alphabet.add('b');
		alphabet.add('c');
		alphabet.add('d');
		alphabet.add('e');
		alphabet.add('f');
		alphabet.add('g');
		alphabet.add('h');
		alphabet.add('i');
		alphabet.add('j');
		alphabet.add('k');
		alphabet.add('l');
		alphabet.add('m');
		alphabet.add('n');
		alphabet.add('o');
		alphabet.add('p');
		alphabet.add('q');
		alphabet.add('r');
		alphabet.add('s');
		alphabet.add('t');
		alphabet.add('u');
		alphabet.add('v');
		alphabet.add('w');
		alphabet.add('x');
		alphabet.add('y');
		alphabet.add('z');

	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> words = new ArrayList<String>();
		words.add(keyboard.next().toLowerCase());
		if (words.contains("/quit")) {
			System.exit(0);
		} else {
			words.add(keyboard.next().toLowerCase());
			if (words.contains("/quit")) {
				System.exit(0);
			}
		}
		return words;
	}

	public static ArrayList<String> LadderReducer() {
		for (int i = 0; i < (ladder.size() - 3); i++) {
			for (int k = 1; k < (ladder.size()); k++) {
				ArrayList<Boolean> similar = new ArrayList<Boolean>();
				for (int j = 0; j < ladder.get(0).length(); j++) {
					if (ladder.get(i).charAt(j) == ladder.get(i + 2).charAt(j)) {
						similar.add(true);
					} else {
						similar.add(false);
					}
				}
				if (similar.contains(false)) {
					similar.remove(false);
					if (!similar.contains(false)) {
						ladder.remove(i + 1);
					}
				}
			}
		}
		return ladder;
	}

	public static ArrayList<String> reverseDFS(String start, String end) {
		try {
			ArrayList<String> newladder = new ArrayList<String>();
			int letterindex;
			int alphabetindex;
			ladder.add(start);
			if (start.equals(end)) {
				return ladder;
			}
			if (dict.contains(start)) {
				dict.remove(start);
			}
			StringBuilder copyWord = new StringBuilder(start); // copyWord now
																// has // start
			for (letterindex = 0; letterindex < start.length(); letterindex++) {
				char actualLetter = start.charAt(letterindex);
				for (alphabetindex = 0; alphabetindex < 26; alphabetindex++) {
					copyWord.setCharAt(letterindex, alphabet.get(alphabetindex));
					if (dict.contains(copyWord.toString())) {
						dict.remove(copyWord.toString());
						String nextWord = new String(copyWord);

						newladder = reverseDFS(nextWord, end);

						if (newladder != null) {
							return ladder;
						} else {
							ladder.remove(nextWord);
						}
					}

				}
				copyWord.setCharAt(letterindex, actualLetter);
			}
			return null;
		} catch (StackOverflowError e) {
			reverseflag = true;
			return null;
		}
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		dict = makeDictionary();
		ArrayList<String> newlads;

		newlads = reverseDFS(start, end);
		if (reverseflag) {
			dict = makeDictionary();
			ladder.clear();
			newlads = reverseDFS(end, start);
			if (newlads != null) {
				Collections.reverse(newlads);
			}
			return newlads;
		} else {
			return LadderReducer();
		}

	}

	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		words.add(start);
		words.add(end);
		dict = makeDictionary();
		int letterindex;
		int alphabetindex;
		Queue<Node> myQueue = new LinkedList<Node>();

		Node beginning = new Node();
		Node check = new Node();

		beginning.worditself = new String(start);
		myQueue.add(beginning);
		while (!myQueue.isEmpty()) {
			check = myQueue.remove();

			check.WordLadder.add(check.worditself);
			if (check.worditself.equals(end)) {
				ladder = check.WordLadder;
				return ladder;
			}
			if (dict.contains(check.worditself)) {
				dict.remove(check.worditself);// could be an if statement, need
												// to remove the start word from
												// the dictionary as well
			}
			StringBuilder newWord = new StringBuilder(check.worditself);

			for (letterindex = 0; letterindex < start.length(); letterindex++) {
				char actualLetter = check.worditself.charAt(letterindex);
				for (alphabetindex = 0; alphabetindex < 26; alphabetindex++) {
					newWord.setCharAt(letterindex, alphabet.get(alphabetindex));

					if (dict.contains(newWord.toString())) {
						Node Next = new Node();
						Next.worditself = new String(newWord);
						Next.WordLadder = new ArrayList<String>(check.WordLadder);
						myQueue.add(Next);
						dict.remove(Next.worditself);
						// remove from the dictionary again
						// hmm we could make a new type that adds the word to it
						// along with a number
					}
				}
				newWord.setCharAt(letterindex, actualLetter);

			}

		}

		return null; // replace this line later with real return
	}

	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toLowerCase());
		}
		return words;
	}

	public static void printLadder(ArrayList<String> ladder) {
		if (ladder == null || ladder.size() == 1) {
			System.out.println("no word ladder can be found between " + words.get(0) + " and " + words.get(1) + ". ");
		} else {
			System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between "
					+ words.get(0).toString() + " and " + words.get(1).toString() + ". ");
			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get(i).toString());
			}
		}

	}
	// TODO
	// Other private static methods here
}
