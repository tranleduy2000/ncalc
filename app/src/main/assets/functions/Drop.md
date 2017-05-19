## Drop

```
Drop(expr, n)
```
> returns `expr` with the first `n` leaves removed.

### Examples
```
>> Drop({a, b, c, d}, 3)
{d}
 
>> Drop({a, b, c, d}, -2)
{a,b}
 
>> Drop({a, b, c, d, e}, {2, -2})
{a,e}
```

Drop a submatrix:
```
>> A = Table(i*10 + j, {i, 4}, {j, 4})
{{11,12,13,14},{21,22,23,24},{31,32,33,34},{41,42,43,44}}
 
>> Drop(A, {2, 3}, {2, 3})
{{11,14},{41,44}}
 
>> Drop(Range(10), {-2, -6, -3})
{1,2,3,4,5,7,8,10}
 
>> Drop(Range(10), {10, 1, -3})
{2, 3, 5, 6, 8, 9}
```

Cannot drop positions -5 through -2 in {1, 2, 3, 4, 5, 6}.
```
>> Drop(Range(6), {-5, -2, -2}) 
Drop({1, 2, 3, 4, 5, 6}, {-5, -2, -2})
```