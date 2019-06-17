## Variance

```
Variance(list)
```

> computes the variance of `list`. `list` may consist of numerical values or symbols. Numerical values may be real or complex.

`Variance({{a1, a2, ...}, {b1, b2, ...}, ...})` will yield
`{Variance({a1, b1, ...}, Variance({a2, b2, ...}), ...}`.


`Variance` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), 
[PoissonDistribution](PoissonDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

```
>> Variance({1, 2, 3})
1

>> Variance({7, -5, 101, 3})
7475/3

>> Variance({1 + 2*I, 3 - 10*I})
74

>> Variance({a, a})
0

>> Variance({{1, 3, 5}, {4, 10, 100}})
{9/2,49/2,9025/2}
```

