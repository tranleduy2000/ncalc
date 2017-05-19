## FixedPoint  

``` 
FixedPoint(f, expr)
```  
 
> starting with `expr`, iteratively applies `f` until the result no longer changes.

``` 
FixedPoint(f, expr, n)
``` 

> performs at most `n` iterations.

### Examples  
``` 
>> FixedPoint(Cos, 1.0)
0.7390851332151607
 
>> FixedPoint(#+1 &, 1, 20)
21

>> FixedPoint(f, x, 0)
x
```

Non-negative integer expected.
```
>> FixedPoint(f, x, -1)
FixedPoint(f, x, -1)

>> FixedPoint(Cos, 1.0, Infinity)
0.739085
```