## Equivalent

``` 
Equivalent(expr1, expr2, ...)
``` 
> `Equivalent(expr1, expr2, ...)` is equivalent to `(expr1 && expr2 && ...) || (!expr1 && !expr2 && ...)`.

### Examples
```
>> Equivalent(True, True, False)
False
```

If all expressions do not evaluate to 'True' or 'False', 'Equivalent'
returns a result in symbolic form:
```
>> Equivalent(a, b, c)
Equivalent(a,b,c)
```

Otherwise, 'Equivalent' returns a result in DNF
```
>> Equivalent(a, b, True, c)
a && b && c
>> Equivalent()
True
>> Equivalent(a)
True
 ```