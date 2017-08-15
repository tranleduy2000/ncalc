## Do

```
Do(expr, {max})
```
> evaluates `expr` `max` times.

```
Do(expr, {i, max})
```
> evaluates `expr` `max` times, substituting `i` in `expr` with values from `1` to `max`.

```
Do(expr, {i, min, max})
```
> starts with `i = max`.

```
Do(expr, {i, min, max, step})
```
> uses a step size of `step`.

```
Do(expr, {i, {i1, i2, ...}})
```
> uses values `i1, i2, ... for i`.

```
Do(expr, {i, imin, imax}, {j, jmin, jmax}, ...)
```

> evaluates expr for each j from jmin to jmax, for each i from imin to imax, etc. 
  
### Examples
``` 
>> Do(Print(i), {i, 2, 4})
 | 2
 | 3
 | 4
 
>> Do(Print({i, j}), {i,1,2}, {j,3,5})
 | {1, 3}
 | {1, 4}
 | {1, 5}
 | {2, 3}
 | {2, 4}
 | {2, 5}
``` 

You can use 'Break()' and 'Continue()' inside 'Do':
``` 
>> Do(If(i > 10, Break(), If(Mod(i, 2) == 0, Continue()); Print(i)), {i, 5, 20})
 | 5
 | 7
 | 9
 
>> Do(Print("hi"),{1+1})
 | hi
 | hi
``` 