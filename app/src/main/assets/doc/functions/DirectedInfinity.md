## DirectedInfinity

``` 
DirectedInfinity(z)
``` 
> represents an infinite multiple of the complex number `z`.

``` 
DirectedInfinity()
``` 
> is the same as `ComplexInfinity`.

### Examples
``` 
>> DirectedInfinity(1)
Infinity
 
>> DirectedInfinity()
ComplexInfinity
 
>> DirectedInfinity(1 + I)
DirectedInfinity((1+I)/Sqrt(2))
 
>> 1 / DirectedInfinity(1 + I)
0
```

Indeterminate expression -Infinity + Infinity encountered.
```
>> DirectedInfinity(1) + DirectedInfinity(-1)
Indeterminate
 
>>DirectedInfinity(1+I)+DirectedInfinity(2+I)
DirectedInfinity((1+I)/Sqrt(2))+DirectedInfinity((2+I)/Sqrt(5))
 
>>DirectedInfinity(Sqrt(3))
Infinity
```