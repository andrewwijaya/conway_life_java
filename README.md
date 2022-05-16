# Conway's Game of Life (GoL) in JavaFX

## Intro
This is a Java port of my original Conway's Game of Life project for my final year dissertation that I originally coded in C#.

GoL is a very well documented cellular automaton invented in the 1970s and is a very cool demonstration of how incredibly complex
behaviours can emerge from very simple rules. Read the [Wikipedia page](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) to understand what these rules are. It has also been proven that this automaton is Turing Complete.

Here is a quick demonstration of how this program currently looks.

![](conway.gif)

## Building and running

You need maven and java 8

1. mvn clean jfx:jar
2. java -jar target/jfx/app/conway-1.0-SNAPSHOT-jfx.jar

Once you run that command you will see a GUI appear with a few controls to start/stop/step/create random patterns.

Feature ideas:
1. Port the rest of the C# code
2. Generalise rules
3. Extend stability detection and automate rule generation to search for unstable rules
4. Generalise the dimensions and automate dimension generation to search for interesting dimensions
5. Save initial state, save results
6. Stability
7. Collect statistics, average life per x size grid
8. Deactivate UI and run continuously
9. In continuous mode, reporting and result generation
10. Snapshot viewer
11. Change colors
12. Increase FPS
13. Create .gol file format and parser to store gol snapshots - csv

Bugs:
1. False stability detection - in some cases, stability is falsely detected when the grid is still evolving. The active
areas of the grid are continually evolving and some parts are either still lives or dead. The active areas are non-periodic
however the algorithm can flag stability as unknown periods are interpreted as dead (period 0). Question is, are dead cells
classed as period 0? Does periodicity only apply to live cells? Comparing the previous grid periodicity with an empty grid
partially solves the problem. Candidate solution: areas that are non periodic but was at some point in the past alive must
be checked for periodicity? Is stability detection also restricted by the window to check periodicity? (32 bit string)
