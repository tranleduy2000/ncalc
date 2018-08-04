## PDF

```
PDF(distribution, value)
```

> returns the probability density function of `value`. 

```
PDF(distribution, {list} )
```

> returns the probability density function of the values of list. 


See:
* [Wikipedia - probability density function](https://en.wikipedia.org/wiki/Probability_density_function)

`PDF` can be applied to the following distributions:

> [BernoulliDistribution](BernoulliDistribution.md), [BinomialDistribution](BinomialDistribution.md), [DiscreteUniformDistribution](DiscreteUniformDistribution.md), [ErlangDistribution](ErlangDistribution.md), [ExponentialDistribution](ExponentialDistribution.md), [FrechetDistribution](FrechetDistribution.md), 
[GammaDistribution](GammaDistribution.md), [GeometricDistribution](GeometricDistribution.md), [GumbelDistribution](GumbelDistribution.md), [HypergeometricDistribution](HypergeometricDistribution.md), [LogNormalDistribution](LogNormalDistribution.md), [NakagamiDistribution](NakagamiDistribution.md), [NormalDistribution](NormalDistribution.md), 
[PoissonDistribution](PoissonDistribution.md), [StudentTDistribution](StudentTDistribution.md), [WeibullDistribution](WeibullDistribution.md) 

### Examples

``` 
>> PDF(NormalDistribution(n, m)) 
1/(Sqrt(2)*E^((-n+#1)^2/(2*m^2))*m*Sqrt(Pi))&
				
>> PDF(GumbelDistribution(n, m),k)
E^(-E^((k-n)/m)+(k-n)/m)/m

>> Table(PDF(NormalDistribution( ), x), {m, {-1, 1, 2}},{x, {-1, 1, 2}})//N  
{{0.24197,0.24197,0.05399},{0.24197,0.24197,0.05399},{0.24197,0.24197,0.05399}}
```
  

