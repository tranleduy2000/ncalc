## Mean

```
Mean(list)
```

> returns the statistical mean of `list`. 

See:
* [Wikipedia - Mean](https://en.wikipedia.org/wiki/Mean)

`Mean` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), 
[PoissonDistribution](PoissonDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

``` 
>> Mean({26, 64, 36})
42

>> Mean({1, 1, 2, 3, 5, 8})
10/3

>> Mean({a, b})
1/2*(a+b)
```

The [mean](https://en.wikipedia.org/wiki/Mean) of the normal distribution is

```
>> Mean(NormalDistribution(m, s))
m
```
  

