# Conway's Game of Life (GoL) in JavaFX

## Intro
This is a Java port of my original Conway's Game of Life project for my final year dissertation that I originally coded in C#.

GoL is a very well documented cellular automaton invented in the 1970s and is a very cool demonstration of how incredibly complex
behaviours can emerge from very simple rules. Read the [Wikipedia page](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) to understand what these rules are. It has also been proven that this automaton is Turing Complete, which means that a contrived execution of a well designed GoL instance can simulate the components that are necessary for a Turing Machine to operate. Which means that this little game is capable of any computation as posited by the Church-Turing thesis. Pretty cool stuff.

Here is a quick demonstration of how this program currently looks.

![](conway.gif)

## Building and running

You need maven and java 8

1. mvn clean jfx:jar
2. java -jar target/jfx/app/conway-1.0-SNAPSHOT-jfx.jar

Once you run that command you will see a GUI appear with a few controls to start/stop/step/create random patterns.

