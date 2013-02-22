# event-stack

### Events

#### Structure
  - Stack
    x FIFO
    - FILO
  x List / Vector
    - Could add to either end
      - I'm not sure what it would mean to add things at the end. Probably just
        an unnesisary complication.
  x Set
    - To represent all event happening simultaneously
    - Don't need to worry about double events
    - Means you can't represent click - time passing - click
  x Tree
    - May allow for bubbling
    - I don't think having events be children of other events really makes
      sense. Though you could use this to represent different streams of
      events. But at the ends you'd still need stacks.
    - Having events in different stacks is semantically similar to having some
      kind of type field. However it would provide better lookup speed.
    - This means that if you care about two different types of event then you
      can't know what order they're in unless we track some kind of global
      ordering as part of the events!

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

#### In comparison to other systems

- I like that one listener can be before or after another. This allows things
  like doors consuming movement events if they wish to preven it.
  - A streaming system where each event is consumed by every listener wouldn't
    allow this directly.
  - You could emit a different type of event for 'door-approved-movement' but
    then your trap code would need to listen for 'door-approved-movement'.
  - Better to have the movement code not know that there are a dozen other
    pieces of the app playing man in the middle.
- To have a spread sheet kind of system you need to have a single piece of code
  say "I'm responsible for Cell A4. I need to know about A1, and B1 to do my
  job."
  - Perhaps that's a good thing. I guess if you want a trap to blow a player
    backwards then perhaps it should be emitting 6 move-south events.
  - So should we bind state up with listeners? So that a single listener only
    manages it's owns state. But everyone sees a version of the whole game
    state which is a combination of all these little state pieces?
- Having explicit listeners is great. It means we can have a trap that removes
  it's self as a listener once it's detonated. Or a switch that removes a trap.
- It's fine for listeners to see the same event multiple times. The state of
  the game may have changed by the time they see it the second time around.

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
