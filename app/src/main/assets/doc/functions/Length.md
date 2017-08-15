## Length
``` 
Length(expr)
``` 
> returns the number of leaves in `expr`.

### Examples
Length of a list:
``` 
>> Length({1, 2, 3})
3
``` 

'Length' operates on the 'FullForm' of expressions:
``` 
>> Length(Exp(x))
2

>> FullForm(Exp(x))
Power(E, x)
``` 

The length of atoms is 0:
``` 
>> Length(a)
0
``` 

Note that rational and complex numbers are atoms, although their 'FullForm' might suggest the opposite:
``` 
>> Length(1/3)
0
 
>> FullForm(1/3)
Rational(1, 3)
``` 