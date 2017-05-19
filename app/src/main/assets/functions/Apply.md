## Apply

```
Apply(f, expr)

f @@ expr
```

> replaces the head of `expr` with `f`.
```
Apply(f, expr, levelspec)
```

> applies `f` on the parts specified by `levelspec`.


### Examples
```
>> f @@ {1, 2, 3}
f(1, 2, 3)
>> Plus @@ {1, 2, 3}
6
```
 
The head of $expr$ need not be 'List':
```
>> f @@ (a + b + c)
f(a, b, c)
```

Apply on level 1:
```
>> Apply(f, {a + b, g(c, d, e * f), 3}, {1})
{f(a, b), f(c, d, e*f), 3}
```

The default level is 0:
```
>> Apply(f, {a, b, c}, {0})
f(a, b, c)
```

Range of levels, including negative level (counting from bottom):
```
>> Apply(f, {{{{{a}}}}}, {2, -3})
{{f(f({a}))}}
```

Convert all operations to lists:
```
>> Apply(List, a + b * c ^ e * f(g), {0, Infinity})
{a,{b,{c,e},{g}}}
```

Level specification x + y is not of the form n, {n}, or {m, n}.
```
>> Apply(f, {a, b, c}, x+y) 
Apply(f, {a, b, c}, x + y)
```