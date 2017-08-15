## CoefficientList

```
CoefficientList(polynomial, variable)
```

> get the coefficient list of a univariate `polynomial`.
 
### Examples

```
>> CoefficientList(a+b*x, x)
{a,b}

>> CoefficientList(a+b*x+c*x^2, x)
{a,b,c}

>> CoefficientList(a+c*x^2, x)
{a,0,c}
```