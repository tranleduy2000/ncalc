## Optional

```
Optional(patt, default)
```

or
```
patt : default
```

> is a pattern which matches `patt`, which if omitted should be replaced by `default`.
	 
### Examples

```
>> f(x_, y_:1) := {x, y}
>> f(1, 2)
{1,2}

>> f(a)
{a,1}
```
