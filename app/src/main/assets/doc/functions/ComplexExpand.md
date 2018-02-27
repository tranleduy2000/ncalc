## ComplexExpand
 
```
ComplexExpand(expr)
```

> get the expanded `expr`. All variable symbols in `expr` are assumed to be non complex numbers.

See:  
* [Wikipedia - List of trigonometric identities](http://en.wikipedia.org/wiki/List_of_trigonometric_identities)

### Examples
```
>> ComplexExpand(Sin(x+I*y))
Cosh(y)*Sin(x)+I*Cos(x)*Sinh(y)
```