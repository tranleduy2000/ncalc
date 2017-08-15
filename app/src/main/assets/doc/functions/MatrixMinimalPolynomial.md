## MatrixMinimalPolynomial

``` 
MatrixMinimalPolynomial(matrix, var)
``` 

> computes the matrix minimal polynomial of a `matrix` for the variable `var`.

See:  
* [Wikipedia - Minimal polynomial (linear algebra)](https://en.wikipedia.org/wiki/Minimal_polynomial_(linear_algebra))

### Examples
 
``` 
>> MatrixMinimalPolynomial({{1, -1, -1}, {1, -2, 1}, {0, 1, -3}}, x)
-1+x+4*x^2+x^3
``` 