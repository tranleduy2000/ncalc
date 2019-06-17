## NestWhileList

```
NestWhileList(f, expr, test)
```

> applies a function `f` repeatedly on an expression `expr`, until applying `test` on the result no longer yields `True`. It returns a list of all intermediate results.

```
NestWhileList(f, expr, test, m)
```

> supplies the last `m` results to `test` (default value: `1`). It returns a list of all intermediate results.
	
```
NestWhileList(f, expr, test, All)
```

> supplies all results gained so far to `test`. It returns a list of all intermediate results.

### Examples

 
 




