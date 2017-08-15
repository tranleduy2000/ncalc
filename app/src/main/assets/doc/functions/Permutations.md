## Permutations

```
Permutations(list)
```
> gives all possible orderings of the items in `list`.
     
```	 
Permutations(list, n)
```
> gives permutations up to length `n`.
		
```
Permutations(list, {n})
```
> finds a list of all possible permutations containing exactly `n` elements.
	
See:  
* [Wikipedia - Permutation](https://en.wikipedia.org/wiki/Permutation)
	 
### Examples

```
>> Permutations({a, b, c})   
{{a,b,c},{a,c,b},{b,a,c},{b,c,a},{c,a,b},{c,b,a}}  

>> Permutations({1, 2, 3}, 2)
{{},{1},{2},{3},{1,2},{1,3},{2,1},{2,3},{3,1},{3,2}}

>> Permutations({a, b, c}, {2})  
{{a,b},{a,c},{b,a},{b,c},{c,a},{c,b}}
```