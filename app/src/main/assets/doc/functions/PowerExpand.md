## PowerExpand
```
PowerExpand(expr)
```

> expands out powers of the form `(x^y)^z` and `(x*y)^z` in `expr`.

### Examples
```
>> PowerExpand((a ^ b) ^ c)
a^(b*c)

>> PowerExpand((a * b) ^ c)
a^c*b^c
```

`PowerExpand` is not correct without certain assumptions:
```
>> PowerExpand((x ^ 2) ^ (1/2))
x
```