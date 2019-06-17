## StandardDeviation

```
StandardDeviation(list)
```
> computes the standard deviation of `list`. `list` may consist of numerical values or symbols. Numerical values may be real or complex.

`StandardDeviation({{a1, a2, ...}, {b1, b2, ...}, ...})` will yield
`{StandardDeviation({a1, b1, ...}, StandardDeviation({a2, b2, ...}), ...}`.

`StandardDeviation` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), 
[PoissonDistribution](PoissonDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

```
>> StandardDeviation({1, 2, 3})
1

>> StandardDeviation({7, -5, 101, 100})
Sqrt(13297)/2

>> StandardDeviation({a, a})  
0

>> StandardDeviation({{1, 10}, {-1, 20}})
{Sqrt(2),5*Sqrt(2)}

>> StandardDeviation(LogNormalDistribution(0, 1))
Sqrt((-1+E)*E)
```

