## Or

``` 
Or(expr1, expr2, ...)'
``` 
> `expr1 || expr2 || ...` evaluates each expression in turn, returning `True` as soon as an expression evaluates to `True`. If all expressions evaluate to `False`, `Or` returns `False`.

### Examples

``` 
>> False || True
True
``` 

If an expression does not evaluate to `True` or `False`, `Or` returns a result in symbolic form:
``` 
>> a || False || b
a || b
``` 