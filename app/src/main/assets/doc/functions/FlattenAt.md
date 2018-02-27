## FlattenAt

```
FlattenAt(expr, position)
```

> flattens out nested lists at the given `position` in `expr`.
	 

### Examples
```
>> FlattenAt(f(a, g(b,c), {d, e}, {f}), -2)
f(a,g(b,c),d,e,{f})

>> FlattenAt(f(a, g(b,c), {d, e}, {f}), 4)
f(a,g(b,c),{d,e},f)
```
