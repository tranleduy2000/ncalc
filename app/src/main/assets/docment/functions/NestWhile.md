## NestWhile

```
NestWhile(f, expr, test)
```
> applies a function `f` repeatedly on an expression `expr`, until applying `test` on the result no longer yields `True`.

```
NestWhile(f, expr, test, m)
```
> supplies the last `m` results to `test` (default value: `1`).
	
```
NestWhile(f, expr, test, All)
```
> supplies all results gained so far to `test`.

### Examples

Divide by 2 until the result is no longer an integer:
``` 
>> NestWhile(#/2&, 10000, IntegerQ)
625/2
```
 




