## Normal

```
Normal(series)
```

> converts a `series` expression into a standard expression.

### Examples

```
>> Normal(SeriesData(x, 0, {1, 0, -1, -4, -17, -88, -549}, -1, 6, 1))
1/x-x-4*x^2-17*x^3-88*x^4-549*x^5
```