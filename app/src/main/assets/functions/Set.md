## Set

``` 
Set(expr, value)

expr = value
``` 
> evaluates `value` and assigns it to `expr`.

``` 
{s1, s2, s3} = {v1, v2, v3}
``` 
> sets multiple symbols `(s1, s2, ...)` to the corresponding values `(v1, v2, ...)`.

### Examples
`Set` can be used to give a symbol a value:  
```   
>> a = 3    
3  
 
>> a      
3
```
 
You can set multiple values at once using lists:    
```
>> {a, b, c} = {10, 2, 3}    
{10,2,3}    
 
>> {a, b, {c, {d}}} = {1, 2, {{c1, c2}, {a}}} 
{1,2,{{c1,c2},{10}}}

>> d    
10    
```

`Set` evaluates its right-hand side immediately and assigns it to the left-hand side:   
``` 
>> a    
1    

>> x = a    
1    

>> a = 2    
2    

>> x    
1    
```

'Set' always returns the right-hand side, which you can again use in an assignment:    
```
>> a = b = c = 2    
>> a == b == c == 2    
True    
```

'Set' supports assignments to parts:    
```
>> A = {{1, 2}, {3, 4}}    
>> A[[1, 2]] = 5    
5    

>> A    
{{1,5}, {3,4}}    

>> A[[;;, 2]] = {6, 7}    
{6,7}    

>> A    
{{1,6},{3,7}} 
```
 
Set a submatrix: 
```   
>> B = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}    
>> B[[1;;2, 2;;-1]] = {{t, u}, {y, z}}   
>> B    
{{1, t, u}, {4, y, z}, {7, 8, 9}}    
```   