Vincent Zhang
vwz2

# INTRODUCTION
(https://git.cs.duke.edu/CompSci308_2016Fall/cellsociety_team03/commit/918f2906b3a031e507a159dbfda8883cf1639a25)
In class, another groups code had one "simulation.java" class that did everything: display, calculations, algorithms, etc. 
While we weren't as bad as them in terms of how much one class did, our AnimationScene class was still responsible for a lot of stuff including drawing the canvas to display, button functions, algorithmically deciding what to display, etc. 

To refactor this code, I made a new Display class to which I moved all the control for the display and and drawing of the display. Now AnimationScene only sets up the animation and the buttons controls. The actual display algorithms and canvas painting are now taken care of by the Display class. This follows what we talked about in class about keeping separate Model, View, and Controller. The tradeoff is now the methods can't be called within the same class so there are some dependencies; some of the variables passed in need to be passed into the AnimationScene class and then directly into the Display class without ever being needed by that class.