Grant Costa, Naijiao Zhang, Vincent Zhang
gac19, nz21, vwz2

# INTRODUCTION
In this assignment, our primary goal is to create an application with the ability to generate Cellular Automata(CA) simulations. Our application should take in a set of rules and parameters from a data file (preferably XML) and be able to generate the corresponding 2D grid animation. From a design perspective, our goal is for our application to be flexible enough to take in any given data that can be modeled by a CA simulation and then animate it. The architecture of our design will be mainly separated into four distinct categories -- Startup, Scene, Services and Component. Startup should include any code and classes needed to start the window of the application. It includes any booting code as well as code that transitions the application to the first scene. Scene code includes all of the front end code that is needed to display things in a window. Things such as buttons, labels, and the classes that returns a scene for a stage to display are all code that are part of scene code. Services code is all of the code that does calculations including management of rules, organizing and storing cells, and determining what gets animated and when. Finally, Component code includes any code that is needed to store things related to objects such as a cell. These will need to be manipulated by Service code to be displayed by scenes. 
# OVERVIEW
Our design of the application is separated into three distinct parts. The first includes the Main class that will start the program and any classes that produce scenes for the Main class's stage to show. These scene classes include a Main Menu with a button that can start animating as well as another button to browse for resource loading. Any code that will be used to grab data that will be given to generate the CA is part of the first part. The second part of the design include the Rules Manager that deals with the management of various rules as well as the internal calculation of the states of cells. It is in Rules Manager that calls that check for rendering and animation can be made from scene so that the application can know what cells to render and when. The final part includes a cell class and any specific cell class that might inherit from cell. These will be used to represent one 2D grid square for the application to animate. 

![Image](images/structure.jpg)
# USER INTERFACE
The user interface at this stage of the project is to be very simplistic. It's to include a main menu that starts a loaded XML file by displaying the corresponding CA. Once users hit the start button, the game will go to an animation scene where there will be various sliders, labels, and buttons to adjust the animation based on the XML rules. In this animation scene, there will also be a button that redirects the scene back to the main menu so that users can once again select an XML file to load and animate. If there is an error in reading data, loading scenes, or the data loaded being incorrect/not in a standard form, the application should redirect to an error page that displays the type of error that is occurring. 

Below is a sketch of our application's intended GUI: 
![Image2](images/userinterface.jpg)

# DESIGN DETAILS
## Main
Our use for main is simply to start the program and initiate main menu scene. 
As a result, there is no need for any subcomponents many other features. 
If needed, we could add other features that need to be initialized before the main menu screen, but realistically this class will only to begin running the program.

## Main Menu
The main menu component will set up the animation scene and will do the background work.
As a result, it will contain several subcomponents that will control the factors needed for cell society
These subcomponents include/are responsible for:
* Read in XML file for rules
	
	It appears the rules for the cell society are supposed to be read in through XML files. The main menu will contain a button that will bring a popup screen that allows the user to provide the location of the XML file. This XML file will then be retrieved and used to initiate the animation scene and determine the rules the cell society follows. Of course, we will need to implement a check that the file exists and is of the right type when reading in. With regards to flexibility, this section of code could also read in other file types if XML file type is changed. Furthermore, this section could be modified to allow the user to type in a set of rules instead of typing in a file location if that aspect is desired.
 
* Buttons for other options and start
	
	There are other factors that must be taken into account when making the cell society animation. These include number of cells, step size for time based steps, pick cell factors (for example, different display colors), etc. While so far the project claims that factors such as number of cells can be decided by the team and hardcoded, in the spirit of flexibility we are including this as an option for the user. These options change values within the class which will be used when initiating the animation scene. As our project demands it, we will be able to include other options to change fairly easy by creating new buttons and variables that will allow the user to control more factors of the animation.
	
* Initiate the animation scene

	Lastly, we need to have a button that allows the user to change the screen and initiates the animation once they have finished reading in the file and deciding all the factors that need to be passed on. 

## Animation Scene
The animation sets up, displays, and progresses the animation grid.  
It also initiates the other classes needed for the animation to work.
Note that these parts may be separated into more classes if needed/convenient.

* Read in and pass on rules/cell options to rule manager
	
	First thing needed to start the animation scene is to read in all the options and the rules that control how the grid will act. The rules will be sent to the rules manager class to set that up. The cell variables will be used to determine type of cells used, number of cells used, etc. All in all, these values will be fairly variable in what they do to have as much flexibility as possible with regards to how much control from the user is needed. This will also allow us to add more factors as needed for how the animation acts.

	The animation class will pass the rules onto the rules manager. The animation scene will call on the rules manager to help it determine the next step in the animation. The animation class will also pass on the cell options to the rule manager. Additional options that want to be included can easily be passed on to classes that will use them.

* Buttons to control animation and return to main menu. 

	We are planning to allow the user to control how the animation works. These buttons will be play, one step forward, pause, and reset. This part will interact with the display progression part of this class to show the current status of the grid on screen. Other features of control can easily be added by including more buttons that will call on display progression differently (eg reverse, go back one step). Lastly, we will also be including a return button that will return to the main menu where the user can set new options and start a new animation.

* Progress display to next step 

	This part of the class will be responsible for changing the display. It will call on the rules manager to do the calculations and return a grid array that contains the new state to display. Again the display is edited through the variables the user controls which easily allow new properties to be added.


## Rules Manager
This class contains the "rules" the animation runs on. It provides the calculations based on the rules and returns a new state for the animation to display.

* Rules

	This subcomponent contains the rules that have been read in from the XML file in the main menu class. These rules will be referred to by the calculation subcomponent that will determine the next step of the animation and when initiating the 2D array. The rules will also be read so we can determine the type of cells used. New features can be added depending on if specific overall rules are demanded to always be included, but our interpretation is all rules will come from the XML file. How the rules are interpreted can be made into new methods called on by this subcomponent to help determine things like type of cell wanted. Unfortunately this is fairly vague, but nothing more specific can really be said without basic knowledge of format of the rules.
	
* 2D Array of Cells

	This subcomponent contains the list of cells that exist in the grid. These cells will individually be initiated from the cell object based on the rules at the beginning. The calculations section will alter this array and the status of the cells in it based on the rules at the beginning. One thing to note is that, since we have the size of the array from the main menu, we can use that to calculate which cells in the array are in the middle, on the edge, and in the corner (and which edge/corner) which can be stored in the cell at initiation and will be useful in our calculations. Flexibility of this array is determined based on the variables read in at the beginning (eg number of cells) and how the rule interpreter handles the cells.

* Calculations

	This subcomponent is responsible for calculating the next state of the grid of cells (2D array) based on the rules provided. This will constitute referring to three other methods that calculates the state of an individual cell based on the rules and the surrounding cells. These three methods will be separated by location (edge, middle, corner) allowing the program to calculate the status more efficiently by already knowing number of surrounding cells. This part of the class will be called on by the animation class which will the display the status. The calculations section really only follows what the rules and 2D array tell it to do, so any new features would mainly be targeted at those parts.  
	
## Cell
This is the cell superclass object. It will contain factors that are determined to be common to all the cells. 
The user will control which type of cell it wants. Variables will be passed in from the rules manager to initiate the cells in the grid as the type of cell desired. It is hard to say what common variables will be included but one idea will be place on grid (edge, middle, corner and which edge or corner).
Additional common aspect to all cells can be added by including them in the main superclass. New types of cells can be added as new classes that inherit the new factors and can have new specific characteristics added likewise. 

* Different types of Cells

	Different types of cells will probably be used for different types of scenarios (ie, an on/off cell versus a range of values cell) which will spawn different factors (ie probability of changing based on state of surrounding vs calculation that changes gradient value based on values around it). As a result we will have different types of cells that inherit from the super class. They will be chosen based on the rules based on and the interpretation of the rules manager. It is fairly easy to add new types of cells as demanded by just creating new classes that inherit the super class. New aspects of specific types can be inserted into those classes as needed. 

## Use Cases
* _Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)_

	Assuming everything is initiated, we will have implemented this animation to use a type of cell that has a boolean state variable (on/off). Since we have already stored the location of the cell, that is retrieved to determine a middle cell. The middle cell will have passed from the calculation to the method that specifically addresses individual middle cell calculations. The method will retrieve the surrounding cells from the array. Using the rules that have been read in from the XML file, it will set the status of the middle array. 

* _Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)_
	
	This process is very similar to the previous one. Assuming everything is initiated, we will have implemented this animation to use a type of cell that has a boolean state variable (on/off). Since we have already stored the location of the cell, that is retrieved to determine a edge cell and where on the edge. The edge cell will have passed from the calculation to the method that specifically addresses individual edge cell calculations. The method will retrieve the surrounding cells from the array (edge specific will use the knowledge of which edge to not try and look for a non-existent cell). Using the rules that have been read in from the XML file, it will set the status of the middle array. 

* _Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically_

	The user will press the next step button which will call the calculations method in the rules manager class once to figure out only one next step. The calculations method will calculate the next state of every cell in the grid using the rules and current state of each cell/surrounding cells. The calculations method will return a new state of cells. The animations class will then use the display method to display the new state of the grid.
	
* _Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in an XML fire_

	Assuming initialization has begun, the XML file will have been read in and the rules manager will have recognized that this simulation is based on a boolean cell type (on fire or not on fire). As a result, we need to set the probCatch parameter for this type of cell.  As a result, the loop that will create all the cells in our grid will set this parameter as a value of probability of change for all the cells individually. This might seem a little unnecessary but in the spirit of flexibility our design is to have the parameter stored in each object for easy access (in case there is some need of having different value probabilities).
	
* _Switch simulations: use the GUI to change the current simulation from Game of Life to Wator_

	The user will hit the return button to get back to the main menu. In the main menu, the user can set the imported rules file to be the Wator XML file. After toggling the other options in the menu, the user can hit the start menu to get to the simulation of Wator.
	
## Justification of Components
The main class is a given to that is needed to start the program and doesn't need to do much besides that.

The main menu component is designed to do all the data retrieval and is the "set up" part of our code. It made sense to put all the gathering of variables and files for the animation in one location. Then we could just move it on to the next part where it is actually used. These variables and files are made available for the user to control for the purpose of flexibility. The main menu serves the purpose of allowing the user control of the simulation and handling all the input variables and then allows us to initiate the actual animation.

The animation component serves as the "graphics" display part of the animation and refer to the other classes that do the behind the scenes work. Conceptually it makes sense to have one class that takes care of just the display. The actual calculations can just be called upon and the new state retrieved. The different buttons allow the user to change how the animation works for flexibility purposes. The return to menu button is added to easily allow changing to a new simulation.

The rules manager is the "heavy lifter" of the group and the one that does the work. This is the class that holds onto the 2D array of cells and does our calculations for the next state. It made sense to us for one class to hold onto the 2D array of cells as well as operate on them. To that effect, we also need the interpretations of the rules to be in this component to define how calculations are made based on cell relations in the grid. Then we can just display in the animation component after the work is done.

The cell object holds the variables such as state, color when displayed, probability of change, etc. We decided we need a cell object to make it easier to hold the different aspects of each cell in the grid. This would allow packaging and organization to be easier to deal with. Our determination for having different cell types were based on the idea of being flexible. There many different cell types that could occur base on different simulations (eg boolean vs. gradient of states). Having different cell types that inherit the overall cell characteristics lets us accommodate these situations. It is also very easy to just add another cell type if we discover we miss something. As a result, this serves as an easy and logical building block for the grid of our simulation.


# DESIGN CONSIDERATIONS

We think this project has a lot more unknown details that gives our design a need for robustness. We want to only make the very general overlay of our classes in order to leave room for development later on. We know we might need our cells to be based on different underlying attributes so we have multiple subclasses of cells to account for this. One design decision that we discussed for a while was how to appropriately start the program (i.e. whether or not a splash screen was necessary, or if the program should be called from main) We felt it was best for Main to call a splash screen that prompted the game because it was gave us a way to facilitate any user input one might need before the start of the game. There are a lot of variations the file could make the application need to alter how it runs. This would require the rules management to be updated.

# TEAM RESPONSIBILTIES

Grant's job will be to work on the Cell class and all of it's subclasses. Naijiao's job is to work on the rules manager to make it robust and add a variety of rules, while also splitting duties with Vincent for the action screen. Vincent will also work on Main and Splash Screen.  
