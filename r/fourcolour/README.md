This is the first script I wrote when I was learning R many years ago. This code is not idiomatic and is very non-performant, I was simply curious about the language at the time.

I hypothesized that 4-coloured matrices (where each node of a `(d)×(d)` matrix can be one of 4 colours, and there can exist no rectangles within the matrix in which the corners share the same colour) could be broken down into simpler validation steps. By taking two valid four-coloured `(d-1)×(d-1)` matrices offset by one row and one column (such that that their overlapping `(d-2)×(d-2)` elements are equivalent) it would mean that only the two corner elements would need to be tested. In this sense the solution could be built from a set of smaller valid matrices (the tradeoff being a large memory footprint, since all valid solutions for d-1 must be analyzed for each valid combination).  4-colour matricies have been proven impossible at d19 and above, my goal was to find a valid solution for d18 if one exists.

Thoughts:
- Find a more efficient way to compare matrix overlap (something hash-like, perhaps a row and col checksum or a colour aggregate key; note: CRCs and counts should survive rotation)
- Find a more efficient way to structure the elements, they're currently stored as doubles but only need to hold 2 bits of data.
- Find a more efficient way to hold the d-1 valid set, this is probably best dumped into a trie (since there is so much data commonality), but consider affect on comparison cost.
- The valid set currently contains rotations, consider removing them to save space and instead find a fast way to test against rotations of valid matrices.
- Graph the relations (indicies, hash hamming distance, etc) between base matrices and valid children, there may be a pattern here which would cut down on comparisons.
