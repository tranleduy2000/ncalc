## NormalDistribution

```
NormalDistribution(m, s)
```

> returns the normal distribution of mean `m` and sigma `s`.

```
NormalDistribution()
```

> returns the standard normal distribution for `m = 0` and `s = 1`.

See:  
* [Wikipedia - Normal distribution](https://en.wikipedia.org/wiki/Normal_distribution)

### Examples

The [probability density function](https://en.wikipedia.org/wiki/Probability_density) of the normal distribution is

```
>> PDF(NormalDistribution(m, s), x)
1/(Sqrt(2)*E^((-m+x)^2/(2*s^2))*Sqrt(Pi)*s)
```

The [cumulative distribution function](https://en.wikipedia.org/wiki/Cumulative_distribution_function) of the standard normal distribution is

```
>> CDF(NormalDistribution( ), x)
1/2*(2-Erfc(x/Sqrt(2)))
```


The [mean](https://en.wikipedia.org/wiki/Mean) of the normal distribution is

```
>> Mean(NormalDistribution(m, s))
m
```

The [standard deviation](https://en.wikipedia.org/wiki/Standard_deviation) of the normal distribution is

```
>> StandardDeviation(NormalDistribution(m, s))
s
```

The [variance](https://en.wikipedia.org/wiki/Variance) of the normal distribution is

```
>> Variance(NormalDistribution(m, s))
s^2
```


The [random variates](https://en.wikipedia.org/wiki/Normal_distribution#Generating_values_from_normal_distribution) of a normal distribution can be generated with function `RandomVariate`

```
>> RandomVariate(NormalDistribution(2,3), 10^1)
{1.14364,6.09674,5.16495,2.39937,-0.52143,-1.46678,3.60142,-0.85405,2.06373,-0.29795}
```

### Related terms 
[CDF](CDF.md), [Mean](Mean.md), [Median](Mean.md), [PDF](PDF.md), [Quantile](Quantile.md), [StandardDeviation](StandardDeviation.md), [Variance](Variance.md) 
