# Text Generator - using Markov Chain Algorithm

**Definition**
*A stochastic model describing a sequence of possible events in which the probability of each event depends only on the state attained in the previous event.*

![Image of Garkov comic strip](http://goo.gl/SrcngF)
*Random implementation of aforementioned algorithm to re-produce Garfield comic strip*

#### SIMPLE WEB UI - Concept only
   http://smarnane.wix.com/landing
 
#### NOTES:
- Reference material: The Practice of Programming (B.Kernighan and R.Pike)
 
#### PSEUDOCODE:
**Building the State Map**

- Read file contents
- If file contents greater than prefix size
- Loop (0..(contents - prefix size))
    - If prefix not in Map, add to Map
    - Add suffix to Map
- Add EOF_MARKER suffix to last prefix.

**Generating the Output**

- Select the initial prefix in StateMap as starting point
- Loop (0..(MAX - prefix size))
    - Randomly choose suffix from ArrayList associated with prefix.
    - Create a new prefix by removing the leading word in the prefix and appending the suffix
    - Repeat steps 2 and 3 until EOF_MARKER is reached (i.e. no more suffixes) or MAX_COUNT limit is reached.

#### ASSUMPTIONS:
1. A word is anything between whitespace (irrelevant for character)
2. The last N characters/words are the current state (prefix params)
3. Next character/word (suffix param) depends on the last N characters/words only (i.e. the present state)  - Memorylessness
4. Next character/word is randomly chosen based on a pre-built statistical model of the txtInput containing prefix key and list of suffixes.
5. Each random character/word that has been selected is appended to the generated output.
6. If iteration of generated output reaches MAX_COUNT or random character/word is selected, break out of loop and return generated output.

###### EXAMPLE INPUT/OUTPUT:
*"The quick brown fox jumps over the brown fox who is slow jumps over the brown fox who is dead."*

PREFIX (order N)   | SUFFIXES
----------------   | ------------------------
 ('\n', '\n')|['The'],
 ('\n', 'The')|['quick'],
 ('The', 'quick')|['brown'],
 ('quick', 'brown')| ['fox'],
 ('brown', 'fox')|['jumps', 'who', 'who'],
 ('fox', 'jumps')|['over'],
 ('jumps', 'over')|['the', 'the'],
 ('over', 'the')|['brown', 'brown'],
 ('the', 'brown')|['fox', 'fox'],
 ('fox', 'who')|['is', 'is'],
 ('who', 'is')|['slow', 'dead.'],
 ('is', 'slow')|['jumps'],
 ('slow', 'jumps')|['over'],
 ('is', 'dead.')|['\n'])

**NOTE:** If we select the suffix 'dead.' the application will end as there will be no more associations.

**Output:** (Order-2)
*"over the brown fox who is slow jumps over the brown fox jumps over the brown fox who is dead."*
 
#### Possible Approaches
*Requirement: Markov algorithm must see all input before generating output*

1. Store as an array of Strings and iterate through the array to find possible suffixes for prefix.
    - definitely a brute force method
    - requires scanning all input text every time a new character/word is generated.
2. Create a custom datatype that can hold the prefix params (Order N) and an array of suffix params.
    - creates a dictionary of unique prefixes with list of possible suffixes
    - hashtable/list combo ??
   
#### ADDITIONAL CONSIDERATIONS
- If Order N is greater than size of input i.e. 50 characters/words in text file, user selects Order = 100;
- If there is no suffix i.e. last character/word in text file is unique, therefore will not have a suffix.
- Markov Chain algorithm is indeterminate. Items are never deleted, which may result in circular/indefinite loop pattern.
- Can improve memory consumption by generating a second hashtable for each unique character/word
- When building the training data, need to consider time complexity when looping through trained data and adding each prefix/suffix.  

#### ISSUES
- Major: NullPointerException in build() when calling stateMap.get(...).
    - Need to override the hashCode and equals methods when adding an object as Hashtable key. See Note (https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
    - Solution:  "alt + insert". - thankfully I didn't have to roll my own version.
- Major: ConcurrentModificationException in generate() on second iteration of for loop.
    - I was creating a subList of the original ArrayList.
    - Solution: Create a deep-copy of src ArrayList - passing src position at index 'pos' and number of values, 'n'. 
- Minor: Empty values in prefix ArrayList
    - Even though I was using strip(regex) function to remove all spaces on each line of buffered input, it doesn't in to account blank lines.
    - Solution: validation check for empty line, return branching statement 'continue' to skip to next iteration.
- Minor: Algorithm for generating the randomized text
    - Doesn't appear to be a specific design pattern.
    - Solution: Adapted a version from "The Practice of Programming", but randomizing the initial key
- Minor: Missing potential suffix values
    - Didn't take in to account suffixes where the number of prefixes is less than N.
    - Solution: Prepend EOF_MARKER (renamed to MARKER) as a filler value for each of the missing prefixes.  Should give more realistic data
- Minor: SWING GUI
    - Haven't done one of these in a while !!
    - Solution: Work in progress...

#### INITIAL THOUGHTS/CONSIDERATIONS
- Read/write text file
    - size of input
    - stored in memory, may lead to OutOfMemoryException
    - speed of reading (bytes, line-by-line, chunks, etc.)

- Selecting Input params
    - best way of storing params for fast read/write
    - prefix/suffix
    - other input param options

- Markov Chain Algorithm
    - What is it?
    - How to implement?
    - Is the algorithm efficient for large data sets?

- Type of application
    - console
    - gui
    - web

- Miscellaneous
    - testing