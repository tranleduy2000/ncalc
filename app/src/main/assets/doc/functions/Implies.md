## Implies

``` 
Implies(expr1, expr2)
``` 
> `Implies(expr1, expr2)` evaluates each expression in turn, returning `True` as soon as the first expression evaluates to `False`. If the first expression evaluates to `True`, `Implies` returns the second expression.

### Examples
```
>> Implies(False, a)
True
>> Implies(True, a)
a
```

If an expression does not evaluate to `True` or `False`, `Implies` returns a result in symbolic form:
```
>> Implies(a, Implies(b, Implies(True, c)))
Implies(a,Implies(b,c))
```