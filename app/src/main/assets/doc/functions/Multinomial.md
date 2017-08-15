## Multinomial

```
Multinomial(n1, n2, ...)
``` 

> gives the multinomial coefficient `(n1+n2+...)!/(n1! n2! ...)`.

See:

* [Wikipedia: Multinomial coefficient](http://en.wikipedia.org/wiki/Multinomial_coefficient)

### Examples
```  
>> Multinomial(2, 3, 4, 5)
2522520

>> Multinomial()
1
```
 
`Multinomial(n-k, k)` is equivalent to `Binomial(n, k)`.
```
>> Multinomial(2, 3)
10
```