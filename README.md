# elevator simulation

##Build the App

Type this command to build the archive:

    mvn clean package

##Launch the App

Type this command to run the application:

    java -jar target/elevator-1.jar [arguments]

##App arguments

    1. floorCount integer - count of the floors in the building (min:5, max: 20)
    2. floorHeight - int - height of the each floor in meters 
    3. speed - int - speed of the lift in meters per second 
    4. doorsTime int - time in seconds between openeing and closing doors 

##Commands
 i|o<number> where
 i - inside elevator
 o - at a floor
 <number> - floor number, from 1 to first app arg
 q - quit app
 
 samples:
 
 i5 - '5' button pushed inside elevator
 o7 - button pushed at 7 floor
 

