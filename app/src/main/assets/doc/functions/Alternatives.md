## Alternatives

```
Alternatives(p1, p2, ..., p_i)
```

or

```
p1 | p2 | ... | p_i
```
> is a pattern that matches any of the patterns `p1, p2,...., p_i`.
 
### Examples

```
>> a+b+c+d/.(a|b)->t
c + d + 2 t
```
 
