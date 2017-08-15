## PerfectNumberQ

```
PerfectNumberQ(n)
```

> returns `True` if `n` is a perfect number. In number theory, a perfect number is a positive integer that is equal to the sum of its proper 
positive divisors, that is, the sum of its positive divisors excluding the number itself. 

See:  
* [Wikipedia - Perfect number](https://en.wikipedia.org/wiki/Perfect_number)
* [Wikipedia - List of perfect numbers](https://en.wikipedia.org/wiki/List_of_perfect_numbers)

### Examples

```
>> Select(Range(1000), PerfectNumberQ)
{6,28,496}
```
  