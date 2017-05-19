## AnyTrue

``` 
AnyTrue({expr1, expr2, ...}, test)
``` 
> returns `True` if any application of `test` to `expr1, expr2, ...` evaluates to `True`.

``` 
AnyTrue(list, test, level)
``` 
> returns `True` if any application of `test` to items of `list` at `level` evaluates to `True`.

``` 
AnyTrue(test)
``` 
> gives an operator that may be applied to expressions.

### Examples
``` 
>> AnyTrue({1, 3, 5}, EvenQ)
False
>> AnyTrue({1, 4, 5}, EvenQ)
True
>> AnyTrue({}, EvenQ)
False
``` 