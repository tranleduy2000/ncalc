## InverseLaplaceTransform

```
InverseLaplaceTransform(f,s,t)
```

> returns the inverse laplace transform.
 
See:
* [Wikipedia - Laplace transform](https://en.wikipedia.org/wiki/Laplace_transform)

### Examples 
```  
>> InverseLaplaceTransform(3/(s-1)+(2*s)/(s^2+4),s,t)
3*E^t+2*Cos(2*t)
```