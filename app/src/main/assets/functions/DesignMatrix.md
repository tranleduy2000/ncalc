## DesignMatrix
```
DesignMatrix(m, f, x)
```
> returns the design matrix.

### Examples
```
>> DesignMatrix({{2, 1}, {3, 4}, {5, 3}, {7, 6}}, x, x)
{{1,2},{1,3},{1,5},{1,7}}
 
>> DesignMatrix({{2, 1}, {3, 4}, {5, 3}, {7, 6}}, f(x), x)
{{1,f(2)},{1,f(3)},{1,f(5)},{1,f(7)}}
```