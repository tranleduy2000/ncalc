## FactorInteger

```
FactorInteger(n)
```

> returns the factorization of `n` as a list of factors and exponents. 

```
FactorInteger(n, GaussianIntegers->True)
```

> for gaussian integers you can set the option `GaussianIntegers->True`


### Examples  

```
>> FactorInteger(990)
{{2,1},{3,2},{5,1},{11,1}}
 
>> FactorInteger(341550071728321)
{{10670053,1},{32010157,1}}
 
>> factors = FactorInteger(2010)
{{2, 1}, {3, 1}, {5, 1}, {67, 1}}
```

To get back the original number:

```
>> Times @@ Power @@@ factors
2010
```
    
`FactorInteger` factors rationals using negative exponents:

```
>> FactorInteger(2010 / 2011)
{{2, 1}, {3, 1}, {5, 1}, {67, 1}, {2011, -1}}
```

Gaussian integers:

```
FactorInteger(10+30*I, GaussianIntegers->True)
>> {{-1,1},{1+I,3},{1+I*2,1},{2+I,2}}

FactorInteger(11+14*I, GaussianIntegers->True)
>> {{11+I*14,1}}
```