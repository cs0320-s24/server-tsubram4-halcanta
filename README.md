> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
Project name: Server

Project description: Creating a web API Server that allows for data retrieval and search. 

Team members: Habram Alcantar, Tanay Subramanian

Contribution: Roughly 20 combined hours of work

Link to repo: https://github.com/cs0320-s24/server-tsubram4-halcanta

# Design Choices
High-level design: The Server class creates a web server for handling requests to the Census and CSV data. CensusHandler and RequestHandler respond to incoming requests from the Census and CSV data, respectively. The CacheManager class is used to cache information retrieved from the ACS data source. APICensusDataSource connects the web server to the Census API. The CensusDataSource interface mandates a getBroadbandData method which is implemented by APICensusDataSource.

Key data structures: We used a list of list of strings to represent responses to queries. This was converted to a JSON object to display on the web server. We used a hashmap to store and retrieve requests and responses.

# Errors/Bugs
From our rigorous testing, we don't think our final code contains any significant errors.

# Tests
We have a testing file to test CensusHandler and RequestHandler, focusing on the loadcsv, viewcsv, searchcsv, and broadband endpoints. We also have a testing file to test CacheManager. We tried to cover both integration and unit tests, including any exceptions or errors. The comments in our code give more specifics on each of the tests and their purposes.

# How to...
Run tests by running each Test class entirely.

Build program by first inputting data files in the data folder.

Run program by executing "mvn package" in the terminal, followed by "./run". Then click the link to access the web browser. Here, you can modify the http address to specify the name of the state and county you want to search the census data for.
