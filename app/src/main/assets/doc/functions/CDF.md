## CDF

```
CDF(distribution, value)
```

> returns the cumulative distribution function of `value`. 

```
PDF(distribution, {list} )
```

> returns the cumulative distribution function of the values of list. 

See:
* [Wikipedia - cumulative distribution function](https://en.wikipedia.org/wiki/Cumulative_distribution_function)

`CDF` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), 
[PoissonDistribution](PoissonDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

``` 
>> CDF(NormalDistribution(),-0.41)
0.3409

>> Table(CDF(NormalDistribution(0, s), x), {s, {.75, 1, 2}}, {x, -6,6}) // N
{{0.0,0.0,0.0,0.00003,0.00383,0.09121,0.5,0.90879,0.99617,0.99997,1.0,1.0,1.0},{0.0,0.0,0.00003,0.00135,0.02275,0.15866,0.5,0.84134,0.97725,0.99865,0.99997,1.0,1.0},{0.00135,0.00621,0.02275,0.06681,0.15866,0.30854,0.5,0.69146,0.84134,0.93319,0.97725,0.99379,0.99865}}
```
  

