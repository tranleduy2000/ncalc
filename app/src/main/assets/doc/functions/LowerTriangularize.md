## LowerTriangularize

``` 
LowerTriangularize(matrix)
```

> create a lower triangular matrix from the given `matrix`.

``` 
LowerTriangularize(matrix, n)
```

> create a lower triangular matrix from the given `matrix` below the `n`-th subdiagonal.

### Examples
 
```
>> LowerTriangularize({{a,b,c,d}, {d,e,f,g}, {h,i,j,k}, {l,m,n,o}}, -1)
{{0,0,0,0} 
 {d,0,0,0} 
 {h,i,0,0}  
 {l,m,n,0}}
```