# Iteration 5 - Group 2

**Evaluator: [Hari Menon](mailto:hmenon@cs.jhu.edu)**

## Running the Project

Your backend is working for the most part. Sending it requests with (say) curl seems to work (and the tests run).

But the frontend appears to be woefully incomplete unless I am missing something. For example, something as basic as the account tab fails to work (even on the latest version on master)? And corresponding to pressing `Submit` on import you have `<form ... action="submit.php" ...>`. 

Maybe the work being done on the frontend branch will help. But overall there is a major imbalance between what your application is capable of and what any user can see. Remember that for the demo, you need everything together and your audience is not just me.

[-7 Points]

## Testing

It is nice that your backend has a decent chunk of low level tests for functionality. However it looks to me like the number of tests that cover failure cases is limited. This is something to improve upon. You also definitely need slightly higher level tests, that is tests that run at the level of the REST api that verify that things behave as expected from a client's point of view.

[-2 Points]

Your frontend has no tests at all, though I think it is a little too late to rectify that now.

## Iterations Plan

The plan seems fine though I really did hope you had be further along on the integration at this point. Anyway make sure you have everything integrated for the demo.

## Code Quality

Your backend code is pretty readable. One main issue: when querying the database, you are often constructing queries by concatenating various parameters. E.g. `"SELECT S.username FROM User as S where S.username = '"+username+"'"`. You do not want to do this as it is both error prone and in some cases a vector for [SQL injection](https://en.wikipedia.org/wiki/SQL_injection). Instead you can use query parameters. Take a look at how this is done in the Todo app. This will not be hard to do.

Minor things I found while looking through the code:
- The default name you choose for new PGN files is dependent only on `System.currentTimeMillis()`. Unfortunately this is not a very precise value and if someone adds PGNs really quickly (maybe programatically or in a test), it is possible that files are clobbered. You may want to consider something like `java.util.UUID` to generate a name.
- Instead of having `Engine.obtainResultAndMaybePrint` take a parameter to dump the data to terminal, you may want to consider using a real logger (like was done in Todo). Loggers have a concept of "log levels" like ERROR, WARNING, INFO, DEBUG etc that can be enabled/disabled (globally) on demand. That allows you to tweak your output to your requirements while not having to clutter function signatures.

Your frontend on the other hand is in a much more haphazard state. Much of your javascript is inside index.html; at the very least move the code to a separate file (possibly separate files). You may want to take a look at the frontend code of Assignment-1 for some ideas. Simply put, Javascript is *also* code; everything that you know about modularization also applies here.

[-6 Points]

**Total Grade: 85/100**
