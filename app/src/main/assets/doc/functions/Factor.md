## Factor 

```
Factor(expr)
``` 

> factors the polynomial expression `expr`

See: 
* [Wikipedia - Factorization of polynomials](https://en.wikipedia.org/wiki/Factorization_of_polynomials)
* [Wikipedia - Gaussian integer](https://en.wikipedia.org/wiki/Gaussian_integer) 

### Examples
``` 
>> Factor(1+2*x+x^2, x)
(1+x)^2
``` 

``` 
>> Factor(x^4-1, GaussianIntegers->True)
(x-1)*(x+1)*(x-I)*(x+I)
```