## Subsets

```
Subsets(list)
```
> finds a list of all possible subsets of `list`.
        
```
Subsets(list, n)
```
> finds a list of all possible subsets containing at most `n` elements.
        
```
Subsets(list, {n})
```
> finds a list of all possible subsets containing exactly `n` elements.
	 
See:  
* [Wikipedia - Combination](https://en.wikipedia.org/wiki/Combination)

### Examples

```
>> Subsets({a, b, c})   
{{},{a},{b},{c},{a,b},{a,c},{b,c},{a,b,c}}  
    
>> Subsets({a, b, c}, 2)    
{{},{a},{b},{c},{a,b},{a,c},{b,c}} 
    
>> Subsets({a, b, c}, {2})  
{{a,b},{a,c},{b,c}}     
     
>> Subsets({})   
{{}} 
    
>> Subsets()   
Subsets()   
```