## Clip

```
Clip(expr)
```

> returns `expr` in the range `-1` to `1`. Returns `-1` if `expr` is less than `-1`. Returns `1` if `expr` is greater than `1`.
  
```
Clip(expr, {min, max})
```

> returns `expr` in the range `min` to `max`. Returns `min` if `expr` is less than `min`. Returns `max` if `expr` is greater than `max`.
  
```
Clip(expr, {min, max}, {vMin, vMax})
```

> returns `expr` in the range `min` to `max`. Returns `vMin` if `expr` is less than `min`. Returns `vMax` if `expr` is greater than `max`.

### Examples

```
>> Clip(Sin(Pi/7))
Sin(Pi/7)

>> Clip(Tan(E))
Tan(E)

>> Clip(Tan(2*E)
-1

>> Clip(Tan(-2*E))
1

>> Clip(x)
Clip(x)

>> Clip(Tan(2*E), {-1/2,1/2})
-1/2

>> Clip(Tan(-2*E), {-1/2,1/2})
1/2

>> Clip(Tan(E), {-1/2,1/2}, {a,b})
Tan(E)

>> Clip(Tan(2*E), {-1/2,1/2}, {a,b})
a

>> Clip(Tan(-2*E), {-1/2,1/2}, {a,b})
b
```