## FixedPointList

``` 
FixedPointList(f, expr)
```  
 
> starting with `expr`, iteratively applies `f` until the result no longer changes, and returns a list of all intermediate results. 

``` 
FixedPointList(f, expr, n)
``` 

> performs at most `n` iterations.

### Examples  
``` 
>> FixedPointList(Cos, 1.0, 4)   
{1.0,0.5403023058681398,0.8575532158463934,0.6542897904977791,0.7934803587425656} 
```

Observe the convergence of Newton's method for approximating square roots:   
```
>> newton(n_) := FixedPointList(.5(# + n/#) &, 1.);   
>> newton(9)   
{1.0,5.0,3.4,3.023529411764706,3.00009155413138,3.000000001396984,3.0,3.0}
```

Get the "hailstone" sequence of a number:   
```
>> collatz(1) := 1;   
>> collatz(x_ ? EvenQ) := x / 2;   
>> collatz(x_) := 3*x + 1;   
>> FixedPointList(collatz, 14)   
{14,7,22,11,34,17,52,26,13,40,20,10,5,16,8,4,2,1,1} 
```

``` 
>> FixedPointList(f, x, 0)   
{x}  
```

Non-negative integer expected.
```
>> FixedPointList(f, x, -1)      
FixedPointList(f,x,-1)   
 
>> Last(FixedPointList(Cos, 1.0, Infinity))   
0.7390851332151607  
```