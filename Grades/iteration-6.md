# Presentation
The presentation was overall understandable.

Positives:
- You assumed nothing and explained chess, the basic rules, methods of study and the problem you are trying to solve
- You had a good explanation of PGN
- You explained clearly what you have done

Issues:
- You should not interrupt a speaker /even if/ you have a better explanation. It confuses the audience.
- You also should try to avoid chatting amongst yourselves when the presentation is going on
- Your UML diagram was unreadable. You even remarked on it! It is best to show portions of the diagram and explain rather than have a full diagram that is not readable.
- The code used in code review was too tiny to read. Same with the PGN. The color scheme for the code also contributed to the readability issues. It is best to use a light background when presenting code.
- Typo on slide: "jQuerry" instead of "jQuery"

# Demo
Overall the demo went fine.

# UI quality
The UI was a bit primitive, likely due to the relatively late start. However it is quite functional. 

One of the suggestions that came up was to create different perspectives for white and black games.

# Tests
Your low level test suite for the backend is reasonable. But as pointed out in Iteration-5 comments, you need more higher level tests - tests that operate at the level of the REST api - to ensure that your server's overall functionality is correct.

Your frontend has no tests at all. But the amount of front-end code is rather limited; so it is less of a problem. On the other hand it makes having REST api level tests in the backend critical.

# GitHub Usage
Good mileage from Github issues

# Code inspection
The backend code quality is overall reasonable. It looks like you are now correctly using (sql) query parameters.

# Project difficulty
This is an original, non-CRUD app with non-trivial data structures, algorithms and parsing related work.

#Overall
Good work on the project overall. You have an interesting application and have hit most of your planned features. With a bit more polish, it will be a practical and useful chess study tool.

*Grade: 95/100*
