## Log

The logarithm function.

```
Log(z)
```
> returns the natural logarithm of `z`.

```
Log(z, base)
```
> get the logarithm of `z` for base `base`.

For the natural logarithm functions `Ln(x)` or `Log(x)` the logarithms are taken with the natural base, `E`. 
To get a logarithm of a different base `b`, use `Log(x, b)`, which is essentially a short-hand for `Log(b)/Log(x)`. Other short-hands are `Log10(x)` for `Log(x)/Log(10)` and `Log2(x)` for `Log(x)/Log(2)`.


### Examples 
```
>> Log(E)
1

>> Log({0, 1, E, E * E, E ^ 3, E ^ x})    
{-Infinity,0,1,2,3,Log(E^x)} 
 
>> Log(0.)    
Indeterminate 
 
>> Log(1000) / Log(10)  
3    
 
>> Log(1.4)    
0.3364722366212129    
 
>> Log(Exp(1.4))    
1.3999999999999997   
 
>> Log(-1.4)     
0.3364722366212129+I*3.141592653589793
 
```