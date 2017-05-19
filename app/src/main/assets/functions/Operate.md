## Operate
```
Operate(p, expr)
```

> applies `p` to the head of `expr`.
	
```
Operate(p, expr, n)
```

> applies `p` to the `n`th head of `expr`.

### Examples
```
>> Operate(p, f(a, b))
p(f)[a,b]
```

The default value of `n` is `1`:
```
>> Operate(p, f(a, b), 1)
p(f)[a,b]
```

With `n = 0`, `Operate` acts like `Apply`:
```
>> Operate(p, f(a)[b][c], 0)
p(f(a)[b][c])

>> Operate(p, f(a)[b][c])
p(f(a)[b])[c] 

>> Operate(p, f(a)[b][c], 1)
p(f(a)[b])[c]

>> Operate(p, f(a)[b][c], 2)
p(f(a))[b][c] 

>> Operate(p, f(a)[b][c], 3)
p(f)[a][b][c]

>> Operate(p, f(a)[b][c], 4)
f(a)[b][c]

>> Operate(p, f)
f

>> Operate(p, f, 0)
p(f)
```

Non-negative integer expected at position `3` in `Operate(p, f, -1)`.
```
>> Operate(p, f, -1)
Operate(p, f, -1)
```