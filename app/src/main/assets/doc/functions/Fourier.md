## Fourier

```
Fourier(vector-of-complex-numbers)
```

> Discrete Fourier transform of a `vector-of-complex-numbers`. Fourier transform is restricted to vectors with length of power of 2.  

### Examples 

```
>> Fourier({1 + 2*I, 3 + 11*I})
{2.82843+I*9.19239,-1.41421+I*(-6.36396)}

>> Fourier({1,2,0,0})
{1.5,0.5+I*1.0,-0.5,0.5+I*(-1.0)}
```

The first argument is restricted to vectors with a length of power of 2.

```
>> Fourier({1,2,0,0,7})
Fourier({1,2,0,0,7}) 
```
				
### Related terms

[InverseFourier](InverseFourier.md) 
