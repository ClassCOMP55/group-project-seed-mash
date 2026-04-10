## Setup
1. Download JavaFX 21 SDK from https://gluonhq.com/products/javafx/
After you got to that website, scroll down there until you see the options u can download
Select 21.something for the JavaFX version
Then put it in a location where it will be easy for you to find.
Then extract it
2. Add JavaFX jars to build path
To add the JavaFx file to the build path:
Project >> Properties >> Libraries
Then click the class path so that "Add External JARs" button on the right will be visible.
Click "Add External JARs" then locate your extracted JavaFX file.
Click it, click the folder called "lib" and select everything from there.
Then apply it and close it.
3. Add VM arguments:
--module-path "PATH_TO_JAVAFX\lib" --add-modules javafx.controls,javafx.media --add-opens javafx.base/com.sun.javafx.logging=ALL-UNNAMED
To add these, you will need to go to run configurations which you can get to by clicking the arrow down option beside your run button.
Then when you click the arguments tab, there will be an option where you will be able to add your VM arguments.
put everything in as shown in the line 15 (But remember that you will need to choose your own file path on where you saved your file instead of "PATH_TO_JAVAFX")
