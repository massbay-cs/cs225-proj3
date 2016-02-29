External Libraries
==================

* JDK 8
* JavaFX
* Lombok


Class Descriptions
==================


Game
----

Basic prototype for a GUI.  The GUI hasn't been finalized yet.  It will use JavaFX.  Provides timer for Race.


Location
--------

Immutable.  Describes a single point on a map.


RaceLeg
-------

Ground between two Locations.  Describes the conditions of the track.  Will be used to calculate how long it will take each car to travel between the two Locations with its specific CarBuild.  Directional.  Mostly immutable, but weather can change (Conditions).


Conditions
----------

Describes the weather on a RaceLeg.  This may affect the usefulness of high-friction tires, etc.


Race
----

Collection of all race logic except the timer.  Does any math that isn't performed by Car.  Each tick, it calculates which car will be next reach its next destination in the race.  It then fast-forwards the race by that exact amount of time, bringing at least one to its exact destination, but never past.  It does not update cards that have already completed.  Depending on the GUI implementation, it may perform a callback to update the GUI.


Car
---

Stores data about a specific car's state in the race.  Also holds a CarBuild, which contains reusable metadata about how the car performs.  Performs some math to determine how long it will take to reach the next destination and to keep track of location in the race.


CarBuild
--------

Immutable.  Interface for build of a car.  Specific builds will be designed at a later point; we'll start with just one for rapid development, and then two or three for debugging.  The exact implementations are of little consequence to the overall design and meant to be modular.  They could theoretically be provided by a third-party.


Engine, Wheels, Tires, GasTank, GasType
---------------------------------------

Describe the metrics of a CarBuild.  Except for GasType, which is an enum, all are interfaces.  Like CarBuild, they are designed to be modular and potentially provided by a third-party.


ReachLocationEventHandler
-------------------------

Used for callbacks when a car reaches a checkpoint.  Aiming for an event-based model, rather than polling.