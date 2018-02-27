## VectorQ

```
VectorQ(v)
```

> returns `True` if `v` is a list of elements which are not themselves lists.

```
VectorQ(v, f)
```

> returns `True` if `v` is a vector and `f(x)` returns `True` for each element `x` of `v`.

### Examples
```
>> VectorQ({a, b, c})
True
```