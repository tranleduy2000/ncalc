## Factor 

```
Factor(expr)
``` 

> factors the polynomial expression `expr`

### Examples
``` 
>> Factor(1+2*x+x^2, x)
(1+x)^2
``` 

``` 
>> Factor(x^4-1, GaussianIntegers->True)
(x-1)*(x+1)*(x-I)*(x+I)
```