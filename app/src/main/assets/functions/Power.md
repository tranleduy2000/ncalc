## Power

```
Power(a, b)  

a ^ b
```

> represents `a` raised to the power of `b`.
	
### Examples
 
```
>> 4 ^ (1/2)
2
 
>> 4 ^ (1/3)
4^(1/3)
 
>> 3^123
48519278097689642681155855396759336072749841943521979872827
 
>> (y ^ 2) ^ (1/2)
Sqrt(y^2)
 
>> (y ^ 2) ^ 3
y^6
```

Use a decimal point to force numeric evaluation:
```
>> 4.0 ^ (1/3)
1.5874010519681994
```

`Power` has default value `1` for its second argument:
```
>> a /. x_ ^ n_. :> {x, n}
{a,1}
```

`Power` can be used with complex numbers:
```
>> (1.5 + 1.0*I) ^ 3.5
-3.682940057821917+I*6.951392664028508
 
>> (1.5 + 1.0*I) ^ (3.5 + 1.5*I)
-3.1918162904562815+I*0.6456585094161581
```

Infinite expression 0^(negative number)
```
>> 1/0 
ComplexInfinity

>> 0 ^ -2
ComplexInfinity

>> 0 ^ (-1/2)
ComplexInfinity

>> 0 ^ -Pi
ComplexInfinity
```

Indeterminate expression 0 ^ (complex number) encountered.
```
>> 0 ^ (2*I*E)
Indeterminate
 
>> 0 ^ - (Pi + 2*E*I)
ComplexInfinity
```

Indeterminate expression 0 ^ 0 encountered.
```
>> 0 ^ 0
Indeterminate
```

```
>> Sqrt(-3+2.*I)
0.5502505227003375+I*1.8173540210239707
 
>> Sqrt(-3+2*I)
Sqrt(-3+I*2) 
 
>> (3/2+1/2I)^2
2+I*3/2
 
>> I ^ I
I^I
 
>> 2 ^ 2.0
4.0
 
>> Pi ^ 4.
97.40909103400242
 
>> a ^ b
a^b
```