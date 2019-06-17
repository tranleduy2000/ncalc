## Median

```
Median(list)
```

> returns the median of `list`. 
  
See:
* [Wikipedia - Median](https://en.wikipedia.org/wiki/Median)

`Median` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md),  [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

``` 
>> Median({26, 64, 36})
36
```

For lists with an even number of elements, Median returns the mean of the two middle values:

```
>> Median({-11, 38, 501, 1183})
539/2
```

Passing a matrix returns the medians of the respective columns:

```
>> Median({{100, 1, 10, 50}, {-1, 1, -2, 2}})
{99/2,1,4,26}

>> Median(LogNormalDistribution(m,s))
E^m
```
