## Scan

```
Scan(f, expr)
```

> applies `f` to each element of `expr` and returns 'Null'.

```
Scan(f, expr, levelspec)
```

> applies `f` to each level specified by `levelspec` of `expr`.

### Examples
```
>> Scan(Print, {1, 2, 3})
 1
 2
 3
>> Scan(Print, f(g(h(x))), 2)
 h(x)
 g(h(x))
>> Scan(Print)({1, 2})
 1
 2
>> Scan(Return, {1, 2})
1
```