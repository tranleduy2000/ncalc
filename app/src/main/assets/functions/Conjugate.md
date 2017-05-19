## Conjugate

```
Conjugate(z)
```
> returns the complex conjugate of the complex number `z`.
 

### Examples
```
>> Conjugate(3 + 4*I)
3 - 4 I
 
>> Conjugate(3)
3
 
>> Conjugate(a + b * I)
-I*Conjugate(b)+Conjugate(a)
 
>> Conjugate({{1, 2 + I*4, a + I*b}, {I}})
{{1,2-I*4,-I*Conjugate(b)+Conjugate(a)},{-I}}
 
>> {Conjugate(Pi), Conjugate(E)}
{Pi,E}
 
>> Conjugate(1.5 + 2.5*I)
1.5+I*(-2.5)
```