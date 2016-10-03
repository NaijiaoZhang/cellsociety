Naijiao Zhang
nz21

# INTRODUCTION
(https://git.cs.duke.edu/CompSci308_2016Fall/cellsociety_team03/commit/f4972ac3074d802acda2aabca23c7d446e7f56c5)

-I refactored the structure of the Cell classes that were being displayed. Originally the cell classes had both the information about how they were displayed (if it was square, hexagon etc.) and the logic and rules behind when to display. This was a problem because it led to the creation of individual classes that were both square grids but had different logic. This did not feel natural and was hindering process as more and more classes that were linked by inheritance were created (predatorpreycell and cell should both just be a square cell class). 

-The code I committed is better because it adds another layer of separation. With the change, adding new features and rules becomes much easier and more intuitive as opposed to the old mess that was there before. Now, there doesnt need to be three cell classes written respectively for hexagon, square and triangle whenever a new simulation is added. 

-The tradeoff of this approach is that there is now additional dependencies attached to the cell class. Calls to get information now all become one level deeper and it leaves room for errors that might bubble up if the lowest level code does not work. 

