# BasicATM Project
## How to run project
1. Download the BasicATM.jar located in the out/artifacts/BasicATM_jar located within this repo
2. Open up a terminal or command prompt
3. In the terminal, go to the location where you downloaded the jar
4. On the command line enter the following command, when in same directory as file(you will need java 8 installed)
```java -jar BasicATM.jar```
5. You should be greeted with a screen like this: ![ATM Splashscreen](https://github.com/zbartholomew/BasicATM/blob/master/Screen%20Shot%202018-08-07%20at%206.19.15%20PM.png)
6. I have provided 3 dummy customers with the following credentials for you to test on
```
Userame: johnsmith   PIN: 1234
Userame: johndoe     PIN: 1111
Userame: billybob    PIN: 2222
```

## Known Issues
* The scanner logic in the front end needs more error checking

## Future enhancements
* Possibly split out business logic from ATM and use it merely as a view - have controllers for data that will be manipulated
* Instead of keeping static field of all users we could split this out and use persistent storage like MySQL
* Allow customers to have multiple accounts
* Add enums for Account Type, Transaction Type
* Have to worry about timezones and timestamps to be same day
* Probably should use BigDecimal when dealing with money, however sine we have a reasonably daily limit we can use int
* Create custom exceptions to be more clear about error
* Could make the daily withdraw limit changeable
* Obscure PIN from input
* Create better front end with more robust error handling and testing
* Many many more, but these are just to name a few
