## ComplexExpand
 
```
ComplexExpand(expr)
```

> get the expanded `expr`. All variable symbols in `expr` are assumed to be non complex numbers.

### Examples
```
>> ComplexExpand(Sin(x+I*y))
Cosh(y)*Sin(x)+I*Cos(x)*Sinh(y)
```