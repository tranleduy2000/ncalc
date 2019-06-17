## Probability

```
Probability(pure-function, data-set)
```

> returns the probability of the `pure-function` for the given `data-set`. 
   

See:
* [Wikipedia - Probability](https://en.wikipedia.org/wiki/Probability)

### Examples

```
>> Probability(#^2 + 3 # < 11 &, {-0.21848,1.67503,0.78687,4.9887,7.06587,-1.27856,0.79225,-0.01164,2.48227,-0.07223})
7/10

>> Probability(x^2 + 3*x < 11,Distributed(x,{-0.21848,1.67503,0.78687,0.9887,2.06587,-1.27856,0.79225,-0.01164,2.48227,-0.07223}))
9/10
```