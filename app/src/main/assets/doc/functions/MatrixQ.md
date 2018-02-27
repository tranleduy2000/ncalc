## MatrixQ
```
MatrixQ(m)
```

> returns `True` if `m` is a list of equal-length lists.

```
MatrixQ[m, f]
```

> only returns `True` if `f(x)`  returns `True` for each element `x` of the matrix `m`.

### Examples

```
>> MatrixQ({{1, 3}, {4.0, 3/2}}, NumberQ)
True
```