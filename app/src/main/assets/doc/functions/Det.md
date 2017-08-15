## Det

``` 
Det(matrix)
``` 
> computes the determinant of the `matrix`.

See:
* [Wikipedia: Determinant](https://en.wikipedia.org/wiki/Determinant)


### Examples
``` 
>> Det({{1, 1, 0}, {1, 0, 1}, {0, 1, 1}})
-2
``` 

Symbolic determinant:
``` 
>> Det({{a, b, c}, {d, e, f}, {g, h, i}})
-c*e*g+b*f*g+c*d*h-a*f*h-b*d*i+a*e*i 
``` 