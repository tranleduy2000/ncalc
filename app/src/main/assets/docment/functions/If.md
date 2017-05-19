## If

```
If(cond, pos, neg)
```

> returns `pos` if `cond` evaluates to `True`, and `neg` if it evaluates to `False`.
 
```
If(cond, pos, neg, other)
```

> returns `other` if `cond` evaluates to neither `True` nor `False`.

```
If(cond, pos)
```

> returns `Null` if `cond` evaluates to `False`.

### Examples

```
>> If(1<2, a, b)
a
```

If the second branch is not specified, `Null` is taken:
>> If(1<2, a)
a
 
>> If(False, a) //FullForm
Null
```

You might use comments (inside `(*` and `*)`) to make the branches of `If` more readable:
```
>> If(a, (*then*) b, (*else*) c);
```