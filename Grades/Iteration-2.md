# Iteration 2 Evaluation - Group 2

**Evaluator: [Hari Menon](mailto:hmenon@cs.jhu.edu)**

Overall this iteration looks well put together! But as usual there are things to improve. I have discussed most of this with you already; but here are some comments.

## Api
This looks pretty reasonable.

A minor point to consider: you are operating a lot on PGNs; currently the apis that do this take a pgn-id parameter in its content; it may be worth considering adding pgn in to your "resource hierarchy". For example, to import a game `POST` to `/chessgear/api/games/<username>/pgns`. To download you simply `GET` to `/chessgear/api/games/<username>/pgns/<pgnid>` and so on. It feels a bit more canonical.

## Class Diagrams

The primary concern I have is the rather disconnected nature of your class diagrams. For example, in the first diagram, the Game class is this isolated thing which has no relationship with the rest of it. Similarly the bottom diagram has a few "cliques" consisting of classes which don't seem to be related to each other in anyway. You want a more consolidated diagram where classes have clear relationships.

It is also a bit unclear what the diagrams represent. Is the top diagram the front-end and the bottom one the backend or is it all the backend and divided in to packages? The `BoardState`, for example, is very stateful class and is more appropriate on the backend.

Note also that you class diagram is very field heavy and has very very few methods. This, unfortunately, has the tendency to lead to a data-centric design. Since objects interact with each other via methods, they are often much more important than fields for our purposes.

For the next iteration, try to fix up the class diagram to be more cohesive and include one for the front-end. You can still divide them in to packages using the appropriate UML notation, but you should at least indicate the relations between classes in the system even across packages. Also remember to include more methods on your classes so that their interactions become more clear.

*(-6 Points)*

**Grade: (94/100)**
