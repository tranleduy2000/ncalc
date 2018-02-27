## Array 

``` 
Array(f, n)
```
> returns the `n`-element list `{f(1), ..., f(n)}`.
	
```
Array(f, n, a)
```
> returns the n-element list `{f(a), ..., f(a + n)}`.

```
Array(f, {n, m}, {a, b})
```
> returns an `n`-by-`m` matrix created by applying `f` to indices ranging from `(a, b)` to `(a + n, b + m)`.

```
Array(f, dims, origins, h)
```
> returns an expression with the specified dimensions and index origins, with head `h` (instead of `List`).

### Examples
``` 
>> Array(f, 4)
{f(1),f(2),f(3),f(4)}
 
>> Array(f, {2, 3})
{{f(1,1),f(1,2),f(1,3)},{f(2,1),f(2,2),f(2,3)}} 
 
>> Array(f, {2, 3}, {4, 6})
{{f(4,6),f(4,7),f(4,8)},{f(5,6),f(5,7),f(5,8)}}

>> Array(f, 4)
{f(1), f(2), f(3), f(4)}
 
>> Array(f, {2, 3})
{{f(1, 1), f(1, 2), f(1, 3)}, {f(2, 1), f(2, 2), f(2, 3)}}
 
>> Array(f, {2, 3}, 3)
{{f(3, 3), f(3, 4), f(3, 5)}, {f(4, 3), f(4, 4), f(4, 5)}}
 
>> Array(f, {2, 3}, {4, 6})
{{f(4,6),f(4,7),f(4,8)},{f(5,6),f(5,7),f(5,8)}}
 
>> Array(f, {2, 3}, 1, Plus)
f(1,1)+f(1,2)+f(1,3)+f(2,1)+f(2,2)+f(2,3)
```

{2, 3} and {1, 2, 3} should have the same length.
```
>> Array(f, {2, 3}, {1, 2, 3})
Array(f, {2, 3}, {1, 2, 3})
```

Single or list of non-negative integers expected at position 2.
```
>> Array(f, a)
Array(f, a)
```

Single or list of non-negative integers expected at position 3.
```
>> Array(f, 2, b)
Array(f, 2, b)
```
 
 