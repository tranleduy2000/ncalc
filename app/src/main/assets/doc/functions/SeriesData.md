## SeriesData

```
SeriesData(x, x0, {coeff0, coeff1, coeff2,...}, nMin, nMax, denominator})
```

> internal structure of a power series at the point `x = x0` the `coeff`-i are coefficients of the power series.

### Examples

```
>> SeriesData(x, 0,{1,0,-1/6,0,1/120,0,-1/5040,0,1/362880}, 1, 11, 2)
Sqrt(x)-x^(3/2)/6+x^(5/2)/120-x^(7/2)/5040+x^(9/2)/362880+O(x)^(11/2)
```
