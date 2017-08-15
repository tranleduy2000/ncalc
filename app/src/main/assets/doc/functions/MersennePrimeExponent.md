## MersennePrimeExponent

```
MersennePrimeExponent(n)
```

> returns the  `n`th mersenne prime exponent. `2^n - 1` must be a prime number. 
Currently `0 <= n <= 45` can be computed, otherwise the function returns unevaluated.

### Examples

```
>> Table(MersennePrimeExponent(i), {i,20})
{2,3,5,7,13,17,19,31,61,89,107,127,521,607,1279,2203,2281,3217,4253,4423}
```