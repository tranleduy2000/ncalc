## PadRight

```
PadRight(list, n)
```

> pads `list` to length `n` by adding `0` on the right. 

```
PadRight(list, n, x)
```

> pads `list` to length `n` by adding `x` on the right. 

```
PadRight(list)
```

> turns the ragged list `list` into a regular list by adding '0' on the right. 

### Examples 

```
>> PadRight({1, 2, 3}, 5)    
{1,2,3,0,0}    

>> PadRight(x(a, b, c), 5)    
x(a,b,c,0,0)  

>> PadRight({1, 2, 3}, 2)    
{1,2}   

>> PadRight({{}, {1, 2}, {1, 2, 3}})    
{{0,0,0},{1,2,0},{1,2,3}}
```