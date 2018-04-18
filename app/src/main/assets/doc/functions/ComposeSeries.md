## ComposeSeries

```
ComposeSeries( series1, series2 )
```

> substitute `series2` into `series1`

```
ComposeSeries( series1, series2, series3 )
```

> return multiple series composed.

### Examples

```
>> ComposeSeries(SeriesData(x, 0, {1, 3}, 2, 4, 1), SeriesData(x, 0, {1, 1,0,0}, 0, 4, 1) - 1)
x^2+3*x^3+O(x)^4
```
