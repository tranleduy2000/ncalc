## AppendTo

```
AppendTo(s, item)
```

> append `item` to value of `s` and sets `s` to the result.

### Examples
```  
>> s = {}    
>> AppendTo(s, 1)    
{1}    

>> s    
{1}    
```

'Append' works on expressions with heads other than 'List':    
```
>> y = f()  
>> AppendTo(y, x)    
f(x)    

>> y    
f(x)    
```

{} is not a variable with a value, so its value cannot be changed.
```
>> AppendTo({}, 1)     
AppendTo({}, 1)   
```

a is not a variable with a value, so its value cannot be changed.
``` 
>> AppendTo(a, b)    
AppendTo(a, b)  
``` 