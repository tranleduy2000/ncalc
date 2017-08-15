## Expand

``` 
Expand(expr)
``` 
 
> expands out positive rational powers and products of sums in `expr`.

### Examples
```
>> Expand((x + y) ^ 3)
x^3+3*x^2*y+3*x*y^2+y^3

>> Expand((a + b) (a + c + d))  
a^2+a*b+a*c+b*c+a*d+b*d 
 
>> Expand((a + b) (a + c + d) (e + f) + e a a)  
2*a^2*e+a*b*e+a*c*e+b*c*e+a*d*e+b*d*e+a^2*f+a*b*f+a*c*f+b*c*f+a*d*f+b*d*f 
 
>> Expand((a + b) ^ 2 * (c + d))  
a^2*c+2*a*b*c+b^2*c+a^2*d+2*a*b*d+b^2*d 

>> Expand((x + y) ^ 2 + x y) 
x^2+3*x*y+y^2  

>> Expand(((a + b) (c + d)) ^ 2 + b (1 + a))  
a^2*c^2+2*a*b*c^2+b^2*c^2+2*a^2*c*d+4*a*b*c*d+2*b^2*c*d+a^2*d^2+2*a*b*d^2+b^2*d^2+b(1+a) 
```

`Expand` expands out rational powers by expanding the `Floor()` part of the rational powers number:
```
>> Expand((x + 3)^(5/2)+(x + 1)^(3/2)) Sqrt(1+x)+x*Sqrt(1+x)+9*Sqrt(3+x)+6*x*Sqrt(3+x)+x^2*Sqrt(3+x)
```

`Expand` expands items in lists and rules:  
```  
>> Expand({4 (x + y), 2 (x + y) -> 4 (x + y)})  
{4*x+4*y,2*(x+y)->4*(x+y)} 
```

`Expand` does not change any other expression.  
```
>> Expand(Sin(x*(1 + y)))  
Sin(x*(1+y)) 
 
>> a*(b*(c+d)+e) // Expand  
a*b*c+a*b*d+a*e 
 
>> (y^2)^(1/2)/(2x+2y)//Expand  
Sqrt(y^2)/(2*x+2*y) 
  
>> 2(3+2x)^2/(5+x^2+3x)^3 // Expand  
18/(5+3*x+x^2)^3+(24*x)/(5+3*x+x^2)^3+(8*x^2)/(5+3*x+x^2)^3 
```