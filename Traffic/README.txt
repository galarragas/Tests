COMMENTS ON THE IMPLEMENTATION OF THE TRAFFIC ASSIGNMENT

The problem described is in my opinion perfectly fitting an implementation based on the Actor model implemented by
frameworks as for example AKKA.
I decided not to use any framework but kept the logic as close as possible as the framework works.
Some of the code might be better modeled to make it more testable and modularised, for example the file parsing logic
might have been written in a specific object, but I wanted to keep the time in the four hours specified.
The POI storage could have been implemented using MongoDB that will have also offered geographical queries.
The in-memory implementation is just a quick one not optimised. A faster implementation might for example have stored
the POIs in sub-lists partitioning the latitude or the longitude avoiding to browse the full set of points.
The distance calculation function has been found on the internet and copied into the Point class.
The best would be to use a GIS library. I tried to find one to quickly insert into the project but didn't find one easy
to integrate.

To run:

Build with maven and then execute:

java -jar target/HailO-1.0-SNAPSHOT-jar-with-dependencies.jar
