# Implementing the Markov Chain Algorithm

*A stochastic model describing a sequence of possible events in which the probability of each event depends only on the state attained in the previous event.*

![Image of Garkov comic strip](http://goo.gl/SrcngF)

#### SIMPLE WEB UI
   http://smarnane.wix.com/landing
 
#### NOTES:
- All classes that have instances used as keys in a hash-like data structure must correctly implement the equals and hashCode methods.  See http://www.ibm.com/developerworks/java/library/j-jtp05273/index.html for an explanation.
- Reference material: The Practice of Programming (B.Kernighan and R.Pike)
 
#### PSEUDOCODE:
**Building the Data Structure**

- Read file contents
- Loop (contents minus prefix size)
    - If prefix not in Map, add to Map, iterate key
    - Add suffix to Map
- Add terminator suffix

4. Add suffix value to Map

**Generating the Output**

1. Select the starting point for the initial prefix.
2. Randomly choose one of the suffixes which is associated with that prefix.
3. Create a new prefix by removing the leading word in the prefix and appending the suffix
4. Repeat steps 2 and 3 until EOF_MARKER is reached (i.e. no more suffixes) or MAX_COUNT limit is reached.
#### ASSUMPTIONS:
1. A word is anything between whitespace (irrelevant for character)
2. The last N characters/
2. The last N characters/words are the current state (prefix params)
2. Next character/word (suffix param) depends on the last N characters/words only (i.e. the present state)  - Memorylessness
3. Next character/word is randomly chosen from a statistical model of the text corpus ()

###### EXAMPLE INPUT/OUTPUT:
*"The quick brown fox jumps over the brown fox who is slow jumps over the brown fox who is dead."*

PREFIX (order N)   | SUFFIXES
----------------   | ------------------------
(('The', 'quick')|['brown'],
 ('quick', 'brown')| ['fox'],
 ('brown', 'fox')|['jumps', 'who', 'who'],
 ('fox', 'jumps')|['over'],
 ('jumps', 'over')|['the', 'the'],
 ('over', 'the')|['brown', 'brown'],
 ('the', 'brown')|['fox', 'fox'],
 ('fox', 'who')|['is', 'is'],
 ('who', 'is')|['slow', 'dead.'],
 ('is', 'slow')|['jumps'],
 ('slow', 'jumps')|['over'])

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

#### INITIAL THOUGHTS/CONCERNS
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