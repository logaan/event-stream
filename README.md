# event-stack

### Events

#### Structure
  - Stack
    x FIFO
    - FILO
  - List / Vector
    - Could add to either end
  - Set
    - To represent all event happening simultaneously
    - Don't need to worry about double events
  - Tree
    - May allow for bubbling

#### Consumption
  x Reacting results in consumption
    - Conditionally disaproved. You could just re-emit.
  - Dissapears once it's been seen by all listeners
  - Listeners see all events and can react to many at once and choose to
    consume them or not
    - I think this is a good idea for the core
    - Allows for more tailored solutions lower
    - Means that you could choose to ignore click events until they become
      double clicks within a certain time

### Examples
- Walking through locked door
  - press w
  - move north
  - reach door
  - unlock door
  - door unlocked
  - moved north
- Identifying double click
  - left click
  - 50ms passes
  - left click

- If the player has is trying to move onto a door
 - If the door is unlocked
   - They keep trying to move. This lets other things prevent movement,
     actually moving is a default behavior. So if you want to prevent
     something happening then you need to register your listener with a
     higher priority and consume the event. This requires that event
     listeners are orders and that consumption of an event is optional.
 - If the door is locked
   - And the player had the right key
     - An unlock event is triggered. This requires that events are processed
       FILO, or not processed in any particular order.
     - And they keep trying to move north. I guess this means that you don't
       reset after each new event is emitted because otherwise this would be
       resan infinite loop. But that might be able to be overcome.
   - If they don't have the right key
     - A failed unlock event is triggered
     - And they stop trying ot move north


## Usage

FIXME

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
