# LibraryManagementSystem1
## Java Code and JavaFX 
I chose JavaFX for the design of UI and used scenebuilder to design the layouts for each page.
The Java Project is divided into following sections.
1.	Java Code(ch.makery.library.code)- This contains the main Class of the JAVAFX project. The application gets launched from here and the different fxml files that are required are loaded from here.
2.	Java Model(ch.makery.library.model)- This contains all the class structures for all the tables that have been used in the UI.
3.	Java Util(ch.mkaery.library.util) – This contains all the DB utility functions that almost all interactions with mySQL would require.
4.	Java View(ch.makery.library.View)- This contains the FXML elements on which the actual UI components lie and the Java Controller classes that initialise these components, handle these components(buttons) and get or set values to these components(TextFields,TableViews). Almost all of the queries that need to be run for each action have been specified and run from the controller class functions.
UI Design
To sum up the UI design very briefly- there is a single Tab Pane consisting of 5 tabs. One each for Book Search and Checkout, Add new Borrower, Return Book, Pay fine for one book at a time, Pay total fine per borrower. Identical refresh functionalities have been provided on both Pay Fine pages. Each tab has it’s own separate fxml file and hence it’s own controller. These fxml files have been directly included in the main Tab Pane fxml file to keep the Tab Pane Controller file less cluttered and to make the design more modular.
