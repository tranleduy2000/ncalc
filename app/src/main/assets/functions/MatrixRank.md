## MatrixRank
```
MatrixRank(matrix)
```

> returns the rank of `matrix`.
 
### Examples
```
>> MatrixRank({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}})
2
>> MatrixRank({{1, 1, 0}, {1, 0, 1}, {0, 1, 1}})
3
>> MatrixRank({{a, b}, {3 a, 3 b}})
1
```

Argument `{{1, 0}, {0}}` at position `1` is not a non-empty rectangular matrix.
```
>> MatrixRank({{1, 0}, {0}})
MatrixRank({{1, 0}, {0}})
```