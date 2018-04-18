## Integrate  
 
``` 
Integrate(f, x)
``` 
 
> integrates `f` with respect to `x`. The result does not contain the additive integration constant.

``` 
Integrate(f, {x,a,b})
``` 
 
> computes the definite integral of `f` with respect to `x` from `a` to `b`.

See: [Wikipedia: Integral](https://en.wikipedia.org/wiki/Integral)

### Examples

```
>> Integrate(x^2, x)
x^3/3

>> Integrate(Tan(x) ^ 5, x)
-Log(Cos(x))-Tan(x)^2/2+Tan(x)^4/4
```