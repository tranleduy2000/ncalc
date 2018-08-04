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

For square roots of non-negative integer arguments `ContinuedFraction` determines the periodic part:

```
>> ContinuedFraction(Sqrt(13))
{3,{1,1,1,1,6}}

>> ContinuedFraction(Sqrt(919))
{30,3,5,1,2,1,2,1,1,1,2,3,1,19,2,3,1,1,4,9,1,7,1,3,6,2,11,1,1,1,29,1,1,1,11,2,6,3,1,7,1,9,4,1,1,3,2,19,1,3,2,1,1,1,2,1,2,1,5,3,60}}
```