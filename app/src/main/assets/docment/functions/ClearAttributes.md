## ClearAttributes

```
ClearAttributes(symbol, attrib)
```

> removes `attrib` from `symbol`'s attributes.
 
### Examples
``` 
>> SetAttributes(f, Flat)    
>> Attributes(f)    
{Flat}    
 
>> ClearAttributes(f, Flat)    
>> Attributes(f)    
{}  
```
 
Attributes that are not even set are simply ignored:    
```
>> ClearAttributes({f}, {Flat})    
>> Attributes(f)    
{}    
```