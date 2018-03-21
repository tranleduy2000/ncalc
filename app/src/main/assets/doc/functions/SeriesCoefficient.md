## SeriesCoefficient

```
SeriesCoefficient(expr, {x, x0, n})
```

> get the coefficient of `(x- x0)^n` at the point `x = x0`

### Examples

```
>> SeriesCoefficient(Sin(x),{x,f+g,n})
Piecewise({{Sin(f+g+1/2*n*Pi)/n!,n>=0}},0)
```
