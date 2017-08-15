## SetAttributes

```
SetAttributes(symbol, attrib)
```

> adds `attrib` to `symbol`'s attributes.
 
### Examples
``` 
>> SetAttributes(f, Flat)    
>> Attributes(f)    
{Flat}    
``` 

Multiple attributes can be set at the same time using lists:    
``` 
>> SetAttributes({f, g}, {Flat, Orderless})    
>> Attributes(g)    
{Flat, Orderless}    
```