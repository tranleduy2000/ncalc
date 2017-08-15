## Dot

```
Dot(x, y) or x . y
```

> `x . y` computes the vector dot product or matrix product `x . y`.
    

See:    
* [Wikipedia - Matrix multiplication](https://en.wikipedia.org/wiki/Matrix_multiplication)

### Examples

Scalar product of vectors:
```
>> {a, b, c} . {x, y, z}
a*x+b*y+c*z 
```

Product of matrices and vectors:
```
>> {{a, b}, {c, d}} . {x, y}
{a*x+b*y,c*x+d*y}
```

Matrix product:
```
>> {{a, b}, {c, d}} . {{r, s}, {t, u}}
{{a*r+b*t,a*s+b*u}, {c*r+d*t,c*s+d*u}}

>> a . b
a.b
```