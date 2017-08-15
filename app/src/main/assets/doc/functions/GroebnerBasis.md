## GroebnerBasis

```
GroebnerBasis({polynomial-list},{variable-list})
```

> returns a Gröbner basis for the `polynomial-list` and `variable-list`.
 
See:
* [Wikipedia - Gröbner basis](https://en.wikipedia.org/wiki/Gröbner_basis)

### Examples 
```  
>> GroebnerBasis({x*y-2*y, 2*y^2-x^2}, {y, x})
{-2*x^2+x^3,-2*y+x*y,-x^2+2*y^2}

>> GroebnerBasis({x*y-2*y, 2*y^2-x^2}, {x, y})
{-2*y+y^3,-2*y+x*y,x^2-2*y^2}
```