## RowReduce

```
RowReduce(matrix)
```

> returns the reduced row-echelon form of `matrix`.

See:   
* [Wikipedia - Row echelon form](http://en.wikipedia.org/wiki/Row_echelon_form)

### Examples
```
>> RowReduce({{1,1,0,1,5},{1,0,0,2,2},{0,0,1,4,-1},{0,0,0,0,0}})
{{1,0,0,2,2},  
 {0,1,0,-1,3},
 {0,0,1,4,-1},
 {0,0,0,0,0}}
 
>> RowReduce({{1, 0, a}, {1, 1, b}})   
{{1,0,a},
 {0,1,-a+b}}
 
>> RowReduce({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}})
{{1,0,-1},
 {0,1,2},
 {0,0,0}}
```  

Argument {{1, 0}, {0}} at position 1 is not a non-empty rectangular matrix.  
```  
>> RowReduce({{1, 0}, {0}})   
RowReduce({{1, 0}, {0}})  
```   