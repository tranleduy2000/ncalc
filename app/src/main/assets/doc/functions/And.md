## And

``` 
And(expr1, expr2, ...) 
``` 
> `expr1 && expr2 && ...` evaluates each expression in turn, returning `False` as soon as an expression evaluates to `False`. If all expressions evaluate to `True`, `And` returns `True`.
 
### Examples

``` 
>> True && True && False
False
``` 

If an expression does not evaluate to `True` or `False`, `And` returns a result in symbolic form:
``` 
>> a && b && True && c
a && b && c
``` 