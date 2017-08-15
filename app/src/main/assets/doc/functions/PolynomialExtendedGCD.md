## PolynomialExtendedGCD

``` 
PolynomialExtendedGCD(p, q, x)
``` 

> returns the extended GCD ('greatest common divisor') of the univariate polynomials `p` and `q`.

``` 
PolynomialExtendedGCD(p, q, x, Modulus -> prime)
``` 

> returns the extended GCD ('greatest common divisor') of the univariate polynomials `p` and `q` modulus the `prime` integer.

### Examples
``` 
>> PolynomialExtendedGCD(x^8 + x^4 + x^3 + x + 1, x^6 + x^4 + x + 1, x, Modulus->2)
{1,{1+x^2+x^3+x^4+x^5,x+x^3+x^6+x^7}}
``` 