## Which

```
Which(cond1, expr1, cond2, expr2, ...)
```

> yields `expr1` if `cond1` evaluates to `True`, `expr2` if `cond2` evaluates to `True`, etc.
 
### Examples
```
>> n=5;
>> Which(n == 3, x, n == 5, y)
y
 
>> f(x_) := Which(x < 0, -x, x == 0, 0, x > 0, x)
>> f(-3)
3
```

If no test yields `True`, `Which` returns `Null`:
```
>> Which(False, a)
```

If a test does not evaluate to `True` or `False`, evaluation stops
and a `Which` expression containing the remaining cases is returned:
```
>> Which(False, a, x, b, True, c)
Which(x,b,True,c)
```

`Which` must be called with an even number of arguments:
```
>> Which(a, b, c)
Which(a, b, c)
```