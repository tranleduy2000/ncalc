## SetDelayed

``` 
SetDelayed(expr, value)

expr := value
``` 
> assigns `value` to `expr`, without evaluating `value`.


### Examples 
`SetDelayed` is like `Set`, except it has attribute `HoldAll`, thus it does not evaluate the right-hand side immediately, but evaluates it when needed.    
```
>> Attributes(SetDelayed)    
{HoldAll}    

>> a = 1    
1    

>> x := a
>> x    
1    
```

Changing the value of `a` affects `x`:   
``` 
>> a = 2    
2    

>> x    
2
```
    
`Condition` (`/;`) can be used with `SetDelayed` to make an assignment that only holds if a condition is satisfied:    
```
>> f(x_) := p(x) /; x>0    
>> f(3)    
p(3)    

>> f(-3)    
f(-3)    
```