Grant Costa
gac19

# INTRODUCTION
(https://git.cs.duke.edu/CompSci308_2016Fall/cellsociety_team03/commit/f2611b624ac797d9c5157ff32ad4a9880af6c5e4)

-In this part of the code our design issue was that we didn't have a data structure that gave us enough freedom for different types of cellular automata. We were using a 2d array to store all of the cells. This limits some of our flexibility when it comes to other simulations.

-By making the 2d array into a map of integers to cells, we can loop over the map still like a 2d array, but in cases that wouldn't work in a 2d array, we can still loop over and store all the cells in the map in a linear way. 

-A tradeoff for this is that we now it is harder to visualize where each cell is when just given its integer value vs given its row and column coordinate. This also makes the code a little bit more mathy and therefore a little less readable. The overall flexibility this granted however, our team viewed was worth it. 

