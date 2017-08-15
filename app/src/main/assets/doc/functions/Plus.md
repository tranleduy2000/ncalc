## Plus

```  
Plus(a, b, ...)

a + b + ...
```   
> represents the sum of the terms `a, b, ...`. 
 

### Examples

```   
>> 1 + 2
3
``` 

`Plus` performs basic simplification of terms:
``` 
>> a + b + a
2*a+b

>> a + a + 3 * a
5*a

>> a + b + 4.5 + a + b + a + 2 + 1.5 * b
6.5+3.0*a+3.5*b 
``` 

Apply `Plus` on a list to sum up its elements:
``` 
>> Plus @@ {2, 4, 6}
12
``` 

The sum of the first `1000` integers:
``` 
>> Plus @@ Range(1000)
500500
``` 

`Plus` has default value `0`:
``` 
>> a /. n_. + x_ :> {n, x}
{0,a}
``` 
 
``` 
>> -2*a - 2*b
-2*a-2*b
 
>> -4+2*x+2*Sqrt(3)
-4+2*x+2*Sqrt(3)
 
>> 1 - I * Sqrt(3)
1-I*Sqrt(3)
 
>> Head(3 + 2 I)
Complex
 
>> N(Pi, 30) + N(E, 30)
5.85987448204883847382293085463
 
>> N(Pi, 30) + N(E, 30) // Precision
30
``` 