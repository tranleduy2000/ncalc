## PadLeft 

```
PadLeft(list, n)
```

> pads `list` to length `n` by adding `0` on the left. 

```
PadLeft(list, n, x)
```

> pads `list` to length `n` by adding `x` on the left. 

```
PadLeft(list)
```

> turns the ragged list `list` into a regular list by adding '0' on the left. 
    
### Examples 

```
>> PadLeft({1, 2, 3}, 5)    
{0,0,1,2,3}   

>> PadLeft(x(a, b, c), 5)    
x(0,0,a,b,c)    

>> PadLeft({1, 2, 3}, 2)    
{2, 3}    

>> PadLeft({{}, {1, 2}, {1, 2, 3}})    
{{0,0,0},{0,1,2},{1,2,3}} 
```