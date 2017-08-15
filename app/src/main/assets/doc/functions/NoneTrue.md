## NoneTrue

``` 
NoneTrue({expr1, expr2, ...}, test)
``` 
> returns `True` if no application of `test` to `expr1, expr2, ...` evaluates to `True`.

``` 
NoneTrue(list, test, level)
``` 

> returns `True` if no application of `test` to items of `list` at `level` evaluates to `True`.

``` 
NoneTrue(test)
``` 

> gives an operator that may be applied to expressions.

### Examples
```
>> NoneTrue({1, 3, 5}, EvenQ)
True
>> NoneTrue({1, 4, 5}, EvenQ)
False
>> NoneTrue({}, EvenQ)
True
```