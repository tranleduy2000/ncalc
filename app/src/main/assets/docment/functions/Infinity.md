## Infinity

``` 
Infinity
``` 
> represents an infinite real quantity.

### Examples
```
>> 1 / Infinity
0
 
>> Infinity + 100
Infinity
```

Use `Infinity` in sum and limit calculations:
```
>> Sum(1/x^2, {x, 1, Infinity})
Pi ^ 2 / 6
 
>> FullForm(Infinity)
DirectedInfinity(1)
 
>> (2 + 3.5*I) / Infinity
0.0"
 
>> Infinity + Infinity
Infinity
```

Indeterminate expression `0` Infinity encountered.
```
>> Infinity / Infinity
Indeterminate
```