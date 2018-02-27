## Inverse
```
Inverse(matrix)
```
> computes the inverse of the `matrix`. 

See:  
* [Wikipedia - Invertible matrix](https://en.wikipedia.org/wiki/Invertible_matrix)

### Examples
``` 
>> Inverse({{1, 2, 0}, {2, 3, 0}, {3, 4, 1}})
{{-3,2,0},
 {2,-1,0},
 {1,-2,1}}
``` 

The matrix `{{1, 0}, {0, 0}}` is singular.
``` 
>> Inverse({{1, 0}, {0, 0}}) 
Inverse({{1, 0}, {0, 0}})
 
>> Inverse({{1, 0, 0}, {0, Sqrt(3)/2, 1/2}, {0,-1 / 2, Sqrt(3)/2}})
{{1,0,0},
 {0,Sqrt(3)/2,-1/2},
 {0,1/2,1/(1/(2*Sqrt(3))+Sqrt(3)/2)}} 
```