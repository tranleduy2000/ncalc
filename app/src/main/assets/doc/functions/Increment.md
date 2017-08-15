## Increment

``` 
Increment(x)

x++
``` 

> increments `x` by `1`, returning the original value of `x`. 

### Examples
```   
>> a = 2;   
>> a++    
2    
 
>> a    
3    
```

Grouping of 'Increment', 'PreIncrement' and 'Plus':   
``` 
>> ++++a+++++2//Hold//FullForm    
Hold(Plus(PreIncrement(PreIncrement(Increment(Increment(a)))), 2))  
``` 
    