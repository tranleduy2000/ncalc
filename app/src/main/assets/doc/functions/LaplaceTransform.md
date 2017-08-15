## LaplaceTransform

```
LaplaceTransform(f,s,t)
```

> returns the laplace transform.
 
See:
* [Wikipedia - Laplace transform](https://en.wikipedia.org/wiki/Laplace_transform)

### Examples 
```  
>> LaplaceTransform(t^2*Exp(2+3*t), t, s)
(-2*E^2)/(3-s)^3
```