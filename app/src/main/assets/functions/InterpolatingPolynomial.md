## InterpolatingPolynomial

``` 
InterpolatingPolynomial(data-list, symbol)
``` 

> get the polynomial representation for the given `data-list`.
  
See:  
* [Wikipedia - Newton Polynomial](http://en.wikipedia.org/wiki/Newton_polynomial]) 
 
### Examples
``` 
>> InterpolatingPolynomial({{1,7},{3,11},{5,27}},x)
(3/2*x-5/2)*(x-1)+7
``` 
