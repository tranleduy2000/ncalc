## Binomial

``` 
Binomial(n, k)
``` 

> returns the binomial coefficient of the 2 integers `n` and `k`
 
For negative integers `k` this function will return `0` no matter what value is the other argument `n`.

See:
* [Wikipedia - Binomial coefficient](http://en.wikipedia.org/wiki/Binomial_coefficient)
* [John D. Cook - Binomial coefficients definition](https://www.johndcook.com/blog/binomial_coefficients/)
* [Kronenburg 2011 - The Binomial Coefficient for Negative Arguments](https://arxiv.org/pdf/1105.3689.pdf)

### Examples

``` 
>> Binomial(4,2)
6
 
>> Binomial(5, 3)   
10   
```