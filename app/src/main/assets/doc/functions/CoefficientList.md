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

In the next line `Coefficient` returns the coefficient of a particular term of a polynomial. In this case `(-210*c^2 * x^2*y*z^2)` is a term of `(c*x-2*y+z)^7` after it's expanded.

```
>> poly=(c*x-2*y+z)^7
(c*x-2*y+z)^7

>> Coefficient(poly, x^2*y*z^4)
-210*c^2
```

`CoefficientList` gets the same information as a list of coefficients. In the line below `Part(coeff, 3,2,5)` returns the coefficient of `x^(3-1)*y^(2-1)*z^(5-1)`. In general if we say `lst=CoefficientList(poly,{x1,x2,x3,...})`  then  `Part(lst,  n1, n2, ,n3, ...)` will be the coefficient of  `x1^(n1-1)*x2^(n2-1)*x3^(n3-1)...`.

```
>> coeff=CoefficientList(poly,{x,y,z}); Part(coeff, 3,2,5)
-210*c^2
```

The next line gives the coefficient of `x^5`.  As expected there is more than one term of poly with `x^5` as a factor.

```
>> Coefficient(poly, x^5)
84*c^5*y^2-84*c^5*y*z+21*c^5*z^2
```
 

We can get the same result as the previous example if we use the next line.

```
>> Coefficient(poly, x, 5)
84*c^5*y^2-84*c^5*y*z+21*c^5*z^2
```

One can't get the result above directly from CoefficientList. Instead pieces of the above result are included in the result of `CoefficientList(poly,{x,y,z})`.  The line below can be used to get pieces of the result above.  Specifically `coeff[[6]]` contains all coefficients of x^5 (including those that are zero).

```
>> coeff[[6]]
{{0,0,21*c^5,0,0,0,0,0},{0,-84*c^5,0,0,0,0,0,0},{84*c^5,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}}
```

* `Part(coeff,6,1,3)` is `21*c^5` the coefficient of `(x^(6 - 1) * y^(1 - 1) * z^(3 - 1)) = (x^5 * z^2)` in poly. 
* `Part(coeff,6,2,2)` is `(-84*c^5)` the coefficient of `(x^(6 - 1) * y^(2 - 1) * z^(2 - 1)) = (x^5 * y * z)` in poly. 
* `Part(coeff,6,3,1)` is `(84*c^5)` the coeficient of `(x^(6 - 1) * y^(3 - 1) * z^(1 - 1)) = (x^5 * y^2)` in poly.

All other coefficients under `coeff[[6]]` are zero which agrees with the result of `Coefficient(poly, x^5)`. 


### Related terms

[Coefficient](Coefficient.md), [Exponent](Exponent.md) 