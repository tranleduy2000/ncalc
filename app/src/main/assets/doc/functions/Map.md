## Map

```
Map(f, expr)  or  f /@ expr 
```

> applies `f` to each part on the first level of `expr`.
	
```
Map(f, expr, levelspec)
```

> applies f to each level specified by `levelspec` of `expr`.

### Examples
```
>> f /@ {1, 2, 3}
{f(1),f(2),f(3)}
>> #^2& /@ {1, 2, 3, 4}
{1,4,9,16}
```
 
Map `f` on the second level:
```
>> Map(f, {{a, b}, {c, d, e}}, {2})
{{f(a),f(b)},{f(c),f(d),f(e)}}
```

Include heads:
```
>> Map(f, a + b + c, Heads->True) 
f(Plus)[f(a),f(b),f(c)]
```

Level specification a + b is not of the form n, {n}, or {m, n}.
```
>> Map(f, expr, a+b, Heads->True) 
Map(f, expr, a + b, Heads -> True)
```