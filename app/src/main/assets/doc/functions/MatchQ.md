## MatchQ

``` 
MatchQ(expr, form)
``` 

> tests whether `expr` matches `form`.
 
### Examples

```  
>> MatchQ(123, _Integer)
True
	 
>> MatchQ(123, _Real)
False
	 
>> MatchQ(_Integer)[123]
True
```