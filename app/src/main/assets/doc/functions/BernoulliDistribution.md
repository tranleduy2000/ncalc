## BernoulliDistribution

```
BernoulliDistribution(p)
```

> returns the Bernoulli distribution.

See:  
* [Wikipedia - Bernoulli distribution](https://en.wikipedia.org/wiki/Bernoulli_distribution)

### Examples

The probability density function of the Bernoulli distribution is

```
>> PDF(BernoulliDistribution(p), x)
Piecewise({{1-p,x==0},{p,x==1}},0)
```

The cumulative distribution function of the Bernoulli distribution is

```
>> CDF(BernoulliDistribution(p), x)
Piecewise({{0,x<0},{1-p,0<=x&&x<1}},1)
```


The mean of the Bernoulli distribution is

```
>> Mean(BernoulliDistribution(p))
p
```

The standard deviation of the Bernoulli distribution is

```
>> StandardDeviation(BernoulliDistribution(p))
Sqrt((1-p)*p)
```

The variance of the Bernoulli distribution is

```
>> Variance(BernoulliDistribution(p))
(1-p)*p
```


The random variates of a Bernoulli distribution can be generated with function `RandomVariate`

```
>> RandomVariate(BernoulliDistribution(0.25), 10^1)
{1,0,0,0,1,1,0,0,0,0}
```

### Related terms 
[CDF](CDF.md), [Mean](Mean.md), [Median](Mean.md), [PDF](PDF.md), [Quantile](Quantile.md), [StandardDeviation](StandardDeviation.md), [Variance](Variance.md) 
