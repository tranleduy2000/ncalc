## Grad

```
Grad(function, list-of-variables)
```

> gives the gradient of the function.

See:  
* [Wikipedia - Gradient](https://en.wikipedia.org/wiki/Gradient)

### Examples

```
>> Grad(2*x+3*y^2-Sin(z), {x, y, z})
{2,6*y,-Cos(z)}
```

