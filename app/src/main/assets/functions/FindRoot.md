## FindRoot

```
FindRoot(f, {x, xmin, xmax})
```

> searches for a numerical root of `f` for the variable `x`, in the range `xmin` to `xmax`. 

### Examples
```
>> FindRoot(Exp(x)==Pi^3,{x,-1,10}, Bisection)
{x->3.434189647436142}

>> FindRoot(Sin(x), {x, -0.5, 0.5})
{x->0.0} 
```
