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

The [A018900 Sum of two distinct powers of 2](https://oeis.org/A018900) integer sequence

```
>> Union(Total/@Subsets(2^Range(0, 10), {2}))
{3,5,6,9,10,12,17,18,20,24,33,34,36,40,48,65,66,68,72,80,96,129,130,132,136,144,160,192,257,258,260,264,272,288,320,384,513,514,516,520,528,544,576,640,768,1025,1026,1028,1032,1040,1056,1088,1152,1280,1536}
```
