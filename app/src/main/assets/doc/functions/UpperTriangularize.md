## UpperTriangularize

``` 
UpperTriangularize(matrix)
```

> create a upper triangular matrix from the given `matrix`.

``` 
UpperTriangularize(matrix, n)
```

> create a upper triangular matrix from the given `matrix` above the `n`-th subdiagonal.

### Examples
 
```
>> UpperTriangularize({{a,b,c,d}, {d,e,f,g}, {h,i,j,k}, {l,m,n,o}}, 1)
{{0,b,c,d}
 {0,0,f,g}
 {0,0,0,k} 
 {0,0,0,0}}
```