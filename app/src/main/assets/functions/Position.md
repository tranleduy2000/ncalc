## Position
```
Position(expr, patt)
```
> returns the list of positions for which `expr` matches `patt`.

```
Position(expr, patt, ls) 
> returns the positions on levels specified by levelspec `ls`.

### Examples

```
>> Position({1, 2, 2, 1, 2, 3, 2}, 2)
{{2},{3},{5},{7}} 
```

Find positions upto 3 levels deep
```
>> Position({1 + Sin(x), x, (Tan(x) - y)^2}, x, 3)
{{1,2,1},{2}} 
```

Find all powers of x
```
>> Position({1 + x^2, x y ^ 2,  4 y,  x ^ z}, x^_)
{{1,2},{4}} 
```

Use Position as an operator
```
>> Position(_Integer)({1.5, 2, 2.5})
{{2}}
```