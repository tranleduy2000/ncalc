## Cyclotomic

```
Cyclotomic(n, x)
```

> returns the Cyclotomic polynomial `C_n(x)`.

See:  
* [Wikipedia - Cyclotomic polynomial](https://en.wikipedia.org/wiki/Cyclotomic_polynomial)

### Examples

```
>> Cyclotomic(25,x)
1+x^5+x^10+x^15+x^20
```
			
The case of the 105-th cyclotomic polynomial is interesting because 105 is the lowest integer that is the product of three distinct odd prime numbers and this polynomial is the first one that has a coefficient other than `1, 0` or `−1`:

```
>> Cyclotomic(105, x) 
1+x+x^2-x^5-x^6-2*x^7-x^8-x^9+x^12+x^13+x^14+x^15+x^16+x^17-x^20-x^22-x^24-x^26-x^28+x^31+x^32+x^33+x^34+x^35+x^36-x^39-x^40-2*x^41-x^42-x^43+x^46+x^47+x^48
```
    
    
    