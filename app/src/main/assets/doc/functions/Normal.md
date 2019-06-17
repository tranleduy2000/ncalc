## Normal

```
Normal(expr)
```

> converts a special Symja expression `expr` into a standard expression.

### Examples

Normalize a series expression:

```
>> Normal(SeriesData(x, 0, {1, 0, -1, -4, -17, -88, -549}, -1, 6, 1))
1/x-x-4*x^2-17*x^3-88*x^4-549*x^5
```

Normalize a byte array expression:

```
>> BinarySerialize(f(g,2)) // Normal
{56,58,102,2,115,8,71,108,111,98,97,108,96,102,115,8,71,108,111,98,97,108,96,103,67,2}
```