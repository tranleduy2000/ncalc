## BinomialDistribution

```
BinomialDistribution(n, p)
```

> returns the binomial distribution.

See:  
* [Wikipedia - Binomial distribution](https://en.wikipedia.org/wiki/Binomial_distribution)

### Examples

The probability density function of the binomial distribution is

```
>> PDF(BinomialDistribution(n, p), x)
Piecewise({{(1-p)^(n-x)*p^x*Binomial(n,x),0<=x<=n}},0)
```

The cumulative distribution function of the binomial distribution is

```
>> CDF(BinomialDistribution(n, p), x)
Piecewise({{BetaRegularized(1-p,n-Floor(x),1+Floor(x)),0<=x&&x<n},{1,x>=n}},0)
```


The mean of the binomial distribution is

```
>> Mean(BinomialDistribution(n, p))
n*p
```

The standard deviation of the binomial distribution is

```
>> StandardDeviation(BinomialDistribution(n, p))
Sqrt(n*(1-p)*p)
```

The variance of the binomial distribution is

```
>> Variance(BinomialDistribution(n, p))
n*(1-p)*p
```


The random variates of a binomial distribution can be generated with function `RandomVariate`

```
>> RandomVariate(BinomialDistribution(10,0.25), 10^1)
{1,2,1,1,4,1,1,3,2,5}
```

### Related terms 
[CDF](CDF.md), [Mean](Mean.md), [Median](Mean.md), [PDF](PDF.md), [Quantile](Quantile.md), [StandardDeviation](StandardDeviation.md), [Variance](Variance.md) 