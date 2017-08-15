## ProductLog

```
ProductLog(z)
```

> returns the value of the Lambert W function at `z`.
 
### Examples
The defining equation:
```  
>> z == ProductLog(z) * E ^ ProductLog(z)    
True    
``` 

Some special values:    
``` 
>> ProductLog(0)    
0    
>> ProductLog(E)    
1   
```
  
 