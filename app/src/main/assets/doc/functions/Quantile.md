## Quantile

```
Quantile(list, q)
```

> returns the `q`-Quantile of `list`. 

```
Quantile(list, {q1, q2, ...})
```

> returns a list of the `q`-Quantiles of `list`. 

See:
* [Wikipedia - Quantile](https://en.wikipedia.org/wiki/Quantile)

`Quantile` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GumbelDistribution](GumbelDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md),  [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 


### Examples

``` 
>> Quantile({1,2}, 0.5)
1

>> Quantile(NormalDistribution(m, s), q) 
ConditionalExpression(m-Sqrt(2)*s*InverseErfc(2*q),0<=q<=1)
```