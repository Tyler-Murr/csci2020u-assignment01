# CSCI 2020U: Assignment #1
 Tyler Murray: 100817518

## Project information
In this project the task involved creating software that was able to sort through a file to determine if it was spam or not. First we had to train the program by reading through the files in spam and ham folder and creating a probability map for the probability that a word certains word in a file made it spam. Then the program will iterate over test files using this map and calculate the probability that file is spam. A table of this data will then be produced on an html file along with the accuracy and precision of the program.    


## How to run
In order to run this file first you run the spamDetectorServer api folder to create the web api.Then you open the index.html in SamDetectorClient and the main.js file should automatically fetch the data from the endpoints and display them approprietly


