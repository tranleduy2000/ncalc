## PrimeQ

```
PrimeQ(n)
```

> returns `True` if `n` is a integer prime number.

```
PrimeQ(n, GaussianIntegers->True)
```
 
> returns `True` if `n` is a Gaussian prime number.

For very large numbers, `PrimeQ` uses [probabilistic prime testing](https://en.wikipedia.org/wiki/Prime_number#Primality_testing_versus_primality_proving), so it might be wrong sometimes
(a number might be composite even though `PrimeQ` says it is prime).

See:
* [Wikipedia - Prime number](https://en.wikipedia.org/wiki/Prime_number)
* [Wikipedia - Gaussian primes](https://en.wikipedia.org/wiki/Gaussian_integer#Gaussian_primes)

### Examples

```
>> PrimeQ(2)   
True   
>> PrimeQ(-3)   
True   
>> PrimeQ(137)   
True   
>> PrimeQ(2 ^ 127 - 1)   
True   
>> PrimeQ(1)   
False   
>> PrimeQ(2 ^ 255 - 1)   
False   
```

All prime numbers between 1 and 100:   

```
>> Select(Range(100), PrimeQ)   
 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97}   
```

'PrimeQ' has attribute 'Listable':

```
>> PrimeQ(Range(20))   
 = {False, True, True, False, True, False, True, False, False, False, True, False, True, False, False, False, True, False, True, False}   
```

The Gaussian integer `2 == (1 + i)*(1 − i)`  isn't a Gaussian prime number:

```
>> PrimeQ(2, GaussianIntegers->True)
False

>> PrimeQ(5+2*I, GaussianIntegers->True)
True
```