## ContinuedFraction

```
ContinuedFraction(number)
```
 
> get the continued fraction representation of `number`.

See:  
* [Wikipedia - Continued fraction](https://en.wikipedia.org/wiki/Continued_fraction)
 
### Examples

```
>> FromContinuedFraction({2,3,4,5})
157/68

>> ContinuedFraction(157/68)
{2,3,4,5} 

>> ContinuedFraction(45/16)
{2,1,4,3}
```