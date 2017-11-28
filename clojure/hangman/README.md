This is a basic predictive engine for [Hangman](http://en.wikipedia.org/wiki/Hangman\_\(game\)) guessing strategies.

The strategy is based on the following formula, where:

     t     is the total number of words on the current branch
     d     is the number of possible character maskings that could result from the following guess
     d_max is the maximum number of possible character maskings across all possible guesses on the current branch
     n_i   is the number of words that are a subset of a given sub-branch (character masking) from a given guess
     c     is a constant for fine-tuning the sub-group size weighting

> The image of the selection formula has been lost to the github CDN over the years
<img src="http://cloud.github.com/downloads/amcnamara/Hangman/CodeCogsEqn.gif" style="display: block; margin: 0px auto;">

The idea is to pick a guess character that will return the most possible information (based on returned character maskings, and the possible subset of words that each of those groups can eliminate).  There are two ways to attack this problem: the first is to get as even a distribution as possible across the possible sub-maskings (for t=100 and d=4, n\_(1...4)=25,25,25,25 is far more valuable than n\_(1...4)=97,1,1,1), and second is to give preferrential weighting to groups which are more granular (higher d).  The summation in the formula above will tend towards zero as the sub-groups tend towards an even and constant distribution, and the weighting on the right will tend towards c as the granularity increases; the guess for which this function is minimal will be the optimal pick.

Improvements:
- Weigh words based on their frequency of use in the language (this would only be necessary if the words were non-randomly selected by a human)