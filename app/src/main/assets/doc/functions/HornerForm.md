## HornerForm

```
HornerForm(polynomial)
```

> Generate the horner scheme for a univariate `polynomial`. 

```
HornerForm(polynomial, x)
```

> Generate the horner scheme for a univariate `polynomial` in `x`. 

See:
* [Wikipedia - Horner scheme](http://en.wikipedia.org/wiki/Horner_scheme)
* [Rosetta Code - Horner's rule for polynomial evaluation](https://rosettacode.org/wiki/) 
 
### Examples
```   
>> HornerForm(3+4*x+5*x^2+33*x^6+x^8)
3+x*(4+x*(5+(33+x^2)*x^4))

>> HornerForm(a+b*x+c*x^2,x)
a+x*(b+c*x)
```
    