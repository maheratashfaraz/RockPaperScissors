# RockPaperScissors

![Demo gif](https://github.com/maheratashfaraz/RockPaperScissors/blob/master/images/rockpaperscissors_demo.gif?raw=true)

Virtual game of rock paper scissors, using a camera!

This repository contains the source code for training the feed-forward neural network using the [Encog](http://www.heatonresearch.com/encog/) library, as well as the GUI as seen above.

## Installation

First clone the repository using:

```
git clone https://github.com/maheratashfaraz/RockPaperScissors.git
```
Gradle is used to install most of the dependencies, however OpenCV is also required.

To install gradle, follow the guide [here](https://docs.gradle.org/current/userguide/installation.html).

Once gradle is installed, in the project directory run `gradle build clean`.

To install openCV, this depends on your IDE. For basic install instructions for eclipse follow
[here](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html).

## Training

To train a new neural network, first set the correct file paths for the training data in [Train.java](https://github.com/maheratashfaraz/RockPaperScissors/blob/master/src/main/neural/Train.java). Running this
script will train a new neural network for you to use in for recognition.

## GUI

To run the GUI, run the [GUIMain.java](https://github.com/maheratashfaraz/RockPaperScissors/blob/master/src/main/sample/GUIMain.java). If you changed the name of the neural network file, you must change the file path
at the top of the class.
