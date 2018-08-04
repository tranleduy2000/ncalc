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

You can use `Break()` and `Continue()` inside `Do`:

```
>> Do(If(i > 10, Break(), If(Mod(i, 2) == 0, Continue()); Print(i)), {i, 5, 20})
 | 5
 | 7
 | 9
 
>> Do(Print("hi"),{1+1})
 | hi
 | hi
```

The [A005132 Recaman's sequence](http://oeis.org/A005132) integer sequence

```
>> a = {1}; Do( If( a[ [ -1 ] ] - n > 0 && Position( a, a[ [ -1 ] ] - n ) == {}, a = Append( a, a[ [ -1 ] ] - n ), a = Append( a, a[ [ -1 ] ] + n ) ), {n, 2, 70} ); a
{1,3,6,2,7,13,20,12,21,11,22,10,23,9,24,8,25,43,62,42,63,41,18,42,17,43,16,44,15,45,14,46,79,113,78,114,77,39,78,38,79,37,80,36,81,35,82,34,83,33,84,32,85,31,86,30,87,29,88,28,89,27,90,26,91,157,224,156,225,155}
```
