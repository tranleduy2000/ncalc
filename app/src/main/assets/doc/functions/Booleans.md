## Booleans

```
Booleans
```

> is the set of boolean values.
 
### Examples
```
>> Solve(Xor(a, b, c, d) && (a || b) && ! (c || d), {a, b, c, d}, Booleans)
{{a->False,b->True,c->False,d->False},{a->True,b->False,c->False,d->False}}
```
